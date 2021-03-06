DROP TABLE SECURITY_USER CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM SECURITY_USER;


CREATE TABLE SECURITY_USER ( 
	USERNAME VARCHAR(100) NOT NULL,
	PASSWORD VARCHAR(200) NOT NULL,
  CONSTRAINT PK_SECURITY_USER
  PRIMARY KEY ( USERNAME ) 
    USING INDEX 
     TABLESPACE IOBEAM PCTFREE 10
     STORAGE ( INITIAL 40K NEXT 40K PCTINCREASE 50 ))
   TABLESPACE IOBEAM NOLOGGING 
   PCTFREE 10
   PCTUSED 40
   INITRANS 1
   MAXTRANS 255
  STORAGE ( 
   INITIAL 40960
   NEXT 40960
   PCTINCREASE 50
   MINEXTENTS 1
   MAXEXTENTS 505
   FREELISTS 1 FREELIST GROUPS 1 ) 
NOCACHE;

CREATE PUBLIC SYNONYM SECURITY_USER FOR SECURITY_USER;



