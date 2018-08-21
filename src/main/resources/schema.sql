create table if not exists user (
    id int primary key AUTO_INCREMENT,
    name varchar(50),
    sex varchar(6)
);

create table if not exists article (
    id int(10),
    subject varchar(50),
    description varchar(50),
    created_at date
);

create table if not exists account (
    username varchar(50) primary key,
    password varchar(50),
    role char(2)
);
