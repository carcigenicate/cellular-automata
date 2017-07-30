(ns cellular-automata.one-dim.main
  (:require [quil.core :as q]
            [quil.middleware :as m]

            [cellular-automata.one-dim.generation :as gen]
            [cellular-automata.one-dim.rule-sets :as rs]))

(defrecord State [generations])

(def width 1000)
(def height 1000)

(def generation-size 100)
(def box-width (/ width generation-size))

(def rule-set rs/odd-set)

(defn setup-state [])

(defn update-state [state])

(defn draw-state [state])

(defn -main []
  (q/defsketch 1-D-CA
    :size [width height]

    :setup setup-state
    :update update-state
    :draw draw-state

    :middleware {m/fun-mode}))