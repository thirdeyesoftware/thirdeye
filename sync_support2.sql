--NEW UNPROCESSED ROUTES QUERY--
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
                             LOAD_PLNG_FCST_DLY H, OGVNDR_EFF_RTE_SCH I 
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
			 AND PLG.PLND_LOAD_GRP_ID IS NULL  
                AND F.SCHN_CNSL_LOC_SRC_SYS_CD = 1 
                AND G.SCHN_CNSL_LOC_SRC_SYS_CD = 2 
                AND AA.EFF_BGN_DT <= {d '2017-03-01'} AND AA.EFF_END_DT >= {d '2017-02-03'} 
                AND A.EFF_BGN_DT <= {d '2017-03-01'} AND A.EFF_END_DT >= {d '2017-02-03'} 
                AND B.EFF_BGN_DT <= {d '2017-03-01'} AND B.EFF_END_DT >= {d '2017-02-03'} 
               AND AB.EFF_BGN_DT <= {d '2017-03-01'} AND AB.EFF_END_DT >= {d '2017-02-03'}
                AND C.EFF_BGN_DT <= {d '2017-03-01'} AND C.EFF_END_DT >= {d '2017-02-03'} 
                AND D.EFF_BGN_DT <= {d '2017-03-01'} AND D.EFF_END_DT >= {d '2017-02-03'}
                AND E.EFF_BGN_DT <= {d '2017-03-01'} AND E.EFF_END_DT >= {d '2017-02-03'} 
                AND I.EFF_BGN_DT <={d '2017-03-01'} AND I.EFF_END_DT >={d '2017-02-03'} 
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
							 LOAD_PLNG_FCST_DLY E 
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
			               AND A.EFF_BGN_DT <= {d '2017-03-01'} AND A.EFF_END_DT >= {d '2017-02-03'} 
                      GROUP BY A.ORIG_SCHN_LOC_ID, A.DEST_SCHN_LOC_ID) K 
  			WHERE J.ORIG_SCHN_LOC_ID = K.ORIG_SCHN_LOC_ID 
  			AND J.DEST_SCHN_LOC_ID = K.DEST_SCHN_LOC_ID 
           AND J.EFF_BGN_DT = K.PRI_RTE_EFF_BGN_DT  AND J.SCHN_EFS_RTE_ID = 2522


--PARM 27---
select distinct a.schn_efs_Rte_id, z.eff_bgn_dt, z.eff_end_dt, b.mbas_mvndr_nbr, ho.src_loc_id, hd.src_loc_id, o.parm_dt_val IT_ONBRD_DT, 
c.EFF_BGN_DT PARM_BGN_DT, c.EFF_END_DT PARM_END_DT, C.PARM_FLG_VAL 
from SCHN_EFS_RTE_STP a
    , SCHN_ORD_GRP_VNDR b
    , SCHN_ORD_GRP_VNDR_PARM c
    , SCHN_EFS_RTE d
    , SCHN_ORD_GRP e
    , SCHN_ORD_GRP_LANE f
        , SCHN_CNSL_LOC_XREF ho
    , SCHN_CNSL_LOC_XREF hd
    , LOAD_PLNG_FCST_DLY_h g,
    MBMVNDR_LOC_OPARM O,
    OGVNDR_EFF_RTE_SCH z
where a.SCHN_ORD_GRP_ID = b.SCHN_ORD_GRP_ID
    and c.SCHN_ORD_GRP_VNDR_ID = b.SCHN_ORD_GRP_VNDR_ID
    and c.LOAD_PLNG_PARM_CD = 27
    and c.PARM_FLG_VAL = 'Y'
    and c.ACTV_FLG = 'Y'
    and d.ACTV_FLG = 'Y'
    and e.ACTV_FLG = 'Y'
    and trunc(current_date+28) >= c.EFF_BGN_DT
    and trunc(current_date) <= c.EFF_END_DT
    and trunc(current_date+28) >= d.EFF_BGN_DT
    and trunc(current_date) <= d.EFF_END_DT
    and trunc(current_date+28) >= e.EFF_BGN_DT
    and trunc(current_date) <= e.EFF_END_DT
    and d.SCHN_EFS_RTE_ID = a.SCHN_EFS_RTE_ID
    and d.SCHN_EFS_RTE_CONFG_CD = 2
    and e.SCHN_ORD_GRP_ID = a.SCHN_ORD_GRP_ID
    and f.SCHN_ORD_GRP_LANE_ID = e.SCHN_ORD_GRP_LANE_ID
    and ho.SCHN_LOC_ID = f.ORIG_SCHN_LOC_ID
    and hd.SCHN_LOC_ID = f.DEST_SCHN_LOC_ID
    and ho.SCHN_CNSL_LOC_SRC_SYS_CD = 1
    and hd.SCHN_CNSL_LOC_SRC_SYS_CD = 2
    and g.MBAS_MVNDR_NBR = b.MBAS_MVNDR_NBR
    and g.ORIG_TMS_FAC_ALIAS_ID = ho.SRC_LOC_ID
    and g.MBAS_SHP_TO_LOC_NBR = hd.SRC_LOC_ID
    and g.HIST_TYP_IND = 'PRE'
    and g.HIST_CRT_TS >= {d '2017-02-01'}
    and g.DCM_FCST_CPNT_TYP_CD = 2
    AND g.mbas_mvndr_nbr = o.mbas_mvndr_nbr
    and hd.schn_loc_id = o.DEST_SCHN_LOC_ID
    and o.SCHN_ONBRD_PARM_CD = 2
    and a.SCHN_EFS_RTE_ID = z.SCHN_EFS_RTE_ID
    and g.MBAS_MVNDR_NBR = z.MBAS_MVNDR_NBR
    and z.ORIG_SCHN_LOC_ID = ho.SCHN_LOC_ID
    and z.DEST_SCHN_LOC_ID = hd.SCHN_LOC_ID
    
