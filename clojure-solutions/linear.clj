(defn numVec? [obj] (and (vector? obj)
                         (every? number? obj)))
(defn matchVectors? [& objs] (every? (partial == (count (first objs))) (mapv count (rest objs))))
(defn matchNumVecs? [& objs] (and (every? numVec? objs)
                                  (apply matchVectors? objs)))
(defn matr? [obj] (and (vector? obj)
                       (every? numVec? obj)
                       (every? (partial matchNumVecs? (first obj)) (rest obj))))
(defn matchMatrs? [& objs] (and (every? matr? objs)
                                (every? #(matchVectors? (first objs) %1) (rest objs))
                                (every? #(matchNumVecs? (first (first objs)) %1) (mapv first (rest objs)))))
(defn crd [op checker] (fn [& args]
                          {:pre [(apply checker args)]}
                          (apply mapv op args)))

(def v+ (crd + matchNumVecs?))
(def v- (crd - matchNumVecs?))
(def v* (crd * matchNumVecs?))
(def vd (crd / matchNumVecs?))
(def m+ (crd v+ matchMatrs?))
(def m- (crd v- matchMatrs?))
(def m* (crd v* matchMatrs?))
(def md (crd vd matchMatrs?))

(defn v*s [v & s]
  {:pre [(numVec? v)]}
  (let [mlt (apply * s)] (mapv (partial * mlt) v)))

(defn scalar [& vs]
  {:pre [(every? numVec? vs)]}
  (reduce + (reduce v* vs)))

(defn m*s [m & s]
  {:pre [(matr? m) (every? number? s)]}
  (let [mlt (apply * s)] mapv #(v*s %1 mlt) m))

(defn m*v [m v]
  {:pre [(matr? m) (numVec? v)]}
  (mapv #(scalar %1 v) m))

(defn transpose [m]
  {:pre [(matr? m)]}
  (apply mapv vector m))

(defn m*m [& ms]
  {:pre [(every? matr? ms)]}
  (reduce #(mapv (partial m*v (transpose %2)) %1) ms))

(defn vect [& args] (letfn [(vect2 [u v]
                              {:pre [(numVec? u) (numVec? v) (== 3 (count u)) (== 3 (count v))]}
                              (let [mu [[0 (- (nth u 2)) (nth u 1)]
                                        [(nth u 2) 0 (- (nth u 0))]
                                        [(- (nth u 1)) (nth u 0) 0]]]
                                (m*v mu v)))]
                      (reduce vect2 args)))

; modification

(defn tens? [obj] (or (number? obj)
                      (numVec? obj)
                      (and (vector? obj)
                           (apply matchVectors? obj)
                           (every? tens? obj))))

(defn tensForm [t]
  {:pre [(tens? t)]}
  (if (not (number? t))
    (conj (tensForm (first t)) (count t))
    (list)))

(defn sharedForm [& ts]
  {:pre [(every? tens? ts)]
   :post [(list? %)]}
  (let [forms (map tensForm ts)
        maxForm (apply max-key count forms)
        match? (fn [f1 f2] (every? true? (map == (reverse f1) (reverse f2))))]
    (if (every? true? (map (partial match? maxForm) forms))
      maxForm nil)))

(defn broadcast [t form]
  {:pre [(tens? t)]}
  (letfn [(prefForm [f cnt] (reverse (subvec (apply vector f) 0 cnt)))
          (expForm [t1 cnt] (apply vector (repeat cnt t1)))
          (dimDiff [f1 f2] (- (count f1) (count f2)))]
    (reduce expForm (conj (prefForm form (dimDiff form (tensForm t))) t))))

(defn tCrd [op] (fn [& ts] (if (every? number? ts)
                             (apply op ts)
                             (apply mapv (tCrd op) ts))))

(def t+ (tCrd +))
(def t- (tCrd -))
(def t* (tCrd *))
(def td (tCrd /))

(defn tensOp [op] (fn [& ts] (let [form (apply sharedForm ts)]
                               (apply op (map #(broadcast %1 form) ts)))))
(def hb+ (tensOp t+))
(def hb- (tensOp t-))
(def hb* (tensOp t*))
(def hbd (tensOp td))
