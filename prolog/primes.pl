divided(N, K) :- 
T is mod(N, K),
0 = T.

next_divider(N, X, X) :-
	X < N,
	divided(N, X).
	
next_divider(N, X, D) :-
	SQR is X * X,
	SQR =< N,
	\+ divided(N, X),
	X1 is X + 1,
	next_divider(N, X1, D).

prime_divisors(N, X, [H | T]) :-
	next_divider(N, X, D),
	H is D,
	N1 is div(N, D),
	prime_divisors(N1, D, T).


prime_divisors(1, _, []).

prime_divisors(N, X, [N]) :-
	\+ next_divider(N, X, _),
	N > 1.

prime_divisors(N, A) :-
	prime_divisors(N, 2, A).

composite(N) :- 
	N > 1,
	next_divider(N, 2, _).

prime(N) :- 
	N > 1,
	\+ composite(N).

