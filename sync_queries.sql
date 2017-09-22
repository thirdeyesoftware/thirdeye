WITH CNVRT AS ( 
 SELECT  A.SKU_NBR 
 	, C.MVNDR_NBR 
 	, F.DC_NBR 
 	, E.GTIN_NBR 
 	, E.GLN_NBR 
 	, F.BUY_UOM_QTY 
 	, MAX(E.LAST_UPD_TS) AS TS_RANK 
 FROM UPC A 
 	, GTIN_GLN_SELL_ITEM B 
 	, MVNDR_GLN C 
 	, GTIN_GLN_CHNL E 
 	, MVNDR_SKU_DC F 
 WHERE A.UPC_CD = B.UPC_CD 
 	AND B.GLN_NBR = C.GLN_NBR 
 	AND B.GTIN_NBR = E.GTIN_NBR 
 	AND B.GLN_NBR = E.GLN_NBR 
 	AND C.MVNDR_NBR = F.MVNDR_NBR 
 	AND A.SKU_NBR = F.SKU_NBR 
 	AND A.SKU_NBR = 101370  AND C.MVNDR_NBR = '153001' AND F.DC_NBR=5520
 GROUP BY  A.SKU_NBR 
 	, C.MVNDR_NBR 
 	, F.DC_NBR 
 	, E.GTIN_NBR 
 	, E.GLN_NBR 
 	, F.BUY_UOM_QTY 
 	), 
		BPK_QRY AS (SELECT A.SKU_NBR 
			, A.MVNDR_NBR 
			, A.DC_NBR 
			, A.GTIN_NBR 
			, A.GLN_NBR 
			, A.BUY_UOM_QTY 
			, A.TS_RANK 
		FROM CNVRT AS A 
			, GTIN_GLN_SCHN AS B 
		WHERE A.BUY_UOM_QTY = B.PKG_UNT_QTY 
			AND A.GTIN_NBR = B.GTIN_NBR 
			AND A.GLN_NBR = B.GLN_NBR				 
		UNION	 
		SELECT DISTINCT A.SKU_NBR 
			, A.MVNDR_NBR 
			, A.DC_NBR 
			, D.GTIN_NBR 
			, D.GLN_NBR 
			, A.BUY_UOM_QTY 
			, A.TS_RANK 
		FROM CNVRT AS A 
			, PRNT_CHLD_GTIN_GLN AS G1 
			, GTIN_GLN_SCHN AS D 
		WHERE A.GLN_NBR = G1.GLN_NBR 
			AND A.GTIN_NBR = G1.CHLD_GTIN_NBR 
			AND G1.PRNT_GTIN_NBR = D.GTIN_NBR 
			AND G1.GLN_NBR = D.GLN_NBR 
			AND A.BUY_UOM_QTY = D.PKG_UNT_QTY 
			UNION 
		SELECT DISTINCT A.SKU_NBR 
			, A.MVNDR_NBR 
			, A.DC_NBR 
			, D.GTIN_NBR 
			, D.GLN_NBR 
			, A.BUY_UOM_QTY 
			, A.TS_RANK 
			FROM CNVRT AS A 
				, PRNT_CHLD_GTIN_GLN AS G1 
				, PRNT_CHLD_GTIN_GLN AS G2 
				, GTIN_GLN_SCHN AS D 
			WHERE A.GLN_NBR = G1.GLN_NBR 
				AND A.GTIN_NBR = G1.CHLD_GTIN_NBR 
				AND G1.PRNT_GTIN_NBR = G2.CHLD_GTIN_NBR 
				AND G1.GLN_NBR = G2.GLN_NBR 
				AND G2.PRNT_GTIN_NBR = D.GTIN_NBR 
				AND G2.GLN_NBR = D.GLN_NBR 
				AND A.BUY_UOM_QTY = D.PKG_UNT_QTY 
				UNION 
			SELECT DISTINCT A.SKU_NBR 
				, A.MVNDR_NBR 
				, A.DC_NBR 
				, D.GTIN_NBR 
				, D.GLN_NBR 
				, A.BUY_UOM_QTY 
				, A.TS_RANK 
			FROM CNVRT AS A 
				, PRNT_CHLD_GTIN_GLN AS G1 
				, PRNT_CHLD_GTIN_GLN AS G2 
				, PRNT_CHLD_GTIN_GLN AS G3 
				, GTIN_GLN_SCHN AS D 
			WHERE A.GLN_NBR = G1.GLN_NBR 
				AND A.GTIN_NBR = G1.CHLD_GTIN_NBR 
				AND G1.PRNT_GTIN_NBR = G2.CHLD_GTIN_NBR 
				AND G1.GLN_NBR = G2.GLN_NBR 
				AND G2.PRNT_GTIN_NBR = G3.CHLD_GTIN_NBR 
				AND G2.GLN_NBR = G3.GLN_NBR 
				AND G3.PRNT_GTIN_NBR = D.GTIN_NBR 
				AND G3.GLN_NBR = D.GLN_NBR 
				AND A.BUY_UOM_QTY = D.PKG_UNT_QTY 
			), 
			PRI_TBL AS ( 
			SELECT A.SKU_NBR 
				, A.MVNDR_NBR 
				, A.DC_NBR 
				, MAX (A.TS_RANK) AS TS_RANK 
			FROM BPK_QRY AS A 
			GROUP BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR) 
   		SELECT 
				A.SKU_NBR, 
   			A.MVNDR_NBR,  
   			A.DC_NBR,  
   			A.GTIN_NBR,  
   			A.GLN_NBR,  
   			A.PKG_HLVL_TYP_CD,  
   			A.RETL_ITEM_GRS_WT,  
   			A.RETL_WT_UOM_CD,  
   			A.RETL_ITEM_HGHT,  
   			A.RETL_HGHT_UOM_CD,  
   			A.RETL_ITEM_WID,  
   			A.RETL_WID_UOM_CD,  
   			A.RETL_ITEM_DPTH,  
   			A.RETL_DPTH_UOM_CD, 
   			A.PKG_UNT_QTY,  
   			A.ORDRBL_UNT_FLG,  
   			A.LAST_UPD_TS,  
   			A.STACK_FLG,  
   			A.NEST_ITEM_FLG, 
   			A.NEST_INCRM_LEN  
   	   FROM  ( 
 SELECT DISTINCT 
 		A.SKU_NBR, 
 		A.MVNDR_NBR, 
 		A.DC_NBR, 
 		J.GTIN_NBR, 
         J.GLN_NBR, 
         J.PKG_HLVL_TYP_CD, 
         J.RETL_ITEM_GRS_WT, 
         J.RETL_WT_UOM_CD, 
         J.RETL_ITEM_HGHT, 
         J.RETL_HGHT_UOM_CD, 
         J.RETL_ITEM_WID, 
         J.RETL_WID_UOM_CD, 
         J.RETL_ITEM_DPTH, 
         J.RETL_DPTH_UOM_CD, 
         J.PKG_UNT_QTY, 
         J.ORDRBL_UNT_FLG, 
         J.LAST_UPD_TS, 
         J.STACK_FLG, 
         J.NEST_ITEM_FLG, 
         J.NEST_INCRM_LEN, 
 		  ROW_NUMBER() OVER(PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR,J.PKG_HLVL_TYP_CD 
 			ORDER BY J.LAST_UPD_TS DESC) RNK 
 FROM   BPK_QRY AS A, 
 		PRI_TBL AS B, 
 		GTIN_GLN_SCHN AS D, 
        PRNT_CHLD_GTIN_GLN AS E, 
        GTIN_GLN_SCHN AS F, 
        PRNT_CHLD_GTIN_GLN AS G, 
        GTIN_GLN_SCHN AS H, 
        PRNT_CHLD_GTIN_GLN AS I, 
        GTIN_GLN_SCHN AS J 
 WHERE  D.GTIN_NBR = A.GTIN_NBR 
        AND D.GLN_NBR = A.GLN_NBR 
        AND D.GTIN_NBR = E.CHLD_GTIN_NBR 
        AND D.GLN_NBR = E.GLN_NBR 
        AND F.GTIN_NBR = E.PRNT_GTIN_NBR 
        AND F.GLN_NBR = E.GLN_NBR 
        AND F.GTIN_NBR = G.CHLD_GTIN_NBR 
        AND F.GLN_NBR = G.GLN_NBR 
        AND H.GTIN_NBR = G.PRNT_GTIN_NBR 
        AND H.GLN_NBR = G.GLN_NBR 
        AND H.GTIN_NBR = I.CHLD_GTIN_NBR 
        AND H.GLN_NBR = I.GLN_NBR 
        AND J.GTIN_NBR = I.PRNT_GTIN_NBR 
        AND J.GLN_NBR = I.GLN_NBR 
         AND A.SKU_NBR = B.SKU_NBR 
        AND A.MVNDR_NBR = B.MVNDR_NBR 
        AND A.DC_NBR = B.DC_NBR 
        AND A.TS_RANK = B.TS_RANK 
        UNION 
 SELECT DISTINCT 
 				A.SKU_NBR, 
 				A.MVNDR_NBR, 
 				A.DC_NBR, 
		H.GTIN_NBR, 
        H.GLN_NBR, 
        H.PKG_HLVL_TYP_CD, 
        H.RETL_ITEM_GRS_WT, 
        H.RETL_WT_UOM_CD, 
        H.RETL_ITEM_HGHT, 
        H.RETL_HGHT_UOM_CD, 
        H.RETL_ITEM_WID, 
        H.RETL_WID_UOM_CD, 
        H.RETL_ITEM_DPTH, 
        H.RETL_DPTH_UOM_CD, 
        H.PKG_UNT_QTY, 
        H.ORDRBL_UNT_FLG, 
        H.LAST_UPD_TS, 
        H.STACK_FLG, 
        H.NEST_ITEM_FLG, 
        H.NEST_INCRM_LEN, 
  		 ROW_NUMBER() OVER( PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR,H.PKG_HLVL_TYP_CD 
        ORDER BY H.LAST_UPD_TS DESC) RNK 
 FROM   BPK_QRY AS A, 
 		PRI_TBL AS B, 
 		GTIN_GLN_SCHN AS D, 
        PRNT_CHLD_GTIN_GLN AS E, 
        GTIN_GLN_SCHN AS F, 
        PRNT_CHLD_GTIN_GLN AS G, 
        GTIN_GLN_SCHN AS H 
 WHERE  D.GTIN_NBR = A.GTIN_NBR 
        AND D.GLN_NBR = A.GLN_NBR 
        AND D.GTIN_NBR = E.CHLD_GTIN_NBR 
        AND D.GLN_NBR = E.GLN_NBR 
        AND F.GTIN_NBR = E.PRNT_GTIN_NBR 
        AND F.GLN_NBR = E.GLN_NBR 
        AND F.GTIN_NBR = G.CHLD_GTIN_NBR 
        AND F.GLN_NBR = G.GLN_NBR 
        AND H.GTIN_NBR = G.PRNT_GTIN_NBR 
        AND H.GLN_NBR = G.GLN_NBR 
         AND A.SKU_NBR = B.SKU_NBR 
        AND A.MVNDR_NBR = B.MVNDR_NBR 
        AND A.DC_NBR = B.DC_NBR 
        AND A.TS_RANK = B.TS_RANK 
 UNION 
 SELECT DISTINCT 
		A.SKU_NBR, 
		A.MVNDR_NBR, 
		A.DC_NBR, 
		F.GTIN_NBR, 
       F.GLN_NBR, 
       F.PKG_HLVL_TYP_CD, 
       F.RETL_ITEM_GRS_WT, 
       F.RETL_WT_UOM_CD, 
       F.RETL_ITEM_HGHT, 
       F.RETL_HGHT_UOM_CD, 
       F.RETL_ITEM_WID, 
       F.RETL_WID_UOM_CD, 
       F.RETL_ITEM_DPTH, 
       F.RETL_DPTH_UOM_CD, 
       F.PKG_UNT_QTY, 
       F.ORDRBL_UNT_FLG, 
       F.LAST_UPD_TS, 
       F.STACK_FLG, 
       F.NEST_ITEM_FLG, 
       F.NEST_INCRM_LEN, 
  		 ROW_NUMBER() OVER( PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR,F.PKG_HLVL_TYP_CD 
        ORDER BY F.LAST_UPD_TS DESC) RNK 
 FROM   BPK_QRY AS A, 
 		PRI_TBL AS B, 
 		GTIN_GLN_SCHN AS D, 
        PRNT_CHLD_GTIN_GLN AS E, 
        GTIN_GLN_SCHN AS F 
 WHERE  D.GTIN_NBR = A.GTIN_NBR 
        AND D.GLN_NBR = A.GLN_NBR 
        AND D.GTIN_NBR = E.CHLD_GTIN_NBR 
        AND D.GLN_NBR = E.GLN_NBR 
        AND F.GTIN_NBR = E.PRNT_GTIN_NBR 
        AND F.GLN_NBR = E.GLN_NBR 
         AND A.SKU_NBR = B.SKU_NBR 
        AND A.MVNDR_NBR = B.MVNDR_NBR 
        AND A.DC_NBR = B.DC_NBR 
        AND A.TS_RANK = B.TS_RANK 
 UNION 
 SELECT DISTINCT 
		A.SKU_NBR, 
		A.MVNDR_NBR, 
		A.DC_NBR, 
		D.GTIN_NBR, 
       D.GLN_NBR, 
       D.PKG_HLVL_TYP_CD, 
       D.RETL_ITEM_GRS_WT, 
       D.RETL_WT_UOM_CD, 
       D.RETL_ITEM_HGHT, 
       D.RETL_HGHT_UOM_CD, 
       D.RETL_ITEM_WID, 
       D.RETL_WID_UOM_CD, 
       D.RETL_ITEM_DPTH, 
       D.RETL_DPTH_UOM_CD, 
       D.PKG_UNT_QTY, 
       D.ORDRBL_UNT_FLG, 
       D.LAST_UPD_TS, 
       D.STACK_FLG, 
       D.NEST_ITEM_FLG, 
       D.NEST_INCRM_LEN, 
       ROW_NUMBER() OVER( PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR,D.PKG_HLVL_TYP_CD 
        ORDER BY D.LAST_UPD_TS DESC) RNK 
 FROM   BPK_QRY AS A, 
 		PRI_TBL AS B, 
 		GTIN_GLN_SCHN AS D 
 WHERE  D.GTIN_NBR = A.GTIN_NBR 
        AND D.GLN_NBR = A.GLN_NBR 
        AND A.SKU_NBR = B.SKU_NBR 
        AND A.MVNDR_NBR = B.MVNDR_NBR 
        AND A.DC_NBR = B.DC_NBR 
        AND A.TS_RANK = B.TS_RANK ) A 
 WHERE 1=1 --AND RETL_ITEM_HGHT=5.250 -- A.SKU_NBR = 1000010760 AND A.MVNDR_NBR = 153001 
 ORDER  BY A.SKU_NBR, A.MVNDR_NBR,A.DC_NBR, A.PKG_HLVL_TYP_CD 

