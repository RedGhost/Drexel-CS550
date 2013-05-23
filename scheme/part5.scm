; CS550 Assignment 5 Part 5
; Mini language scheme interpreter

(define (eval prog env)
    (eval-stmtlist prog env))

(define (eval-stmtlist stmtlist env)
  (if (null? stmtlist)
    env
    (eval-stmtlist (cdr stmtlist) (eval-stmt (car stmtlist) env))))

(define (eval-stmt stmt env)
  (cond
    ((assign? stmt) (eval-assign stmt env))
    ((if? stmt) (eval-if stmt env))
    ((while? stmt) (eval-while stmt env))
    (else (quote "illegal statement"))))

(define (while? stmt)
  (if (eq? (car stmt) 'while)
    #t
    #f))

(define (eval-while stmt env)
  (loop (cadr stmt) (caddr stmt) env))

(define (loop expr stmtlist env)
  (if (> (eval-expr expr env) 0)
   (loop expr stmtlist (eval-stmtlist stmtlist env))
    env))

(define (if? stmt)
  (if (eq? (car stmt) 'if)
    #t
    #f))

(define (eval-if stmt env)
  (let ((expr (cadr stmt)) (S1 (caddr stmt)) (S2 (cadddr stmt)))
    (if (> (eval-expr expr env) 0)
      (eval-stmtlist S1 env)
      (eval-stmtlist S2 env))))

;Start assign code
(define (assign? stmt)
  (if (eq? (car stmt) 'assign)
    #t
    #f))

(define (eval-assign stmt env)
  (let ((var (cadr stmt)) (expr (caddr stmt)))
    (insert-binding var (eval-expr expr env) env)))

(define (insert-binding var value env)
  (if (in-env? var env)
    (update-value var value env)
    (cons (list var value) env)))

(define (in-env? var env)
  (if (null? env)
    #f ;reached the end of the env, so the var isn't in it.
    (if (eq? var (car (car env)))
      #t
      (in-env? var (cdr env)))))

;This works by deleting the current binding of the value from the environment and then re-adding the variable name with the new value. Not elegant but works.
(define (update-value var value env)
  (cons (list var value) (delete-from-list var env)))

(define (delete-from-list var env)
  (if (eq? var (car (car env)))
    (cdr env)
    (cons (car env) (delete-from-list var (cdr env)))))
;End assign code

;Start expression code
(define (eval-expr expr env)
  (cond
    ((number? expr) expr)
    ((symbol? expr) (lookup expr env))
    ((plus? expr) (eval-plus expr env))
    ((minus? expr) (eval-minus expr env))
    ((times? expr) (eval-times expr env))
    (else (quote "illegal expression"))))

(define (lookup expr env)
  (if (null? expr)
    (quote "bad lookup expression")
    (if (eq? expr (car (car env)))
      (cadr (car env))
      (lookup expr (cdr env)))))

(define (plus? exp)
  (if (eq? (car exp) '+)
     #t
     #f))

(define (eval-plus expr env)
  (if (not (eq? (length expr) 3))
    (quote "bad plus expression")
    (+ (eval-expr (cadr expr) env) (eval-expr (caddr expr) env))))

(define (minus? exp)
  (if (eq? (car exp) '-)
     #t
     #f))

(define (eval-minus expr env)
  (if (not (eq? (length expr) 3))
    (quote "bad minus expression")
    (- (eval-expr (cadr expr) env) (eval-expr (caddr expr) env))))

(define (times? exp)
  (if (eq? (car exp) '*)
     #t
     #f))

(define (eval-times expr env)
  (if (not (eq? (length expr) 3))
    (quote "bad times expression")
    (* (eval-expr (cadr expr) env) (eval-expr (caddr expr) env))))
;End expression code