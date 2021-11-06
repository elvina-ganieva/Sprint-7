-- логин = пароль
insert into my_user(login, password)
values ('user', '$2a$10$BOLQKY9MmEi/2PHo4XK7I.P3YXvHyolN6VcgRuoNsLCPQ/Itga0cW');
insert into my_user(login, password, role)
values ('api', '$2a$10$56x5zTZ9EyPuJTAAQlFwc.ygRyQj1IT/KHW25p77mqhlOmQ5MUW4K', 'API');
insert into my_user(login, password, role)
values ('delete', '$2a$10$ldEIEgin/cMu3yGUJQ8g2OefhbprzQXqyIymVX/spchaBWnELge5a', 'DELETE');
