(ns cellular-automata.one-dim.main
  (:require [quil.core :as q]
            [quil.middleware :as m]

            [cellular-automata.one-dim.generation :as gen]
            [cellular-automata.one-dim.rule-sets :as rs]))

; TODO: Allow cells states from 0 to 255^3, and color accordingly?

(defrecord State [generations])

(def width 1000)
(def height 1000)

(def fps 100)

(def generation-size 100)
(def box-width (/ width generation-size))
(def max-generations (int (inc (/ height box-width))))

(def rule-set rs/odd-set)
(def initial-generation (assoc (gen/new-generation 0 generation-size)
                               (int (/ generation-size 2)) 1))

(defn fix-overflow [state]
  (update state :generations
          #(if (> (count %) max-generations)
             (subvec % 1)
             %)))

(defn xs-for-boxes [generation]
  ; TODO: inc width to ensure full coverage?
  (range 0 width box-width))

(defn ys-for-generations [generations]
  (let [neg-width (- box-width)]
      (range (- height box-width) neg-width neg-width)))

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
    (update :generations
            #(conj %
                   (gen/next-generation (last %) rule-set)))

    (fix-overflow)))

(defn draw-state [state]
  (let [{gens :generations} state
        y-gens (map vector (ys-for-generations gens) gens)]

    (doseq [[y gen] y-gens]
      (draw-generation gen y))))

(defn -main []
  (q/defsketch One-D-CA
    :size [width height]

    :setup setup-state
    :update update-state
    :draw draw-state

    :middleware [m/fun-mode]))