(ns cellular-automata.one-dim.rule-sets)

(defn odd-set [neighb]
  (if (odd? (apply - neighb))
      0
      1))



