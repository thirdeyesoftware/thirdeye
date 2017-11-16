DROP TABLE ASSET CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM ASSET;

DROP SEQUENCE ASSET_SEQ;

DROP PUBLIC SYNONYM ASSET_SEQ;

CREATE TABLE ASSET ( 
	ASSETID NUMBER NOT NULL,
	DESCRIPTION VARCHAR(200),
	SERIALNUMBER VARCHAR(50),
	PRODUCTID NUMBER NOT NULL,
	FOREIGN KEY(PRODUCTID) REFERENCES
	PRODUCT(PRODUCTID),
  CONSTRAINT PK_ASSET_ID
  PRIMARY KEY ( ASSETID ) 
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

CREATE PUBLIC SYNONYM ASSET FOR ASSET;

CREATE SEQUENCE ASSET_SEQ START WITH 1000;

CREATE PUBLIC SYNONYM ASSET_SEQ FOR ASSET_SEQ;

