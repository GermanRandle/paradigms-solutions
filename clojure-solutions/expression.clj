(defn divFunc
  ([arg] (/ 1.0 arg))
  ([arg1 & args] (/ (double arg1) (apply * args))))
(defn sumexpFunc [& args] (apply + (mapv #(Math/exp %1) args)))
(defn softmaxFunc [& args] (divFunc (Math/exp (first args)) (apply sumexpFunc args)))
(defn expFunc [arg] (Math/exp arg))

(defn operation [sign] (fn [& args] (fn [defs] (apply sign (map #(%1 defs) args)))))

(defn constant [value] (constantly value))
(defn variable [name] (fn [defs] (get defs name)))
(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation divFunc))
(def negate (operation -))
(def sumexp (operation sumexpFunc))
(def softmax (operation softmaxFunc))

(def functions {'+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'sumexp sumexp, 'softmax softmax})

(defn parseFunctionRecur [expr] (cond
                                  (list? expr) (apply (get functions (first expr)) (map parseFunctionRecur (rest expr)))
                                  (symbol? expr) (variable (name expr))
                                  (number? expr) (constant expr)))
(defn parseFunction [expr] (parseFunctionRecur (read-string expr)))

; Homework 11 below

(load-file "proto.clj")

(def evaluate (method :evaluate))
(def toString (method :toString))
(def toStringInfix (method :toStringInfix))
(def diff (method :diff))

(def _value (field :value))
(def ConstantPrototype
  {:evaluate _value
   :toString (fn [this] (str (_value this)))
   :toStringInfix (fn [this] (str (_value this)))
   :diff (fn [this difVar] (assoc this :value 0))})
(defn ConstantConstr [this value] (assoc this :value value))
(def Constant (constructor ConstantConstr ConstantPrototype))

(def ZERO (Constant 0))
(def ONE (Constant 1))

(def _name (field :name))
(def VariablePrototype
  {:evaluate (fn [this defs] (get defs (str (Character/toLowerCase (nth (_name this) 0)))))
   :toString (fn [this] (_name this))
   :toStringInfix (fn [this] (_name this))
   :diff (fn [this difVar] (if (= (_name this) difVar) ONE ZERO))})
(defn VariableConstr [this name] (assoc this :name name))
(def Variable (constructor VariableConstr VariablePrototype))

(def _operands (field :operands))
(def _sign (field :sign))
(def _func (field :func))

(def Operation
  {:evaluate (fn [this defs] (apply (_func this) (map #(evaluate %1 defs) (_operands this))))
   :toString (fn [this] (apply str "(" (_sign this) " " (clojure.string/join " " (map toString (_operands this))) ")"))
   :toStringInfix (fn [this] (let [ops (_operands this)]
                               (if (== 2 (count ops))
                                 (str "(" (toStringInfix (nth ops 0)) " "
                                      (_sign this) " " (toStringInfix (nth ops 1)) ")")
                                 (str (_sign this) "(" (toStringInfix (nth ops 0)) ")"))))})

(defn initOperation [sign func diffFunc]
  (constructor (fn [this & args] (assoc this :operands args))
               (assoc Operation :diff diffFunc :sign sign :func func)))

(def Add (initOperation "+" + (fn [this difVar]
                                (apply Add (map #(diff %1 difVar) (_operands this))))))

(def Subtract (initOperation "-" - (fn [this difVar]
                                     (apply Subtract (map #(diff %1 difVar) (_operands this))))))

(def Multiply (initOperation "*" * (fn [this difVar]
                                     (let [f (_operands this)
                                           getMlt (fn [difNum] (map #(if (= %1 difNum)
                                                                       (diff (nth f %1) difVar)
                                                                       (nth f %1))
                                                                    (range (count f))))]
                                       (apply Add (map #(apply Multiply (getMlt %1)) (range (count f))))))))

(def Negate (initOperation "negate" - (fn [this difVar]
                                        (apply Negate (map #(diff %1 difVar) (_operands this))))))

(def Divide (initOperation "/" divFunc (fn [this difVar]
                                         (let [ops (_operands this)
                                               f (first ops)
                                               g (apply Multiply (rest ops))]
                                           (if (= 1 (count ops))
                                             (Divide (Negate (diff f difVar)) (Multiply f f))
                                             (Divide (Subtract (Multiply (diff f difVar) g)
                                                               (Multiply f (diff g difVar))) (Multiply g g)))))))

(def Exp (initOperation "exp" expFunc (fn [this difVar]
                                        (let [f (first (_operands this))]
                                          (Multiply (Exp f) (diff f difVar))))))

(defn SumexpObject [args] (apply Add (map Exp args)))
(defn SoftmaxObject [args] (Divide (Exp (first args)) (SumexpObject args)))

(def Sumexp (initOperation "sumexp" sumexpFunc (fn [this difVar] (diff (SumexpObject (_operands this)) difVar))))
(def Softmax (initOperation "softmax" softmaxFunc (fn [this difVar] (diff (SoftmaxObject (_operands this)) difVar))))

(def objExprs {'+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate, 'sumexp Sumexp, 'softmax Softmax})

(defn parseObjectRecur [expr] (cond
                                (list? expr) (apply (get objExprs (first expr)) (map parseObjectRecur (rest expr)))
                                (symbol? expr) (Variable (name expr))
                                (number? expr) (Constant expr)))
(defn parseObject [expr] (parseObjectRecur (read-string expr)))

; Homework 12 below

(defn bit-impl [a b] (bit-or (bit-not a) b))
(defn bit-iff [a b] ((comp bit-not bit-xor) a b))
(defn bitDbl [f] (fn [a b] (Double/longBitsToDouble
                             (f (Double/doubleToLongBits a)
                                (Double/doubleToLongBits b)))))
(def BitAnd (initOperation "&" (bitDbl bit-and) nil))
(def BitOr (initOperation "|" (bitDbl bit-or) nil))
(def BitXor (initOperation "^" (bitDbl bit-xor) nil))
(def BitImpl (initOperation "=>" (bitDbl bit-impl) nil))
(def BitIff (initOperation "<=>" (bitDbl bit-iff) nil))

(def strToObj {"+" Add, "-" Subtract, "*" Multiply, "/" Divide, "negate" Negate,
               "&" BitAnd, "|" BitOr, "^" BitXor, "=>" BitImpl "<=>" BitIff})

(load-file "parser.clj")

(def *uint (+plus (+char "0123456789")))
(def *int (+seqf #(if (some? %1) (conj %2 %1) %2) (+opt (+char "-")) *uint))
(def *fractionalPart (+seqf cons (+char ".") *int))
(def *number (+seqf (comp seq concat) *int (+opt *fractionalPart)))
(def *constant (+map Constant (+double *number)))
(def *variable (+map Variable (+str (+plus (+char "xyzXYZ")))))
(def *ws (+ignore (+star (+char " "))))

(defn strToParser [s]
  (apply +seqf (constantly s) (map +char (clojure.string/split s #""))))

(def *infixExpr)
(def *basic)

(def *bracketExpr (+seqn 1 *ws (+char "(") (delay *infixExpr) (+char ")")))
(def *negate (+seqf Negate (+seqn 1 *ws (strToParser "negate") *ws (delay *basic))))

(def *basic
  (+or
    *constant
    *variable
    *bracketExpr
    *negate))

(defn *operation [foldFunc rev]
  (fn [*arg & signs]
    (let [getObj (fn [s] (if (contains? strToObj s) (get strToObj s) nil))
          *level (apply +or (reduce #(conj %1 (strToParser %2)) [] signs))
          *other (+star (+seq *ws (delay *level) *ws (delay *arg)))
          arrFirst (if rev last first)
          arrRest (if rev (comp rest reverse) rest)
          collect (fn [a b] (let [arr (reduce #(into %1 %2) [a] b)]
                              (reduce #(if (some? (getObj %2))
                                         (foldFunc %1 %2)
                                         (%1 %2)) (arrFirst arr) (arrRest arr))))]
      (+seqf collect (delay *arg) *other))))
 
(def *leftAssoc
  (let [foldFunc (fn [a b] (partial (get strToObj b nil) a))]
    (*operation foldFunc false)))

(def *rightAssoc
  (let [foldFunc (fn [a b] #((get strToObj b nil) %1 a))]
    (*operation foldFunc true)))

(def *levelMulDiv (*leftAssoc *basic "*" "/"))
(def *levelPlusMinus (*leftAssoc *levelMulDiv "+" "-"))
(def *levelAnd (*leftAssoc *levelPlusMinus "&"))
(def *levelOr (*leftAssoc *levelAnd "|"))
(def *levelXor (*leftAssoc *levelOr "^"))
(def *levelImpl (*rightAssoc *levelXor "=>"))
(def *levelIff (*leftAssoc *levelImpl "<=>"))

(def *infixExpr (+seqn 0 *ws *levelIff *ws))
(def parseObjectInfix (+parser *infixExpr))

