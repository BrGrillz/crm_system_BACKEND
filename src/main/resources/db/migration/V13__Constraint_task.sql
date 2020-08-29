alter table if exists task_users
    add constraint FK_task_users_users
        foreign key (users_id) references users;

alter table if exists task_users
    add constraint FK_task_users_task
        foreign key (task_id) references task;

alter table if exists task
    add constraint FK_task_author_users
        foreign key (author_id) references users;

alter table if exists task
    add constraint FK_task_responsible_users
        foreign key (responsible_id) references users;

alter table if exists task
    add constraint FK_task_task_status
        foreign key (task_status_id) references task_status;