--EXTERNAL DEMAND--

select * from prthd.host_rqst_po where RQST_ID = 148273852-- order by CRT_TS desc

SELECT 
	C.ACCT_PO_TYP_CD, 
	C.PO_CTRL_NBR, 
	C.BYO_NBR, 
	C.CRT_DT, 
	C.DSVC_TYP_CD, 
	D.MBAS_MVNDR_NBR, 
	D.MBAS_DEPT_NBR, 
	D.MER_BASE_CD, 
	D.MBAS_SHTO_LOC_NBR, 
	C.CENTRV_LOC_NBR, 
	C.EXPCTD_ARVL_DT, 
	E.SKU_NBR, 
	E.LOC_NBR, 
	E.ORD_QTY, 
	F.HNDLNG_DISP_TYP_CD, 
	G.BUY_UOM_QTY 
 FROM PRTHD.HOST_RQST  A, 
     PRTHD.HOST_RQST_PO  B, 
     PRTHD.BYO_PO C, 
     PRTHD.MBMVNDR_LOC_ONBRD  D, 
     PRTHD.BYO_PO_LOC_SKU E, 
	  PRTHD.SKU_DC F, 
	  PRTHD.MVNDR_SKU_DC G 
 WHERE A.RQST_ID =148273852 
 AND   A.RQST_ID = B.RQST_ID 
 AND   B.ACCT_PO_TYP_CD = C.ACCT_PO_TYP_CD 
 AND   B.BYO_NBR = C.BYO_NBR 
 AND   B.MKT_DC_IND = C.MKT_DC_IND 
 AND   B.MKT_DC_NBR = C.MKT_DC_NBR 
 AND   C.ORD_SEQ_NBR = '1'
 AND   B.PO_CTRL_NBR = C.PO_CTRL_NBR 
 AND   C.PO_STAT_CD = '1'
 AND   C.DSVC_TYP_CD IN (2,3) 
 AND   C.EXPCTD_ARVL_DT >= {d '2017-02-01'}
 AND   C.EXPCTD_ARVL_DT <  {d '2017-03-01'}
 AND   C.OCYC_SOQ_RSN_CD NOT IN (130, 170, 171, 290, 300) 
 AND   CAST(C.MVNDR_NBR AS VARCHAR(20)) = D.MBAS_MVNDR_NBR 
 AND   CAST(C.MER_DEPT_NBR AS VARCHAR(20)) = D.MBAS_DEPT_NBR 
 AND   CAST(C.CENTRV_LOC_NBR AS VARCHAR(20)) = D.MBAS_SHTO_LOC_NBR 
 AND   D.MBAS_SHTO_LTYP_CD = 2
 AND   D.MER_BASE_CD = 1
 AND   D.LPLNG_ACTV_FLG = 'Y' 
 AND   C.ACCT_PO_TYP_CD = E.ACCT_PO_TYP_CD 
 AND   C.BYO_NBR = E.BYO_NBR 
 AND   C.MKT_DC_IND = E.MKT_DC_IND 
 AND   C.MKT_DC_NBR = E.MKT_DC_NBR 
 AND   C.ORD_SEQ_NBR = E.ORD_SEQ_NBR 
 AND   C.PO_CTRL_NBR = E.PO_CTRL_NBR 
 AND	C.CENTRV_LOC_NBR = F.DC_NBR 
 AND	E.SKU_NBR = F.SKU_NBR 
 AND	C.CENTRV_LOC_NBR = G.DC_NBR 
 AND	E.SKU_NBR = G.SKU_NBR 
 AND	C.MVNDR_NBR = G.MVNDR_NBR 
 ORDER BY 
 	C.ACCT_PO_TYP_CD, 
 	C.PO_CTRL_NBR, 
 	C.BYO_NBR, 
 	D.MBAS_MVNDR_NBR, 
 	D.MBAS_DEPT_NBR, 
 	C.CENTRV_LOC_NBR, 
	E.LOC_NBR, 
	E.ORD_QTY 
