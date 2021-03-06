DROP TABLE SUBSCRIPTION_ASSET CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM SUBSCRIPTION_ASSET;

CREATE TABLE SUBSCRIPTION_ASSET ( 
	SUBSCRIPTIONID NUMBER NOT NULL,
	ASSETID NUMBER NOT NULL,
	FOREIGN KEY(SUBSCRIPTIONID) REFERENCES
	SUBSCRIPTION(SUBSCRIPTIONID),
	FOREIGN KEY(ASSETID) REFERENCES
	ASSET(ASSETID),
  CONSTRAINT PK_SUBSCRIPTION_ASSET_ID
  PRIMARY KEY ( SUBSCRIPTIONID, ASSETID ) 
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

CREATE PUBLIC SYNONYM SUBSCRIPTION_ASSET FOR SUBSCRIPTION_ASSET;
