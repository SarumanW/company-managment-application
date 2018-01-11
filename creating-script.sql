DROP TABLE LINKTYPES;
DROP TABLE LINKS;
DROP TABLE PARAMS;
DROP TABLE ATTRIBUTES;
DROP TABLE OBJECTS; 
DROP TABLE TYPES;

CREATE TABLE TYPES
    (type_id number(10) not null,
     name varchar2(20) not null,
     CONSTRAINT type_pk primary key (type_id));

INSERT INTO TYPES (type_id, name) VALUES (1, 'department');
INSERT INTO TYPES (type_id, name) VALUES (2, 'employee');
INSERT INTO TYPES (type_id, name) VALUES (3, 'customer');
INSERT INTO TYPES (type_id, name) VALUES (4, 'manager');
INSERT INTO TYPES (type_id, name) VALUES (5, 'project');
INSERT INTO TYPES (type_id, name) VALUES (6, 'sprint');
INSERT INTO TYPES (type_id, name) VALUES (7, 'task');

CREATE TABLE OBJECTS 
    (object_id number(10) not null,
     name varchar2(20) not null,
     CONSTRAINT object_pk primary key (object_id),
     type_id number(10) constraint object_type_fk references TYPES(type_id))

INSERT INTO OBJECTS (object_id, name, type_id) VALUES (11, 'Jackie', 2); 
INSERT INTO OBJECTS (object_id, name, type_id) VALUES (12, 'Sandra', 2);
INSERT INTO OBJECTS (object_id, name, type_id) VALUES (13, 'Julia', 2);
INSERT INTO OBJECTS (object_id, name, type_id) VALUES (14, 'Advertising', 1);
INSERT INTO OBJECTS (object_id, name, type_id) VALUES (15, 'Adriano', 3);
INSERT INTO OBJECTS (object_id, name, type_id) VALUES (16, 'Germiona', 4);
INSERT INTO OBJECTS (object_id, name, type_id) VALUES (17, 'School Advertising', 5);
INSERT INTO OBJECTS (object_id, name, type_id) VALUES (18, 'Creating Naming', 6);
INSERT INTO OBJECTS (object_id, name, type_id) VALUES (19, 'Create Name', 7);
    
CREATE TABLE ATTRIBUTES
    (attribute_id number(10) not null,
     name varchar2(20) not null,
     CONSTRAINT attribute_pk primary key (attribute_id),
     type_id number(10) constraint attribute_fk references TYPES(type_id));

INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (101, 'dep_name', 1);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (102, 'emp_name', 2);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (103, 'emp_surname', 2);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (104, 'emp_salary', 2);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (105, 'cus_name', 3);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (106, 'cus_surname', 3);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (107, 'man_name', 4);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (108, 'man_surname', 4);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (109, 'man_salary', 4);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (110, 'pr_name', 5);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (111, 'pr_start', 5);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (112, 'pr_end', 5);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (113, 'spr_name', 6);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (114, 'task_name', 7);
INSERT INTO ATTRIBUTES (attribute_id, name, type_id) VALUES (115, 'task_time', 7);

CREATE TABLE PARAMS
    (text_value varchar2(200),
     number_value number(38),
     date_value date,
     object_id number(10) constraint param_object_fk references OBJECTS(object_id),
     attribute_id number(10) constraint param_attr_fk references ATTRIBUTES(attribute_id));

INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Jackie', null, null, 11, 102);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Sandra', null, null, 12, 102);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Julia', null, null, 13, 102);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Chan', null, null, 11, 103);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Bulloc', null, null, 12, 103);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Roberts', null, null, 13, 103);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES (null, 2000, null, 11, 104);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES (null, 2000, null, 12, 104);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES (null, 2000, null, 13, 104);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Advertising', null, null, 14, 101);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Adriano', null, null, 15, 105);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Chelentano', null, null, 15, 106);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Germiona', null, null, 16, 107);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Grenjer', null, null, 16, 108);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES (null, 5000, null, 16, 109);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('School Advertising', null, null, 17, 110);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES (null, null, to_date('21-05-2017', 'dd-mm-yyyy'), 17, 111);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES (null, null, to_date('21-10-2017', 'dd-mm-yyyy'), 17, 112);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Creating Naming', null, null, 18, 113);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES ('Create Name', null, null, 19, 114);
INSERT INTO PARAMS (text_value, number_value, date_value, object_id, attribute_id) VALUES (null, 30, null, 19, 115);


CREATE TABLE LINKTYPES
    (link_type_id number(10),
     name varchar2(20),
     CONSTRAINT link_type_id_pk primary key (link_type_id));

INSERT INTO LINKTYPES (link_type_id, name) VALUES (150, 'dept-employee');
INSERT INTO LINKTYPES (link_type_id, name) VALUES (151, 'manager-project');
INSERT INTO LINKTYPES (link_type_id, name) VALUES (152, 'customer-project');
INSERT INTO LINKTYPES (link_type_id, name) VALUES (153, 'project-sprint');
INSERT INTO LINKTYPES (link_type_id, name) VALUES (154, 'sprint-task');
INSERT INTO LINKTYPES (link_type_id, name) VALUES (155, 'employee-task');

CREATE TABLE LINKS 
    (link_id number(10),
     CONSTRAINT link_id_pk primary key (link_id),
     parent_id number(10) constraint parent_id_fk references OBJECTS(object_id),
     child_id number(10) constraint child_id_fk references OBJECTS(object_id),
     link_type_id number(10) constraint lin_type__id_fk references LINKTYPES(link_type_id));

INSERT INTO LINKS (link_id, parent_id, child_id, link_type_id) VALUES (1001, 14, 11, 150);
INSERT INTO LINKS (link_id, parent_id, child_id, link_type_id) VALUES (1002, 14, 12, 150);
INSERT INTO LINKS (link_id, parent_id, child_id, link_type_id) VALUES (1003, 14, 13, 150);
INSERT INTO LINKS (link_id, parent_id, child_id, link_type_id) VALUES (1004, 16, 17, 151);
INSERT INTO LINKS (link_id, parent_id, child_id, link_type_id) VALUES (1005, 15, 17, 152);
INSERT INTO LINKS (link_id, parent_id, child_id, link_type_id) VALUES (1006, 17, 18, 153);
INSERT INTO LINKS (link_id, parent_id, child_id, link_type_id) VALUES (1007, 18, 19, 154);
INSERT INTO LINKS (link_id, parent_id, child_id, link_type_id) VALUES (1008, 11, 19, 155);
INSERT INTO LINKS (link_id, parent_id, child_id, link_type_id) VALUES (1009, 12, 19, 155);
INSERT INTO LINKS (link_id, parent_id, child_id, link_type_id) VALUES (1010, 13, 19, 155);
