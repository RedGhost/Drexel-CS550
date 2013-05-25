(beval #t ())
;Value: #t
(beval 'p '((p #t)))
;Value: #t
(beval '(and #t p) '((p #t)))
;Value: #t
(beval '(and p q r) '((p #t) (q #t) (r #t)))
;Value: #t
(beval '(or p q r) '((p #t) (q #f) (r #f)))
;Value: #t
(beval '(imply p q) '((p #t) (q #f)))
;Value: #f
(beval '(imply p q) '((p #t) (q #t)))
;Value: #t
(beval '(equiv (imply p q) (or (not p) q)) '((p #t) (q #f)))
;Value: #t