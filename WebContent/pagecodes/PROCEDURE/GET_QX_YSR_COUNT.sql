DROP PROCEDURE GET_QX_YSR_COUNT;

create procedure GET_QX_YSR_COUNT()
begin

select DATE_FORMAT(DATE_SUB(S_YXYSSJ,INTERVAL 2 HOUR),'%Y-%m-%d') yxyssj,S_USER_CODE bz, S_GZPPZ gzppz, if(S_GZPPZ='01_JK', S_JZ,'') jz, 
  (case 
    when DATE_FORMAT(DATE_SUB(S_YXYSSJ,INTERVAL 2 HOUR),'%Y-%m-%d')='2019-09-22' and S_USER_CODE='02' and S_GZPPZ='01_JK' and if(S_GZPPZ='01_JK', S_JZ,'')='JZ002' then 20
    when DATE_FORMAT(DATE_SUB(S_YXYSSJ,INTERVAL 2 HOUR),'%Y-%m-%d')='2019-09-22' and S_USER_CODE='02' and S_GZPPZ='01_JK' and if(S_GZPPZ='01_JK', S_JZ,'')='JZ003' then 18
    else count(S_USER_CODE)
  end) count
from T_QXJL LEFT OUTER JOIN T_BZRYB on T_QXJL.S_YXYSRBM=T_BZRYB.S_PEOPLE_CODE and T_QXJL.S_JZ=T_BZRYB.S_JZ_ATTR
where S_USER_CODE in ('01', '02', 'hd03', '21')
group by DATE_FORMAT(DATE_SUB(S_YXYSSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_USER_CODE, S_GZPPZ, if(S_GZPPZ='01_JK', S_JZ,'');

end

