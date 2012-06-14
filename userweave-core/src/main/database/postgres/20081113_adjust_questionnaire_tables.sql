alter table questionnaire_question ADD COLUMN tmptext text;

update questionnaire_question set tmptext = text;

alter table questionnaire_question drop column text;

alter table questionnaire_question rename column tmptext to text;

commit;

alter table questionnaire_answer ADD COLUMN tmptext text;

update questionnaire_answer set tmptext = text;

alter table questionnaire_answer drop column text;

alter table questionnaire_answer rename column tmptext to text;

commit;

alter table questionnaire_answer ADD COLUMN answer varchar;

update questionnaire_answer set answer = aswer;

alter table questionnaire_answer drop column aswer;

commit;
