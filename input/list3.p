list := [[1,2,3],2,[4,5]] ;
a := 12 ;
d := cons((a),(list)) ;
b := a ;
c := car(cdr(list)) ;
cc := car([[[2,3],5],1]) ;
e := cdr(list) ;
ee := cdr([2,[[2,3],5],1]);
f := intp(a);
g := intp(list);
h := listp(a);
i := listp(list);
j := nullp(list);
k := list || list || list || (cdr(list))