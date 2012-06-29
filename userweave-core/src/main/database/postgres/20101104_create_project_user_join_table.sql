drop table if exists project_user_role_join;

create table project_user_role_join
(
	projectId integer NOT NULL,
	userId integer NOT NULL,
	constraint foreign_key_project foreign key (projectId) references project (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	constraint foreign_key_user foreign key (userId) references all_user (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

INSERT INTO project_user_role_join (projectId, userId)
SELECT id, owner_id FROM project;

commit;