## estate
---
## 资料
[bootstrap中文文档](https://v3.bootcss.com/)

[lombok官方文档](https://projectlombok.org)

mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
mvn flyway:migrate
找到占用8080的进程netstat -ano|findstr "8080"


create table notification
(
	id bigint auto_increment,
	notifier bigint,
	receiver bigint,
	outerId bigint not null,
	type int not null,
	gmt_create bigint,
	status int default 0 not null,
	constraint notification_pk
		primary key (id)
);