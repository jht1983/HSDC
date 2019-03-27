DROP PROCEDURE GET_GZP_ZJXKR_COUNT;

create procedure GET_GZP_ZJXKR_COUNT()
begin

select xksj,bz,bpdd,jz,SUM(count) count from 
(
(select DATE_FORMAT(S_GZZJ_GZFZRQMSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_DQYZGZP LEFT OUTER JOIN T_BZRYB on T_DQYZGZP.S_GZZJ_GZFZRQM=T_BZRYB.S_PEOPLE_CODE and T_DQYZGZP.S_JZ=T_BZRYB.S_JZ_ATTR
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_GZFZRQMSJ<>''
group by DATE_FORMAT(S_GZZJ_GZFZRQMSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_FZRQMSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_DQEZGZP LEFT OUTER JOIN T_BZRYB on T_DQEZGZP.S_GZZJGZFZRBM=T_BZRYB.S_PEOPLE_CODE and T_DQEZGZP.S_JZ=T_BZRYB.S_JZ_ATTR
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_FZRQMSJ<>''
group by DATE_FORMAT(S_GZZJ_FZRQMSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_GZFZRSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_RLYZGZP LEFT OUTER JOIN T_BZRYB on T_RLYZGZP.S_GZZJGZFZRBM=T_BZRYB.S_PEOPLE_CODE and T_RLYZGZP.S_JZ=T_BZRYB.S_JZ_ATTR
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_GZFZRSJ<>''
group by DATE_FORMAT(S_GZZJ_GZFZRSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_GZZJSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_JBGZP LEFT OUTER JOIN T_BZRYB on T_JBGZP.S_GZJZ_GZFZRBM=T_BZRYB.S_PEOPLE_CODE and T_JBGZP.S_JZ=T_BZRYB.S_JZ_ATTR
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_GZZJSJ<>''
group by DATE_FORMAT(S_GZZJ_GZZJSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_YJDHGZP LEFT OUTER JOIN T_BZRYB on T_YJDHGZP.S_ZJYXXKRBM=T_BZRYB.S_PEOPLE_CODE and T_YJDHGZP.S_JZ=T_BZRYB.S_JZ_ATTR
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>''
group by DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_ZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_EJDHGZP LEFT OUTER JOIN T_BZRYB on T_EJDHGZP.S_ZJ_YXXKRBM=T_BZRYB.S_PEOPLE_CODE and T_EJDHGZP.S_JZ=T_BZRYB.S_JZ_ATTR
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_ZJ_XKRQZSJ<>''
group by DATE_FORMAT(S_ZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDJ bpdd, if(S_BPDJ='01_JK', S_ZJ,'') jz, count(S_USER_CODE) count
from T_RKGZP LEFT OUTER JOIN T_BZRYB on T_RKGZP.S_GZZJGZXKRBM=T_BZRYB.S_PEOPLE_CODE and T_RKGZP.S_ZJ=T_BZRYB.S_JZ_ATTR
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_XKRQZSJ<>''
group by DATE_FORMAT(S_GZZJ_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDJ, if(S_BPDJ='01_JK', S_ZJ,''))

UNION All

(select DATE_FORMAT(S_GZZJ_GZXKRSPSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_YJQXGZP LEFT OUTER JOIN T_BZRYB on T_YJQXGZP.S_GZZJ_GZXKRQZB=T_BZRYB.S_PEOPLE_CODE and T_YJQXGZP.S_JZ=T_BZRYB.S_JZ_ATTR
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_GZZJ_GZXKRSPSJ<>''
group by DATE_FORMAT(S_GZZJ_GZXKRSPSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_DTGZP LEFT OUTER JOIN T_BZRYB on T_DTGZP.S_GZZJ_DTXKRBM=T_BZRYB.S_PEOPLE_CODE and T_DTGZP.S_JZ=T_BZRYB.S_JZ_ATTR
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XKRQZSJ<>''
group by DATE_FORMAT(S_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))
) gzp
group by xksj,bz,bpdd,jz;

end