WITH CNVRT AS (  SELECT  A.SKU_NBR  	, C.MVNDR_NBR  	, F.DC_NBR  	, E.GTIN_NBR  	, E.GLN_NBR  	, F.BUY_UOM_QTY  	, MAX(E.LAST_UPD_TS) AS TS_RANK  FROM UPC A  	,
GTIN_GLN_SELL_ITEM B  	, MVNDR_GLN C  	, GTIN_GLN_CHNL E  	, MVNDR_SKU_DC F  WHERE A.UPC_CD = B.UPC_CD  	AND B.GLN_NBR = C.GLN_NBR  	AND B.GTIN_NBR = E.GTIN_NBR  	
AND B.GLN_NBR = E.GLN_NBR  	AND C.MVNDR_NBR = F.MVNDR_NBR  	AND A.SKU_NBR = F.SKU_NBR  	 AND A.SKU_NBR IN (101370)  AND C.MVNDR_NBR IN (153001)  AND F.DC_NBR = 5520   
GROUP BY  A.SKU_NBR  	, C.MVNDR_NBR  	, F.DC_NBR  	, E.GTIN_NBR  	, E.GLN_NBR  	, F.BUY_UOM_QTY  	), 		BPK_QRY AS (SELECT A.SKU_NBR 			, A.MVNDR_NBR 			, A.DC_NBR 			, A.GTIN_NBR 			, A.GLN_NBR 			, A.BUY_UOM_QTY 			, A.TS_RANK 		FROM CNVRT AS A 			, GTIN_GLN_SCHN AS B 		WHERE A.BUY_UOM_QTY = B.PKG_UNT_QTY 			AND A.GTIN_NBR = B.GTIN_NBR 			AND A.GLN_NBR = B.GLN_NBR				 		UNION	 		SELECT DISTINCT A.SKU_NBR 			, A.MVNDR_NBR 			, A.DC_NBR 			, D.GTIN_NBR 			, D.GLN_NBR 			, A.BUY_UOM_QTY 			, A.TS_RANK 		FROM CNVRT AS A 			, PRNT_CHLD_GTIN_GLN AS G1 			, GTIN_GLN_SCHN AS D 		WHERE A.GLN_NBR = G1.GLN_NBR 			AND A.GTIN_NBR = G1.CHLD_GTIN_NBR 			AND G1.PRNT_GTIN_NBR = D.GTIN_NBR 			AND G1.GLN_NBR = D.GLN_NBR 			AND A.BUY_UOM_QTY = D.PKG_UNT_QTY 			 			UNION 			 		SELECT DISTINCT A.SKU_NBR 			, A.MVNDR_NBR 			, A.DC_NBR 			, D.GTIN_NBR 			, D.GLN_NBR 			, A.BUY_UOM_QTY 			, A.TS_RANK 			FROM CNVRT AS A 				, PRNT_CHLD_GTIN_GLN AS G1 				, PRNT_CHLD_GTIN_GLN AS G2 				, GTIN_GLN_SCHN AS D 			WHERE A.GLN_NBR = G1.GLN_NBR 				AND A.GTIN_NBR = G1.CHLD_GTIN_NBR 				AND G1.PRNT_GTIN_NBR = G2.CHLD_GTIN_NBR 				AND G1.GLN_NBR = G2.GLN_NBR 				AND G2.PRNT_GTIN_NBR = D.GTIN_NBR 				AND G2.GLN_NBR = D.GLN_NBR 				AND A.BUY_UOM_QTY = D.PKG_UNT_QTY 					 				UNION 				 			SELECT DISTINCT A.SKU_NBR 				, A.MVNDR_NBR 				, A.DC_NBR 				, D.GTIN_NBR 				, D.GLN_NBR 				, A.BUY_UOM_QTY 				, A.TS_RANK 			FROM CNVRT AS A 				, PRNT_CHLD_GTIN_GLN AS G1 				, PRNT_CHLD_GTIN_GLN AS G2 				, PRNT_CHLD_GTIN_GLN AS G3 				, GTIN_GLN_SCHN AS D 			WHERE A.GLN_NBR = G1.GLN_NBR 				AND A.GTIN_NBR = G1.CHLD_GTIN_NBR 				AND G1.PRNT_GTIN_NBR = G2.CHLD_GTIN_NBR 				AND G1.GLN_NBR = G2.GLN_NBR 				AND G2.PRNT_GTIN_NBR = G3.CHLD_GTIN_NBR 				AND G2.GLN_NBR = G3.GLN_NBR 				AND G3.PRNT_GTIN_NBR = D.GTIN_NBR 				AND G3.GLN_NBR = D.GLN_NBR 				AND A.BUY_UOM_QTY = D.PKG_UNT_QTY 			 			), 			 			PRI_TBL AS ( 			SELECT A.SKU_NBR 				, A.MVNDR_NBR 				, A.DC_NBR 				, MAX (A.TS_RANK) AS TS_RANK 			FROM BPK_QRY AS A 			GROUP BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR)    		SELECT 				A.SKU_NBR,    			A.MVNDR_NBR,     			A.DC_NBR,     			A.GTIN_NBR,     			A.GLN_NBR,     			A.PKG_HLVL_TYP_CD,     			A.RETL_ITEM_GRS_WT,     			A.RETL_WT_UOM_CD,     			A.RETL_ITEM_HGHT,     			A.RETL_HGHT_UOM_CD,     			A.RETL_ITEM_WID,     			A.RETL_WID_UOM_CD,     			A.RETL_ITEM_DPTH,     			A.RETL_DPTH_UOM_CD,    			A.PKG_UNT_QTY,     			A.ORDRBL_UNT_FLG,     			A.LAST_UPD_TS,     			A.STACK_FLG,     			A.NEST_ITEM_FLG,    			A.NEST_INCRM_LEN     	   FROM  (  SELECT DISTINCT  		A.SKU_NBR,  		A.MVNDR_NBR,  		A.DC_NBR,  		J.GTIN_NBR,          J.GLN_NBR,          J.PKG_HLVL_TYP_CD,          J.RETL_ITEM_GRS_WT,          J.RETL_WT_UOM_CD,          J.RETL_ITEM_HGHT,          J.RETL_HGHT_UOM_CD,          J.RETL_ITEM_WID,          J.RETL_WID_UOM_CD,          J.RETL_ITEM_DPTH,          J.RETL_DPTH_UOM_CD,          J.PKG_UNT_QTY,          J.ORDRBL_UNT_FLG,          J.LAST_UPD_TS,          J.STACK_FLG,          J.NEST_ITEM_FLG,          J.NEST_INCRM_LEN,  		  ROW_NUMBER() OVER(PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR,J.PKG_HLVL_TYP_CD  			ORDER BY J.LAST_UPD_TS DESC) RNK  FROM   BPK_QRY AS A,  		PRI_TBL AS B,  		GTIN_GLN_SCHN AS D,         PRNT_CHLD_GTIN_GLN AS E,         GTIN_GLN_SCHN AS F,         PRNT_CHLD_GTIN_GLN AS G,         GTIN_GLN_SCHN AS H,         PRNT_CHLD_GTIN_GLN AS I,         GTIN_GLN_SCHN AS J  WHERE  D.GTIN_NBR = A.GTIN_NBR         AND D.GLN_NBR = A.GLN_NBR         AND D.GTIN_NBR = E.CHLD_GTIN_NBR         AND D.GLN_NBR = E.GLN_NBR         AND F.GTIN_NBR = E.PRNT_GTIN_NBR         AND F.GLN_NBR = E.GLN_NBR         AND F.GTIN_NBR = G.CHLD_GTIN_NBR         AND F.GLN_NBR = G.GLN_NBR         AND H.GTIN_NBR = G.PRNT_GTIN_NBR         AND H.GLN_NBR = G.GLN_NBR         AND H.GTIN_NBR = I.CHLD_GTIN_NBR         AND H.GLN_NBR = I.GLN_NBR         AND J.GTIN_NBR = I.PRNT_GTIN_NBR         AND J.GLN_NBR = I.GLN_NBR          AND A.SKU_NBR = B.SKU_NBR         AND A.MVNDR_NBR = B.MVNDR_NBR         AND A.DC_NBR = B.DC_NBR         AND A.TS_RANK = B.TS_RANK          UNION   SELECT DISTINCT  				A.SKU_NBR,  				A.MVNDR_NBR,  				A.DC_NBR, 		H.GTIN_NBR,         H.GLN_NBR,         H.PKG_HLVL_TYP_CD,         H.RETL_ITEM_GRS_WT,         H.RETL_WT_UOM_CD,         H.RETL_ITEM_HGHT,         H.RETL_HGHT_UOM_CD,         H.RETL_ITEM_WID,         H.RETL_WID_UOM_CD,         H.RETL_ITEM_DPTH,         H.RETL_DPTH_UOM_CD,         H.PKG_UNT_QTY,         H.ORDRBL_UNT_FLG,         H.LAST_UPD_TS,         H.STACK_FLG,         H.NEST_ITEM_FLG,         H.NEST_INCRM_LEN,   		 ROW_NUMBER() OVER( PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR,H.PKG_HLVL_TYP_CD         ORDER BY H.LAST_UPD_TS DESC) RNK  FROM   BPK_QRY AS A,  		PRI_TBL AS B,  		GTIN_GLN_SCHN AS D,         PRNT_CHLD_GTIN_GLN AS E,         GTIN_GLN_SCHN AS F,         PRNT_CHLD_GTIN_GLN AS G,         GTIN_GLN_SCHN AS H  WHERE  D.GTIN_NBR = A.GTIN_NBR         AND D.GLN_NBR = A.GLN_NBR         AND D.GTIN_NBR = E.CHLD_GTIN_NBR         AND D.GLN_NBR = E.GLN_NBR         AND F.GTIN_NBR = E.PRNT_GTIN_NBR         AND F.GLN_NBR = E.GLN_NBR         AND F.GTIN_NBR = G.CHLD_GTIN_NBR         AND F.GLN_NBR = G.GLN_NBR         AND H.GTIN_NBR = G.PRNT_GTIN_NBR         AND H.GLN_NBR = G.GLN_NBR          AND A.SKU_NBR = B.SKU_NBR         AND A.MVNDR_NBR = B.MVNDR_NBR         AND A.DC_NBR = B.DC_NBR         AND A.TS_RANK = B.TS_RANK  UNION  SELECT DISTINCT 		A.SKU_NBR, 		A.MVNDR_NBR, 		A.DC_NBR, 		F.GTIN_NBR,        F.GLN_NBR,        F.PKG_HLVL_TYP_CD,        F.RETL_ITEM_GRS_WT,        F.RETL_WT_UOM_CD,        F.RETL_ITEM_HGHT,        F.RETL_HGHT_UOM_CD,        F.RETL_ITEM_WID,        F.RETL_WID_UOM_CD,        F.RETL_ITEM_DPTH,        F.RETL_DPTH_UOM_CD,        F.PKG_UNT_QTY,        F.ORDRBL_UNT_FLG,        F.LAST_UPD_TS,        F.STACK_FLG,        F.NEST_ITEM_FLG,        F.NEST_INCRM_LEN,   		 ROW_NUMBER() OVER( PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR,F.PKG_HLVL_TYP_CD         ORDER BY F.LAST_UPD_TS DESC) RNK  FROM   BPK_QRY AS A,  		PRI_TBL AS B,  		GTIN_GLN_SCHN AS D,         PRNT_CHLD_GTIN_GLN AS E,         GTIN_GLN_SCHN AS F  WHERE  D.GTIN_NBR = A.GTIN_NBR         AND D.GLN_NBR = A.GLN_NBR         AND D.GTIN_NBR = E.CHLD_GTIN_NBR         AND D.GLN_NBR = E.GLN_NBR         AND F.GTIN_NBR = E.PRNT_GTIN_NBR         AND F.GLN_NBR = E.GLN_NBR          AND A.SKU_NBR = B.SKU_NBR         AND A.MVNDR_NBR = B.MVNDR_NBR         AND A.DC_NBR = B.DC_NBR         AND A.TS_RANK = B.TS_RANK  UNION  SELECT DISTINCT 		A.SKU_NBR, 		A.MVNDR_NBR, 		A.DC_NBR, 		D.GTIN_NBR,        D.GLN_NBR,        D.PKG_HLVL_TYP_CD,        D.RETL_ITEM_GRS_WT,        D.RETL_WT_UOM_CD,        D.RETL_ITEM_HGHT,        D.RETL_HGHT_UOM_CD,        D.RETL_ITEM_WID,        D.RETL_WID_UOM_CD,        D.RETL_ITEM_DPTH,        D.RETL_DPTH_UOM_CD,        D.PKG_UNT_QTY,        D.ORDRBL_UNT_FLG,        D.LAST_UPD_TS,        D.STACK_FLG,        D.NEST_ITEM_FLG,        D.NEST_INCRM_LEN,        ROW_NUMBER() OVER( PARTITION BY A.SKU_NBR, A.MVNDR_NBR, A.DC_NBR,D.PKG_HLVL_TYP_CD         ORDER BY D.LAST_UPD_TS DESC) RNK  FROM   BPK_QRY AS A,  		PRI_TBL AS B,  		GTIN_GLN_SCHN AS D  WHERE  D.GTIN_NBR = A.GTIN_NBR         AND D.GLN_NBR = A.GLN_NBR         AND A.SKU_NBR = B.SKU_NBR         AND A.MVNDR_NBR = B.MVNDR_NBR         AND A.DC_NBR = B.DC_NBR         AND A.TS_RANK = B.TS_RANK ) A  WHERE 1=1    ORDER  BY A.SKU_NBR, A.MVNDR_NBR,A.DC_NBR, A.PKG_HLVL_TYP_CD   WITH UR   

