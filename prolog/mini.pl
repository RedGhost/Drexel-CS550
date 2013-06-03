% Part I of assignment 4
% reduction rules for arithmetic expressions.
% Author: Jeremy Johnson

% test cases.
%
% reduce_all(times(plus(2,3),minus(5,1)),V).
%    V = 20 ?
%

reduce(plus(E,E2),plus(E1,E2)) :- reduce(E,E1).
reduce(minus(E,E2),minus(E1,E2)) :- reduce(E,E1).
reduce(times(E,E2),times(E1,E2)) :- reduce(E,E1).

reduce(plus(V,E),plus(V,E1)) :- reduce(E,E1).
reduce(minus(V,E),minus(V,E1)) :- reduce(E,E1).
reduce(times(V,E),times(V,E1)) :- reduce(E,E1).

reduce(plus(V1,V2),R) :- integer(V1), integer(V2), !, R is V1+V2.
reduce(minus(V1,V2),R) :- integer(V1), integer(V2), !, R is V1-V2.
reduce(times(V1,V2),R) :- integer(V1), integer(V2), !, R is V1*V2.

reduce_all(V,V) :- integer(V), !.
reduce_all(E,E2) :- reduce(E,E1), reduce_all(E1,E2).