alter table if exists files add constraint FK_files_comment foreign key (comment_id) references comment;
alter table if exists files add constraint FK_files_task foreign key (task_id) references task;