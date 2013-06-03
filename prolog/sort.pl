sort(X,Y) :- permute(X,Y), sorted(Y).

insert(X,YS,[X|YS]).
insert(X,[Y|YS],[Y|ZS]) :- insert(X,YS,ZS).

permute([],[]).
permute([X|XS],YS) :- permute(XS,ZS), insert(X,ZS,YS).

sorted([]).
sorted([_]).
sorted([XA,XB|XS]) :- XA =< XB, sorted([XB|XS]).

