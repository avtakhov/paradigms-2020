(defn vv
		[func]
		(fn [v1 v2] (mapv func v1 v2))
)

(def v+
  (vv +)
)

(def v*
		(vv *)
)

(def v-
		(vv -)
)

(defn scalar
  [a b]
  (reduce + (v* a b))
)

(defn vect
  [a b]
  (letfn 
  		[
    		(vect2 [x y] (- (* (nth a x) (nth b y)) (* (nth b x) (nth a y))))
    ]
    (vector (vect2 1 2) (- (vect2 0 2)) (vect2 0 1))
  )
)

(defn v*s
  [v s]
  (mapv (fn [x] (* x s)) v)
)

(def m+
		(vv v+)
)

(def m-
		(vv v-)
)

(def m*
		(vv v*)
)

(defn m*s
	 [m s]
	 (mapv (fn [x] (v*s x s)) m)
)

(defn m*v
		[m v]
		(mapv (fn [x] (scalar x v)) m)
)

(defn transpose
		[m]
		(apply mapv vector m)
)

(defn m*m
		[m1 m2]
		(mapv (fn [row1] (mapv (fn [row2] (scalar row1 row2)) (transpose m2))) m1)
)
