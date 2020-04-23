(defn constant [c] (fn [values] c))

(defn variable [str] (fn [values] (get values str)))

(defn oper [f]
  (fn [& args]
    (fn [values]
      (apply f (mapv (fn [argument] (argument values)) args)))))

(def add (oper +))

(def subtract (oper -))

(def multiply (oper *))

(def divide (oper /))

(def negate (oper -))

(def operMap
  {"+" add
   "-" subtract
   "*" multiply
   "/" divide
   "negate" negate})

(defn trace [x] (do (println x) x))

(defn parseFunction [str]
  (letfn
    [(parseList [lst]
                (let [oper  (trace (first lst))
                      other (trace (pop lst))]
                  (apply (trace (get operMap (name oper))) (mapv parseSomething other))))

     (parseConst [c] (constant c))

     (parseVariable [var] (variable (name var)))

     (parseSomething [smth]
                     (cond
                       (list? smth)   (parseList smth)
                       (number? smth) (parseConst smth)
                       :else          (parseVariable smth)))]
    (parseSomething (read-string str))))
