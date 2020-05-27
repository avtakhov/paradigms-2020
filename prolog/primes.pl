divided(N, K) :- 
T is mod(N, K),
0 = T.

inc(N, R) :- number(N), !, R is N + 1.
inc(N, R) :- number(R), !, N is R - 1.

my_div(N, D, R) :- number(N), !, R is div(N, D).
my_div(N, D, R) :- number(R), !, N is R * D.

next_divider(N, X, D) :- table(N, X, D), !.

next_divider(N, X, X) :-
	number(N),
	!,
	X < N,
	divided(N, X),
	assert(table(N, X, X)).
	
next_divider(N, X, D) :-
	number(N),
	SQR is X * X,
	SQR =< N,
	\+ divided(N, X),
	inc(X, X1),
	next_divider(N, X1, D),
	assert(table(N, X1, D)).


next_divider(N, X, D) :-
	number(D),
	!,
	N is X * D.


prime_divisors(N, X, [D | T]) :-
	next_divider(N, X, D),
	my_div(N, D, N1),
	prime_divisors(N1, D, T).

prime_divisors(1, _, []).

prime_divisors(N, X, [D]) :-
	\+ next_divider(N, X, D),
	N > 1.

prime_divisors(N, A) :-
	prime_divisors(N, 2, A).

composite(N) :- 
	N > 1,
	next_divider(N, 2, _).

prime(N) :- 
	N > 1,
	\+ composite(N).

