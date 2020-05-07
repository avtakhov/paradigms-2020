(definterface Expression
  (diff [target])
  (evaluate [values]))

(declare zero)

(deftype Const [c]
  Object
  (toString [this] (format "%.1f" c))
  Expression
  (evaluate [this values] c)
  (diff [this target] zero))

(def zero (Const. 0))
(def one (Const. 1))

(deftype Var [s]
  Object
  (toString [this] s)
  Expression
  (evaluate [this values] (get values s))
  (diff [this target] (if (= s target) one zero)))

(defn toString [obj] (.toString obj))
(defn evaluate [expression value] (.evaluate expression value))
(defn diff [expression target] (.diff expression target))
(defn Constant [c] (Const. c))
(defn Variable [c] (Var. c))

(deftype Operation [fun diff_fun string_value args]
  Object
  (toString [this] (apply str "(" string_value (apply str (mapv (fn [x] (str " " (toString x))) args)) ")"))
  Expression
  (evaluate [this values] (apply fun (mapv (fn [x] (evaluate x values)) args)))
  (diff [this target] (diff_fun (mapv (fn [x] (diff x target)) args) args))
  )

(defn Add [a b]
  (Operation. + (fn [[x' y'] [x y]] (Add x' y')) "+" [a b]))

(defn Subtract [a b]
  (Operation. - (fn [[x' y'] [x y]] (Subtract x' y')) "-" [a b]))

(defn Multiply [a b]
  (Operation. * (fn [[x' y'] [x y]] (Add (Multiply x' y) (Multiply y' x))) "*" [a b]))

(defn Divide [a b]
  (Operation. (fn [x y] (/ (double x) y)) (fn [[x' y'] [x y]] (Divide (Subtract (Multiply x' y) (Multiply y' x)) (Multiply y y))) "/" [a b]))

(defn Negate [a]
  (Operation. - (fn [[x'] [x]] (Negate x')) "negate" [a]))

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
