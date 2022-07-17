drop table if exists tasks;
create table tasks(
    id Integer primary key auto_increment,
    description varchar(200) not null,
    done bit
);