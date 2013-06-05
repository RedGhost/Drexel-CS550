% Assignment 7 Part 5
% reduction rules for arithmetic expressions.
% Author: Mark Albrecht, Timothy Hanna, Zachary Long, Mateusz Stankiewicz

% test cases.
%
% reduce_all(config(times(plus(x,3),minus(5,y)),[value(x,2),value(y,1)]),V).
%    V = config(20,[value(x,2),value(y,1)]) ? 
%
% reduce_program(config([assign(x,3),assign(y,4)],[]),Env).
% reduce_program(config([assign(x,plus(1,2))],[]),Env).
%
% reduce_program(config([if(3,[assign(x,3)],[assign(x,4)])],[]),Env).
% reduce_program(config([if(minus(0,4),[assign(x,3)],[assign(x,4)])],[]),Env).
%
% reduce_program(config([while(-3,[assign(x,3)])],[value(x,1)]),Env).
% reduce_program(config([while(x,[assign(x,-1)])],[value(x,1)]),Env).

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
reduce_stmt(config(assign(I,E),Env),Env1) :- reduce_value(config(E,Env),V), reduce_stmt(config(assign(I,V),Env),Env1).

reduce_stmt(config(if(V,L1,L2),Env),Env1) :- integer(V), V = 0, reduce_program(config(L2,Env),Env1).
reduce_stmt(config(if(V,L1,L2),Env),Env1) :- integer(V), V < 0, reduce_program(config(L2,Env),Env1).
reduce_stmt(config(if(V,L1,L2),Env),Env1) :- integer(V), V > 0, reduce_program(config(L1,Env),Env1).
reduce_stmt(config(if(E,L1,L2),Env),Env1) :- reduce_value(config(E,Env),V), reduce_stmt(config(if(V,L1,L2),Env),Env1).

reduce_stmt(config(while(V,L),Env),Env1) :- integer(V), V = 0, append(Env,[],Env1).
reduce_stmt(config(while(V,L),Env),Env1) :- integer(V), V < 0, append(Env,[],Env1).
reduce_stmt(config(while(V,L),Env),Env1) :- integer(V), V > 0, reduce_program(config(L,Env),Env1). %TODO check the loop value again.
reduce_stmt(config(while(E,L),Env),Env1) :- reduce_value(config(E,Env),V), reduce_stmt(config(while(V,L),Env),Env1).

%Statements will be passed into this function as a list. e.g. [assign(x,3),assign(y,4)]
%When the first statement is complete, the resulting environment is passed along as the environment for the next statement.
reduce_stmt_all(config([],Env),Env).
reduce_stmt_all(config([S|Ss],Env),Env2) :- reduce_stmt(config(S,Env),Env1), reduce_stmt_all(config(Ss,Env1),Env2).

reduce_program(config(StmtList,StaringEnv),Env) :- reduce_stmt_all(config(StmtList,StaringEnv),Env).
%%%
