select last_upd_sysusr_id, DCM_SIM_ID, SCHN_APPL_RUN_ID, BGN_TS as BEGIN_DT,END_TS as END_DT, SRVLT_NM, URL_QRY_PARM_TXT,((extract(DAY FROM END_TS - BGN_TS)*24*60*60)+
   (extract(HOUR FROM END_TS - BGN_TS)*60*60)+
   (extract(MINUTE FROM END_TS - BGN_TS)*60)+
   extract(SECOND FROM END_TS - BGN_TS)) / 60 as duration, SCHN_APPL_RSPN_CD  from schn_appl_run where trunc(BGN_TS) >= {d '2017-06-01'}
--and SCHN_TRIG_METH_TYP_CD = 0
AND SCHN_APPL_RUN.SRVLT_NM = 'XD_JB_LP'
--and LAST_UPD_SYSUSR_ID = 'scsync_batch_user'
order by SCHN_APPL_RUN_ID desc

select * from schn_appl_run order by schn_appl_run_id desc --where schn_appl_run_id = 2341

SELECT
      A.*, (extract(DAY FROM CPNT_END_TS - CPNT_BGN_TS)*24*60*60)+
   (extract(HOUR FROM CPNT_END_TS - CPNT_BGN_TS)*60*60)+
   (extract(MINUTE FROM CPNT_END_TS - CPNT_BGN_TS)*60)+
   extract(SECOND FROM CPNT_END_TS - CPNT_BGN_TS) RUN
   FROM SCHN_APPL_EVNT_LOG A, SCHN_APPL_CPNT B, SCHN_APPL_RUN R--, SCHN_APPL_WRKFLW C
   WHERE A.SCHN_TRANS_ID = R.SCHN_APPL_RUN_ID AND
       A.SCHN_APPL_CPNT_ID = B.SCHN_APPL_CPNT_ID
       --AND A.CRT_TS >= R.BGN_TS
    --AND A.CRT_TS <=  NVL(R.END_TS, CURRENT_TIMESTAMP)
    --AND A.SCHN_APPL_WRKFLW_ID = C.SCHN_APPL_WRKFLW_ID
   AND A.SCHN_APPL_CPNT_ID = 7
   --AND A.SCHN_APPL_WRKFLW_ID = 4
   AND A.SCHN_TRANS_ID = '3438'
   and a.SCHN_TRANS_WRKFLW_NBR = 892;

SELECT
      SCHN_APPL_CPNT_NM, A.SCHN_APPL_CPNT_ID, A.SCHN_APPL_WRKFLW_ID, AVG((extract(DAY FROM CPNT_END_TS - CPNT_BGN_TS)*24*60*60)+
   (extract(HOUR FROM CPNT_END_TS - CPNT_BGN_TS)*60*60)+
   (extract(MINUTE FROM CPNT_END_TS - CPNT_BGN_TS)*60)+
   extract(SECOND FROM CPNT_END_TS - CPNT_BGN_TS)) AVG_RUN,
  SUM((extract(DAY FROM CPNT_END_TS - CPNT_BGN_TS)*24*60*60)+
   (extract(HOUR FROM CPNT_END_TS - CPNT_BGN_TS)*60*60)+
   (extract(MINUTE FROM CPNT_END_TS - CPNT_BGN_TS)*60)+
   extract(SECOND FROM CPNT_END_TS - CPNT_BGN_TS)) SUM_RUN, count(*)
   FROM SCHN_APPL_EVNT_LOG A, SCHN_APPL_CPNT B, SCHN_APPL_RUN R--, SCHN_APPL_WRKFLW C
   WHERE A.SCHN_TRANS_ID = R.SCHN_APPL_RUN_ID AND
       A.SCHN_APPL_CPNT_ID = B.SCHN_APPL_CPNT_ID
       --AND A.CRT_TS >= R.BGN_TS
    --AND A.CRT_TS <=  NVL(R.END_TS, CURRENT_TIMESTAMP)
    --AND A.SCHN_APPL_WRKFLW_ID = C.SCHN_APPL_WRKFLW_ID
   --AND A.SCHN_APPL_CPNT_ID = 7
   --AND A.SCHN_APPL_WRKFLW_ID in (2,13,16,17)
   AND A.SCHN_TRANS_ID = '3438'
   GROUP BY SCHN_APPL_CPNT_NM,  A.SCHN_APPL_CPNT_ID, A.SCHN_APPL_WRKFLW_ID

select * from SCHN_APPL_WRKFLW where SCHN_APPL_WRKFLW_ID = 6

SELECT min(cpnt_bgn_ts), max(cpnt_end_ts) FROM schn_appl_evnt_log where SCHN_TRANS_ID = '2635'

SELECT count(distinct plnd_load_id) FROM PLND_LOAD_ORD_TMS_SHP_X
WHERE PO_CRT_DT > {d '2017-02-26'}
and PO_CRT_DT < {d '2017-03-06'}

select count() from plnd_load_sku_h h, PLND_LOAD_ORD_TMS_SHP_X X
where X.PO_CRT_DT >= {d '2017-02-26'}
and X.PO_CRT_DT <= {d '2017-03-06'}
AND X.PLND_LOAD_ID = H.PLND_LOAD_ID

select SUM(RND_UP_SKU_QTY / DC_BUY_UOM_QTY) from PLND_LTL_LOAD_SKU_H H,
where H.CRT_TS >= {d '2017-02-26'}
and H.CRT_TS <= {d '2017-03-06'}

select count(*) from schN_efs_rte where ACTV_FLG = 'Y' and eff_bgn_dt <= current_date
and eff_end_dt > current_date
and SCHN_EFS_RTE_CONFG_CD = 2

select count(distinct(SCHN_EFS_SCH_ID)) from OGVNDR_EFF_RTE_SCH WHERE EFF_BGN_DT < current_date
and eff_end_dt > current_date



select sum(DLY_TRUCK_ORD_MAX_QTY) from SCHN_EFS_RTE_STATS
where plnd_Load_eff_dt > {d '2017-02-26'}
and plnd_load_eff_dt < {d '2017-03-06'}

select --distinct grp.schn_efs_Rte_id, V.MBAS_MVNDR_NBR, LX.SRC_LOC_ID, sku.mbas_sku_nbr,
  sum(sku.PLND_LOAD_SKU_QTY * m.curr_cost_amt)
from plnd_load_grp_h grp, plnd_load_sku_h sku, plnd_load_h h, PLND_LOAD_ORD_TMS_SHP_X X,
  schn_efs_Rte_stp stp, SCHN_ORD_GRP og, SCHN_ORD_GRP_VNDR V, SCHN_ORD_GRP_LANE L, SCHN_CNSL_LOC_XREF LX, mvndr_sku_dc m
where X.PO_CRT_DT >= {d '2017-02-26'}
and X.PO_CRT_DT <= {d '2017-03-06'}
AND X.PLND_LOAD_ID = H.PLND_LOAD_ID
and h.plnd_load_grp_id = grp.PLND_LOAD_GRP_ID
AND grp.schn_efs_rte_id = stp.SCHN_EFS_RTE_ID
AND stp.SCHN_ORD_GRP_ID = og.SCHN_ORD_GRP_ID
AND og.SCHN_ORD_GRP_LANE_ID = L.SCHN_ORD_GRP_LANE_ID
AND OG.SCHN_ORD_GRP_ID = V.SCHN_ORD_GRP_ID
AND L.DEST_SCHN_LOC_ID = LX.SCHN_LOC_ID
AND sku.plnd_load_id = h.PLND_LOAD_ID
and v.MBAS_MVNDR_NBR = m.mvndr_nbr
and sku.MBAS_SKU_NBR = m.sku_nbr
and lx.src_loc_id = m.dc_nbr
and lx.SCHN_CNSL_LOC_SRC_SYS_CD = 2
--and sd.dc_nbr = lx.src_loc_id
--and sd.sku_nbr = sku.MBAS_SKU_NBR


