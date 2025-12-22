create table daily_menus(
	daily_menu_id int primary key generated always as identity,
	daily_menu_date date not null unique default current_date,
	approved_by varchar not null
);

create type categories_of_dishes as enum ('первое', 'второе', 'десерт');

create table dishes(
	dish_id smallint primary key generated always as identity,
	dish_name varchar not null unique,
	dish_category categories_of_dishes not null,
	standard_temp_min smallint not null,
	standard_temp_max smallint not null
);

create table menu_dishes(
	menu_id int,
	dish_id smallint,
	primary key (menu_id, dish_id),
	foreign key (menu_id) references daily_menus(daily_menu_id),
	foreign key (dish_id) references dishes(dish_id)
);

create table feedback(
	feedback_id int primary key generated always as identity,
	dish_id smallint not null,
	feedback_rating smallint not null check (feedback_rating > 0 and feedback_rating < 6) default 1,
	feedback_comment text,
	feedback_date date not null default current_date,
	student_is_class smallint not null check (student_is_class > 0 and student_is_class < 12),
	anonymous bool not null default false,
	foreign key (dish_id) references dishes(dish_id)
);

create type status as enum ('новый', 'в обработке', 'завершённый');

create table complaints(
	complaint_id int primary key generated always as identity,
	complaint_date date not null default current_date,
	student_is_class smallint not null check (student_is_class > 0 and student_is_class < 12),
	complaint_text text,
	complaint_photo bytea,
	complaint_status status not null default 'новый',
	response text,
	responded_at timestamp
);

insert into daily_menus (daily_menu_date, approved_by) values
('2024-01-15', 'Иванова А.П.'),
('2024-01-16', 'Петров С.И.'),
('2024-01-17', 'Сидорова М.К.'),
('2024-01-18', 'Кузнецов В.А.'),
('2024-01-19', 'Николаева О.В.'),
('2024-01-22', 'Фёдоров Д.С.'),
('2024-01-23', 'Михайлова Е.П.'),
('2024-01-24', 'Алексеев И.Н.'),
('2024-01-25', 'Орлова Т.М.'),
('2024-01-26', 'Васильев Р.К.');

insert into dishes (dish_name, dish_category, standard_temp_min, standard_temp_max) values
('Борщ', 'первое', 65, 75),
('Куриный суп', 'первое', 60, 70),
('Гречневая каша', 'второе', 55, 65),
('Котлета куриная', 'второе', 60, 70),
('Пюре картофельное', 'второе', 58, 68),
('Компот из сухофруктов', 'второе', 10, 20),
('Шарлотка', 'десерт', 15, 25),
('Блины', 'десерт', 50, 60),
('Оладьи', 'десерт', 45, 55),
('Фруктовый салат', 'десерт', 5, 15);

insert into menu_dishes (menu_id, dish_id) VALUES
(1, 1), (1, 3), (1, 7),
(2, 2), (2, 4), (2, 8),
(3, 1), (3, 5), (3, 9),
(4, 2), (4, 6), (4, 10),
(5, 1), (5, 4), (5, 7),
(6, 2), (6, 3), (6, 8),
(7, 1), (7, 5), (7, 10),
(8, 2), (8, 4), (8, 9),
(9, 1), (9, 6), (9, 7),
(10, 2), (10, 3), (10, 8);

insert into feedback (dish_id, feedback_rating, feedback_comment, student_is_class, anonymous) values
(1, 5, 'Очень вкусный борщ!', 5, false),
(3, 4, 'Каша хорошая, но мало соли', 7, true),
(7, 5, 'Шарлотка просто объедение!', 3, false),
(4, 3, 'Котлеты суховатые', 9, true),
(2, 5, 'Лучший куриный суп!', 6, false),
(8, 4, 'Блины вкусные, но мало сметаны', 4, true),
(5, 5, 'Пюре нежное и воздушное', 8, false),
(10, 4, 'Салат свежий, но мало заправки', 2, true),
(6, 3, 'Компот слишком сладкий', 10, false),
(9, 5, 'Оладьи просто супер!', 5, true);

insert into complaints (student_is_class, complaint_text, complaint_status, response, responded_at) values
(5, 'В столовой холодно, еда быстро остывает', 'завершённый', 'Проверили отопление, проблема устранена', '2024-01-15 14:30:00'),
(7, 'Не хватает столовых приборов', 'в обработке', 'Заказали дополнительные приборы', null),
(3, 'Грязные столы после предыдущих классов', 'новый', null, null),
(9, 'Очередь в столовой слишком длинная', 'завершённый', 'Оптимизировали график посещения', '2024-01-16 13:15:00'),
(6, 'Шумно в обеденном зале', 'в обработке', 'Проводим беседы с учениками', null),
(4, 'Не работает одна из микроволновок', 'завершённый', 'Микроволновка отремонтирована', '2024-01-17 10:45:00'),
(8, 'Мало вегетарианских блюд', 'новый', null, null),
(2, 'Хлеб часто заканчивается', 'в обработке', 'Увеличили закупку хлеба', null),
(10, 'Неудобное расписание столовой', 'новый', null, null),
(5, 'В салате попался маленький камушек', 'завершённый', 'Провели инструктаж с поварами', '2024-01-18 16:20:00');