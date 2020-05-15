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

(declare zero Add)

(def Constant-prototype
  {:evaluate (fn [self values] ((field :value) self))
   :diff     (fn [self target] zero)
   :toString (fn [self] (format "%.1f" ((field :value) self)))})

(defn Constant [c]
  {:proto Constant-prototype
   :value c})

(def zero (Constant 0.0))
(def one (Constant 1.0))

(def Variable-prototype
  {:evaluate (fn [self values] (get values ((field :value) self)))
   :diff     (fn [self target] (if (= target ((field :value) self)) one zero))
   :toString (fn [self] ((field :value) self))})

(defn Variable [s]
  {:proto Variable-prototype
   :value s})

(defn Operation [& args]
  {:evaluate (fn [self values] (apply ((field :user-fun) self) (mapv (fn [x] (evaluate x values)) ((field :args) self))))
   :diff     (fn [self target] (((field :diff-fun) self) target))
   :toString (fn [self] (apply str "(" ((field :str-value) self) " " (clojure.string/join " " (mapv toString ((field :args) self))) ")"))
   :args     args})

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

(defn parseObject [str]
  (letfn
    [(parseList [lst]
       (let [operMap {"+"      Add
                      "-"      Subtract
                      "*"      Multiply
                      "/"      Divide
                      "negate" Negate}
             ]
         (apply (get operMap (name (first lst))) (mapv parseSomething (pop lst)))))

     (parseSomething [smth]
       (cond
         (list? smth) (parseList smth)
         (number? smth) (Constant smth)
         :else (Variable (name smth))))]
    (parseSomething (read-string str))))