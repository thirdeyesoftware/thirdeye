DROP TABLE ADDRESS CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM ADDRESS;

DROP SEQUENCE ADDRESS_SEQ;

DROP PUBLIC SYNONYM ADDRESS_SEQ;

CREATE TABLE ADDRESS ( 
	ADDRESSID NUMBER NOT NULL,
	ADDRESS1 VARCHAR(100) NOT NULL,
	ADDRESS2 VARCHAR(100) NULL,
	CITY VARCHAR(100) NOT NULL,
	STATE CHAR(2) NOT NULL,
	ZIPCODE VARCHAR(13) NOT NULL,
	COUNTRYID NUMBER DEFAULT 1,
	FOREIGN KEY (COUNTRYID)
	REFERENCES COUNTRY(COUNTRYID),
  CONSTRAINT PK_ADDRESS_ID
  PRIMARY KEY ( ADDRESSID ) 
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

CREATE PUBLIC SYNONYM ADDRESS FOR ADDRESS;

CREATE SEQUENCE ADDRESS_SEQ START WITH 1;

CREATE PUBLIC SYNONYM ADDRESS_SEQ FOR ADDRESS_SEQ;

