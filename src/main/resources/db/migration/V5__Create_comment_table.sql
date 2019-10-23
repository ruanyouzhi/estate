create table comment
(
    id BIGINT,
    parent_id BIGINT,
    type int,
    commentator BIGINT,
    gmt_create BIGINT,
    gmt_modified BIGINT,
    like_count BIGINT default 0,
    content VARCHAR(1024),
    constraint comment_pk
        primary key (id)
);