select sum(m.curr_cost_amt * qty) from
  mvndr_sku_dc m,
  (select distinct grp.schn_efs_Rte_id, V.MBAS_MVNDR_NBR, LX.SRC_LOC_ID, sku.mbas_sku_nbr, sku.PLND_LOAD_SKU_QTY qty
from plnd_load_grp_h grp, plnd_load_sku_h sku, plnd_load_h h, PLND_LOAD_ORD_TMS_SHP_X X,
  schn_efs_Rte_stp stp, SCHN_ORD_GRP og, SCHN_ORD_GRP_VNDR V, SCHN_ORD_GRP_LANE L, SCHN_CNSL_LOC_XREF LX
where X.PO_CRT_DT >= {d '2017-02-26'}
and X.PO_CRT_DT <= {d '2017-03-06'}
AND X.PLND_LOAD_ID = H.PLND_LOAD_ID
and h.plnd_load_grp_id = grp.PLND_LOAD_GRP_ID
AND grp.schn_efs_rte_id = stp.SCHN_EFS_RTE_ID
AND stp.SCHN_ORD_GRP_ID = og.SCHN_ORD_GRP_ID
AND og.SCHN_ORD_GRP_LANE_ID = L.SCHN_ORD_GRP_LANE_ID
AND OG.SCHN_ORD_GRP_ID = V.SCHN_ORD_GRP_ID
AND L.DEST_SCHN_LOC_ID = LX.SCHN_LOC_ID
AND sku.plnd_load_id = h.PLND_LOAD_ID
and lx.SCHN_CNSL_LOC_SRC_SYS_CD = 2) A
WHERE m.mvndr_nbr = a.MBAS_MVNDR_NBR
and m.sku_nbr = a.MBAS_SKU_NBR
and m.dc_nbr = a.SRC_LOC_ID

delete FROM SCHN_APPL_RUN where LAST_UPD_TS < {d '2017-01-31'}

select count(*) from schN_appl_evnt_log

select min(CRT_TS) from schN_appl_evnt_log

select * from schN_appl_run where SRVLT_NM = 'XD_JB_PD' order by last_upd_ts desc

