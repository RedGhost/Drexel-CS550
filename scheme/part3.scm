; CS550 Assignment 5 Part 1
; Proposition calculus scheme interpreter

;;; Requires SICP
(load "ch4-mceval.scm")


(trace-entry eval)
(trace-entry apply)
(trace-entry list-of-values)
(trace-entry eval-if)
(trace-entry eval-sequence)
(trace-entry eval-assignment)
(trace-entry eval-definition)
(trace-entry self-evaluating?)
(trace-entry quoted?)
(trace-entry text-of-quotation)
(trace-entry tagged-list?)
(trace-entry variable?)
(trace-entry assignment?)
(trace-entry assignment-variable)
(trace-entry assignment-value)
(trace-entry definition?)
(trace-entry definition-variable)
(trace-entry definition-value)
(trace-entry lambda?)
(trace-entry lambda-parameters)
(trace-entry lambda-body)
(trace-entry make-lambda)
(trace-entry if?)
(trace-entry if-predicate)
(trace-entry if-consequent)
(trace-entry if-alternative)
(trace-entry make-if)
(trace-entry begin?)
(trace-entry begin-actions)
(trace-entry last-exp?)
(trace-entry first-exp)
(trace-entry rest-exps)
(trace-entry sequence->exp)
(trace-entry make-begin)
(trace-entry application?)
(trace-entry operator)
(trace-entry operands)
(trace-entry no-operands?)
(trace-entry first-operand)
(trace-entry rest-operands)
(trace-entry cond?)
(trace-entry cond-clauses)
(trace-entry cond-else-clause?)
(trace-entry cond-predicate)
(trace-entry cond-actions)
(trace-entry cond->if)
(trace-entry expand-clauses)
(trace-entry true?)
(trace-entry false?)
(trace-entry make-procedure)
(trace-entry compound-procedure?)
(trace-entry procedure-parameters)
(trace-entry procedure-body)
(trace-entry procedure-environment)
(trace-entry enclosing-environment)
(trace-entry first-frame)
(trace-entry make-frame)
(trace-entry frame-variables)
(trace-entry frame-values)
(trace-entry add-binding-to-frame!)
(trace-entry extend-environment)
(trace-entry lookup-variable-value)
(trace-entry set-variable-value!)
(trace-entry define-variable!)
(trace-entry setup-environment)
(trace-entry primitive-procedure?)
(trace-entry primitive-implementation)
(trace-entry primitive-procedure-names)
(trace-entry primitive-procedure-objects)
(trace-entry apply-primitive-procedure)
(trace-entry driver-loop)
(trace-entry prompt-for-input)
(trace-entry announce-output)
(trace-entry user-print)

(define the-global-environment (setup-environment))
(driver-loop)

(define (fact n)
  (if (= n 0) 1 (* n (fact (- n 1)))))

(fact 3)