WITH UR 

 SELECT DISTINCT A.RQST_ID 
 FROM PRTHD.HOST_RQST  A, 
     PRTHD.HOST_RQST_PO  B, 
    PRTHD.BYO_PO C, 
     PRTHD.MBMVNDR_LOC_ONBRD  D 
 WHERE A.RQST_ID = B.RQST_ID 
 AND B.ACCT_PO_TYP_CD = C.ACCT_PO_TYP_CD 
 AND B.BYO_NBR = C.BYO_NBR 
 AND B.MKT_DC_IND = C.MKT_DC_IND 
 AND B.MKT_DC_NBR = C.MKT_DC_NBR 
 AND C.ORD_SEQ_NBR = 1
 AND B.PO_CTRL_NBR = C.PO_CTRL_NBR 
 AND C.PO_STAT_CD = 1
 AND C.DSVC_TYP_CD IN (2) 
 AND C.EXPCTD_ARVL_DT >= {d '2017-01-31'}
 AND C.EXPCTD_ARVL_DT <  {d '2017-03-01'} 
 AND C.OCYC_SOQ_RSN_CD NOT IN (130, 170, 171, 290, 300) 
 AND CAST(C.MVNDR_NBR as VARCHAR(20)) = D.MBAS_MVNDR_NBR 
 AND CAST(C.MER_DEPT_NBR as VARCHAR(20)) = D.MBAS_DEPT_NBR 
 AND CAST(C.CENTRV_LOC_NBR as VARCHAR(20)) = D.MBAS_SHTO_LOC_NBR 
 AND D.MBAS_SHTO_LTYP_CD = 2
 AND D.MER_BASE_CD = 1
 AND D.LPLNG_ACTV_FLG = 'Y' 
 ORDER BY A.RQST_ID 
 WITH UR
 
 
 SELECT
      SCHN_APPL_CPNT_NM, A.SCHN_APPL_WRKFLW_ID, AVG((extract(DAY FROM CPNT_END_TS - CPNT_BGN_TS)*24*60*60)+
   (extract(HOUR FROM CPNT_END_TS - CPNT_BGN_TS)*60*60)+
   (extract(MINUTE FROM CPNT_END_TS - CPNT_BGN_TS)*60)+
   extract(SECOND FROM CPNT_END_TS - CPNT_BGN_TS)) AVG_RUN,
  SUM((extract(DAY FROM CPNT_END_TS - CPNT_BGN_TS)*24*60*60)+
   (extract(HOUR FROM CPNT_END_TS - CPNT_BGN_TS)*60*60)+
   (extract(MINUTE FROM CPNT_END_TS - CPNT_BGN_TS)*60)+
   extract(SECOND FROM CPNT_END_TS - CPNT_BGN_TS)) SUM_RUN
   FROM SCHN_APPL_EVNT_LOG A, SCHN_APPL_CPNT B, SCHN_APPL_RUN R WHERE A.SCHN_TRANS_ID = R.SCHN_APPL_RUN_ID AND
                                                                      A.SCHN_APPL_CPNT_ID = B.SCHN_APPL_CPNT_ID
                                                                      AND A.CRT_TS >= R.BGN_TS
    AND A.CRT_TS <=  R.END_TS
   --AND SCHN_APPL_CPNT_ID = 0
   --AND A.SCHN_APPL_WRKFLW_ID in (2,13,16,17)
   AND A.SCHN_TRANS_ID = '1195'
   GROUP BY SCHN_APPL_CPNT_NM,  A.SCHN_APPL_WRKFLW_ID
   
   
   

--ORD GRP VNDR PARM
SELECT D.MBAS_MVNDR_NBR, F.LOAD_PLNG_PARM_CD, PARM_CHAR_VAL, PARM_INTG_VAL, PARM_DEC_VAL, PARM_FLG_VAL, 
       F.LOAD_PLNG_PARM_TYP_IND, G.LOAD_PLNG_PARM_CTGRY_CD, E.EFF_BGN_DT, E.EFF_END_DT 
FROM SCHN_EFS_RTE  A, 
	SCHN_EFS_RTE_STP B, 
     SCHN_ORD_GRP  C, 
     SCHN_ORD_GRP_VNDR  D, 
     SCHN_ORD_GRP_VNDR_PARM E, 
     LOAD_PLNG_PARM_CD F, 
	LPLNG_PARM_CTGRY_XREF G 
