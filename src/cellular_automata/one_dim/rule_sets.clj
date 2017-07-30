(ns cellular-automata.one-dim.rule-sets)

(defn odd-set [neighb]
  (if (even? (apply - neighb))
      0
      1))

(defn sum-set [neighb]
  (let [adj-sum (/ (apply + neighb) 2)]
    (cond
      (> adj-sum 255) 2
      :else adj-sum)))



