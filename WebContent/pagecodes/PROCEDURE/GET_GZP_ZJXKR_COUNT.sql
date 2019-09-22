DROP PROCEDURE GET_GZP_ZJXKR_COUNT;

create procedure GET_GZP_ZJXKR_COUNT()
begin

(select 'T_DQYZGZP','电气一种工作票',DATE_FORMAT(DATE_SUB(S_GZZJ_GZXKRQMSJ,INTERVAL 2 HOUR),'%Y-%m-%d') xksj,S_GZZJ_BZBM bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(IFNULL(aqcsCount,0))+sum(IFNULL(aqcsCount2,0)) count
from (select * from T_DQYZGZP where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_DQYZGZP.S_ZZ='001017' and S_GZZJ_BZBM in ('01', '02', 'hd03', '21') and S_GZZJ_GZXKRQMSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from s_dy_f1 where S_A<>'' GROUP BY s_fid) f1 on f1.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_dy_f2 where S_A<>'' or S_B<>'' GROUP BY s_fid) f2 on f2.s_fid=gzp.S_ID
group by DATE_FORMAT(DATE_SUB(S_GZZJ_GZXKRQMSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_GZZJ_BZBM, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_DQEZGZP','电气二种工作票',DATE_FORMAT(DATE_SUB(S_GZZJ_XKRQMSJ,INTERVAL 2 HOUR),'%Y-%m-%d') xksj,S_GZZJ_BZBM bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(IFNULL(aqcsCount,0))+sum(IFNULL(aqcsCount2,0)) count
from (select * from T_DQEZGZP where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_DQEZGZP.S_ZZ='001017' and S_GZZJ_BZBM in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQMSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from s_d2_aqcs where S_BXCQDAQCS<>'' GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from s_d2_bcaqcs where S_BCDAC<>'' GROUP BY s_fid) bcaqcs on bcaqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(DATE_SUB(S_GZZJ_XKRQMSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_GZZJ_BZBM, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_RLYZGZP','热力机械工作票',DATE_FORMAT(DATE_SUB(S_GZZJ_XKRQZSJ,INTERVAL 2 HOUR),'%Y-%m-%d') xksj,S_GZZJ_BZBM bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(IFNULL(aqcsCount,0))+sum(IFNULL(aqcsCount2,0))+sum(IFNULL(aqcsCount3,0)) count
from (select * from T_RLYZGZP where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_RLYZGZP.S_ZZ='001017' and S_GZZJ_BZBM in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_rly_aqcs where S_BXCQDAC<>'' GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_rly_bcaqcs where S_BCDAC<>'' GROUP BY s_fid) bcaqcs on bcaqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount3 from t_rly_yjcaqcs where S_YJCDBH<>'' GROUP BY s_fid) yjcaqcs on yjcaqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(DATE_SUB(S_GZZJ_XKRQZSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_GZZJ_BZBM, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_YJDHGZP','一级动火工作票',DATE_FORMAT(DATE_SUB(S_GZZJ_XKRQZSJ,INTERVAL 2 HOUR),'%Y-%m-%d') xksj,S_GZZJ_BZBM bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(IFNULL(aqcsCount,0)) count
from (select * from T_YJDHGZP where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_YJDHGZP.S_WXZZ='001017' and S_GZZJ_BZBM in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_dhy_aqcs where S_DHBMYCQDAC<>'' or S_YXYCQDAC<>'' or S_XFYCQDAC<>'' GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(DATE_SUB(S_GZZJ_XKRQZSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_GZZJ_BZBM, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_EJDHGZP','二级动火工作票',DATE_FORMAT(DATE_SUB(S_ZJ_XKRQZSJ,INTERVAL 2 HOUR),'%Y-%m-%d') xksj,S_GZZJ_BZBM bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(IFNULL(aqcsCount,0)) count
from (select * from T_EJDHGZP where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_EJDHGZP.S_ZZ='001017' and S_GZZJ_BZBM in ('01', '02', 'hd03', '21') and S_ZJ_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_dhe_aqcs where S_DHBMYCQDAC<>'' or S_YXYCQDAC<>'' or S_XFYCQDQC<>'' GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(DATE_SUB(S_ZJ_XKRQZSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_GZZJ_BZBM, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_RKGZP','热控工作票',DATE_FORMAT(DATE_SUB(S_GZZJ_XKRQZSJ,INTERVAL 2 HOUR),'%Y-%m-%d') xksj,S_GZZJ_BZBM bz, S_BPDJ bpdd, if(S_BPDJ='01_JK', S_ZJ,'') jz, sum(IFNULL(aqcsCount,0))+sum(IFNULL(aqcsCount2,0)) count
from (select * from T_RKGZP where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_RKGZP.S_ZZ='001017' and S_GZZJ_BZBM in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_rk_aqcs where S_YXRYZXD<>'' GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_rk_aqcs2 where S_BCDAQCS<>'' GROUP BY s_fid) aqcs2 on aqcs2.s_fid=gzp.S_ID
group by DATE_FORMAT(DATE_SUB(S_GZZJ_XKRQZSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_GZZJ_BZBM, S_BPDJ, if(S_BPDJ='01_JK', S_ZJ,''))

UNION All

(select 'T_YJQXGZP','应急抢修工作票',DATE_FORMAT(DATE_SUB(S_GZZJ_GZXKRSPSJ,INTERVAL 2 HOUR),'%Y-%m-%d') xksj,S_GZZJ_BZBM bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(IFNULL(aqcsCount,0))+sum(IFNULL(aqcsCount2,0)) count
from (select * from T_YJQXGZP where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_YJQXGZP.S_ZZ='001017' and S_GZZJ_BZBM in ('01', '02', 'hd03', '21') and S_GZZJ_GZXKRSPSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_yj_aqcs where S_BXCQDAC<>'' GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_yj_bcaqcs where S_ZBRYBCDAC<>'' GROUP BY s_fid) bcaqcs on bcaqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(DATE_SUB(S_GZZJ_GZXKRSPSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_GZZJ_BZBM, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_DTGZP','动土工作票',DATE_FORMAT(DATE_SUB(S_XKRQZSJ,INTERVAL 2 HOUR),'%Y-%m-%d') xksj,S_GZZJ_BZBM bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(IFNULL(aqcsCount,0)) count
from (select * from T_DTGZP where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_DTGZP.S_ZZ='001017' and S_GZZJ_BZBM in ('01', '02', 'hd03', '21') and S_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_dt_aqcs where S_AQCS<>'' or S_YJJY<>'' GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(DATE_SUB(S_XKRQZSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_GZZJ_BZBM, S_BPDD, if(S_BPDD='01_JK', S_JZ,''));

end