INSERT INTO ssys_parm(SUB_SYS_CD, SSYS_PARM_NM, SEQ_NBR, CRT_DT, SSYS_PARM_CHAR_VAL, SSYS_PARM_DEC_VAL, SSYS_PARM_INTG_VAL, SSYS_PARM_DT, SSYS_PARM_TM, SSYS_PARM_TS, ENV_VRBL_EXPD_FLG) VALUES ('XD', 'SKU_DIM_CACHE_READ', 1, TO_DATE('2012-12-04 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'N', null, null, null, null, null, 'N');
INSERT INTO ssys_parm(SUB_SYS_CD, SSYS_PARM_NM, SEQ_NBR, CRT_DT, SSYS_PARM_CHAR_VAL, SSYS_PARM_DEC_VAL, SSYS_PARM_INTG_VAL, SSYS_PARM_DT, SSYS_PARM_TM, SSYS_PARM_TS, ENV_VRBL_EXPD_FLG) VALUES ('XD', 'SKU_DIM_CACHE_WRITE', 1, TO_DATE('2012-12-04 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'N', null, null, null, null, null, 'N');
INSERT INTO ssys_parm(SUB_SYS_CD, SSYS_PARM_NM, SEQ_NBR, CRT_DT, SSYS_PARM_CHAR_VAL, SSYS_PARM_DEC_VAL, SSYS_PARM_INTG_VAL, SSYS_PARM_DT, SSYS_PARM_TM, SSYS_PARM_TS, ENV_VRBL_EXPD_FLG) VALUES ('XD', 'MIGRATION_CHUNK_SIZE', 1, TO_DATE('2017-02-06 16:09:02', 'YYYY-MM-DD HH24:MI:SS'), null, null, 5, null, null, null, 'N');
INSERT INTO ssys_parm(SUB_SYS_CD, SSYS_PARM_NM, SEQ_NBR, CRT_DT, SSYS_PARM_CHAR_VAL, SSYS_PARM_DEC_VAL, SSYS_PARM_INTG_VAL, SSYS_PARM_DT, SSYS_PARM_TM, SSYS_PARM_TS, ENV_VRBL_EXPD_FLG) VALUES ('XD', 'SYNC_SIM_READ_SCHEMA', 1, TO_DATE('2017-02-06 14:38:44', 'YYYY-MM-DD HH24:MI:SS'), 'SCSYSC031', null, null, null, null, null, 'N');
INSERT INTO ssys_parm(SUB_SYS_CD, SSYS_PARM_NM, SEQ_NBR, CRT_DT, SSYS_PARM_CHAR_VAL, SSYS_PARM_DEC_VAL, SSYS_PARM_INTG_VAL, SSYS_PARM_DT, SSYS_PARM_TM, SSYS_PARM_TS, ENV_VRBL_EXPD_FLG) VALUES ('XD', 'PO_RELEASE_RETRY_DELAY', 1, TO_DATE('2017-02-07 14:21:52', 'YYYY-MM-DD HH24:MI:SS'), null, 5000.000000, null, null, null, null, 'N');
INSERT INTO ssys_parm(SUB_SYS_CD, SSYS_PARM_NM, SEQ_NBR, CRT_DT, SSYS_PARM_CHAR_VAL, SSYS_PARM_DEC_VAL, SSYS_PARM_INTG_VAL, SSYS_PARM_DT, SSYS_PARM_TM, SSYS_PARM_TS, ENV_VRBL_EXPD_FLG) VALUES ('XD', 'PO_RELEASE_RETRY_MAX', 1, TO_DATE('2017-02-07 14:21:54', 'YYYY-MM-DD HH24:MI:SS'), null, 5.000000, null, null, null, null, 'N');

update ssys_parm set ssys_parm_char_val = 'SCSYSC021' where ssys_parm_nm = 'SYNC_SIM_READ_SCHEMA'

delete from scsysc021.plnd_load_grp;
delete from scsysc021.plnd_load;
delete from scsysc021.plnd_load_sku;
delete from scsysc021.plnd_ltl_load;
delete from scsysc021.plnd_ltl_load_sku;

delete from scsysc021.pre_plnd_load_grp;
delete from scsysc021.pre_plnd_load;
delete from scsysc021.pre_plnd_load_sku;
delete from scsysc021.pre_plnd_ltl_load;
delete from scsysc021.pre_plnd_ltl_load_sku;

select count(*) from scsysc021.pre_plnd_load_grp

select a.schn_efs_rte_id, a.plnd_load_Eff_dt, Z.plnd_load_eff_dt
FROM scsysc021.pre_plnd_load_grp A left JOIN scsysc02.pre_plnd_load_grp Z
  on a.schn_efs_rte_id = Z.schn_efs_rte_id and a.PLND_LOAD_EFF_DT = Z.plnd_load_eff_dt
WHERE Z.plnd_load_eff_dt is NULL

select * from scsysc021.pre_plnd_load_grp where schn_efs_rte_id = 2613

delete from LOAD_PLNG_FCST_DLY

   INSERT INTO load_plng_fcst_dly (LOAD_PLNG_DLY_FCST_ID,CRT_SYSUSR_ID,CRT_TS,LAST_UPD_SYSUSR_ID,LAST_UPD_TS,ORIG_TMS_FAC_ALIAS_ID,MBAS_SHP_TO_LOC_NBR,MBAS_SHP_TO_LOC_TYP_CD,MBAS_MVNDR_NBR,MBAS_DEPT_NBR,MBAS_SKU_NBR,MER_BASE_CD,PO_PROD_GRP_CD,DSVC_TYP_CD,DC_BUY_UOM_QTY,DCM_FCST_CPNT_TYP_CD,DAY_1_VAL,DAY_2_VAL,DAY_3_VAL,DAY_4_VAL,DAY_5_VAL,DAY_6_VAL,DAY_7_VAL,DAY_8_VAL,DAY_9_VAL,DAY_10_VAL,DAY_11_VAL,DAY_12_VAL,DAY_13_VAL,DAY_14_VAL,DAY_15_VAL,DAY_16_VAL,DAY_17_VAL,DAY_18_VAL,DAY_19_VAL,DAY_20_VAL,DAY_21_VAL,DAY_22_VAL,DAY_23_VAL,DAY_24_VAL,DAY_25_VAL,DAY_26_VAL,DAY_27_VAL,DAY_28_VAL, DCM_SIM_ID)
   select LOAD_PLNG_DLY_FCST_ID,CRT_SYSUSR_ID,CRT_TS,LAST_UPD_SYSUSR_ID,LAST_UPD_TS,ORIG_TMS_FAC_ALIAS_ID,MBAS_SHP_TO_LOC_NBR,MBAS_SHP_TO_LOC_TYP_CD,MBAS_MVNDR_NBR,MBAS_DEPT_NBR,MBAS_SKU_NBR,MER_BASE_CD,PO_PROD_GRP_CD,DSVC_TYP_CD,DC_BUY_UOM_QTY,DCM_FCST_CPNT_TYP_CD,DAY_1_VAL,DAY_2_VAL,DAY_3_VAL,DAY_4_VAL,DAY_5_VAL,DAY_6_VAL,DAY_7_VAL,DAY_8_VAL,DAY_9_VAL,DAY_10_VAL,DAY_11_VAL,DAY_12_VAL,DAY_13_VAL,DAY_14_VAL,DAY_15_VAL,DAY_16_VAL,DAY_17_VAL,DAY_18_VAL,DAY_19_VAL,DAY_20_VAL,DAY_21_VAL,DAY_22_VAL,DAY_23_VAL,DAY_24_VAL,DAY_25_VAL,DAY_26_VAL,DAY_27_VAL,DAY_28_VAL, 0
   FROM scsysc02.load_plng_fcst_dly_h where hist_crt_ts >  to_timestamp('2017-08-17 22:00:00', 'YYYY-MM-DD HH24:MI:SS')
   and hist_crt_ts <  to_timestamp('2017-08-18 04:00:00', 'YYYY-MM-DD HH24:MI:SS')
   and HIST_TYP_IND = 'PRE'

update scsysc021.load_plng_fcst_dly set dcm_rqst_id = 1, schn_appl_run_id = 100

delete from scsysc021.load_plng_fcst_dly


   INSERT INTO scsysc021.load_plng_fcst_dly (LOAD_PLNG_DLY_FCST_ID,CRT_SYSUSR_ID,CRT_TS,LAST_UPD_SYSUSR_ID,LAST_UPD_TS,ORIG_TMS_FAC_ALIAS_ID,MBAS_SHP_TO_LOC_NBR,MBAS_SHP_TO_LOC_TYP_CD,MBAS_MVNDR_NBR,MBAS_DEPT_NBR,MBAS_SKU_NBR,MER_BASE_CD,PO_PROD_GRP_CD,DSVC_TYP_CD,DC_BUY_UOM_QTY,DCM_FCST_CPNT_TYP_CD,DAY_1_VAL,DAY_2_VAL,DAY_3_VAL,DAY_4_VAL,DAY_5_VAL,DAY_6_VAL,DAY_7_VAL,DAY_8_VAL,DAY_9_VAL,DAY_10_VAL,DAY_11_VAL,DAY_12_VAL,DAY_13_VAL,DAY_14_VAL,DAY_15_VAL,DAY_16_VAL,DAY_17_VAL,DAY_18_VAL,DAY_19_VAL,DAY_20_VAL,DAY_21_VAL,DAY_22_VAL,DAY_23_VAL,DAY_24_VAL,DAY_25_VAL,DAY_26_VAL,DAY_27_VAL,DAY_28_VAL, DCM_SIM_ID, DCM_RQST_ID, SCHN_APPL_RUN_ID)

   select h.LOAD_PLNG_DLY_FCST_ID,h.CRT_SYSUSR_ID,h.CRT_TS,h.LAST_UPD_SYSUSR_ID,h.LAST_UPD_TS,h.ORIG_TMS_FAC_ALIAS_ID,h.MBAS_SHP_TO_LOC_NBR,h.MBAS_SHP_TO_LOC_TYP_CD,h.MBAS_MVNDR_NBR,h.MBAS_DEPT_NBR,h.MBAS_SKU_NBR,h.MER_BASE_CD,h.PO_PROD_GRP_CD,DSVC_TYP_CD,DC_BUY_UOM_QTY,DCM_FCST_CPNT_TYP_CD,DAY_1_VAL,DAY_2_VAL,DAY_3_VAL,DAY_4_VAL,DAY_5_VAL,DAY_6_VAL,DAY_7_VAL,DAY_8_VAL,DAY_9_VAL,DAY_10_VAL,DAY_11_VAL,DAY_12_VAL,DAY_13_VAL,DAY_14_VAL,DAY_15_VAL,DAY_16_VAL,DAY_17_VAL,DAY_18_VAL,DAY_19_VAL,DAY_20_VAL,DAY_21_VAL,DAY_22_VAL,DAY_23_VAL,DAY_24_VAL,DAY_25_VAL,DAY_26_VAL,DAY_27_VAL,DAY_28_VAL
   , 1001, 101, 100
FROM
   SCHN_ORD_GRP_VNDR A,
   SCHN_ORD_GRP B,
   SCHN_ORD_GRP_LANE C,
   SCHN_CNSL_LOC D,
   SCHN_CNSL_LOC E,
   SCHN_CNSL_LOC_XREF F,
   SCHN_CNSL_LOC_XREF G,
   LOAD_PLNG_FCST_DLY_H H,
   SKU_DC I,
   SCHN_EFS_RTE_STP J,
   SCHN_EFS_RTE K
WHERE
   A.SCHN_ORD_GRP_ID = B.SCHN_ORD_GRP_ID
AND B.SCHN_ORD_GRP_LANE_ID = C.SCHN_ORD_GRP_LANE_ID
AND C.ORIG_SCHN_LOC_ID = D.SCHN_LOC_ID
AND C.DEST_SCHN_LOC_ID = E.SCHN_LOC_ID
AND C.ORIG_SCHN_LOC_ID = F.SCHN_LOC_ID
AND C.DEST_SCHN_LOC_ID = G.SCHN_LOC_ID
AND A.SCHN_ORD_GRP_ID = J.SCHN_ORD_GRP_ID
AND J.SCHN_EFS_RTE_ID = K.SCHN_EFS_RTE_ID
AND F.SRC_LOC_ID = H.ORIG_TMS_FAC_ALIAS_ID
AND G.SRC_LOC_ID = H.MBAS_SHP_TO_LOC_NBR
AND A.MBAS_MVNDR_NBR = H.MBAS_MVNDR_NBR
AND A.MBAS_DEPT_NBR = H.MBAS_DEPT_NBR
AND A.MER_BASE_CD = H.MER_BASE_CD
AND H.MBAS_SKU_NBR = I.SKU_NBR
AND H.MBAS_SHP_TO_LOC_NBR = I.DC_NBR
AND J.SCHN_EFS_RTE_ID IN (4214,2043)
AND H.HIST_CRT_TS >= to_timestamp('2017-03-07 22:00:00', 'YYYY-MM-DD HH24:MI:SS')
--AND H.HIST_CRT_TS < to_timestamp('2017-03-07 22:00:00', 'YYYY-MM-DD HH24:MI:SS')
AND H.HIST_TYP_IND = 'PRE'


select * from plnd_load_grp where plnd_load_grp_id in (247456, 247114)


select plnd_load_grp_id, count(*) from plnd_load group by plnd_load_grp_id having count(*) > 5

select * from scsysc02.plnd_load_grp where schn_efs_Rte_id = 2043

select count(*) from scsysc02.plnd_load where plnd_load_grp_id = 247456

SELECT D.SCHN_ORD_GRP_VNDR_ID,D.MBAS_MVNDR_NBR, F.LOAD_PLNG_PARM_CD, PARM_CHAR_VAL, PARM_INTG_VAL, PARM_DEC_VAL, PARM_FLG_VAL,
       F.LOAD_PLNG_PARM_TYP_IND, G.LOAD_PLNG_PARM_CTGRY_CD, E.EFF_BGN_DT, E.EFF_END_DT
FROM SCHN_EFS_RTE  A,
	SCHN_EFS_RTE_STP B,
     SCHN_ORD_GRP  C,
     SCHN_ORD_GRP_VNDR  D,
     SCHN_ORD_GRP_VNDR_PARM E,
     LOAD_PLNG_PARM_CD F,
	LPLNG_PARM_CTGRY_XREF G
WHERE A.SCHN_EFS_RTE_ID in (4451,2596,1862)
AND   (A.EFF_BGN_DT <= {d '2017-03-08'} AND A.EFF_END_DT >= {d '2017-03-08'})
AND   A.ACTV_FLG = 'Y'
AND   A.SCHN_EFS_RTE_ID = B.SCHN_EFS_RTE_ID
AND   (B.EFF_BGN_DT <= {d '2017-03-08'} AND B.EFF_END_DT >= {d '2017-03-08'} )
AND   B.ACTV_FLG = 'Y'
AND   B.SCHN_ORD_GRP_ID = C.SCHN_ORD_GRP_ID
--AND   G.LOAD_PLNG_PARM_CTGRY_CD = 26
AND   (C.EFF_BGN_DT <= {d '2017-03-08'} AND C.EFF_END_DT >= {d '2017-03-08'} )
AND   C.ACTV_FLG = 'Y'
AND   C.SCHN_ORD_GRP_ID = D.SCHN_ORD_GRP_ID
AND   C.ACTV_FLG = D.ACTV_FLG
AND   (D.EFF_BGN_DT <= {d '2017-03-08'} AND D.EFF_END_DT >= {d '2017-03-08'} )
AND   D.SCHN_ORD_GRP_VNDR_ID = E.SCHN_ORD_GRP_VNDR_ID
AND   C.ACTV_FLG = E.ACTV_FLG
AND   (E.EFF_BGN_DT <= {d '2017-03-08'} AND E.EFF_END_DT >= {d '2017-03-08'} )
AND   E.LOAD_PLNG_PARM_CD = F.LOAD_PLNG_PARM_CD
AND   E.LOAD_PLNG_PARM_CD = G.LOAD_PLNG_PARM_CD
ORDER BY D.MBAS_MVNDR_NBR, F.LOAD_PLNG_PARM_CD

select * from OGVNDR_EFF_RTE_SCH where mbas_mvndr_nbr = 801743

select * from SCHN_ORD_GRP_VNDR_PARM where SCHN_ORD_GRP_VNDR_ID in (1682,2912,5065) and load_plng_parm_cd = 26

select * from load_plng_parm_cd where load_plng_parm_cd = 26

select h.mbas_sku_nbr,-- h.HIST_TYP_IND, h.DCM_FCST_CPNT_TYP_CD,
H.DAY_1_VAL ,
   H.DAY_2_VAL ,
   H.DAY_3_VAL ,
   H.DAY_4_VAL ,
   H.DAY_5_VAL ,
   H.DAY_6_VAL ,
   H.DAY_7_VAL ,
   H.DAY_8_VAL ,
   H.DAY_9_VAL ,
   H.DAY_10_VAL ,
   H.DAY_11_VAL ,
   H.DAY_12_VAL ,
   H.DAY_13_VAL ,
   H.DAY_14_VAL,
    H.DAY_15_VAL ,
   H.DAY_16_VAL ,
   H.DAY_17_VAL ,
   H.DAY_18_VAL ,
   H.DAY_19_VAL ,
   H.DAY_20_VAL ,
   H.DAY_21_VAL ,
   H.DAY_22_VAL ,
   H.DAY_23_VAL ,
   H.DAY_24_VAL ,
   H.DAY_25_VAL ,
   H.DAY_26_VAL ,
   H.DAY_27_VAL ,
   H.DAY_28_VAL
from scsysc02.load_plng_fcst_dly_h h
where h.hist_crt_ts >= {ts '2017-03-15 22:00:00'} and h.hist_crt_ts <= {ts '2017-03-16 03:00:00'}
and h.hist_typ_ind in ('PRE')
and h.ORIG_TMS_FAC_ALIAS_ID = '817880MO002' and h.MBAS_SHP_TO_LOC_NBR = '5520'
and h.DCM_FCST_CPNT_TYP_CD in (2) --and dcm_sim_id = 345


select * from SCHN_CNSL_LOC_XREF where src_loc_id = '5085'

select * from OGVNDR_EFF_RTE_SCH where ORIG_SCHN_LOC_ID = 329  and DEST_SCHN_LOC_ID = 496

select * from plnd_load_grp_h h where SCHN_EFS_RTE_ID = 1862
and h.hist_crt_ts >= {ts '2017-03-03 22:00:00'} and h.hist_crt_ts <= {ts '2017-03-04 05:00:00'}

select * from plnd_load_h where plnd_load_grp_id = 245266

select * from PLND_LTL_LOAD_h where plnd_load_grp_id = 247151

select s.* from plnd_load_grp_h h, plnd_load_h l, plnd_load_sku_h s where SCHN_EFS_RTE_ID = 1862
and h.hist_crt_ts >= {ts '2017-03-03 22:00:00'} and h.hist_crt_ts <= {ts '2017-03-04 05:00:00'}
and h.plnd_load_grp_id = l.plnd_load_grp_id and
  l.plnd_load_id = s.plnd_load_id and s.MBAS_SKU_NBR = 1002155076

UPDATE SSYS_PARM SET SSYS_PARM_CHAR_VAL = 'N'
WHERE SSYS_PARM_NM = 'SKU_DIM_CACHE_WRITE'

SELECT count(*) FROM SCSYSC021.PLND_LOAD_GRP WHERE CRT_TS > {d '2017-03-10'} ORDER BY LAST_UPD_TS DESC

SELECT * from SCHN_EFS_SKU_DIM_CACHE where last_upd_ts >= {ts '2017-05-04 14:00:00'} order by LAST_UPD_TS desc

SELECT D.MBAS_MVNDR_NBR, F.LOAD_PLNG_PARM_CD, PARM_CHAR_VAL, PARM_INTG_VAL, PARM_DEC_VAL, PARM_FLG_VAL,
       F.LOAD_PLNG_PARM_TYP_IND, G.LOAD_PLNG_PARM_CTGRY_CD, E.EFF_BGN_DT, E.EFF_END_DT
FROM SCHN_EFS_RTE  A,
	SCHN_EFS_RTE_STP B,
     SCHN_ORD_GRP  C,
     SCHN_ORD_GRP_VNDR  D,
     SCHN_ORD_GRP_VNDR_PARM E,
     LOAD_PLNG_PARM_CD F,
	LPLNG_PARM_CTGRY_XREF G
WHERE A.SCHN_EFS_RTE_ID = 2490
AND   (A.EFF_BGN_DT <= {d '2017-03-16'} AND A.EFF_END_DT >= {d '2017-04-10'})
AND   A.ACTV_FLG = 'Y'
AND   A.SCHN_EFS_RTE_ID = B.SCHN_EFS_RTE_ID
AND   (B.EFF_BGN_DT <= {d '2017-03-16'} AND B.EFF_END_DT >= {d '2017-04-10'} )
AND   B.ACTV_FLG = 'Y'
AND   B.SCHN_ORD_GRP_ID = C.SCHN_ORD_GRP_ID
AND   G.LOAD_PLNG_PARM_CTGRY_CD = 1
AND   (C.EFF_BGN_DT <= {d '2017-03-16'} AND C.EFF_END_DT >= {d '2017-04-10'} )
AND   C.ACTV_FLG = 'Y'
AND   C.SCHN_ORD_GRP_ID = D.SCHN_ORD_GRP_ID
AND   C.ACTV_FLG = D.ACTV_FLG
AND   (D.EFF_BGN_DT <= {d '2017-03-16'} AND D.EFF_END_DT >= {d '2017-04-10'} )
AND   D.SCHN_ORD_GRP_VNDR_ID = E.SCHN_ORD_GRP_VNDR_ID
AND   C.ACTV_FLG = E.ACTV_FLG
AND   (E.EFF_BGN_DT <= {d '2017-03-16'} AND E.EFF_END_DT >= {d '2017-04-10'} )
AND   E.LOAD_PLNG_PARM_CD = F.LOAD_PLNG_PARM_CD
AND   E.LOAD_PLNG_PARM_CD = G.LOAD_PLNG_PARM_CD
ORDER BY D.MBAS_MVNDR_NBR, F.LOAD_PLNG_PARM_CD


SELECT DISTINCT D.MBAS_MVNDR_NBR, E.MBAS_SKU_NBR, E.LOAD_PLNG_PARM_CD, PARM_CHAR_VAL, PARM_INTG_VAL,
					 PARM_DEC_VAL, PARM_FLG_VAL, G.LOAD_PLNG_PARM_TYP_IND, H.LOAD_PLNG_PARM_CTGRY_CD, E.EFF_BGN_DT, E.EFF_END_DT, E.LAST_UPD_SYSUSR_ID
					FROM SCHN_EFS_RTE  A,
						SCHN_EFS_RTE_STP   B,
					     SCHN_ORD_GRP  C,
					     SCHN_ORD_GRP_VNDR  D,
					     SCHN_OGVNDR_SKU_EX_PARM  E,
					     LOAD_PLNG_FCST_DLY F,
					     LOAD_PLNG_PARM_CD G,
						LPLNG_PARM_CTGRY_XREF H
					WHERE A.SCHN_EFS_RTE_ID = 2490
					AND (A.EFF_BGN_DT <= {d '2017-03-16'} AND A.EFF_END_DT >= {d '2017-04-10'})
					AND A.ACTV_FLG = 'Y'
					AND A.SCHN_EFS_RTE_ID = B.SCHN_EFS_RTE_ID
					AND (B.EFF_BGN_DT <= {d '2017-03-16'} AND B.EFF_END_DT >= {d '2017-04-10'} )
					AND B.ACTV_FLG = 'Y'
					AND B.SCHN_ORD_GRP_ID = C.SCHN_ORD_GRP_ID
					AND H.LOAD_PLNG_PARM_CTGRY_CD = 3
					AND (C.EFF_BGN_DT <= {d '2017-03-16'} AND C.EFF_END_DT >= {d '2017-04-10'} )
					AND C.ACTV_FLG = 'Y'
					AND C.SCHN_ORD_GRP_ID = D.SCHN_ORD_GRP_ID
					AND (D.EFF_BGN_DT <= {d '2017-03-16'} AND D.EFF_END_DT >= {d '2017-04-10'} )
					AND C.ACTV_FLG = D.ACTV_FLG
					AND D.SCHN_ORD_GRP_VNDR_ID = E.SCHN_ORD_GRP_VNDR_ID
					AND   (E.EFF_BGN_DT <= {d '2017-03-16'} AND E.EFF_END_DT >= {d '2017-04-10'} )
					AND C.ACTV_FLG = E.ACTV_FLG
					AND D.MBAS_MVNDR_NBR = F.MBAS_MVNDR_NBR
					AND E.MBAS_SKU_NBR = F.MBAS_SKU_NBR
					AND E.LOAD_PLNG_PARM_CD = G.LOAD_PLNG_PARM_CD
					AND E.LOAD_PLNG_PARM_CD = H.LOAD_PLNG_PARM_CD
					AND F.DCM_FCST_CPNT_TYP_CD = 1 ;


select distinct MBAS_SKU_NBR, PARM_FLG_VAL, count(*) from SCHN_OGVNDR_SKU_EX_PARM
where LOAD_PLNG_PARM_CD in (25,26) group by MBAS_SKU_NBR, PARM_FLG_VAL having count(*) > 1


select * from OGVNDR_EFF_RTE_SCH where mbas_mvndr_nbr = 87452 and DEST_SCHN_LOC_ID = 423

select * from SCHN_CNSL_LOC_XREF where src_loc_id = '5520'


select * from scsysc021.pre_plnd_load_grp where SCHN_EFS_RTE_ID = 3843

select * from schn_efs_rte_stp where schn_efs_Rte_id = 3843

select * from SCHN_ORD_GRP where schn_ord_grp_id in (1678,3101)

SELECT E.PLND_LOAD_EFF_DT, A.SCHN_ORD_GRP_NM AS EFS_ORDER_GRP_NAME,
                 A.SCHN_ORD_GRP_ID AS EFS_ORDER_GRP_ID,
                 I.SCHN_EFS_RTE_ID AS EFS_RTE_ID,
                 C.SRC_LOC_ID AS ORIGIN,
                 D.SRC_LOC_ID AS RDC_NBR,
                 H.MBAS_MVNDR_NBR AS MVNDR_PARTY_ID,
                 H.MBAS_SKU_NBR AS SKU_NBR,
                 G.PRE_PLN_LOAD_ID AS TRUCK_ID,
                 G.LOAD_UTILZTN_VOL AS TRUCK_VOLUME,
                 G.LOAD_UTILZTN_WT AS TRUCK_WEIGHT,
                 SUM(H.PLND_LOAD_SKU_QTY) AS SKU_QTY,
                 SUM(H.PLND_LOAD_SKU_QTY/DC_BUY_UOM_QTY) AS PACK_QTY,
                 E.SCHN_APPL_RUN_ID AS RUN_ID

FROM                   SCHN_ORD_GRP A,
                       SCHN_ORD_GRP_LANE B,
                       SCHN_CNSL_LOC_XREF C,
                       SCHN_CNSL_LOC_XREF D,
             SCSYSC02.PRE_PLND_LOAD_GRP E,
             SCSYSC02.PRE_PLND_LOAD_GRP_VER F,
             SCSYSC02.PRE_PLND_LOAD G,
             SCSYSC02.PRE_PLND_LOAD_SKU H,
                                                                   SCHN_EFS_RTE_STP I,
                                                        SCHN_ORD_GRP_VNDR J

WHERE     A.SCHN_ORD_GRP_LANE_ID = B.SCHN_ORD_GRP_LANE_ID
AND       B.ORIG_SCHN_LOC_ID = C.SCHN_LOC_ID
AND       B.DEST_SCHN_LOC_ID = D.SCHN_LOC_ID
AND       E.SCHN_EFS_RTE_ID = I.SCHN_EFS_RTE_ID
AND       E.PRE_PLN_LOAD_GRP_ID = F.PRE_PLN_LOAD_GRP_ID
AND       F.PLND_LOAD_GRP_ORD_ELIG_FLG = 'Y'
AND       F.PRE_PLN_LOAD_GRP_ID = G.PRE_PLN_LOAD_GRP_ID
AND       F.PRE_PLN_LOAD_GRP_VER_NBR = G.PRE_PLN_LOAD_GRP_VER_NBR
AND       G.PRE_PLN_LOAD_ID = H.PRE_PLN_LOAD_ID
AND       C.SCHN_CNSL_LOC_SRC_SYS_CD = 1
AND       D.SCHN_CNSL_LOC_SRC_SYS_CD = 2
AND       G.LOAD_TNDR_ELIG_FLG = 'Y'
AND       I.SCHN_ORD_GRP_ID = A.SCHN_ORD_GRP_ID
AND       J.SCHN_ORD_GRP_ID = A.SCHN_ORD_GRP_ID
AND       J.MBAS_MVNDR_NBR = H.MBAS_MVNDR_NBR
AND       I.ACTV_FLG = 'Y'
--AND       C.SRC_LOC_ID = '558045GA003'
--AND       A.SCHN_ORD_GRP_ID in (2107)
AND       I.SCHN_EFS_RTE_ID IN (3843)
--AND      E.SCHN_APPL_RUN_ID = 1365
GROUP BY   A.SCHN_ORD_GRP_NM,
                        A.SCHN_ORD_GRP_ID,
                        I.SCHN_EFS_RTE_ID,
                        C.SRC_LOC_ID,
                        D.SRC_LOC_ID,
                        H.MBAS_MVNDR_NBR,
                        H.MBAS_SKU_NBR,
                        G.PRE_PLN_LOAD_ID,
                        G.LOAD_UTILZTN_VOL,
                        G.LOAD_UTILZTN_WT,
                        E.SCHN_APPL_RUN_ID,
                        E.PLND_LOAD_EFF_DT
ORDER BY A.SCHN_ORD_GRP_ID,G.PRE_PLN_LOAD_ID,H.MBAS_SKU_NBR

select * from PLND_LOAD_SKU

select l.pre_pln_load_id, s.SCHN_EFS_RTE_STP_ID, sum(LOAD_UTILZTN_VOL_PCT), sum(LOAD_UTILZTN_WT_PCT)
from pre_plnd_Load_sku s, pre_plnd_load l, PRE_PLND_LOAD_GRP_VER v
where l.PRE_PLN_LOAD_ID = s.PRE_PLN_LOAD_ID
  and v.PRE_PLN_LOAD_GRP_ID = l.PRE_PLN_LOAD_GRP_ID
  and v.PLND_LOAD_GRP_ORD_ELIG_FLG = 'Y'
  and v.PRE_PLN_LOAD_GRP_VER_NBR = l.PRE_PLN_LOAD_GRP_VER_NBR
and l.PRE_PLN_LOAD_GRP_ID = 2719239
group by l.pre_pln_load_id, SCHN_EFS_RTE_STP_ID


select * from OGVNDR_EFF_RTE_SCH where schn_efs_rte_id = 4084

select * from schn_efs_rte_stp where schn_efs_rte_id = 4084

select * from schN_ord_grp where schN_ord_grp_id in (3404,1561)

update ssys_parm set ssys_Parm_char_val = '150000' where ssys_parm_nm like 'RP_MAX_ITER%'

select * from load_Plng_fcst_dly where mbas_sku_nbr = 631819

select * from SCHN_EFS_SCH_DT_V where schn_efs_rte_id = 892 -- and tndr_dt = {d '2017-03-29'}

select sum(RND_UP_SKU_QTY / DC_BUY_UOM_QTY) from pre_plnd_load_grp g,  PRE_PLND_LTL_LOAD l, PRE_PLND_LTL_LOAD_SKU s
where g.PRE_PLN_LOAD_GRP_ID = l.PRE_PLN_LOAD_GRP_ID and l.PRE_PLND_LTL_LOAD_ID = s.PRE_PLND_LTL_LOAD_ID

select sum(RND_UP_SKU_QTY / DC_BUY_UOM_QTY)
FROM pre_plnd_load_grp_h g,  PRE_PLND_LTL_LOAD_h l, PRE_PLND_LTL_LOAD_SKU_h s
where g.PRE_PLN_LOAD_GRP_ID = l.PRE_PLN_LOAD_GRP_ID and l.PRE_PLND_LTL_LOAD_ID = s.PRE_PLND_LTL_LOAD_ID
and g.HIST_CRT_TS >= {ts '2017-03-29 21:00:00'}
and g.HIST_CRT_TS <= {ts '2017-03-21 14:00:00'}



   SELECT H.*

FROM
   SCHN_ORD_GRP_VNDR A,
   SCHN_ORD_GRP B,
   SCHN_ORD_GRP_LANE C,
   SCHN_CNSL_LOC D,
   SCHN_CNSL_LOC E,
   SCHN_CNSL_LOC_XREF F,
   SCHN_CNSL_LOC_XREF G,
   scsysc02.LOAD_PLNG_FCST_DLY_H H,
   SKU_DC I,
   SCHN_EFS_RTE_STP J,
   SCHN_EFS_RTE K
WHERE
   A.SCHN_ORD_GRP_ID = B.SCHN_ORD_GRP_ID
AND B.SCHN_ORD_GRP_LANE_ID = C.SCHN_ORD_GRP_LANE_ID
AND C.ORIG_SCHN_LOC_ID = D.SCHN_LOC_ID
AND C.DEST_SCHN_LOC_ID = E.SCHN_LOC_ID
AND C.ORIG_SCHN_LOC_ID = F.SCHN_LOC_ID
AND C.DEST_SCHN_LOC_ID = G.SCHN_LOC_ID
AND A.SCHN_ORD_GRP_ID = J.SCHN_ORD_GRP_ID
AND J.SCHN_EFS_RTE_ID = K.SCHN_EFS_RTE_ID
AND F.SRC_LOC_ID = H.ORIG_TMS_FAC_ALIAS_ID
AND G.SRC_LOC_ID = H.MBAS_SHP_TO_LOC_NBR
AND A.MBAS_MVNDR_NBR = H.MBAS_MVNDR_NBR
AND A.MBAS_DEPT_NBR = H.MBAS_DEPT_NBR
AND A.MER_BASE_CD = H.MER_BASE_CD
AND H.MBAS_SKU_NBR = I.SKU_NBR
AND H.MBAS_SHP_TO_LOC_NBR = I.DC_NBR --AND H.DCM_SIM_ID=3152
AND J.SCHN_EFS_RTE_ID IN (5745,5820)
AND H.HIST_TYP_IND = 'PRE'
AND H.HIST_CRT_TS >= {ts '2017-05-03 23:00:00'}
ORDER BY
   H.MBAS_SKU_NBR ASC,
   H.DCM_FCST_CPNT_TYP_CD DESC

select * from OGVNDR_EFF_RTE_SCH where schN_efs_rte_id = 3843

SELECT J.SCHN_EFS_RTE_ID, J.SCHN_EFS_RTE_NM, J.SCHN_EFS_RTE_CONFG_CD, J.RTE_ACTV_FLG, J.SCHN_EFS_RTE_TYP_ID,
        J.SCHN_EFS_RTE_STP_ID, J.STP_SEQ_NBR, J.STP_ACTV_FLG, J.SCHN_ORD_GRP_ID, J.ORIG_NBR, J.DEST_NBR,
        J.DEST_TYP_CD, J.EFF_BGN_DT, J.EFF_END_DT, J.MBAS_MVNDR_NBR, J.MBAS_DEPT_NBR, J.PLND_LOAD_GRP_ID
FROM
        (SELECT DISTINCT A.SCHN_EFS_RTE_ID, AA.SCHN_EFS_RTE_NM, AA.SCHN_EFS_RTE_CONFG_CD, AA.ACTV_FLG RTE_ACTV_FLG, AA.SCHN_EFS_RTE_TYP_ID,
                         A.SCHN_EFS_RTE_STP_ID,A.STP_SEQ_NBR, A.ACTV_FLG STP_ACTV_FLG, A.SCHN_ORD_GRP_ID, C.ORIG_SCHN_LOC_ID, C.DEST_SCHN_LOC_ID,
                         H.ORIG_TMS_FAC_ALIAS_ID ORIG_NBR, H.MBAS_SHP_TO_LOC_NBR DEST_NBR, H.MBAS_SHP_TO_LOC_TYP_CD DEST_TYP_CD,
                         I.EFF_BGN_DT, I.EFF_END_DT, I.MBAS_MVNDR_NBR, I.MBAS_DEPT_NBR, PLG.PLND_LOAD_GRP_ID
             FROM SCHN_EFS_RTE AA, SCHN_EFS_RTE_STP A
                  LEFT JOIN PLND_LOAD_GRP PLG ON A.SCHN_EFS_RTE_ID = PLG.SCHN_EFS_RTE_ID,
                             SCHN_ORD_GRP_VNDR AB,
                             SCHN_ORD_GRP B,
                             SCHN_ORD_GRP_LANE C, SCHN_CNSL_LOC D,  SCHN_CNSL_LOC E,
                             SCHN_CNSL_LOC_XREF F, SCHN_CNSL_LOC_XREF G,
                             scsysc02.LOAD_PLNG_FCST_DLY H, OGVNDR_EFF_RTE_SCH I
             WHERE AA.SCHN_EFS_RTE_ID = A. SCHN_EFS_RTE_ID
                        AND A.SCHN_ORD_GRP_ID = B.SCHN_ORD_GRP_ID
                        AND B.SCHN_ORD_GRP_LANE_ID = C.SCHN_ORD_GRP_LANE_ID
                AND C.ORIG_SCHN_LOC_ID = D.SCHN_LOC_ID
                AND C.DEST_SCHN_LOC_ID = E.SCHN_LOC_ID
                AND C.ORIG_SCHN_LOC_ID = F.SCHN_LOC_ID
                AND C.DEST_SCHN_LOC_ID = G.SCHN_LOC_ID
                AND F.SRC_LOC_ID = H.ORIG_TMS_FAC_ALIAS_ID
                AND G.SRC_LOC_ID = H.MBAS_SHP_TO_LOC_NBR
                AND A.SCHN_ORD_GRP_ID = AB.SCHN_ORD_GRP_ID
                AND AB.MBAS_MVNDR_NBR = H.MBAS_MVNDR_NBR
                AND AB.MBAS_DEPT_NBR = H.MBAS_DEPT_NBR
                AND AB.MER_BASE_CD = H.MER_BASE_CD
                AND AB.SCHN_ORD_GRP_VNDR_ID = I.SCHN_ORD_GRP_VNDR_ID
                AND A.SCHN_EFS_RTE_ID = I.SCHN_EFS_RTE_ID
                AND A.SCHN_EFS_RTE_STP_ID = I.SCHN_EFS_RTE_STP_ID
			 --AND PLG.PLND_LOAD_GRP_ID IS NULL
                AND F.SCHN_CNSL_LOC_SRC_SYS_CD = 1
                AND G.SCHN_CNSL_LOC_SRC_SYS_CD = 2
                AND AA.EFF_BGN_DT <= {d '2017-03-29'} AND AA.EFF_END_DT >= {d '2017-04-27'}
                AND A.EFF_BGN_DT <= {d '2017-03-29'} AND A.EFF_END_DT >= {d '2017-04-27'}
                AND B.EFF_BGN_DT <= {d '2017-03-29'} AND B.EFF_END_DT >= {d '2017-04-27'}
               AND AB.EFF_BGN_DT <= {d '2017-03-29'} AND AB.EFF_END_DT >= {d '2017-04-27'}
                AND C.EFF_BGN_DT <= {d '2017-03-29'} AND C.EFF_END_DT >= {d '2017-04-27'}
                AND D.EFF_BGN_DT <= {d '2017-03-29'} AND D.EFF_END_DT >= {d '2017-04-27'}
                AND E.EFF_BGN_DT <= {d '2017-03-29'} AND E.EFF_END_DT >= {d '2017-04-27'}
                AND I.EFF_BGN_DT <={d '2017-03-29'} AND I.EFF_END_DT >={d '2017-04-27'}
                AND A.ACTV_FLG = 'Y'
                AND AA.ACTV_FLG = 'Y'
                AND B.ACTV_FLG ='Y'
                AND AB.ACTV_FLG = 'Y'
                AND C.ACTV_FLG = 'Y'
                AND D.ACTV_FLG = 'Y'
                AND E.ACTV_FLG = 'Y'
        		  ) J,
						(SELECT A.ORIG_SCHN_LOC_ID, A.DEST_SCHN_LOC_ID, MIN(A.EFF_BGN_DT) AS PRI_RTE_EFF_BGN_DT
                		FROM OGVNDR_EFF_RTE_SCH A,
							 SCHN_ORD_GRP_LANE B,
                			 SCHN_CNSL_LOC_XREF C,
							 SCHN_CNSL_LOC_XREF D,
							 scsysc02.LOAD_PLNG_FCST_DLY E
            		   WHERE
			               A.ORIG_SCHN_LOC_ID = B.ORIG_SCHN_LOC_ID
			               AND A.DEST_SCHN_LOC_ID = B.DEST_SCHN_LOC_ID
			               AND B.ORIG_SCHN_LOC_ID = C.SCHN_LOC_ID
			               AND B.DEST_SCHN_LOC_ID = D.SCHN_LOC_ID
			               AND C.SRC_LOC_ID = E.ORIG_TMS_FAC_ALIAS_ID
			               AND D.SRC_LOC_ID = E.MBAS_SHP_TO_LOC_NBR
			               AND A.MBAS_MVNDR_NBR = E.MBAS_MVNDR_NBR
			               AND A.MBAS_DEPT_NBR = E.MBAS_DEPT_NBR
			               AND A.MER_BASE_CD = E.MER_BASE_CD
			               AND C.SCHN_CNSL_LOC_SRC_SYS_CD=1
			               AND D.SCHN_CNSL_LOC_SRC_SYS_CD=2
			               AND A.EFF_BGN_DT <= {d '2017-03-29'} AND A.EFF_END_DT >= {d '2017-04-27'}
                      GROUP BY A.ORIG_SCHN_LOC_ID, A.DEST_SCHN_LOC_ID) K
  			WHERE J.ORIG_SCHN_LOC_ID = K.ORIG_SCHN_LOC_ID
  			AND J.DEST_SCHN_LOC_ID = K.DEST_SCHN_LOC_ID
           AND J.EFF_BGN_DT = K.PRI_RTE_EFF_BGN_DT )-- AND J.SCHN_EFS_RTE_ID = 6070

select * from ssys_parm where ssys_parm_nm like 'RP_MAX_ITER%'

select * from N_SCHN_CNSL_LOC_TYP

select * from LOAD_PLNG_EXTNL_DMND_LOC order by ACT_ORD_OPEN_DT desc

select * from PRE_PLND_LOAD_HRQST_X order by last_upd_ts desc

select * from scsysc02.load_plng_err
where mbas_sku_nbr = 210235
order by crt_ts desc

select * from load_plng_fcst_dly where mbas_sku_nbr = 210235 and MBAS_SHP_TO_LOC_NBR = 5851 and DCM_FCST_CPNT_TYP_CD in (1,2,3) and ORIG_TMS_FAC_ALIAS_ID = '28874TX001'

select * from SCHN_CNSL_LOC_XREF where src_loc_id in ('28874TX001', '5851')

select * from OGVNDR_EFF_RTE_SCH where ORIG_SCHN_LOC_ID = 335 and DEST_SCHN_LOC_ID = 465

select s.* from pre_plnd_load_grp g, pre_plnd_load l, pre_plnd_load_sku s
where g.PRE_PLN_LOAD_GRP_ID = l.PRE_PLN_LOAD_GRP_ID and l.PRE_PLN_LOAD_ID = s.PRE_PLN_LOAD_ID
and g.schn_efs_rte_id = 2582 and s.MBAS_SKU_NBR = 210235

select * from scsysc021.pre_plnd_load_sku where mbas_sku_nbr = 210235

select * from LPLNG_EXTNL_DMND_LOC_SKU where mbas_sku_nbr = 210235

select * from scsysc021.pre_plnd_load_grp where schn_efs_rte_id = 2582 and schN_appl_run_id = 1629

select count(*) from load_plng_fcst_dly where ORIG_TMS_FAC_ALIAS_ID = '28874TX001' and MBAS_SHP_TO_LOC_NBR = 5851


--RESTORE FORECAST
delete from scsysc02.LOAD_PLNG_FCST_DLY;

INSERT INTO load_plng_fcst_dly (LOAD_PLNG_DLY_FCST_ID,CRT_SYSUSR_ID,CRT_TS,LAST_UPD_SYSUSR_ID,LAST_UPD_TS,ORIG_TMS_FAC_ALIAS_ID,MBAS_SHP_TO_LOC_NBR,MBAS_SHP_TO_LOC_TYP_CD,MBAS_MVNDR_NBR,MBAS_DEPT_NBR,MBAS_SKU_NBR,MER_BASE_CD,PO_PROD_GRP_CD,DSVC_TYP_CD,DC_BUY_UOM_QTY,DCM_FCST_CPNT_TYP_CD,DAY_1_VAL,DAY_2_VAL,DAY_3_VAL,DAY_4_VAL,DAY_5_VAL,DAY_6_VAL,DAY_7_VAL,DAY_8_VAL,DAY_9_VAL,DAY_10_VAL,DAY_11_VAL,DAY_12_VAL,DAY_13_VAL,DAY_14_VAL,DAY_15_VAL,DAY_16_VAL,DAY_17_VAL,DAY_18_VAL,DAY_19_VAL,DAY_20_VAL,DAY_21_VAL,DAY_22_VAL,DAY_23_VAL,DAY_24_VAL,DAY_25_VAL,DAY_26_VAL,DAY_27_VAL,DAY_28_VAL, DCM_SIM_ID)
   SELECT LOAD_PLNG_DLY_FCST_ID,CRT_SYSUSR_ID,CRT_TS,LAST_UPD_SYSUSR_ID,LAST_UPD_TS,ORIG_TMS_FAC_ALIAS_ID,MBAS_SHP_TO_LOC_NBR,MBAS_SHP_TO_LOC_TYP_CD,MBAS_MVNDR_NBR,MBAS_DEPT_NBR,MBAS_SKU_NBR,MER_BASE_CD,PO_PROD_GRP_CD,DSVC_TYP_CD,DC_BUY_UOM_QTY,DCM_FCST_CPNT_TYP_CD,DAY_1_VAL,DAY_2_VAL,DAY_3_VAL,DAY_4_VAL,DAY_5_VAL,DAY_6_VAL,DAY_7_VAL,DAY_8_VAL,DAY_9_VAL,DAY_10_VAL,DAY_11_VAL,DAY_12_VAL,DAY_13_VAL,DAY_14_VAL,DAY_15_VAL,DAY_16_VAL,DAY_17_VAL,DAY_18_VAL,DAY_19_VAL,DAY_20_VAL,DAY_21_VAL,DAY_22_VAL,DAY_23_VAL,DAY_24_VAL,DAY_25_VAL,DAY_26_VAL,DAY_27_VAL,DAY_28_VAL, 0
   FROM scsysc02.load_plng_fcst_dly_h where hist_crt_ts >  to_timestamp('2017-06-14 22:00:00', 'YYYY-MM-DD HH24:MI:SS') and HIST_TYP_IND = 'PRE';

select count(*) from load_plng_fcst_dly

select a.schn_appl_cpnt_nm, b.* from SCHN_APPL_WRKFLW_cpnt b, schn_appl_cpnt a where SCHN_APPL_WRKFLW_ID = 2 and a.SCHN_APPL_CPNT_ID = b.SCHN_APPL_CPNT_ID

select V2.MBAS_MVNDR_NBR, X.SRC_LOC_ID, LOC.SCHN_CNSL_LOC_TYP_CD
FROM SCHN_ORD_GRP_VNDR V1, SCHN_ORD_GRP_VNDR V2, SCHN_ORD_GRP G, SCHN_ORD_GRP_LANE L,
SCHN_CNSL_LOC_XREF X, SCHN_CNSL_LOC LOC, MBMVNDR_LOC_ONBRD M
WHERE V1.MBAS_MVNDR_NBR = 640105
AND V1.SCHN_ORD_GRP_ID = V2.SCHN_ORD_GRP_ID
AND V1.EFF_BGN_DT <= CURRENT_DATE AND V1.EFF_END_DT >= CURRENT_DATE AND V1.ACTV_FLG = 'Y'
AND V2.EFF_BGN_DT <= CURRENT_DATE AND V2.EFF_END_DT >= CURRENT_DATE AND V2.ACTV_FLG = 'Y'
AND V1.SCHN_ORD_GRP_ID = G.SCHN_ORD_GRP_ID
AND G.SCHN_ORD_GRP_LANE_ID = L.SCHN_ORD_GRP_LANE_ID
AND L.DEST_SCHN_LOC_ID = X.SCHN_LOC_ID
AND LOC.SCHN_LOC_ID = X.SCHN_LOC_ID
AND M.MBAS_MVNDR_NBR = V1.MBAS_MVNDR_NBR
AND M.MBAS_SHTO_LOC_NBR = X.SRC_LOC_ID
AND M.LPLNG_ACTV_FLG = 'Y'
AND X.SRC_LOC_ID = '5023'
AND LOC.SCHN_CNSL_LOC_TYP_CD = 13
ORDER BY X.SRC_LOC_ID, V2.MBAS_MVNDR_NBR

SELECT * FROM N_SCHN_CNSL_LOC_TYP

select * from schn_efs_rte_stats

select a.ORIG_TMS_FAC_ALIAS_ID, a.MBAS_SHP_TO_LOC_NBR, a.MBAS_MVNDR_NBR, a.MBAS_SKU_NBR, a.DCM_FCST_CPNT_TYP_CD,
a.DAY_1_VAL - b.day_1_val,
a.day_2_val - b.day_2_val,
a.day_3_val - b.day_3_val,
a.day_4_val - b.day_4_val,
a.day_5_val - b.day_5_val,
a.day_6_val - b.day_6_val,
a.day_7_val - b.day_7_val,
a.day_8_val - b.day_8_val,
a.day_9_val - b.day_9_val,
a.day_10_val - b.day_10_val,
a.day_11_val - b.day_11_val,
a.day_12_val - b.day_12_val,
a.day_13_val - b.day_13_val,
a.day_14_val - b.day_14_val,
a.day_15_val - b.day_15_val,
a.day_16_val - b.day_16_val,
a.day_17_val - b.day_17_val,
a.day_18_val - b.day_18_val,
a.day_19_val - b.day_19_val,
a.day_20_val - b.day_20_val,
a.day_21_val - b.day_21_val,
a.day_22_val - b.day_22_val,
a.day_23_val - b.day_23_val,
a.day_24_val - b.day_24_val,
a.day_25_val - b.day_25_val,
a.day_26_val - b.day_26_val,
a.day_27_val - b.day_27_val,
a.day_28_val - b.day_28_val
from load_plng_fcst_dly_h A,
scsysc021.STG_LOAD_PLNG_FCST_DLY B
WHERE a.ORIG_TMS_FAC_ALIAS_ID = b.ORIG_TMS_FAC_ALIAS_ID and
a.MBAS_MVNDR_NBR = b.mbas_mvndr_nbr and
a.MBAS_SHP_TO_LOC_NBR = b.MBAS_SHP_TO_LOC_NBR AND
a.MBAS_SKU_NBR = b.MBAS_SKU_NBR
AND a.DCM_FCST_CPNT_TYP_CD = b.DCM_FCST_CPNT_TYP_CD
and a.SCHN_APPL_RUN_ID = 1701
and B.DCM_SIM_ID = 3157


  select * from schn_appl_run where srvlt_nm  = 'XD_JB_LP' and schN_appl_run_typ_cd = 1 order by LAST_UPD_TS desc

select distinct (b.host_rqst_id) from LOAD_PLNG_EXTNL_DMND_LOC a, LOAD_PLNG_EXTNL_DMND b where a.LOAD_PLNG_EXTNL_DMND_ID = b.LOAD_PLNG_EXTNL_DMND_ID
and plnd_load_eff_dt = {d '2017-04-05'}

select * from LOAD_PLNG_EXTNL_DMND where host_rqst_id in (149368118,160956816)

select * from LOAD_PLNG_EXTNL_DMND_LOC WHERE LOAD_PLNG_EXTNL_DMND_ID = 470

select * from schn_cnsl_loc_xref where schn_loc_id = 183

select MBAS_MVNDR_NBR, ORIG_SCHN_LOC_ID, DEST_SCHN_LOC_ID, EFF_BGN_DT, EFF_END_DT, SCHN_EFS_SCH_ID, LAST_UPD_TS from OGVNDR_EFF_RTE_SCH
where schn_efs_rte_id = 6670
order by eff_bgn_dt asc, ORIG_SCHN_LOC_ID, mbas_mvndr_nbr asc

select * from scsysc021.schn_efs_rte_stats where schn_efs_rte_id = 5722 and schn_appL_run_id = 1818

select * from schn_appl_evnt_log where SCHN_TRANS_ID = 2492 order by CRT_TS desc

select * from schn_efs_rte_stats where schn_efs_Rte_id = 6899

update schn_efs_rte_stats set dly_hazmat_truck_qty = 2 where schn_efs_rte_id = 6899 and plnd_load_eff_dt = {d '2017-06-16'}

select * from scsysc021.pre_plnd_load_grp where schn_appl_run_id =2496

select * from schn_efs_rte_stats --set dly_hazmat_truck_qty = DLY_TRUCK_ORD_MAX_QTY
where plnd_load_eff_dt > current_date and dly_hazmat_truck_qty > 0 and TNDR_LOAD_FLG = 'Y' and dly_hazmat_truck_qty < DLY_TRUCK_ORD_MAX_QTY



SELECT * FROM