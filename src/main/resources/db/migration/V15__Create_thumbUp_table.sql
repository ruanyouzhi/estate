create table thumb_up
(
    fans bigint,
    comment_id bigint not null,
    question_id bigint not null,
    commentator bigint not null,
    constraint thumb_up_pk
        primary key (fans),
    foreign key (fans) references USER(ID) on delete cascade ,
    foreign key (comment_id) references COMMENT(ID) on delete cascade ,
    foreign key (question_id) references QUESTION(ID) on delete  cascade ,
    foreign key (commentator) references USER(ID) on delete cascade
);