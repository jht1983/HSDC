DROP PROCEDURE GET_GZP_XKXKR_COUNT;

create procedure GET_GZP_XKXKR_COUNT()
begin

select xksj,bz,bpdd,jz,SUM(count) count from 
(
(select DATE_FORMAT(S_XKKSGZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_DQYZGZP LEFT OUTER JOIN T_BZRYB on T_DQYZGZP.S_GZXKRQM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XKKSGZSJ<>''
group by DATE_FORMAT(S_XKKSGZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_XK_XKRQZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_DQEZGZP LEFT OUTER JOIN T_BZRYB on T_DQEZGZP.S_XKGZXKRQMBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_XKRQZSJ<>''
group by DATE_FORMAT(S_XK_XKRQZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_XK_XKRSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_RLYZGZP LEFT OUTER JOIN T_BZRYB on T_RLYZGZP.S_XKGZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_XKRSJ<>''
group by DATE_FORMAT(S_XK_XKRSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_XK_XKKSGZSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_JBGZP LEFT OUTER JOIN T_BZRYB on T_JBGZP.S_XK_GZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_XKKSGZSJ<>''
group by DATE_FORMAT(S_XK_XKKSGZSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_XK_YXXKDHSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_YJDHGZP LEFT OUTER JOIN T_BZRYB on T_YJDHGZP.S_XKYXBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_YXXKDHSJ<>''
group by DATE_FORMAT(S_XK_YXXKDHSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_XK_YXXKDHSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_EJDHGZP LEFT OUTER JOIN T_BZRYB on T_EJDHGZP.S_XK_YXXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_YXXKDHSJ<>''
group by DATE_FORMAT(S_XK_YXXKDHSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

UNION All

(select DATE_FORMAT(S_XK_XKRSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDJ bpdd, if(S_BPDJ='01_JK', S_ZJ,'') jz, count(S_USER_CODE) count
from T_RKGZP LEFT OUTER JOIN T_BZRYB on T_RKGZP.S_GZXKRBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_XKRSJ<>''
group by DATE_FORMAT(S_XK_XKRSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDJ, if(S_BPDJ='01_JK', S_ZJ,''))

UNION All

(select DATE_FORMAT(S_XK_GZXKRSPSJ,'%Y-%m-%d') xksj,S_USER_CODE bz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_USER_CODE) count
from T_YJQXGZP LEFT OUTER JOIN T_BZRYB on T_YJQXGZP.S_XK_GZXKRQZBM=T_BZRYB.S_PEOPLE_CODE
where S_DEL<>'1' and S_USER_CODE in ('01', '02', 'hd03', '21') and S_XK_GZXKRSPSJ<>''
group by DATE_FORMAT(S_XK_GZXKRSPSJ,'%Y-%m-%d'),S_USER_CODE, S_BPDD, if(S_BPDD='01_JK', S_JZ,''))

) gzp
group by xksj,bz,bpdd,jz;

end

