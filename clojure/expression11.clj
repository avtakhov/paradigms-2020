
(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn _show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                       "!"))
(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))
(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))

(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))
(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))

(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))


(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(def +parser _parser)

(def +ignore (partial +map (constantly 'ignore)))
(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
  (reduce _either p ps))
(defn +opt [p]
  (+or p (_empty nil)))
(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))



(defn +stop-when [stop p]
  (_either (+seqf list stop) (+seqf (fn [x col] (conj col x)) p (delay (+stop-when stop p)))))

(def *digit (+char "0123456789.-"))

(def *number (+map (fn [s] (Constant (read-string s))) (+str (+plus *digit))))

(def *string (+seqn 1 (+char "\"") (+str (+star (+char-not "\""))) (+char "\"")))

(def *space (+char " \t\n\r"))

(def *ws (+ignore (+star *space)))

(def *all-chars (mapv char (range 32 128)))

(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))

(def *identifier (+map Variable (+str (+seqf cons *letter (+star (+or *letter *digit))))))

(defn *string-value [input] (apply +seqf str (mapv +char (mapv str (seq input)))))

(def *operation (let [oper {"+"      Add
                            "-"      Subtract
                            "*"      Multiply
                            "/"      Divide
                            "negate" Negate}] (apply +or
                                                     (mapv
                                                       (partial +map (fn [x] (get oper x)))
                                                       (mapv *string-value (keys oper))))))

(def *expression
  (+seqn 0
         *ws
         (+or
           (+or *identifier *number)
           (+seqn
             0
             (+ignore (+char "("))
             *ws
             (let [end (+seqn 0 *ws *operation *ws (+char ")"))
                   lst (+stop-when end (delay *expression))
                   expr (fn [x] (apply (last x) (drop-last x)))
                   ans (+map expr lst)]
               ans)))
         *ws))

(def parseObjectSuffix (partial (comp -value *expression)))

