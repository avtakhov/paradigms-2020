;===============================================================
(defn proto-get [self key]
  (cond
    (some? (get self key)) (get self key)
    (some? (get self :proto)) (proto-get (get self :proto) key)))

(defn proto-call [self key & args]
  (apply (proto-get self key) self args))

(defn field [key] (fn [self] (proto-get self key)))

(defn method [key]
  (fn [self & args]
    (apply proto-call self key args)))

(def evaluate (method :evaluate))
(def diff (method :diff))
(def toString (method :toString))
(def toStringSuffix (method :toStringSuffix))

(declare zero Add)

(def Constant-prototype
  {:evaluate       (fn [self values] ((field :value) self))
   :diff           (fn [self target] zero)
   :toString       (fn [self] (format "%.1f" ((field :value) self)))
   :toStringSuffix (fn [self] (format "%.1f" ((field :value) self)))})

(defn Constant [c]
  {:proto Constant-prototype
   :value c})

(def zero (Constant 0.0))
(def one (Constant 1.0))

(def Variable-prototype
  {:evaluate       (fn [self values] (get values ((field :value) self)))
   :diff           (fn [self target] (if (= target ((field :value) self)) one zero))
   :toString       (fn [self] ((field :value) self))
   :toStringSuffix (fn [self] ((field :value) self))})

(defn Variable [s]
  {:proto Variable-prototype
   :value s})

(defn Operation [& args]
  {:evaluate       (fn [self values] (apply ((field :user-fun) self) (mapv (fn [x] (evaluate x values)) args)))
   :diff           (fn [self target] (((field :diff-fun) self) target))
   :toString       (fn [self] (apply str "(" ((field :str-value) self) " " (clojure.string/join " " (mapv toString args)) ")"))
   :toStringSuffix (fn [self] (apply str "(" (clojure.string/join " " (mapv toStringSuffix args)) " " ((field :str-value) self) ")"))})

(defn constructor [user-fun diff-fun str-value & args]
  {:proto     (apply Operation args)
   :user-fun  user-fun
   :diff-fun  diff-fun
   :str-value str-value})

(defn Add [& args]
  {:proto (apply constructor
                 +
                 (fn [target]
                   (apply Add (mapv (fn [x] (diff x target)) args)))
                 "+"
                 args)})

(defn Subtract [& args]
  {:proto (apply constructor
                 -
                 (fn [target]
                   (apply Subtract (mapv (fn [x] (diff x target)) args)))
                 "-"
                 args)})


(defn Multiply [& args]
  {:proto (apply constructor
                 *
                 (fn [target] (reduce (fn [x y] (Add
                                                  (Multiply (diff x target) y)
                                                  (Multiply (diff y target) x))) args))
                 "*"
                 args)})

(defn Divide [& args]
  {:proto (apply constructor
                 (fn [x y] (/ (double x) y))
                 (fn [target] (reduce (fn [x y] (Divide
                                                  (Subtract
                                                    (Multiply (diff x target) y)
                                                    (Multiply (diff y target) x))
                                                  (Multiply y y))) args))
                 "/"
                 args)})

(defn Negate [x]
  {:proto (constructor
            -
            (fn [target] (Negate (diff x target)))
            "negate"
            x)})

(declare e Lg)

(defn abs [n] (max n (- n)))

(defn Ln [x] {:proto    (constructor
                          (fn [x] (Math/log (abs x)))
                          (fn [target] (Divide (diff x target) x))
                          "not_used"
                          x)
              :toString (str "(lg " (toString x) " e)")})

(defn Pw [x y]
  {:proto (constructor
            (fn [x y] (Math/pow x y))
            (fn [target] (Multiply
                           (diff (Multiply (Ln x) y) target)
                           (Pw x y)))
            "pw"
            x
            y)})


(defn Lg [x base]
  {:proto (constructor
            (fn [x base] (/ (Math/log (abs base)) (Math/log (abs x))))
            (fn [target] (diff (Divide (Ln base) (Ln x)) target))
            "lg"
            x
            base)})

(defn double-bit-oper [fun]
  (fn [x y]
    (Double/longBitsToDouble (fun (Double/doubleToLongBits x) (Double/doubleToLongBits y)))))

(defn And [& args]
  {:proto (apply constructor
                 (fn [& args] (reduce (double-bit-oper bit-and) args))
                 nil
                 "&"
                 args)})

(defn Or [& args]
  {:proto (apply constructor
                 (fn [& args] (reduce (double-bit-oper bit-or) args))
                 nil
                 "|"
                 args)})

(defn Xor [& args]
  {:proto (apply constructor
                 (fn [& args] (reduce (double-bit-oper bit-xor) args))
                 nil
                 "^"
                 args)})

(def e (Constant Math/E))

(defn parseObject [str]
  (letfn
    [(parseList [lst]
       (let [operMap {"+"      Add
                      "-"      Subtract
                      "*"      Multiply
                      "/"      Divide
                      "negate" Negate
                      "pw"     Pw
                      "lg"     Lg}
             ]
         (apply (get operMap (name (first lst))) (mapv parseSomething (pop lst)))))

     (parseSomething [smth]
       (cond
         (list? smth) (parseList smth)
         (number? smth) (Constant smth)
         :else (Variable (name smth))))]
    (parseSomething (read-string str))))

;============================================================================================

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

(defn *string-value [& inputs]
  (letfn [(to-string-array [input]
            (mapv (comp +char str) (seq input)))
          (val [input]
            (apply +seqf str (to-string-array input)))]
    (apply +or (mapv val inputs))))

(def *operation (let [oper {"+"      Add
                            "-"      Subtract
                            "*"      Multiply
                            "/"      Divide
                            "negate" Negate
                            "&"      And
                            "|"      Or
                            "^"      Xor}] (apply +or
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

(declare *mul-div *simple *unary)

(defn *ws-all [p] (+seqn 0 *ws p *ws))

(defn trace [x] (do (println x) x))

(defn bin-list [& args] (let [oper {"+" Add
                                    "-" Subtract
                                    "*" Multiply
                                    "/" Divide}]
                          (if (= 1 (count (trace args)))
                            (first args)
                            ((get oper (second args)) (first args) (last args)))))

(defn un-list [& args] (let [oper {"-" Negate}]
                         (if (= 1 (count args))
                           (first args)
                           ((get oper (first args)) (second args)))))

(def start_prior 0)

(defn *hard-expression [level]
  (let
    [oper-vec [["+" "-"]
               ["*" "/"]]
     oper-parser (mapv (partial apply *string-value) oper-vec)]
    (if (= (count oper-vec) level)
      *simple
      (+seqf (partial reduce (partial apply bin-list))
             (*ws-all (delay (*hard-expression (+ 1 level))))
             (+star
               (+seq
                 (nth oper-parser level)
                 (delay (*hard-expression (+ 1 level)))))))))

(def *unary (let [oper ["-"]] (+seqf un-list
                                     (+opt (apply *string-value oper))
                                     (delay *simple))))

(def *start-parse (*hard-expression start_prior))

(def *simple (*ws-all (+or
                        *identifier
                        *number
                        (+seqn 0 (+ignore (+char "(")) (delay *start-parse) (+ignore (+char ")"))))))


(def parseObjectSuffix (partial (comp -value *expression)))

;(tabulate *start-parse ["x+x" "(10)" "x" "10+2+1+1+1*2" "(10 * (2 + 1))"])

(def expr (-value (*start-parse "x + 2.0")))

(println (toString expr))