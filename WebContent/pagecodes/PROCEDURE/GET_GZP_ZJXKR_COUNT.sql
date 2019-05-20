DROP PROCEDURE GET_GZP_ZJXKR_COUNT;

create procedure GET_GZP_ZJXKR_COUNT()
begin

select xksj,bz,bpdd,jz,SUM(count) count from 
(
(select DATE_FORMAT(S_GZZJ_GZXKRQMSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_DQYZGZP LEFT OUTER JOIN T_BZRYB on T_DQYZGZP.S_GZZJ_GZXKRQM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_GZXKRQMSJ<>''
group by DATE_FORMAT(S_GZZJ_GZXKRQMSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_XKRQMSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_DQEZGZP LEFT OUTER JOIN T_BZRYB on T_DQEZGZP.S_GZZJGZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQMSJ<>''
group by DATE_FORMAT(S_GZZJ_XKRQMSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_RLYZGZP LEFT OUTER JOIN T_BZRYB on T_RLYZGZP.S_GZZJGZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>''
group by DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_YJDHGZP LEFT OUTER JOIN T_BZRYB on T_YJDHGZP.S_ZJYXXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>''
group by DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_ZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_EJDHGZP LEFT OUTER JOIN T_BZRYB on T_EJDHGZP.S_ZJ_YXXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_ZJ_XKRQZSJ<>''
group by DATE_FORMAT(S_ZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDJ bpdd, if(S_BPDJ='01_JK', S_ZJ,'') jz, count(S_USER_CODE) count
from T_RKGZP LEFT OUTER JOIN T_BZRYB on T_RKGZP.S_GZZJGZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>''
group by DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDJ, if(S_BPDJ='01_JK', S_ZJ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_GZXKRSPSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_YJQXGZP LEFT OUTER JOIN T_BZRYB on T_YJQXGZP.S_GZZJ_GZXKRQZB=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_GZXKRSPSJ<>''
group by DATE_FORMAT(S_GZZJ_GZXKRSPSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_DTGZP LEFT OUTER JOIN T_BZRYB on T_DTGZP.S_GZZJ_DTXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XKRQZSJ<>''
group by DATE_FORMAT(S_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))
) gzp
group by xksj,bz,bpdd,jz;

end

