(defn v+
  [a b]
  (mapv + a b)
)

(defn v*
  [a b]
  (mapv * a b)
)

(defn v-
  [a b]
  (mapv - a b)
)

(defn scalar
  [a b]
  (reduce + (v* a b))
)

(defn vect
  [a b]
  {
    :pre ((== 2 (count a)) (== 2 (count b)))
  }
  (- (* (nth a 0)(nth b 1)) (* (nth b 0)(nth a 1)))
)

(defn v*s
  [v, s]
  (mapv (fn [x] (* x s)) v)
)



(println (v*s [1, 2], 5))