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
