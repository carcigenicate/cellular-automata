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

(defn full-color-set [neighb]
  (let [rs (map first neighb)
        gs (map second neighb)
        bs (map #(get % 2) neighb)
        sl #(rem (/ (apply - %) 2) 255)]
    [(sl rs)
     (sl gs)
     (sl bs)]))
