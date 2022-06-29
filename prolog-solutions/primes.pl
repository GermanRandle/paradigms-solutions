sift(I, N, S) :- 
	assertz((composite(I) :- !)),
	I1 is I + S, I1 =< N, sift(I1, N, S).

sieve(I, N) :- (prime(I), I1 is I * I, \+ sift(I1, N, I), !), sieve_next(I, N), !.
sieve(I, N) :- sieve_next(I, N).
sieve_next(I, N) :- I1 is I + 2, SQ is I1 * I1, SQ =< N, sieve(I1, N).

prime(N) :- \+ composite(N).

get_smd(N, D, I) :- 0 is mod(N, I), D = I, !.
get_smd(N, D, I) :- I1 is I + 1, get_smd(N, D, I1).

prime_divisors(1, []) :- !.
prime_divisors(N, [N]) :- prime(N), !.

prime_divisors(N, [H | T]) :- number(N), 
	get_smd(N, D, 2), H = D, 
	N1 is N / H, prime_divisors(N1, T), !.
	
prime_divisors(N, [H1, H2 | T]) :- H1 =< H2,
	prime_divisors(N1, [H2 | T]), N is H1 * N1.

reduce([], [], R) :- R is 1, !.
reduce([H | T], [], R) :- reduce(T, [], R1), R is R1 * H, !.
reduce([], [H | T], R) :- reduce([], T, R1), R is R1 * H, !.
reduce([H1 | T1], [H2 | T2], R) :- H1 is H2, reduce(T1, T2, R1), R is R1 * H1, !.
reduce([H1 | T1], [H2 | T2], R) :- H1 < H2, reduce(T1, [H2 | T2], R1), R is R1 * H1, !.
reduce([H1 | T1], [H2 | T2], R) :- H1 > H2, reduce([H1 | T1], T2, R1), R is R1 * H2.

lcm(A, B, LCM) :- prime_divisors(A, AD), prime_divisors(B, BD), reduce(AD, BD, LCM).

init(N) :- \+ sift(4, N, 2), \+ sieve(3, N).

