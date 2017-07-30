(ns cellular-automata.one-dim.generation)

; TODO: Is all the indexing necessary?

; TODO: Necessary? Immedietely replace with the filler?
(def out-of-bounds-cell ::oob)
(def out-of-bounds-filler 0)

(defn new-generation [starting-val num-of-cells]
  (vec (repeat num-of-cells starting-val)))

(defn inbounds? [generation-size cell-i]
  (< -1 cell-i generation-size))

(defn neighborhood-indices-of [generation-size cell-i]
  (map #(if (inbounds? generation-size %) % out-of-bounds-cell)
    (range (dec cell-i) (+ cell-i 2))))

(defn neighborhood-of [generation cell-i]
  (let [gen-size (count generation)
        neigh-indices (neighborhood-indices-of gen-size cell-i)]

    (map #(if (= out-of-bounds-cell %)
            out-of-bounds-filler
            (generation %))
         neigh-indices)))

(defn next-state-of [generation cell-i rule-set]
  (let [neighborhood (neighborhood-of generation cell-i)]
    ; TODO: Do checking to ensure that the neighborhood
    ;  is actually in the ruleset?
    (rule-set (vec neighborhood))))

(defn next-generation
  "Returns the next generation according to the rule-set.
  rule-set can either be a map mapping a neighborhood to a value, or a plain
   function that accepts a neighborhood and returns the new cell."
  [generation rule-set]
  (mapv #(next-state-of generation % rule-set)
        (range (count generation))))