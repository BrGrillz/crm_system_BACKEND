create table files (
    id int8 not null,
    name varchar(255),
    path varchar(255),
    file int8,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
)