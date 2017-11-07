DROP TABLE SUBSCRIPTION_TYPE CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM SUBSCRIPTION_TYPE;

DROP SEQUENCE SUBSCRIPTION_TYPE_SEQ;

DROP PUBLIC SYNONYM SUBSCRIPTION_TYPE_SEQ;

CREATE TABLE SUBSCRIPTION_TYPE ( 
	SUBSCRIPTIONTYPEID NUMBER NOT NULL,
	DESCRIPTION VARCHAR(200) NULL,
	GENERATEDTYPEID NUMBER,
	DEFAULTSUBSCRIPTIONPROTOTYPEID NUMBER,
	ACTIVE CHAR(1) DEFAULT 'Y',
	FOREIGN KEY (GENERATEDTYPEID)
	REFERENCES SUBSCRIPTION_TYPE(SUBSCRIPTIONTYPEID),
	FOREIGN KEY (DEFAULTSUBSCRIPTIONPROTOTYPEID)
	REFERENCES SUBSCRIPTION_PROTOTYPE(SUBSCRIPTIONPROTOTYPEID),
  CONSTRAINT PK_SUBSCRIPTION_TYPE_ID
  PRIMARY KEY ( SUBSCRIPTIONTYPEID ) 
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

CREATE PUBLIC SYNONYM SUBSCRIPTION_TYPE FOR SUBSCRIPTION_TYPE;

CREATE SEQUENCE SUBSCRIPTION_TYPE_SEQ START WITH 1;

CREATE PUBLIC SYNONYM SUBSCRIPTION_TYPE_SEQ FOR SUBSCRIPTION_TYPE_SEQ;


insert into subscription_type (
subscriptiontypeid, description,
generatedtypeid, defaultsubscriptionprototypeid, active)
values (
1, 'PublicMember', null, 3, 'Y');

insert into subscription_type (
subscriptiontypeid, description,
generatedtypeid, defaultsubscriptionprototypeid, active)
values (
2, 'PublicVenue', 1, null, 'Y');


insert into subscription_type (
subscriptiontypeid, description,
generatedtypeid, defaultsubscriptionprototypeid, active)
values (
5, 'PrivateMember', null, 5, 'Y');

insert into subscription_type (
subscriptiontypeid, description,
generatedtypeid, defaultsubscriptionprototypeid, active)
values (
3, 'PrivateVenue', 5, null, 'Y');


insert into subscription_type (
subscriptiontypeid, description,
generatedtypeid, defaultsubscriptionprototypeid, active)
values (
6, 'VenueOperator', null, 6, 'Y');

insert into subscription_type (
subscriptiontypeid, description,
generatedtypeid, defaultsubscriptionprototypeid, active)
values (
4, 'VenueStaff', 6, 2, 'Y');

insert into subscription_type (
subscriptiontypeid, description,
generatedtypeid, defaultsubscriptionprototypeid, active)
values (
7, 'EquipmentLease', null, null, 'Y');

insert into subscription_type (
subscriptiontypeid, description,
generatedtypeid, defaultsubscriptionprototypeid, active)
values (
8, 'WebHosting', null, null, 'Y');
