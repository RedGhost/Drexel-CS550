% Assignment 7 Part 5
% reduction rules for arithmetic expressions.
% Author: Mark Albrecht, Timothy Hanna, Zachary Long, Mateusz Stankiewicz

% test cases.
%
% reduce_all(config(times(plus(x,3),minus(5,y)),[value(x,2),value(y,1)]),V).
%    V = config(20,[value(x,2),value(y,1)]) ? 
%
% reduce_program(config([assign(x,3),assign(y,4)],[]),Env).

lookup([value(I,V)|_],I,V).
lookup([_|Es],I,V) :- lookup(Es,I,V), !.

reduce(config(plus(E,E2),Env),config(plus(E1,E2),Env)) :- 
     reduce(config(E,Env),config(E1,Env)).
reduce(config(minus(E,E2),Env),config(minus(E1,E2),Env)) :- 
     reduce(config(E,Env),config(E1,Env)).
reduce(config(times(E,E2),Env),config(times(E1,E2),Env)) :- 
     reduce(config(E,Env),config(E1,Env)).

reduce(config(plus(V,E),Env),config(plus(V,E1),Env)) :- 
     reduce(config(E,Env),config(E1,Env)).
reduce(config(minus(V,E),Env),config(minus(V,E1),Env)) :- 
     reduce(config(E,Env),config(E1,Env)).
reduce(config(times(V,E),Env),config(times(V,E1),Env)) :- 
     reduce(config(E,Env),config(E1,Env)).

reduce(config(plus(V1,V2),Env),config(R,Env)) :- integer(V1), integer(V2), !, R is V1+V2.
reduce(config(minus(V1,V2),Env),config(R,Env)) :- integer(V1), integer(V2), !, R is V1-V2.
reduce(config(times(V1,V2),Env),config(R,Env)) :- integer(V1), integer(V2), !, R is V1*V2.

reduce(config(I,Env),config(V,Env)) :- atom(I), lookup(Env,I,V).

reduce_all(config(V,Env),config(V,Env)) :- integer(V), !.
reduce_all(config(E,Env),config(E2,Env)) :- 
     reduce(config(E,Env),config(E1,Env)), reduce_all(config(E1,Env),config(E2,Env)).

reduce_value(config(E,Env),V) :- reduce_all(config(E,Env),config(V,Env)).

%%% Assignment 7 Code
valueDelete(X,[],[]).
valueDelete(X,[value(X,V)|T],R) :- valueDelete(X,T,R).
valueDelete(X,[value(H,V)|T],[value(H,V)|R]) :- valueDelete(X,T,R).

reduce_stmt(config(assign(I,V),Env),Env1) :- atom(I), integer(V), valueDelete(I,Env,X), append([value(I,V)],X,Env1).
reduce_stmt(config(assign(I,E),Env),config(assign(I,V),Env)) :- reduce_value(config(E,Env),V).

%Statements will be passed into this function as a list. e.g. [assign(x,3),assign(y,4)]
%When the first statement is complete, the resulting environment is passed along as the environment for the next statement.
reduce_stmt_all(config([],Env),Env).
reduce_stmt_all(config([E|Es],Env),Env2) :- reduce_stmt(config(E,Env),Env1), reduce_stmt_all(config(Es,Env1),Env2).

reduce_program(config(StmtList,StaringEnv),Env) :- reduce_stmt_all(config(StmtList,StaringEnv),Env).
%%%
