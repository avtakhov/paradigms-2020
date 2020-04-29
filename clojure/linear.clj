; checking
(defn checkSize [& vectors]
  (every?
    (fn [vector] (== (count vector) (count (first vectors))))
    vectors))

(defn checkVector [vectors]
  (every?
    (fn [vector]
      (and (vector? vector) (every? number? vector))
      )
    vectors))

(defn full_check_vector [& vectors]
  (and (checkSize vectors)
       (checkVector vectors))
  )

(defn checkMatrix [& matrices]
  (every?
    (fn [matrix]
      (and
        (vector? matrix)
        (full_check_vector matrix)))
    matrices))

(defn matrixSize [& matrices]
  (checkMatrix (mapv first matrices)))

(defn full_check_matrices [& matrices]
  (and (checkMatrix matrices)
       (matrixSize matrices)))

(defn transpose [matrix]
  {
   :pre [(full_check_matrices matrix)]
   }
  (apply mapv vector matrix))

(defn rightMultiply [first second]
  (== (count first) (count (transpose second))))

;..............................................................
(defn vectorOperation [function]
  (fn [& vectors]
    {:pre  [(full_check_vector vectors)]
     :post [(checkVector %) (checkSize % (first vectors))]}
    (apply mapv function vectors)))

(def v+ (vectorOperation +))

(def v- (vectorOperation -))

(def v* (vectorOperation *))

(defn scalar [first second]
  {
   :pre [(full_check_vector first second)]
   :post [(number? %)]
   }
  (apply + (v* first second)))

(defn vect [& vectors]
  (letfn [(multiply [first second]
            {
             :pre [(full_check_vector first second) (== (count first) 3)]
             :post [(checkVector %) (== (count %) 3)]
             }
            (vector (- (* (nth first 1) (nth second 2)) (* (nth first 2) (nth second 1)))
                    (- (- (* (nth first 0) (nth second 2)) (* (nth first 2) (nth second 0))))
                    (- (* (nth first 0) (second 1)) (* (first 1) (nth second 0)))
                    )
            )]
  (if (== (count vectors) 1)
    (first vectors)
    (multiply (first vectors) (vect (rest vectors)))
    )
  ))

(defn v*s [vector scalar]
  {
   :pre [(full_check_vector vector)]
   }
  (mapv (fn [element] (* element scalar)) vector))

(defn matrixOperations [function]
  (fn [& matrix]
    {
     :pre [(full_check_matrices matrix)]
     }
    (apply mapv function matrix)))

(def m+ (matrixOperations v+))

(def m- (matrixOperations v-))

(def m* (matrixOperations v*))

(defn m*s [matrix scalar]
  {
   :pre [(full_check_matrices matrix)]
   }
  (mapv (fn [vector] (v*s vector scalar)) matrix))

(defn m*m [& matrix]
  {
   :pre [(full_check_matrices matrix)]
   }
  (letfn
    [(matrixMulti
       [first second]
       {
        :pre [(rightMultiply first second)]
        }
       (transpose
         (mapv (fn [row_second]
                 (mapv (fn [row_first]
                         (scalar row_first row_second)) first))
               (transpose second))))]
  (if (= (count matrix) 1)
  (first matrix)
  (matrixMulti (first matrix) (m*m (rest matrix))))))