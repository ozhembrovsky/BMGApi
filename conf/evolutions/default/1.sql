# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  constraint pk_category primary key (id))
;

create table job (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  category_id               bigint,
  constraint pk_job primary key (id))
;

alter table job add constraint fk_job_category_1 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_job_category_1 on job (category_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table category;

drop table job;

SET FOREIGN_KEY_CHECKS=1;

