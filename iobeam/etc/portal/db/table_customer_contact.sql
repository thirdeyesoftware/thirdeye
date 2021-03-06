DROP TABLE CUSTOMER_CONTACT CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM CUSTOMER_CONTACT;

DROP SEQUENCE CUSTOMER_CONTACT_SEQ;

DROP PUBLIC SYNONYM CUSTOMER_CONTACT_SEQ;

CREATE TABLE CUSTOMER_CONTACT ( 
	CUSTOMERCONTACTID NUMBER NOT NULL,
	FIRST_NAME VARCHAR(100),
	MIDDLE_INITIAL VARCHAR(1),
	LAST_NAME VARCHAR(100),
	COMPANYNAME VARCHAR(100),
	PHONENUMBER VARCHAR(100) NOT NULL,
	FAXNUMBER VARCHAR(100),
	EMAILADDRESS VARCHAR(100),
	ADDRESSID NUMBER,
	FOREIGN KEY (ADDRESSID)
	REFERENCES ADDRESS(ADDRESSID),
	PRIMARYBILLINGDELIVERYID NUMBER,
  CONSTRAINT PK_CUSTOMER_CONTACT_ID
  PRIMARY KEY ( CUSTOMERCONTACTID ) 
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

CREATE PUBLIC SYNONYM CUSTOMER_CONTACT FOR CUSTOMER_CONTACT;

CREATE SEQUENCE CUSTOMER_CONTACT_SEQ START WITH 1;

CREATE PUBLIC SYNONYM CUSTOMER_CONTACT_SEQ FOR CUSTOMER_CONTACT_SEQ;


