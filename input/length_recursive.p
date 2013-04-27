define Length proc ( list )
  if listp( list ) then
    if nullp( list ) then
      return 0
    else
      return Length(cdr(list)) + 1
    fi
  else
    return 0
  fi
end;

list := [1,2,3,[3,4,5]];
x := Length ( list )
