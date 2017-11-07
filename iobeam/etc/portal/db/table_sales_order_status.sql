DROP TABLE SALES_ORDER_STATUS CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM SALES_ORDER_STATUS;

DROP SEQUENCE SALES_ORDER_STATUS_SEQ;

DROP PUBLIC SYNONYM SALES_ORDER_STATUS_SEQ;

CREATE TABLE SALES_ORDER_STATUS ( 
	SALESORDERSTATUSID NUMBER NOT NULL,
	STATUS VARCHAR(100) NOT NULL,
  CONSTRAINT PK_SALES_ORDER_STATUS_ID
  PRIMARY KEY ( SALESORDERSTATUSID ) 
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

CREATE PUBLIC SYNONYM SALES_ORDER_STATUS FOR SALES_ORDER_STATUS;

CREATE SEQUENCE SALES_ORDER_STATUS_SEQ START WITH 1;

CREATE PUBLIC SYNONYM SALES_ORDER_STATUS_SEQ FOR SALES_ORDER_STATUS_SEQ;


