create schema rave;

create table rave.users (
	    id serial primary key,
	    followers int,
		photo_link varchar(2048)
);

create table rave.user_followers (
	user_id int,
	follower_id int
);

insert into rave.movies (title, genres, imdb, poster_link, created_at)
values (1,
		129,
		'https://instagram.ftxl3-1.fna.fbcdn.net/vp/3889dabfea62fb3ff61b1d606e5a353a/5D31BCAF/t51.2885-19/s150x150/11191309_1674877519414943_649792557_a.jpg?_nc_ht=instagram.ftxl3-1.fna.fbcdn.net'
		);
insert into rave.movies (title, genres, imdb, poster_link, created_at)
values (2,
		129,
		'https://instagram.ftxl3-1.fna.fbcdn.net/vp/dc1dac7b49be919ed2997f8381e153d5/5D4B36D2/t51.2885-19/s150x150/40424285_277442789539909_2685725990302253056_n.jpg?_nc_ht=instagram.ftxl3-1.fna.fbcdn.net'
		);
insert into rave.movies (title, genres, imdb, poster_link, created_at)
values (3,
		129,
		'https://instagram.ftxl3-1.fna.fbcdn.net/vp/d15c7d9017e1c226a4968dc0e97b29e7/5D4612B8/t51.2885-19/s150x150/43915506_353241822101684_4865047148535742464_n.jpg?_nc_ht=instagram.ftxl3-1.fna.fbcdn.net'
		);
insert into rave.movies (title, genres, imdb, poster_link, created_at)
values (4,
		129,
		'https://instagram.ftxl3-1.fna.fbcdn.net/vp/c9aa95c51e1fd1cc992031ddfa321eec/5D40A44B/t51.2885-19/s150x150/54731637_402048060584784_409742719011782656_n.jpg?_nc_ht=instagram.ftxl3-1.fna.fbcdn.net'
		);

insert into rave.user_followers (user_id, follower_id)
values (1, 2);
insert into rave.user_followers (user_id, follower_id)
values (1, 3);
insert into rave.user_followers (user_id, follower_id)
values (1, 4);