select * from LOAD_PLNG_AGG_DMND_H where hist_crt_ts >  {ts '2014-12-11 00:00:00'} and hist_crt_ts <  {ts '2014-12-12 00:00:00'}

insert into LOAD_PLNG_AGG_DMND 
SELECT LOAD_PLNG_AGG_DMND_ID,CRT_SYSUSR_ID,CRT_TS,LAST_UPD_SYSUSR_ID,LAST_UPD_TS,ORIG_TMS_FAC_ALIAS_ID,MBAS_SHP_TO_LOC_NBR,MBAS_SHP_TO_LOC_TYP_CD,MBAS_MVNDR_NBR,MBAS_DEPT_NBR,
PO_PROD_GRP_CD,MBAS_SKU_NBR,DSVC_TYP_CD,MER_BASE_CD,TOT_AGG_DMND_QTY,MIN_AGG_ORD_QTY,AGG_SER_QTY,ADJ_AGG_DMND_QTY,DC_BUY_UOM_QTY,DC_OO_QTY,PO_WK_DAY_NBR,PRCSS_FLG FROM LOAD_PLNG_AGG_DMND_H
where hist_crt_ts >  {ts '2014-12-11 00:00:00'} and hist_crt_ts <  {ts '2014-12-12 00:00:00'}

