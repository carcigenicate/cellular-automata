(ns cellular-automata.one-dim.main
  (:require [quil.core :as q]
            [quil.middleware :as m]

            [cellular-automata.one-dim.generation :as gen]
            [cellular-automata.one-dim.rule-sets :as rs]))

; TODO: Allow cells states from 0 to 255^3, and color accordingly?

(defrecord State [generations])

(def screen-width 1000)
(def screen-height 1000)

(def fps 100)

(def generation-size 100)
(def box-width (/ screen-width generation-size))
(def max-generations (int (inc (/ screen-height box-width))))

(def rule-set rs/odd-set)
(def initial-generation (assoc (gen/new-generation 0 generation-size)
                               (int (/ generation-size 2)) 1))

(defn add-new-generation [state]
  (update state :generations
          #(conj %
                 (gen/next-generation (last %) rule-set))))

(defn fix-overflow
  "Removes the oldest generations in the event of an overflow."
  [state]
  (update state :generations
          #(if (> (count %) max-generations)
             (subvec % 1)
             %)))

(defn xs-for-boxes
  "Returns the x-values each box in the generation should be drawn at."
  [generation]
  (range 0 screen-width box-width))

(defn ys-for-generations
  "Returns the y-values each generation should be drawn at."
  [generations]
  (let [neg-width (- box-width)]
    (range (- screen-height box-width) neg-width neg-width)))

(defn draw-block [x y cell-state]
  (let [c (if (zero? cell-state) [0 0 0] [255 255 255])]
    (q/with-fill c
      (q/rect x y box-width box-width))))

(defn draw-generation [generation y]
  (let [x-boxes (map vector generation (xs-for-boxes generation))]
    (doseq [[box-state x] x-boxes]
      (draw-block x y box-state))))

(defn setup-state []
  (q/frame-rate fps)

  (let [starting-gens [initial-generation]]
    (->State starting-gens)))

(defn update-state [state]
  (-> state
    (add-new-generation)
    (fix-overflow)))

(defn draw-state [state]
  (let [{gens :generations} state
        y-gens (map vector (ys-for-generations gens) gens)]

    (doseq [[y gen] y-gens]
      (draw-generation gen y))))

(defn -main []
  (q/defsketch One-D-CA
    :size [screen-width screen-height]

    :setup setup-state
    :update update-state
    :draw draw-state

    :middleware [m/fun-mode]))