makeadder :=
proc( x )
  return proc( y )
    return x + y
  end
end;
add := makeadder( 1 );
x := 2;
value := add ( 2 )
