DROP TABLE JMSState;
DROP TABLE JMSStore;

CREATE TABLE JMSState (recordHandle int, recordState int, recordGeneration int);
CREATE TABLE JMSStore (recordHandle int, recordState int, record LONG RAW);

CREATE INDEX JMSMSG_X ON JMSState (recordHandle);
CREATE INDEX JMSMSGQ_X ON JMSStore (recordHandle);
COMMIT;
