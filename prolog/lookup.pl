assoc(X,[[X,Y] | _],Y).
assoc(X,[_ | YS],Z) :- assoc(X,YS,Z).
lookup(X,Y) :- assoc(X,[[break,statement],[double,type],[int,type],[return,statement]],Y),!.
lookup(_,identifier).