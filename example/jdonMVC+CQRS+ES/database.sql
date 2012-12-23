use test;
create table testuser (
       userId           char(20) not null,
       name             varchar(20) null,
       age              int(5) default NULL,
       PRIMARY KEY  (userId)
);
INSERT INTO testuser VALUES ('1', 'tony');
INSERT INTO testuser VALUES ('2', 'sunny');
INSERT INTO testuser VALUES ('3', 'kevin');

CREATE TABLE upload (
  objectId            char(50) NOT NULL default '',
  name                varchar(50) default '',
  description         varchar(200) default '',
  datas               LONGBLOB,
  size                int(20) NOT NULL default '0',  
  messageId           varchar(20) NOT NULL default '0',
  creationDate        VARCHAR(15) NOT NULL,
  contentType         varchar(50) default '',
  PRIMARY KEY  (objectId),
  KEY messageId (messageId)
)