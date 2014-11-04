
# table structure

DROP TABLE IF EXISTS user;

CREATE TABLE user (
  id int(8) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(25) NOT NULL,
  age int(3) unsigned NOT NULL,
  isAdmin bit(1) DEFAULT b'0',
  createdDate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=19223 DEFAULT CHARSET=utf8;
