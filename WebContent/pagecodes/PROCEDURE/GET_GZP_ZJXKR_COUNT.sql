DROP PROCEDURE GET_GZP_ZJXKR_COUNT;

create procedure GET_GZP_ZJXKR_COUNT()
begin

select xksj,bz,bpdd,jz,SUM(count) count from 
(
(select DATE_FORMAT(S_GZZJ_GZXKRQMSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount)+sum(aqcsCount2) count
from (select DISTINCT T_DQYZGZP.*,S_USER_CODE from T_DQYZGZP LEFT OUTER JOIN T_BZRYB on T_DQYZGZP.S_GZZJ_GZXKRQM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_GZXKRQMSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from s_dy_f1 GROUP BY s_fid) f1 on f1.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_dy_f2 GROUP BY s_fid) f2 on f2.s_fid=gzp.S_ID
group by DATE_FORMAT(S_GZZJ_GZXKRQMSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_XKRQMSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount)+sum(aqcsCount2) count
from (select DISTINCT T_DQEZGZP.*,S_USER_CODE from T_DQEZGZP LEFT OUTER JOIN T_BZRYB on T_DQEZGZP.S_GZZJGZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQMSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from s_d2_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from s_d2_bcaqcs GROUP BY s_fid) bcaqcs on bcaqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_GZZJ_XKRQMSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount)+sum(aqcsCount2)+sum(aqcsCount3) count
from (select DISTINCT T_RLYZGZP.*,S_USER_CODE from T_RLYZGZP LEFT OUTER JOIN T_BZRYB on T_RLYZGZP.S_GZZJGZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_rly_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_rly_bcaqcs GROUP BY s_fid) bcaqcs on bcaqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount3 from t_rly_yjcaqcs GROUP BY s_fid) yjcaqcs on yjcaqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount) count
from (select DISTINCT T_YJDHGZP.*,S_USER_CODE from T_YJDHGZP LEFT OUTER JOIN T_BZRYB on T_YJDHGZP.S_ZJYXXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_dhy_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_ZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount) count
from (select DISTINCT T_EJDHGZP.*,S_USER_CODE from T_EJDHGZP LEFT OUTER JOIN T_BZRYB on T_EJDHGZP.S_ZJ_YXXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_ZJ_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_dhe_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_ZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDJ bpdd, if(S_BPDJ='01_JK', S_ZJ,'') jz, sum(aqcsCount)+sum(aqcsCount2) count
from (select DISTINCT T_RKGZP.*,S_USER_CODE from T_RKGZP LEFT OUTER JOIN T_BZRYB on T_RKGZP.S_GZZJGZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_rk_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_rk_aqcs2 GROUP BY s_fid) aqcs2 on aqcs2.s_fid=gzp.S_ID
group by DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDJ, if(S_BPDJ='01_JK', S_ZJ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_GZXKRSPSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount)+sum(aqcsCount2) count
from (select DISTINCT T_YJQXGZP.*,S_USER_CODE from T_YJQXGZP LEFT OUTER JOIN T_BZRYB on T_YJQXGZP.S_GZZJ_GZXKRQZB=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_GZXKRSPSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_yj_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_yj_bcaqcs GROUP BY s_fid) bcaqcs on bcaqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_GZZJ_GZXKRSPSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount) count
from (select DISTINCT T_DTGZP.*,S_USER_CODE from T_DTGZP LEFT OUTER JOIN T_BZRYB on T_DTGZP.S_GZZJ_DTXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_dt_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))
) gzp
group by xksj,bz,bpdd,jz;

end

