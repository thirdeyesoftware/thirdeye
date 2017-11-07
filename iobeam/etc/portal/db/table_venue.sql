DROP TABLE VENUE CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM VENUE;

DROP SEQUENCE VENUE_SEQ;

DROP PUBLIC SYNONYM VENUE_SEQ;

CREATE TABLE VENUE ( 
	VENUEID NUMBER NOT NULL,
	VENUENAME VARCHAR(100),
	SITE_SECURE_RANDOM NUMBER(20) NOT NULL,
	VENUETYPEID NUMBER,
	FOREIGN KEY(VENUETYPEID) REFERENCES VENUE_TYPE(VENUETYPEID),
	CUSTOMERID NUMBER NOT NULL,
  CONSTRAINT PK_VENUE_ID
  PRIMARY KEY ( VENUEID ) 
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

CREATE PUBLIC SYNONYM VENUE FOR VENUE;

CREATE SEQUENCE VENUE_SEQ START WITH 1;

CREATE PUBLIC SYNONYM VENUE_SEQ FOR VENUE_SEQ;


