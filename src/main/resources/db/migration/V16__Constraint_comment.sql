alter table if exists comment
    add constraint FK_comment_users
        foreign key (user_id) references users;

alter table if exists comment
    add constraint FK_comment_task
        foreign key (task_id) references task;