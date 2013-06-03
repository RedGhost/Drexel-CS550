num(0).
num(X):-num(Y),X is Y+1.

writeint(I,J) :- num(X),I =< X, X =< J, write(X), nl, fail.
writeint(I,J) :- num(X),I =< X, X =< J, write(X), nl, X = J, !, fail.
