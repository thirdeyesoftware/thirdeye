DROP TABLE SUBSCRIPTION_STATUS CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM SUBSCRIPTION_STATUS;

DROP SEQUENCE SUBSCRIPTION_STATUS_SEQ;

DROP PUBLIC SYNONYM SUBSCRIPTION_STATUS_SEQ;

CREATE TABLE SUBSCRIPTION_STATUS ( 
	SUBSCRIPTIONSTATUSID NUMBER NOT NULL,
	STATUS VARCHAR(100),
  CONSTRAINT PK_SUBSCRIPTION_STATUS_ID
  PRIMARY KEY ( SUBSCRIPTIONSTATUSID ) 
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

CREATE PUBLIC SYNONYM SUBSCRIPTION_STATUS FOR SUBSCRIPTION_STATUS;

CREATE SEQUENCE SUBSCRIPTION_STATUS_SEQ START WITH 1;

CREATE PUBLIC SYNONYM SUBSCRIPTION_STATUS_SEQ FOR SUBSCRIPTION_STATUS_SEQ;


insert into subscription_status (
subscriptionstatusid, status)
values (1, 'created');

insert into subscription_status (
subscriptionstatusid, status)
values (2, 'active');

insert into subscription_status (
subscriptionstatusid, status)
values (3, 'closed');

insert into subscription_status (
subscriptionstatusid, status)
values (4, 'suspended');
