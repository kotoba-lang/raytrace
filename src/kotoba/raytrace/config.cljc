(ns kotoba.raytrace.config
  "Tunables extracted from kami-rt's Rust source (bvh.rs LEAF_MAX/epsilons,
   lib.rs RtConfig::default()) so the BVH heuristics + integrator defaults
   are data (`resources/kotoba/raytrace/config.edn`), not literals scattered
   across the port. JVM loads the EDN resource (single source of truth,
   authority pattern shared with kami-scene-contracts); cljs has no
   `io/resource`, so it carries an identical embedded literal instead —
   `config-test` cross-checks the two never drift apart."
  #?(:clj (:require [clojure.edn :as edn]
                     [clojure.java.io :as io])))

(def embedded
  "cljs fallback / cross-check fixture — must equal config.edn exactly."
  '{:bvh/leaf-max 2
    :bvh/det-epsilon 1e-8
    :bvh/hit-t-epsilon 1e-4
    :bvh/extent-epsilon 1e-6
    :bvh/morton-scale 1023
    :rt/default-config {:max-bounces 4 :spp 8 :clamp 10.0 :seed 0}})

#?(:clj
   (defn load-config
     "Read config.edn from the classpath; falls back to `embedded` if the
      resource isn't found (e.g. running from a jar that dropped resources)."
     []
     (if-let [res (io/resource "kotoba/raytrace/config.edn")]
       (edn/read-string (slurp res))
       embedded)))

(def config
  #?(:clj (load-config)
     :cljs embedded))
