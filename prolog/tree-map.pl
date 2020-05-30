node(X, Val, Y, L, R).

split(null, K, null, null).

split(node(X, Val, Y, L, R), K, node(X, Val, Y, L, R1), S) :-
	K >= X,
	split(R, K, R1, S).

split(node(X, Val, Y, L, R), K, F, node(X, Val, Y, L1, R)) :-
	K < X,
	split(L, K, F, L1).

merge(T, null, T) :- !.

merge(null, T, T) :- !.

merge(node(X1, Val1, Y1, L1, R1), node(X2, Val2, Y2, L2, R2), node(X1, Val1, Y1, L1, Right)) :-
	Y2 >= Y1,
	merge(R1, node(X2, Y2, L2, R2), Right).

merge(node(X1, Val1, Y1, L1, R1), node(X2, Val2, Y2, L2, R2), node(X2, Val2, Y2, Left, R2)) :-
	Y2 < Y1,
	merge(node(X1, Y1, L1, R1), L2, Left).

% merge(node(1, V, 1, null, null), node(3, V, 0, null, null)).
% split(node(3,V,0,node(1, V,1,null,null),null), 2, F, S).

map_get(node(X, Val, Y, L, R), X, Val) :- !.

map_get(node(X, Val, Y, L, R), Key, Value) :-
	Key < X,
	map_get(L, Key, Value).

map_get(node(X, Val, Y, L, R), Key, Value) :-
	Key > X,
	map_get(R, Key, Value).

random(X) :- rand_int(10000, X).

map_put(Tree, Key, Value, Res) :-
	split(Tree, Key, L, R),
	split(L, Key - 1, L1, R1),
	random(Rand),
	merge(L1, node(Key, Value, Rand, null, null), Res1),
	merge(Res1, R, Res).

map_build([], null).

map_build([(Key, Value) | T], Tree) :-
	 map_build(T, Tree1),
	 map_put(Tree1, Key, Value, Tree).