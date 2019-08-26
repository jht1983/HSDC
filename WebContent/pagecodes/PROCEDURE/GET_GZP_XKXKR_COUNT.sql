DROP PROCEDURE GET_GZP_XKXKR_COUNT;

create procedure GET_GZP_XKXKR_COUNT()
begin

(select 'T_DQYZGZP','电气一种工作票',DATE_FORMAT(S_XKKSGZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount)+sum(aqcsCount2) count
from (select DISTINCT T_DQYZGZP.*,S_USER_CODE from T_DQYZGZP LEFT OUTER JOIN T_BZRYB on T_DQYZGZP.S_GZXKRQM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_DQYZGZP.S_ZZ='001017' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XKKSGZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from s_dy_f1 GROUP BY s_fid) f1 on f1.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_dy_f2 GROUP BY s_fid) f2 on f2.s_fid=gzp.S_ID
group by DATE_FORMAT(S_XKKSGZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_DQEZGZP','电气二种工作票',DATE_FORMAT(S_XK_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount)+sum(aqcsCount2) count
from (select DISTINCT T_DQEZGZP.*,S_USER_CODE from T_DQEZGZP LEFT OUTER JOIN T_BZRYB on T_DQEZGZP.S_XKGZXKRQMBM=T_BZRYB.S_PEOPLE_CODE
 where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_DQEZGZP.S_ZZ='001017' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_XKRQZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from s_d2_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from s_d2_bcaqcs GROUP BY s_fid) bcaqcs on bcaqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_XK_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_RLYZGZP','热力机械工作票',DATE_FORMAT(S_XK_XKRSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount)+sum(aqcsCount2)+sum(aqcsCount3) count
from (select DISTINCT T_RLYZGZP.*,S_USER_CODE from T_RLYZGZP LEFT OUTER JOIN T_BZRYB on T_RLYZGZP.S_XKGZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_RLYZGZP.S_ZZ='001017' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_XKRSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_rly_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_rly_bcaqcs GROUP BY s_fid) bcaqcs on bcaqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount3 from t_rly_yjcaqcs GROUP BY s_fid) yjcaqcs on yjcaqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_XK_XKRSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_JBGZP','继保工作票',DATE_FORMAT(S_XK_XKKSGZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount) count
from (select DISTINCT T_JBGZP.*,S_USER_CODE from T_JBGZP LEFT OUTER JOIN T_BZRYB on T_JBGZP.S_XK_GZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_JBGZP.S_ZZ='001017' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_XKKSGZSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_jb_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_XK_XKKSGZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_YJDHGZP','一级动火工作票',DATE_FORMAT(S_XK_YXXKDHSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount) count
from (select DISTINCT T_YJDHGZP.*,S_USER_CODE from T_YJDHGZP LEFT OUTER JOIN T_BZRYB on T_YJDHGZP.S_XKYXBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_YJDHGZP.S_WXZZ='001017' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_YXXKDHSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_dhy_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_XK_YXXKDHSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_EJDHGZP','二级动火工作票',DATE_FORMAT(S_XK_YXXKDHSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount) count
from (select DISTINCT T_EJDHGZP.*,S_USER_CODE from T_EJDHGZP LEFT OUTER JOIN T_BZRYB on T_EJDHGZP.S_XK_YXXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_EJDHGZP.S_ZZ='001017' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_YXXKDHSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_dhe_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_XK_YXXKDHSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select 'T_RKGZP','热控工作票',DATE_FORMAT(S_XK_XKRSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDJ bpdd, if(S_BPDJ='01_JK', S_ZJ,'') jz, sum(aqcsCount)+sum(aqcsCount2) count
from (select DISTINCT T_RKGZP.*,S_USER_CODE from T_RKGZP LEFT OUTER JOIN T_BZRYB on T_RKGZP.S_GZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_RKGZP.S_ZZ='001017' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_XKRSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_rk_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_rk_aqcs2 GROUP BY s_fid) aqcs2 on aqcs2.s_fid=gzp.S_ID
group by DATE_FORMAT(S_XK_XKRSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDJ, if(S_BPDJ='01_JK', S_ZJ,''))

UNION All

(select 'T_YJQXGZP','应急抢修工作票',DATE_FORMAT(S_XK_GZXKRSPSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, sum(aqcsCount)+sum(aqcsCount2) count
from (select DISTINCT T_YJQXGZP.*,S_USER_CODE from T_YJQXGZP LEFT OUTER JOIN T_BZRYB on T_YJQXGZP.S_XK_GZXKRQZBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_GZPZT<>'GZPZT051' and T_YJQXGZP.S_ZZ='001017' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_GZXKRSPSJ<>'') gzp
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount from t_yj_aqcs GROUP BY s_fid) aqcs on aqcs.s_fid=gzp.S_ID
LEFT OUTER JOIN (select s_fid,count(*) aqcsCount2 from t_yj_bcaqcs GROUP BY s_fid) bcaqcs on bcaqcs.s_fid=gzp.S_ID
group by DATE_FORMAT(S_XK_GZXKRSPSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''));

end

