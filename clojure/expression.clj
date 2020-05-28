;===============================================================

(defn trace [x] (do (println x) x))

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
(def toStringInfix (method :toStringInfix))

(declare Zero Add)

(def Constant-prototype
  {:evaluate      (fn [self values] ((field :value) self))
   :diff          (fn [self target] Zero)
   :toString      (fn [self] (format "%.1f" ((field :value) self)))
   :toStringInfix (method :toString)})

(defn Constant [c]
  {:proto Constant-prototype
   :value c})

(def Zero (Constant 0.0))
(def one (Constant 1.0))

(def +args (field :args))

(def Variable-prototype
  {:evaluate      (fn [self values] (get values ((field :value) self)))
   :diff          (fn [self target] (if (= target ((field :value) self)) one Zero))
   :toString      (fn [self] ((field :value) self))
   :toStringInfix (method :toString)})

(defn Variable [s]
  {:proto Variable-prototype
   :value s})

(def Operation
  {:evaluate      (fn [self values] (apply ((field :user-fun) self) (mapv (fn [x] (evaluate x values)) (+args self))))
   :diff          (fn [self target] (((field :diff-fun) self) target))
   :toString      (fn [self] (apply str "(" ((field :str-value) self) " " (clojure.string/join " " (mapv toString (+args self))) ")"))
   :toStringInfix (fn [self] (apply str "(" (clojure.string/join (str " " ((field :str-value) self) " ") (mapv toStringInfix (+args self))) ")"))})

(defn constructor [user-fun diff-fun str-value]
  {:proto     Operation
   :user-fun  user-fun
   :diff-fun  diff-fun
   :str-value str-value})

(defn unary-constructor [user-fun diff-fun str-value]
  {:proto         (constructor user-fun diff-fun str-value)
   :toStringInfix (fn [self] (str str-value "(" (apply toStringInfix (+args self)) ")"))})

(defn Add [& args]
  {:proto (apply constructor
                 +
                 (fn [target]
                   (apply Add (mapv (fn [x] (diff x target)) args)))
                 "+")
   :args  args})

(defn Subtract [& args]
  {:proto (apply constructor
                 -
                 (fn [target]
                   (apply Subtract (mapv (fn [x] (diff x target)) args)))
                 "-")
   :args  args})


(defn Multiply [& args]
  {:proto (apply constructor
                 *
                 (fn [target] (reduce (fn [x y] (Add
                                                  (Multiply (diff x target) y)
                                                  (Multiply (diff y target) x))) args))
                 "*")
   :args  args})

(defn Divide [& args]
  {:proto (apply constructor
                 (fn [x y] (/ (double x) y))
                 (fn [target] (reduce (fn [x y] (Divide
                                                  (Subtract
                                                    (Multiply (diff x target) y)
                                                    (Multiply (diff y target) x))
                                                  (Multiply y y))) args))
                 "/")
   :args  args})

(defn Negate [x]
  {:proto (unary-constructor
            -
            (fn [target] (Negate (diff x target)))
            "negate")
   :args  (vector x)})

(declare e Lg)

(defn abs [n] (max n (- n)))

(defn Ln [x] {:proto    (constructor
                          (fn [x] (Math/log (abs x)))
                          (fn [target] (Divide (diff x target) x))
                          "not_used")
              :toString (str "(lg " (toString x) " e)")})

(defn Pw [x y]
  {:proto (constructor
            (fn [x y] (Math/pow x y))
            (fn [target] (Multiply
                           (diff (Multiply (Ln x) y) target)
                           (Pw x y)))
            "pw")
   :args  (vector x y)})


(defn Lg [x base]
  {:proto (constructor
            (fn [x base] (/ (Math/log (abs base)) (Math/log (abs x))))
            (fn [target] (diff (Divide (Ln base) (Ln x)) target))
            "lg")
   :args  (vector x base)})

(defn double-bit-oper [fun]
  (fn [x y]
    (Double/longBitsToDouble (fun (Double/doubleToLongBits x) (Double/doubleToLongBits y)))))

(defn And [& args]
  {:proto (apply constructor
                 (fn [& args] (reduce (double-bit-oper bit-and) args))
                 nil
                 "&")
   :args  args})

(defn Or [& args]
  {:proto (apply constructor
                 (fn [& args] (reduce (double-bit-oper bit-or) args))
                 nil
                 "|")
   :args  args})

(defn Xor [& args]
  {:proto (apply constructor
                 (fn [& args] (reduce (double-bit-oper bit-xor) args))
                 nil
                 "^")
   :args  args})

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

(def *digit (+char "0123456789"))

(def *number (+str (+plus *digit)))

(def *string (+seqn 1 (+char "\"") (+str (+star (+char-not "\""))) (+char "\"")))

(def *space (+char " \t\n\r"))

(def *ws (+ignore (+star *space)))

(def *all-chars (mapv char (range 32 128)))

(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))

(def *identifier (+map Variable (+str (+seqf cons *letter (+star (+or *letter *digit))))))

(def *double (+map (comp Constant read-string) (+seqf str (+opt (+char "-")) *number (+char ".") *number)))

(defn *string-value [& inputs]
  (letfn [(to-string-array [input]
            (mapv (comp +char str) (seq input)))
          (val [input]
            (apply +seqf str (to-string-array input)))]
    (apply +or (mapv val inputs))))

(def parsing-bin-oper-map {"+" Add
                           "-" Subtract
                           "*" Multiply
                           "/" Divide
                           "&" And
                           "|" Or
                           "^" Xor})

(def parsing-unary-oper-map {"negate" Negate})

(declare *simple *unary)

(defn *ws-all [p] (+seqn 0 *ws p *ws))

(defn bin-list [& args] ((get parsing-bin-oper-map (second args)) (first args) (last args)))

(defn un-list [& args] ((get parsing-unary-oper-map (first args)) (second args)))

(defn *hard-expression [level]
  (let
    [oper-vec [["^"]
               ["|"]
               ["&"]
               ["+" "-"]                                    ; 1
               ["*" "/"]]                                   ; 2
     oper-parser (mapv (partial apply *string-value) oper-vec)]
    (if (= (count oper-vec) level)
      *simple
      (+seqf (partial reduce (partial apply bin-list))
             (*ws-all (delay (*hard-expression (+ 1 level))))
             (+star
               (+seq
                 (nth oper-parser level)
                 (delay (*hard-expression (+ 1 level)))))))))

(def *unary (+seqf un-list
                   (apply *string-value (keys parsing-unary-oper-map))
                   (delay *simple)))

(def *simple (*ws-all (+or
                        (delay *unary)
                        *identifier
                        *double
                        (+seqn 0 (+ignore (+char "(")) (delay (*hard-expression 0)) (+ignore (+char ")"))))))


(def parseObjectInfix (partial (comp -value (*hard-expression 0))))