INSERT INTO LOAD_PLNG_LOC_SKU_DMND
SELECT LOAD_PLNG_LOC_SKU_DMND_ID,LAST_UPD_SYSUSR_ID,LAST_UPD_TS,MBAS_SHP_TO_LOC_NBR,MBAS_SHP_TO_LOC_TYP_CD,MBAS_MVNDR_NBR,MBAS_DEPT_NBR,MBAS_SKU_NBR,MER_BASE_CD,STR_SKU_DMND_TYP_CD,EFF_OH_QTY,OH_QTY,OO_QTY,ALLOC_NOT_SHPD_QTY,CORD_ALLOC_QTY,CORD_RSVD_QTY,CHG_OH_QTY,MIN_AGG_ORD_QTY,AGG_OUTL_THRH_PCT,BUY_UOM_QTY,TRGT_OH_QTY,TRGT_OH_PCT,ORIG_DMND_QTY,RND_DMND_QTY,ADDL_QTY_RQST_ID,ADDL_QTY_RSN_CD,ADDL_ORD_QTY,CALLOC_QTY,ORD_NBR,LOAD_PLNG_AGG_DMND_ID FROM LOAD_PLNG_LOC_SKU_DMND_H
where hist_crt_ts >  {ts '2014-12-11 00:00:00'} and hist_crt_ts <  {ts '2014-12-12 00:00:00'}

update LOAD_PLNG_AGG_DMND SET PRCSS_FLG = 'N' 

select * from plnd_ltl_load where ltl_load_id = 103 --_sku where mbas_sku_nbr = 101370