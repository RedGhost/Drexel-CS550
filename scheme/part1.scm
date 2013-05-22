; CS550 Assignment 5 Part 1
; Proposition calculus scheme interpreter

(define (beval exp env)
  (cond
    ((boolean? exp) exp)
    ((symbol? exp) (lookup exp env))
    ((conjuct? exp) (beval-and exp env))
    ((disjunct? exp) (beval-or exp env))
    ((negation? exp) (beval-not exp env))
    ((implies? exp) (beval-implies exp env))
    ((equiv? exp) (beval-equiv exp env))
    (else (quote "beval: illegal syntax"))))

; Start boolean stuff

(define (boolean? exp)
  (cond
    ((eq? exp #t) #t)
    ((eq? exp #f) #t)
    (else #f)))

;End boolean stuff

;Start variable stuff

(define (lookup exp env)
  (if (null? env)
    (quote "beval: bad variable")
    (if (eq? exp (car (car env)))
      (cadr (car env))
      (lookup exp (cdr env)))))

;End variable stuff

;Start AND stuff

; TODO check the size of the exp list?
(define (conjuct? exp)
  (if (eq? (car exp) 'and)
     #t
     #f))
  
; If the first expression in the and is false, then whole thing is false
; Otherwise, strip off the first true expression and look at the rest
(define (beval-and exp env)
  (if (null? (cdr exp)) ;nothing after the 'and symbol
    #t ;looked at all conditions so the whole and expression is true.
    (if (not (first-and-or exp env))
      #f ;at least one expression is false so the whole thing is false.
      (beval-and (next-and exp) env))))
    
(define (first-and-or exp env)
  (beval (cadr exp) env))

(define (next-and exp)
 (cons 'and (cdr (cdr exp))))

;End AND stuff

;Start OR stuff

; TODO check the size of the exp list?
(define (disjunct? exp)
  (if (eq? (car exp) 'or)
     #t
     #f))
  
; If the first expression in the or is true, then whole thing is true
; Otherwise, strip off the first false expression and look at the rest
(define (beval-or exp env)
  (if (null? (cdr exp)) ;nothing after the 'or symbol
    #f ;looked at all conditions so the whole or expression is false.
    (if (first-and-or exp env)
      #t ;at least one expression is true so the whole thing is true.
      (beval-or (next-or exp) env))))

(define (next-or exp)
 (cons 'or (cdr (cdr exp))))

;End OR stuff

;Start NOT stuff

; TODO check the size of the exp list?
(define (negation? exp)
  (if (eq? (car exp) 'not)
     #t
     #f))

(define (beval-not exp env)
  (if (eq? (beval (cadr exp) env) #t)
    #f
    (if (eq? (beval (cadr exp) env) #f) ;done this way for debug purposes, not the best i know
      #t
      (quote "bad beval-not"))))

;End NOT stuff

;Start IMPLIES stuff

(define (implies? exp)
  (if (eq? (car exp) 'implies)
     #t
     #f))

;p => q    If p and !q then false, otherwise true
(define (beval-implies exp env)
  (if (eq? (beval (get-p exp) env) #f)
      #t
      (if (eq? (beval (get-q exp) env) #f)
        #f
	#t)))

(define (get-p exp)
  (cadr exp))

(define (get-q exp)
  (caddr exp))

;End IMPLIES stuff

;Start EQUIV stuff

(define (equiv? exp)
  (if (eq? (car exp) 'equiv)
     #t
     #f))

;p <=> q    If p != q then false, otherwise true
(define (beval-equiv exp env)
  (if (eq? (beval (get-p exp) env) #t)
      (if (eq? (beval (get-q exp) env) #f)
        #f
	#t)
      (if (eq? (beval (get-q exp) env) #t)
	#f
	#t)))

;End EQUIV stuff


