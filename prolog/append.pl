myappend([],Y,Y).
myappend([X|XS],Y,[X|ZS]) :- myappend(XS,Y,ZS).
