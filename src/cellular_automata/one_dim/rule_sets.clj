(ns cellular-automata.one-dim.rule-sets)

(defn odd-set [neighb]
  (if (even? (apply - neighb))
      0
      1))

(defn gt-set [neighb]
  (if (apply <= neighb)
    1
    0))



