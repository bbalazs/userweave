#*******************************************************************************
# This file is part of UserWeave.
#
#     UserWeave is free software: you can redistribute it and/or modify
#     it under the terms of the GNU Affero General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     (at your option) any later version.
#
#     UserWeave is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU Affero General Public License for more details.
#
#     You should have received a copy of the GNU Affero General Public License
#     along with UserWeave.  If not, see <http://www.gnu.org/licenses/>.
#
# Copyright 2012 User Prompt GmbH | Psychologic IT Expertise
#*******************************************************************************
#!/usr/bin/perl

$statements =
"
CREATE TABLE study_supportedlocales (
   study_id int NOT NULL,
   element varchar(255)
);

ALTER TABLE study_supportedlocales ADD CONSTRAINT fkcdd5aca1c88f7cf1
FOREIGN KEY (study_id) REFERENCES study(id) ON DELETE NO ACTION ON UPDATE NO ACTION;

insert into study_supportedlocales 
select id, 'de' from study
union
select id, 'en' from study;

update study set locale = 'de' where locale is null;

CREATE TABLE localized_string (
    id integer NOT NULL,
    de text,
    en text
);

ALTER TABLE public.localized_string OWNER TO postgres;

ALTER TABLE ONLY localized_string
    ADD CONSTRAINT localized_string_pkey PRIMARY KEY (id);


-- temporary field to put id values from tables
alter table localized_string add column temp_id integer;

-- 
-- PATTERN ALTER TABLE #TABLE# ADD COLUMN #COLUMN#_id INTEGER;
-- PATTERN INSERT INTO localized_string SELECT nextval('hibernate_sequence'), #COLUMN#, #COLUMN#, id FROM #TABLE#;
-- PATTERN UPDATE #TABLE# t set #COLUMN#_id = (select l.id FROM localized_string l WHERE temp_id = t.id);
-- PATTERN ALTER TABLE #TABLE# DROP COLUMN #COLUMN#;
-- PATTERN UPDATE LOCALIZED_STRING SET temp_id = null;
--

-- TABLE antipodepair                      antipode1
-- TABLE antipodepair                      antipode2
-- TABLE freetext_configuration            description
-- TABLE freetext_configuration            freetext
-- TABLE itm_configuration                 description
-- TABLE questionnaire_configuration       description
-- TABLE questionnaire_question            name
-- TABLE questionnaire_question            text
-- TABLE questionnaire_ratingterm          text
-- TABLE rrt_configuration                 description
-- TABLE rrt_configuration                 postfix
-- TABLE rrt_configuration                 prefix
-- TABLE rrt_term                          description
-- TABLE rrt_term                          value
-- TABLE study                             description
-- TABLE study                             headline


-- CollectionOfElements => OneToMany Relationship
CREATE TABLE questionnaire_question_localized_string (
    questionnaire_question_id integer NOT NULL,
    possibleanswers_id integer NOT NULL
);


ALTER TABLE questionnaire_question_possibleanwers add column id integer;
UPDATE questionnaire_question_possibleanwers set id = nextval('hibernate_sequence');
INSERT INTO localized_string SELECT  id, element, element, id FROM questionnaire_question_possibleanwers;
INSERT INTO  questionnaire_question_localized_string SELECT questionnaire_question_id, id FROM questionnaire_question_possibleanwers;
DROP TABLE questionnaire_question_possibleanwers;
UPDATE LOCALIZED_STRING SET temp_id = null;
-- 

alter table localized_string drop column temp_id;
";

$PATTERN = "";
foreach (split("\n",$statements)) {
    print;
    print "\n";

    if(/PATTERN\s+(.*)$/) {
	$PATTERN .= $1;
	$PATTERN .= "\n";
    }
    if(/-- TABLE\s+(\S+)\s+(\S+)/) {
	$table = $1;
	$column = $2;
	$row = $PATTERN;
	$row =~ s/#TABLE#/$table/g;
	$row =~ s/#COLUMN#/$column/g;
	print $row;
    }
}










