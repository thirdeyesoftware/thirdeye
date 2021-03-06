DROP TABLE PAYMENT_INSTRUMENT_TYPE CASCADE CONSTRAINTS;

DROP PUBLIC SYNONYM PAYMENT_INSTRUMENT_TYPE;

DROP SEQUENCE PAYMENT_INSTRUMENT_TYPE_SEQ;

DROP PUBLIC SYNONYM PAYMENT_INSTRUMENT_TYPE_SEQ;

CREATE TABLE PAYMENT_INSTRUMENT_TYPE ( 
	PAYMENTINSTRUMENTTYPEID NUMBER NOT NULL,
	PAYMENTINSTRUMENTTYPE VARCHAR(100) NOT NULL,
  CONSTRAINT PK_PAYMENT_INSTRUMENT_TYPE_ID
  PRIMARY KEY ( PAYMENTINSTRUMENTTYPEID ) 
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

CREATE PUBLIC SYNONYM PAYMENT_INSTRUMENT_TYPE FOR PAYMENT_INSTRUMENT_TYPE;

CREATE SEQUENCE PAYMENT_INSTRUMENT_TYPE_SEQ START WITH 1;

CREATE PUBLIC SYNONYM PAYMENT_INSTRUMENT_TYPE_SEQ FOR PAYMENT_INSTRUMENT_TYPE_SEQ;


insert into payment_instrument_type (
paymentinstrumenttypeid,
paymentinstrumenttype)
values (1, 'Credit Card');

insert into payment_instrument_type (
paymentinstrumenttypeid,
paymentinstrumenttype)
values (2, 'Electronic Check');

insert into payment_instrument_type (
paymentinstrumenttypeid,
paymentinstrumenttype)
values (3, 'ACH Transfer');

insert into payment_instrument_type (
paymentinstrumenttypeid,
paymentinstrumenttype)
values (4, 'Check');
