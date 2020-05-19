(defn constant [c] (fn [values] c))

(defn variable [str] (fn [values] (get values str)))

(defn oper [f]
  (fn [& args]
    (fn [values]
      (apply f (mapv (fn [argument] (argument values)) args)))))

(def add (oper +))

(def subtract (oper -))

(def multiply (oper *))

(def divide (oper (fn [& args] (/ (double (first args)) (apply * (rest args))))))

(def negate (oper -))

(def min (oper clojure.core/min))

(def max (oper clojure.core/max))

(defn parseFunction [str]
  (letfn
    [(parseList [lst]
       (let [operMap {"+"      add
                      "-"      subtract
                      "*"      multiply
                      "/"      divide
                      "negate" negate
                      "max"    max
                      "min"    min}
             ]
         (apply (get operMap (name (first lst))) (mapv parseSomething (pop lst)))))

     (parseSomething [smth]
       (cond
         (list? smth) (parseList smth)
         (number? smth) (constant smth)
         :else (variable (name smth))))]
    (parseSomething (read-string str))))

;============================================================================================

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
  {:evaluate (fn [self values] ((field :value) self))
   :diff     (fn [self target] zero)
   :toString (fn [self] (format "%.1f" ((field :value) self)))
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
  {:evaluate (fn [self values] (apply ((field :user-fun) self) (mapv (fn [x] (evaluate x values)) args)))
   :diff     (fn [self target] (((field :diff-fun) self) target))
   :toString (fn [self] (apply str "(" ((field :str-value) self) " " (clojure.string/join " " (mapv toString args)) ")"))
   :toStringSuffix (fn [self] (apply str "("  (clojure.string/join " " (mapv toStringSuffix args)) " " ((field :str-value) self) ")"))})

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

(defn Ln [x] {:proto (constructor
                       (fn [x] (Math/log (abs x)))
                       (fn [target] (Divide (diff x target) x))
                       "!kek!"
                       x)})

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


;===================================================

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
(mapv (_parser (_combine str (_char #{\a \b}) (_char #{\x}))) ["ax" "ax~" "bx" "bx~" "" "a" "x" "xa"])


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
(def *number (+map read-string (+str (+plus *digit))))
(def *double (+map read-string (+seqf str (+opt (+char "-")) (+str (+plus *digit)) (+opt (+seqf str (+char "." ) (+str (+plus *digit)))))))

(def *string (+seqn 1 (+char "\"") (+str (+star (+char-not "\""))) (+char "\"")))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))

(def *all-chars (mapv char (range 32 128)))
;(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))

(def *letter (+char "xyz"))

(def *identifier (+str (+seqf cons *letter (+star (+or *letter *digit)))))

(defn *string-value [input] (apply +seqf str (mapv +char (mapv str (seq (char-array input))))))

(def operations ["+", "-", "*", "/", "negate"])

(def *operation (apply +or (mapv *string-value operations)))

(declare *ws-expression)

(def *expression (+or (+or *identifier *double) (+seqf list (+ignore (+char "(")) (+plus (delay *ws-expression)) *operation *ws (+ignore (+char ")")))))

(def *ws-expression (+map first (+seq *ws *expression *ws)))

(defn trace [x] (do (println x) x))

(defn parseObjectSuffix [input]
  (letfn
    [(parseList [lst]
       (let [operMap {"+"      Add
                      "-"      Subtract
                      "*"      Multiply
                      "/"      Divide
                      "negate" Negate}
             ]
         (apply (get operMap (last lst)) (mapv parseSomething (first lst)))))
     (parseSomething [smth]
       (cond
         (list? smth) (parseList smth)
         (number? smth) (Constant (double smth))
         :else (Variable smth)))]
    (parseSomething (-value (*ws-expression input)))))

(tabulate *ws-expression ["(1 2 +)" "1" "3123" "(x negate)" "10.0" "z" "(x 2.0 +)" "((770194950.0 -335724728.0 -) z +)"])

(println (toString (parseObjectSuffix "((770194950.0 -335724728.0 -) z +)")))

(toStringSuffix (parseObjectSuffix "((770194950.0 -335724728.0 -) z +)"))

