define Length proc ( list )
  if listp( list ) then
    current := list;
    length := 0;
    while not(nullp(current)) do
      length := length + 1;
      current := cdr(current)
    od;
    return length
  else
    return 0
  fi
end;

list := [1,2,3];
x := Length ( list )
