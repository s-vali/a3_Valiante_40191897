(ns db)

;Option 1
(defn displayCustomerTable [vectCust]
  (doseq [n (get vectCust 0)]
    (println (get n 0) ":" "[\"" (get n 1) "\"," "\"" (get n 2) "\"," "\"" (get n 3) "\"]")))
  ;loop through elements, increment a counter, and display info
  ;note: n is considered vector, so can use get

;Option 2
(defn displayProductTable [vectProd]
  (doseq [n (get vectProd 0)]
    (println (get n 0) ":" "[\"" (get n 1) "\"," "\"" (get n 2) "\"]")))

;Option 3
(defn helperSearchCT [custID vectCust] ;custID:string, vectCust:vector
  (loop [k 0] ;pass (get vectCust 0)
    (when k < (count vectCust) 
          (if (= custID (get-in vectCust [k 0]))
            (get-in vectCust [k 1]))
          ;(some #(= custID %) (get vectCust k))) ;test if custID 
    )) )

(defn b [custID vectCust] 
  (loop [k 0]
    (when (not= custID (Integer/parseInt (get-in vectCust [0 k 0]))) ;parse for a nb, if the nb doesn't exist, the loop will go forever
      (some #(= custID %) vectCust) ;pass whole each sequence line
      (println k)
      (println "custID:" custID)
      (println "-------->")
      (println (get vectCust [0 k 1]))
      (def custName (get vectCust [0 k 1]))
      (recur (inc k))))
  (println "after loop:")
  (println custName);returns the custName
  ;login page will easiler have 6 test
  )
(defn a [custID vectCust]
  (def boo (map #(= custID %) vectCust))
  (println "boo: "boo))
;(map #())
(defn helperDST [innerVectSales i]
  (loop [j 0]
    (when (< j (count innerVectSales))
      (print "=>" (get-in innerVectSales [j]) ",") ;gets each individual element of the lines
      (recur (inc j)))))
(defn displaySalesTable [vectSales] ;is (get vectSale 0)
  (loop [i 0]
    (when (< i (count vectSales))
      ;(println (get vectSales i)) ;prints each vector for line of sales.txt 
      (println "Testing get-in[i 0]: " (get-in vectSales [i 1])) ;returns individual custID with each iteration
      (println i)
      ;(helperDST (get vectSales i) i)
      (recur (inc i)))))
;(get-in vectSales [0 i]) prints each index but only for first line (1|1|1|3)
(defn display [vectSales vectCust vectProd]
  ;loop through each element of sales.txt by line
  (loop [i 0]
    (when (< i (count vectSales))
      (def custID (get-in vectSales [i 1])) ;custID is string
      (def prodID (get-in vectSales [i 2]))
      (println (+ i 1) ": [\"" (get-in vectCust [(- (Integer/parseInt custID) 1), 1]) " \", " ;custID will be a nb 
              "\""  (get-in vectProd [(- (Integer/parseInt prodID) 1), 1]) "\", "
               "\""(get-in vectSales [i 3]) "\"]") 
      (recur (inc i)))))

;Option 4
(defn searchCust [custName vectCust]
  (def i (atom 0))
  (def custID "0") ;default is 0, meaning non-existent
  (while (< @i (count vectCust))
    (if (some #(= custName %) (get vectCust @i))
      (do (def custID (get-in vectCust [@i 0]))
          (reset! i (count vectCust)))
      (swap! i inc)))
  ;(println "@a:" @i)
  (reset! i 0) 
  custID ;return custID, if exists is number, if not is nil
  )

(defn searchCust9 [custName vectCust]
  (def i (atom 0))
  (loop [i 0]
    (when (< @i (count vectCust)) 
      (if (some #(= custName %) (get vectCust @i)) 
        (do (get-in vectCust [@i 0])
            (reset! i (count vectCust)))
        0)
      (println (some #(= custName %) (get vectCust i)))
      (recur (swap! i inc)))) 
)
;search for customer and return 0 if does not exist and custID if does

(defn getPrice [prodID vectProd] ;make sure argument passed is a string
  (def price 0)
  (def i (atom 0))
  ;loop through vectProd fo prodId and return the price, if not return zero
  (while (< @i (count vectProd)) 
    (if (= prodID (get-in vectProd [@i 0]))
      (do (def price (get-in vectProd [@i 2]))
          (reset! i (count vectProd))))
    (swap! i inc))
  price ;returns string
  )

(defn totalValue [vectSales vectCust vectProd]
  ;prompt for name
  (println "Enter name of customer: ")
  (def custName (read-line))
  (def custID (searchCust custName vectCust)) ;if customer does not exist, will be nil
  ;loop through vectSales for custID purchases
  (def sum (atom 0))
  (def w (atom 0))
  (while (< @w (count vectSales)) 
    (if (= custID (get-in vectSales [@w 1]))
      (do (def price (getPrice (get-in vectSales [@w 2]) vectProd))
          (def product (* (Double/parseDouble price) (Double/parseDouble (get-in vectSales [@w 3]))))
          (def prevValue @sum)
          (reset! sum (+ prevValue product))))
    (swap! w inc))
  (println "custID: " custID)
  (if (not= custID "0")
    (println "Returned: $" @sum)
    (println "Returned: $ 0.0"))
  ;(print ": $"  @sum)
  ;reset the sum back to zero for next function calls
  ;(reset! sum 0)
  (reset! w 0)
  )

;Option 5
(defn searchProd [prodName vectProd]
  (def prodID 0)
  (def c (atom 0))
  (while (< @c (count vectProd))
    (if (= prodName (get-in vectProd [@c 1]))
      (do 
        (println @c)
        (def prodID (get-in vectProd [@c 0]))
        (reset! c (count vectProd))))
    (swap! c inc))
  prodID)

(defn totalCount [vectSales vectProd]
  (println "Enter the item name (case sensitive): ")
  (def prodName (read-line))
  (def prodId (searchProd prodName vectProd))
  (def total (atom 0))
  (def p (atom 0))
  (println "prodID:" prodID)
  (while (< @p (count vectSales)) 
    (if (= prodID (get-in vectSales [@p 2]))
      (do 
        (println "@p:" @p)
        (reset! total (+ @total (Integer/parseInt (get-in vectSales [@p 3]))))))
    (swap! p inc))
  (println "Returned:" @total))
