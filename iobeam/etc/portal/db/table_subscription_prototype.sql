DROP TABLE SUBSCRIPTION_PROTOTYPE CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM SUBSCRIPTION_PROTOTYPE;

DROP SEQUENCE SUBSCRIPTION_PROTOTYPE_SEQ;

DROP PUBLIC SYNONYM SUBSCRIPTION_PROTOTYPE_SEQ;

CREATE TABLE SUBSCRIPTION_PROTOTYPE ( 
	SUBSCRIPTIONPROTOTYPEID NUMBER NOT NULL,
	DESCRIPTION VARCHAR(100) NOT NULL,
	SUBSCRIPTIONTYPEID NUMBER,
	FOREIGN KEY (SUBSCRIPTIONTYPEID) REFERENCES
	SUBSCRIPTION_TYPE(SUBSCRIPTIONTYPEID),
	MAXGENERATIONCOUNT NUMBER,
	DURATION NUMBER(20) NOT NULL,
	BILLINGCYCLEAMOUNT NUMBER(10,2) NOT NULL,
	BILLINGCYCLE NUMBER,
	BILLINGCYCLECOUNT NUMBER,
	ISTAXABLE CHAR(1) DEFAULT 'N',
	ISCOMMISSIONABLE CHAR(1) DEFAULT 'Y',
	DEFAULTCOMMISSIONRATE NUMBER(5,3),
	ALLOWANONYMOUSACCESS CHAR(1) DEFAULT 'N',
	REQUIRESREGISTRATION CHAR(1) DEFAULT 'N',
	ACTIVE CHAR(1) DEFAULT 'Y',
  CONSTRAINT PK_SUBSCRIPTION_PROTOTYPE_ID
  PRIMARY KEY ( SUBSCRIPTIONPROTOTYPEID ) 
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

CREATE PUBLIC SYNONYM SUBSCRIPTION_PROTOTYPE FOR SUBSCRIPTION_PROTOTYPE;

CREATE SEQUENCE SUBSCRIPTION_PROTOTYPE_SEQ START WITH 1;

CREATE PUBLIC SYNONYM SUBSCRIPTION_PROTOTYPE_SEQ FOR SUBSCRIPTION_PROTOTYPE_SEQ;

GRANT SELECT, INSERT, DELETE ON SUBSCRIPTION_PROTOTYPE TO PORTALUSER;



insert into subscription_prototype (
subscriptionprototypeid, description,
subscriptiontypeid,
maxgenerationcount, duration,
billingcycleamount, billingcycle, billingcyclecount, istaxable,
iscommissionable, defaultcommissionrate, allowanonymousaccess,
requiresregistration, active)
values (1, 'Anonymous Public Venue',
2, 
0, 9223372036854775807,
0.0, 1, 0, 'N', 'N', 0.0, 'Y',
'N', 'Y');


insert into subscription_prototype (
subscriptionprototypeid, description,
subscriptiontypeid,
maxgenerationcount, duration,
billingcycleamount, billingcycle, billingcyclecount, istaxable,
iscommissionable, defaultcommissionrate, allowanonymousaccess,
requiresregistration, active)
values (2, 'Venue Staff Default',
4, 
2, 9223372036854775807,
0.0, 1, 0, 'N', 'N', 0.0, 'N',
'Y', 'Y');

insert into subscription_prototype (
subscriptionprototypeid, description,
subscriptiontypeid,
maxgenerationcount, duration,
billingcycleamount, billingcycle, billingcyclecount, istaxable,
iscommissionable, defaultcommissionrate, allowanonymousaccess,
requiresregistration, active)
values (3, 'Public Member Default',
1, 
null, 9223372036854775807,
14.95, 1, 0, 'Y', 'Y', 12.0, 'N',
'Y', 'Y');

insert into subscription_prototype (
subscriptionprototypeid, description,
subscriptiontypeid,
maxgenerationcount, duration,
billingcycleamount, billingcycle, billingcyclecount, istaxable,
iscommissionable, defaultcommissionrate, allowanonymousaccess,
requiresregistration, active)
values (4, 'Private Venue 50',
3, 
50, 9223372036854775807,
495.0, 1, 0, 'Y', 'Y', 12.0, 'N',
'N', 'Y');

insert into subscription_prototype (
subscriptionprototypeid, description,
subscriptiontypeid,
maxgenerationcount, duration,
billingcycleamount, billingcycle, billingcyclecount, istaxable,
iscommissionable, defaultcommissionrate, allowanonymousaccess,
requiresregistration, active)
values (5, 'Private Member Default',
5, 
null, 9223372036854775807,
0.0, 1, 0, 'Y', 'Y', 12.0, 'N',
'Y', 'Y');

insert into subscription_prototype (
subscriptionprototypeid, description,
subscriptiontypeid,
maxgenerationcount, duration,
billingcycleamount, billingcycle, billingcyclecount, istaxable,
iscommissionable, defaultcommissionrate, allowanonymousaccess,
requiresregistration, active)
values (6, 'Venue Operator Default',
6, 
null, 9223372036854775807,
0.0, 1, 0, 'N', 'N', 0.0, 'N',
'Y', 'Y');


insert into subscription_prototype (
subscriptionprototypeid, description,
subscriptiontypeid,
maxgenerationcount, duration,
billingcycleamount, billingcycle, billingcyclecount, istaxable,
iscommissionable, defaultcommissionrate, allowanonymousaccess,
requiresregistration, active)
values (7, 'Promotional Venue Equipment Lease (6 mo)',
7, 
null, 15552000000,
0.0, 1, 0, 'N', 'N', 0.0, 'N',
'N', 'Y');

insert into subscription_prototype (
subscriptionprototypeid, description,
subscriptiontypeid,
maxgenerationcount, duration,
billingcycleamount, billingcycle, billingcyclecount, istaxable,
iscommissionable, defaultcommissionrate, allowanonymousaccess,
requiresregistration, active)
values (8, 'Web Hosting Services',
8, 
null, 9223372036854775807,
100.0, 1, 0, 'N', 'N', 0.0, 'N',
'N', 'Y');
