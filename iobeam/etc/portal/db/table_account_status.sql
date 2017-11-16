DROP TABLE ACCOUNT_STATUS CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM ACCOUNT_STATUS;

DROP SEQUENCE ACCOUNT_STATUS_SEQ;

DROP PUBLIC SYNONYM ACCOUNT_STATUS_SEQ;

CREATE TABLE ACCOUNT_STATUS ( 
	ACCOUNTSTATUSID NUMBER NOT NULL,
	STATUS VARCHAR(100) NOT NULL,
  CONSTRAINT PK_ACCOUNT_STATUS_ID
  PRIMARY KEY ( ACCOUNTSTATUSID ) 
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

CREATE PUBLIC SYNONYM ACCOUNT_STATUS FOR ACCOUNT_STATUS;

CREATE SEQUENCE ACCOUNT_STATUS_SEQ START WITH 1;

CREATE PUBLIC SYNONYM ACCOUNT_STATUS_SEQ FOR ACCOUNT_STATUS_SEQ;

