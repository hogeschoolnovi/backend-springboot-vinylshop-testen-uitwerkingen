
-- Insert into publishers
insert into publishers (created_date, edited_date, address, contact_details, name)
values (now(), now(), 'test', 'publisher van Uitgever', 'newtestpublisher');

-- Insert into genres
insert into genres (created_date, edited_date, name, description)
values (now(), now(), 'test', 'Rock genre description'),
       (now(), now(), 'testy', 'Jazz genre description');

-- -- Insert into Albums using dynamically retrieved genre_id and publisher_id
-- insert into Albums (created_date, edited_date, genre_id, publisher_id, title, release_year)
-- values (
--            now(),
--            now(),
--            (select id from genres where name = 'Rock'),
--            (select id from publishers where name = 'newpublisher'),
--            'new album',
--            2021
--        );
--
-- -- Insert into artists
-- insert into artists (created_date, edited_date, biography, name)
-- values (now(), now(), 'just started', 'nerdyJava');
--
--

