; CS550 Assignment 5 Part 1
; Proposition calculus scheme interpreter

;;; Requires code from part1
(load "part1.scm")



;;; Functions for checking tautologies
;;; Example runs:
;;;
;;;(taut '(and p p) '(p))
;;; #f
;;;
;;;(taut '(or #t p) '(p))
;;; #t
;;;
;;;(taut '(or p (not p)) '(p))
;;; #t

(define (taut exp varlist)
  (taut-aux exp (gen-combo varlist)))

(define (taut-aux exp envlist)
  (cond
   ((null? envlist) #t)
   (else (if (beval exp (car envlist)) (taut-aux exp (cdr envlist)) #f))))



;;; Functions for generating the truth tables:
;;; Example run:
;;; (gen-combo '(p q))
;;; (((p #t) (q #t)) ((p #t) (q #f)) ((p #f) (q #t)) ((p #f) (q #f)))

(define (gen-combo varlist)
  (cond
   ((null? varlist) varlist)
   (else (gen-combo-aux varlist '()))))

(define (gen-combo-aux varlist newlist)
  (cond
   ((null? varlist) (list newlist))
   (else (append (gen-combo-aux (cdr varlist) (append newlist (list (cons (car varlist) '(#t)))))
	         (gen-combo-aux (cdr varlist) (append newlist (list (cons (car varlist) '(#f)))))))))
