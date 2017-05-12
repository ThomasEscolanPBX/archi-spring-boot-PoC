insert into users(username, password, enabled, version) values ('test', 'test', TRUE, 0);
insert into user_roles(user_username, role, version) values ('test', 'ROLE_ADMIN', 0);

insert into users(username, password, enabled, version) values ('foo1', 'bar', TRUE, 0);
insert into user_roles(user_username, role, version) values ('foo1', 'ROLE_USER', 0);
insert into users(username, password, enabled, version) values ('foo2', 'bar', TRUE, 0);
insert into user_roles(user_username, role, version) values ('foo2', 'ROLE_USER', 0);

insert into country(name, version) values ('France', 0);
insert into country(name, version) values ('United Kingdom', 0);

insert into author(name, birth, version) values ('foobar', '2000-01-01', 0);
insert into author(name, birth, version) values ('foobar1', '2000-01-02', 0);
insert into author(name, birth, version) values ('foobar2', '2000-01-03', 0);