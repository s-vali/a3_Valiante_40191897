(ns app
  (:require [db])
  (:require [menu])
  (:require [clojure.string :as str])
  (:require [clojure.java.io]))

;Load files to databases
;Function splits each line into vectors, nested vectors, access starts at [0 0 0]
(defn loadData [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (mapv #(clojure.string/split % #"\|") (line-seq rdr))))
;cust.txt
(def vectCust [(loadData "cust.txt")])
;prod.txt
(def vectProd [(loadData "prod.txt")])
;sales.txt
(def vectSales [(loadData "sales.txt")]) ;use vectors as arguments to case functions

(defn -main
  []
  ;Switch case for menu options 
  
  ;(db/a 2 vectCust)
  ;(println (get-in vectCust [0 0 1]))
  ;(println (get-in vectCust [0 2 1]))
  ;(println (get-in vectCust [0 1]))
  ;(println "TESTING NEW SEARCHCUST: ")
  ;(def ab (db/search "Sue Jones" (get vectCust 0)))
  ;(println "ab:" ab)
  (println "TESTING GETTING PRICE: ")
  (println (db/getPrice "3" (get vectProd 0)))
  (println "testing finding prod name")
  (println (db/searchProd "eggs" (get vectProd 0)))
  
  (menu/displayMenu) ;works
  (def option (read-line))
  (while (not= option "6") ;if put only 6 and not "6", the while loop will not stop
    (flush) ;flush before read-line
    (case option
      "1" (db/displayCustomerTable vectCust) ;each case leads to respective function in db that handles it, structure passed as argument
      "2" (db/displayProductTable vectProd)
      "3" (db/display (get vectSales 0) (get vectCust 0) (get vectProd 0))
      "4" (db/totalValue (get vectSales 0) (get vectCust 0) (get vectProd 0))
      "5" (db/totalCount (get vectSales 0) (get vectProd 0)))
    (if (>= (Integer/parseInt option) 7)
      (println "Incorrect input!"))
      (menu/displayMenu)
      (def option (read-line))) ;end of while
  (println "Good-Bye!")
  (db/helperSearchCT 2 vectCust)
  ;(println (count (get vectProd 0)))
  ;(println(get-in vectProd [0 2])) 
  
  ) ;end of main
;call main fn
(-main)