WHERE A.SCHN_EFS_RTE_ID = ? 
AND   (A.EFF_BGN_DT <= {d '2017-03-16'} AND A.EFF_END_DT >= {d '2017-04-10'}) 
AND   A.ACTV_FLG = 'Y' 
AND   A.SCHN_EFS_RTE_ID = B.SCHN_EFS_RTE_ID 
AND   (B.EFF_BGN_DT <= {d '2017-03-16'} AND B.EFF_END_DT >= {d '2017-04-10'} ) 
AND   B.ACTV_FLG = 'Y' 
AND   B.SCHN_ORD_GRP_ID = C.SCHN_ORD_GRP_ID 
AND   G.LOAD_PLNG_PARM_CTGRY_CD = 26 
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
					 PARM_DEC_VAL, PARM_FLG_VAL, G.LOAD_PLNG_PARM_TYP_IND, H.LOAD_PLNG_PARM_CTGRY_CD, E.EFF_BGN_DT, E.EFF_END_DT  

					FROM SCHN_EFS_RTE  A,  
						SCHN_EFS_RTE_STP   B,  
					     SCHN_ORD_GRP  C,  
					     SCHN_ORD_GRP_VNDR  D,  
					     SCHN_OGVNDR_SKU_EX_PARM  E,  
					     {0}LOAD_PLNG_FCST_DLY F,  
					     LOAD_PLNG_PARM_CD G,  
						LPLNG_PARM_CTGRY_XREF H  

					WHERE A.SCHN_EFS_RTE_ID = 2490  
					AND (A.EFF_BGN_DT <= {d '2017-03-16'} AND A.EFF_END_DT >= {d '2017-04-10'})  
					AND A.ACTV_FLG = 'Y'  
					AND A.SCHN_EFS_RTE_ID = B.SCHN_EFS_RTE_ID  
					AND (B.EFF_BGN_DT <= {d '2017-03-16'} AND B.EFF_END_DT >= {d '2017-04-10'} )  
					AND B.ACTV_FLG = 'Y'  
					AND B.SCHN_ORD_GRP_ID = C.SCHN_ORD_GRP_ID  
					AND H.LOAD_PLNG_PARM_CTGRY_CD = {d '2017-04-10'}  
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
					
					
					
 WITH CNVRT AS ( SELECT A.SKU_NBR
    , A.MVNDR_NBR
    , A.BPK_GTIN_NBR AS GTIN_NBR
    , A.BPK_GLN_NBR AS GLN_NBR
    , A.DC_NBR, B.PLT_LAYR_CASE_QTY, B.PLT_CASE_LAYR_QTY, B.PLT_STACK_QTY, I.HAZMAT_CD_VAL
 FROM prthd.MVNDR_SKU_DC       A
    , GTIN_GLN_SCHN      B
    , GTIN_GLN_CHNL      C
    , MVNDR_GLN          D
    , UPC_SKU            E
    , MVNDR_UPC          F
    , GTIN_GLN_SELL_ITEM G
    , OMS_ONLN_ITEM      H
    , UPC_WST_SRC I
 WHERE 1=1
    --A.SKU_NBR = 1000037804 AND A.MVNDR_NBR = 16156
    AND A.SKU_NBR = 538628 AND A.MVNDR_NBR = 662518
    AND A.DC_NBR = 5087 AND C.GTIN_STAT_CD IN (100,720)
    AND F.ACTV_FLG = 'Y'
    AND A.BPK_GTIN_NBR = B.GTIN_NBR
    AND A.BPK_GLN_NBR = B.GLN_NBR
    AND A.BUY_UOM_QTY = B.PKG_EA_QTY
    AND C.GTIN_NBR = B.OBASE_GTIN_NBR
    AND C.GLN_NBR = B.GLN_NBR
    AND D.MVNDR_NBR = A.MVNDR_NBR
    AND D.GLN_NBR = B.GLN_NBR
    AND E.SKU_NBR = A.SKU_NBR
    AND F.UPC_CD = E.UPC_CD
    AND F.MVNDR_NBR = D.MVNDR_NBR
    AND G.GTIN_NBR = B.OBASE_GTIN_NBR
    AND G.GLN_NBR = B.GLN_NBR
    AND G.UPC_CD = F.UPC_CD
    AND H.GTIN_NBR = B.OBASE_GTIN_NBR
    AND H.GLN_NBR = B.GLN_NBR
    AND E.UPC_CD = I.UPC_CD
                            ) ,
 FIRST_LVL_QRY AS
            ( SELECT  A.SKU_NBR
                , A.MVNDR_NBR
                , A.DC_NBR
                , B.GTIN_NBR
                , B.GLN_NBR
                , B.PKG_HLVL_TYP_CD
                , B.RETL_ITEM_GRS_WT
                , B.RETL_WT_UOM_CD
                , B.RETL_ITEM_HGHT
                , B.RETL_HGHT_UOM_CD
                , B.RETL_ITEM_WID
                , B.RETL_WID_UOM_CD
                , B.RETL_ITEM_DPTH
                , B.RETL_DPTH_UOM_CD
                , B.PKG_EA_QTY
                , B.ORDRBL_UNT_FLG
                , B.LAST_UPD_TS
                , B.STACK_FLG
                , B.NEST_ITEM_FLG
                , B.NEST_INCRM_LEN
                , C.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
                , ROW_NUMBER() OVER( PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR ORDER BY C.OMS_ID DESC ) AS RNK
            FROM CNVRT          A
                , GTIN_GLN_SCHN B
                , OMS_ONLN_ITEM C
            WHERE A.GTIN_NBR = B.GTIN_NBR
                AND A.GLN_NBR = B.GLN_NBR
                AND C.GTIN_NBR = B.GTIN_NBR
                AND C.GLN_NBR = B.GLN_NBR
                AND (( B.PKG_HLVL_EXPIR_DT IS NULL) OR ( B.PKG_HLVL_EXPIR_DT >= current_date))
            GROUP BY A.SKU_NBR
                , A.MVNDR_NBR
                , A.DC_NBR
                , B.GTIN_NBR
                , B.GLN_NBR
                , B.PKG_HLVL_TYP_CD
                , B.RETL_ITEM_GRS_WT
                , B.RETL_WT_UOM_CD
                , B.RETL_ITEM_HGHT
                , B.RETL_HGHT_UOM_CD
                , B.RETL_ITEM_WID
                , B.RETL_WID_UOM_CD
                , B.RETL_ITEM_DPTH
                , B.RETL_DPTH_UOM_CD
                , B.PKG_EA_QTY
                , B.ORDRBL_UNT_FLG
                , B.LAST_UPD_TS
                , B.STACK_FLG
                , B.NEST_ITEM_FLG
                , B.NEST_INCRM_LEN
                , C.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL) ,gi
 SECOND_LVL_QRY AS
                ( SELECT A.SKU_NBR
                , A.MVNDR_NBR
                , A.DC_NBR
                , E.GTIN_NBR
                , E.GLN_NBR
                , E.PKG_HLVL_TYP_CD
                , E.RETL_ITEM_GRS_WT
                , E.RETL_WT_UOM_CD
                , E.RETL_ITEM_HGHT
                , E.RETL_HGHT_UOM_CD
                , E.RETL_ITEM_WID
                , E.RETL_WID_UOM_CD
                , E.RETL_ITEM_DPTH
                , E.RETL_DPTH_UOM_CD
                , E.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
                , E.ORDRBL_UNT_FLG
                , E.LAST_UPD_TS
                , E.STACK_FLG
                , E.NEST_ITEM_FLG
                , E.NEST_INCRM_LEN
                , F.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
                , ROW_NUMBER() OVER( PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR ORDER BY F.OMS_ID DESC ) AS RNK
            FROM FIRST_LVL_QRY       A
                , PRNT_CHLD_GTIN_GLN D
                , GTIN_GLN_SCHN      E
                , OMS_ONLN_ITEM      F
            WHERE A.GTIN_NBR = D.CHLD_GTIN_NBR
                AND A.GLN_NBR = D.GLN_NBR
                AND E.GTIN_NBR = D.PRNT_GTIN_NBR
                AND E.GLN_NBR = D.GLN_NBR
                AND F.GTIN_NBR = E.GTIN_NBR
                AND F.GLN_NBR = E.GLN_NBR
                AND A.RNK = 1
                AND (( E.PKG_HLVL_EXPIR_DT IS NULL) OR ( E.PKG_HLVL_EXPIR_DT >= CURRENT_DATE ))
            GROUP BY A.SKU_NBR
                , A.MVNDR_NBR
                , A.DC_NBR
                , E.GTIN_NBR
                , E.GLN_NBR
                , E.PKG_HLVL_TYP_CD
                , E.RETL_ITEM_GRS_WT
                , E.RETL_WT_UOM_CD
                , E.RETL_ITEM_HGHT
                , E.RETL_HGHT_UOM_CD
                , E.RETL_ITEM_WID
                , E.RETL_WID_UOM_CD
                , E.RETL_ITEM_DPTH
                , E.RETL_DPTH_UOM_CD
                , E.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
                , E.ORDRBL_UNT_FLG
                , E.LAST_UPD_TS
                , E.STACK_FLG
                , E.NEST_ITEM_FLG
                , E.NEST_INCRM_LEN
                , F.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL) ,
 THIRD_LVL_QRY AS
            ( SELECT A.SKU_NBR
                , A.MVNDR_NBR
                , A.DC_NBR
                , E.GTIN_NBR
                , E.GLN_NBR
                , E.PKG_HLVL_TYP_CD
                , E.RETL_ITEM_GRS_WT
                , E.RETL_WT_UOM_CD
                , E.RETL_ITEM_HGHT
                , E.RETL_HGHT_UOM_CD
                , E.RETL_ITEM_WID
                , E.RETL_WID_UOM_CD
                , E.RETL_ITEM_DPTH
                , E.RETL_DPTH_UOM_CD
                , E.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
                , E.ORDRBL_UNT_FLG
                , E.LAST_UPD_TS
                , E.STACK_FLG
                , E.NEST_ITEM_FLG
                , E.NEST_INCRM_LEN
                , F.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
                , ROW_NUMBER() OVER( PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR ORDER BY F.OMS_ID DESC ) AS RNK
            FROM SECOND_LVL_QRY      A
                , PRNT_CHLD_GTIN_GLN D
                , GTIN_GLN_SCHN      E
                , OMS_ONLN_ITEM      F
            WHERE A.GTIN_NBR = D.CHLD_GTIN_NBR
                AND A.GLN_NBR = D.GLN_NBR
                AND E.GTIN_NBR = D.PRNT_GTIN_NBR
                AND E.GLN_NBR = D.GLN_NBR
                AND F.GTIN_NBR = E.GTIN_NBR
                AND F.GLN_NBR = E.GLN_NBR
                AND A.RNK = 1
                AND (( E.PKG_HLVL_EXPIR_DT IS NULL) OR ( E.PKG_HLVL_EXPIR_DT >= CURRENT_DATE ))
            GROUP BY A.SKU_NBR
                , A.MVNDR_NBR
                , A.DC_NBR
                , E.GTIN_NBR
                , E.GLN_NBR
                , E.PKG_HLVL_TYP_CD
                , E.RETL_ITEM_GRS_WT
                , E.RETL_WT_UOM_CD
                , E.RETL_ITEM_HGHT
                , E.RETL_HGHT_UOM_CD
                , E.RETL_ITEM_WID
                , E.RETL_WID_UOM_CD
                , E.RETL_ITEM_DPTH
                , E.RETL_DPTH_UOM_CD
                , E.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
                , E.ORDRBL_UNT_FLG
                , E.LAST_UPD_TS
                , E.STACK_FLG
                , E.NEST_ITEM_FLG
                , E.NEST_INCRM_LEN
                , F.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL),
 FOURTH_LVL_QRY AS
            (SELECT A.SKU_NBR
                , A.MVNDR_NBR
                , A.DC_NBR
                , E.GTIN_NBR
                , E.GLN_NBR
                , E.PKG_HLVL_TYP_CD
                , E.RETL_ITEM_GRS_WT
                , E.RETL_WT_UOM_CD
                , E.RETL_ITEM_HGHT
                , E.RETL_HGHT_UOM_CD
                , E.RETL_ITEM_WID
                , E.RETL_WID_UOM_CD
                , E.RETL_ITEM_DPTH
                , E.RETL_DPTH_UOM_CD
                , E.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
                , E.ORDRBL_UNT_FLG
                , E.LAST_UPD_TS
                , E.STACK_FLG
                , E.NEST_ITEM_FLG
                , E.NEST_INCRM_LEN
                , F.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
                , ROW_NUMBER() OVER( PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR ORDER BY F.OMS_ID DESC ) AS RNK
            FROM THIRD_LVL_QRY       A
                , PRNT_CHLD_GTIN_GLN D
                , GTIN_GLN_SCHN      E
                , OMS_ONLN_ITEM      F
            WHERE A.GTIN_NBR = D.CHLD_GTIN_NBR
                AND A.GLN_NBR = D.GLN_NBR
                AND E.GTIN_NBR = D.PRNT_GTIN_NBR
                AND E.GLN_NBR = D.GLN_NBR
                AND F.GTIN_NBR = E.GTIN_NBR
                AND F.GLN_NBR = E.GLN_NBR
                AND A.RNK = 1
                AND (( E.PKG_HLVL_EXPIR_DT IS NULL) OR ( E.PKG_HLVL_EXPIR_DT >= CURRENT_DATE ))
            GROUP BY A.SKU_NBR
                , A.MVNDR_NBR
                , A.DC_NBR
                , E.GTIN_NBR
                , E.GLN_NBR
                , E.PKG_HLVL_TYP_CD
                , E.RETL_ITEM_GRS_WT
                , E.RETL_WT_UOM_CD
                , E.RETL_ITEM_HGHT
                , E.RETL_HGHT_UOM_CD
                , E.RETL_ITEM_WID
                , E.RETL_WID_UOM_CD
                , E.RETL_ITEM_DPTH
                , E.RETL_DPTH_UOM_CD
                , E.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
                , E.ORDRBL_UNT_FLG
                , E.LAST_UPD_TS
                , E.STACK_FLG
                , E.NEST_ITEM_FLG
                , E.NEST_INCRM_LEN
                , F.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL)
 SELECT A.SKU_NBR
    , A.MVNDR_NBR
    , A.DC_NBR
    , A.GTIN_NBR
    , A.GLN_NBR
    , A.PKG_HLVL_TYP_CD
    , A.RETL_ITEM_GRS_WT
    , A.RETL_WT_UOM_CD
    , A.RETL_ITEM_HGHT
    , A.RETL_HGHT_UOM_CD
    , A.RETL_ITEM_WID
    , A.RETL_WID_UOM_CD
    , A.RETL_ITEM_DPTH
    , A.RETL_DPTH_UOM_CD
    , A.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
    , A.ORDRBL_UNT_FLG
    , A.LAST_UPD_TS
    , A.STACK_FLG
    , A.NEST_ITEM_FLG
    , A.NEST_INCRM_LEN
    , A.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
        FROM((SELECT A.SKU_NBR
              , A.MVNDR_NBR
              , A.DC_NBR
              , A.GTIN_NBR
              , A.GLN_NBR
              , A.PKG_HLVL_TYP_CD
              , A.RETL_ITEM_GRS_WT
              , A.RETL_WT_UOM_CD
              , A.RETL_ITEM_HGHT
              , A.RETL_HGHT_UOM_CD
              , A.RETL_ITEM_WID
              , A.RETL_WID_UOM_CD
              , A.RETL_ITEM_DPTH
              , A.RETL_DPTH_UOM_CD
              , A.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
              , A.ORDRBL_UNT_FLG
              , A.LAST_UPD_TS
              , A.STACK_FLG
              , A.NEST_ITEM_FLG
              , A.NEST_INCRM_LEN
              , A.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
           FROM FIRST_LVL_QRY AS A
           WHERE A.RNK = 1
           GROUP BY A.SKU_NBR
              , A.MVNDR_NBR
              , A.DC_NBR
              , A.GTIN_NBR
              , A.GLN_NBR
              , A.PKG_HLVL_TYP_CD
              , A.RETL_ITEM_GRS_WT
              , A.RETL_WT_UOM_CD
              , A.RETL_ITEM_HGHT
              , A.RETL_HGHT_UOM_CD
              , A.RETL_ITEM_WID
              , A.RETL_WID_UOM_CD
              , A.RETL_ITEM_DPTH
              , A.RETL_DPTH_UOM_CD
              , A.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
              , A.ORDRBL_UNT_FLG
              , A.LAST_UPD_TS
              , A.STACK_FLG
              , A.NEST_ITEM_FLG
              , A.NEST_INCRM_LEN
              , A.OMS_ID
          )
          UNION
           (SELECT A.SKU_NBR
              , A.MVNDR_NBR
              , A.DC_NBR
              , A.GTIN_NBR
              , A.GLN_NBR
              , A.PKG_HLVL_TYP_CD
              , A.RETL_ITEM_GRS_WT
              , A.RETL_WT_UOM_CD
              , A.RETL_ITEM_HGHT
              , A.RETL_HGHT_UOM_CD
              , A.RETL_ITEM_WID
              , A.RETL_WID_UOM_CD
              , A.RETL_ITEM_DPTH
              , A.RETL_DPTH_UOM_CD
              , A.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
              , A.ORDRBL_UNT_FLG
              , A.LAST_UPD_TS
              , A.STACK_FLG
              , A.NEST_ITEM_FLG
              , A.NEST_INCRM_LEN
              , A.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
              FROM SECOND_LVL_QRY AS A
              WHERE A.RNK = 1
              GROUP BY A.SKU_NBR
              , A.MVNDR_NBR
              , A.DC_NBR
              , A.GTIN_NBR
              , A.GLN_NBR
              , A.PKG_HLVL_TYP_CD
              , A.RETL_ITEM_GRS_WT
              , A.RETL_WT_UOM_CD
              , A.RETL_ITEM_HGHT
              , A.RETL_HGHT_UOM_CD
              , A.RETL_ITEM_WID
              , A.RETL_WID_UOM_CD
              , A.RETL_ITEM_DPTH
              , A.RETL_DPTH_UOM_CD
              , A.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
              , A.ORDRBL_UNT_FLG
              , A.LAST_UPD_TS
              , A.STACK_FLG
              , A.NEST_ITEM_FLG
              , A.NEST_INCRM_LEN
              , A.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
            )
           UNION
           (SELECT A.SKU_NBR
              , A.MVNDR_NBR
              , A.DC_NBR
              , A.GTIN_NBR
              , A.GLN_NBR
              , A.PKG_HLVL_TYP_CD
              , A.RETL_ITEM_GRS_WT
              , A.RETL_WT_UOM_CD
              , A.RETL_ITEM_HGHT
              , A.RETL_HGHT_UOM_CD
              , A.RETL_ITEM_WID
              , A.RETL_WID_UOM_CD
              , A.RETL_ITEM_DPTH
              , A.RETL_DPTH_UOM_CD
              , A.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
              , A.ORDRBL_UNT_FLG
              , A.LAST_UPD_TS
              , A.STACK_FLG
              , A.NEST_ITEM_FLG
              , A.NEST_INCRM_LEN
              , A.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
              FROM THIRD_LVL_QRY AS A
              WHERE A.RNK = 1
              GROUP BY A.SKU_NBR
              , A.MVNDR_NBR
              , A.DC_NBR
              , A.GTIN_NBR
              , A.GLN_NBR
              , A.PKG_HLVL_TYP_CD
              , A.RETL_ITEM_GRS_WT
              , A.RETL_WT_UOM_CD
              , A.RETL_ITEM_HGHT
              , A.RETL_HGHT_UOM_CD
              , A.RETL_ITEM_WID
              , A.RETL_WID_UOM_CD
              , A.RETL_ITEM_DPTH
              , A.RETL_DPTH_UOM_CD
              , A.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
              , A.ORDRBL_UNT_FLG
              , A.LAST_UPD_TS
              , A.STACK_FLG
              , A.NEST_ITEM_FLG
              , A.NEST_INCRM_LEN
              , A.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
            )
           UNION
           (SELECT A.SKU_NBR
              , A.MVNDR_NBR
              , A.DC_NBR
              , A.GTIN_NBR
              , A.GLN_NBR
              , A.PKG_HLVL_TYP_CD
              , A.RETL_ITEM_GRS_WT
              , A.RETL_WT_UOM_CD
              , A.RETL_ITEM_HGHT
              , A.RETL_HGHT_UOM_CD
              , A.RETL_ITEM_WID
              , A.RETL_WID_UOM_CD
              , A.RETL_ITEM_DPTH
              , A.RETL_DPTH_UOM_CD
              , A.PKG_EA_QTY
              , A.ORDRBL_UNT_FLG
              , A.LAST_UPD_TS
              , A.STACK_FLG
              , A.NEST_ITEM_FLG
              , A.NEST_INCRM_LEN
              , A.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
              FROM FOURTH_LVL_QRY AS A
              WHERE A.RNK = 1
              GROUP BY A.SKU_NBR
              , A.MVNDR_NBR
              , A.DC_NBR
              , A.GTIN_NBR
              , A.GLN_NBR
              , A.PKG_HLVL_TYP_CD
              , A.RETL_ITEM_GRS_WT
              , A.RETL_WT_UOM_CD
              , A.RETL_ITEM_HGHT
              , A.RETL_HGHT_UOM_CD
              , A.RETL_ITEM_WID
              , A.RETL_WID_UOM_CD
              , A.RETL_ITEM_DPTH
              , A.RETL_DPTH_UOM_CD
              , A.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
              , A.ORDRBL_UNT_FLG
              , A.LAST_UPD_TS
              , A.STACK_FLG
              , A.NEST_ITEM_FLG
              , A.NEST_INCRM_LEN
              , A.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
            )
                                    ) A
 GROUP BY A.SKU_NBR
    , A.MVNDR_NBR
    , A.DC_NBR
    , A.GTIN_NBR
    , A.GLN_NBR
    , A.PKG_HLVL_TYP_CD
    , A.RETL_ITEM_GRS_WT
    , A.RETL_WT_UOM_CD
    , A.RETL_ITEM_HGHT
    , A.RETL_HGHT_UOM_CD
    , A.RETL_ITEM_WID
    , A.RETL_WID_UOM_CD
    , A.RETL_ITEM_DPTH
    , A.RETL_DPTH_UOM_CD
    , A.PKG_EA_QTY -- or A.PKG_UNT_QTY if Mama Baby SKU
    , A.ORDRBL_UNT_FLG
    , A.LAST_UPD_TS
    , A.STACK_FLG
    , A.NEST_ITEM_FLG
    , A.NEST_INCRM_LEN
    , A.OMS_ID, A.PLT_LAYR_CASE_QTY, A.PLT_CASE_LAYR_QTY, A.PLT_STACK_QTY, A.HAZMAT_CD_VAL
 ORDER BY A.SKU_NBR
        , A.MVNDR_NBR
        , A.DC_NBR
        , A.PKG_HLVL_TYP_CD
 WITH UR



-- TRUNCATE SIM TABLES


delete from scsysc021.plnd_load_grp;
delete from scsysc021.plnd_load_grp_ver;
delete from scsysc021.plnd_load;
delete from scsysc021.plnd_load_sku;
delete from scsysc021.plnd_ltl_load;
delete from scsysc021.plnd_ltl_load_sku;
delete from scsysc021.plnd_load_sku_need_date;
delete from scsysc021.PLSKU_LPLNG_EXTNL_DMND_X;
delete from scsysc021.PLLSKU_LPLNG_EDMND_X;

delete from scsysc021.pre_plnd_load_grp;
delete from scsysc021.pre_plnd_load_grp_ver;
delete from scsysc021.pre_plnd_load;
delete from scsysc021.pre_plnd_load_sku;
delete from scsysc021.pre_plnd_ltl_load;
delete from scsysc021.pre_plnd_ltl_load_sku;
delete from scsysc021.pre_plnd_load_sku_need_date;
delete from scsysc021.schn_efs_Rte_stats;
delete from scsysc021.PPLLSKU_LPLNG_EDMND_X;
delete from scsysc021.PPLSKU_LPLNG_EDMND_X;

					