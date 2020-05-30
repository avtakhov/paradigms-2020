divided(N, K) :- 
T is mod(N, K),
0 = T.

next_divider(N, X, X) :-
	X =< N,
	divided(N, X),
	!.
	
next_divider(N, X, D) :-
	X =< N,
	\+ divided(N, X),
	X1 is X + 1,
	next_divider(N, X1, D).

prime(N) :- 
	2 =< N,
	next_divider(N, 2, N).

composite(N) :-
	2 =< N, 
	\+ prime(N).

prime_divisors(1, []) :- !. 

prime_divisors(N, [H | T]) :-
	number(N), 
	!,
	next_divider(N, 2, H),
	N1 is div(N, H),
	prime_divisors(N1, T).

less(X, []).

less(X, [H | T]) :- X =< H.

prime_divisors(N, [H | T]) :-
	less(H, T),
	prime_divisors(N1, T),
	N is H * N1.