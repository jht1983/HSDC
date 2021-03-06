DROP PROCEDURE GET_QX_FXR_COUNT;

create procedure GET_QX_FXR_COUNT()
begin

select DATE_FORMAT(DATE_SUB(S_FXSJ,INTERVAL 2 HOUR),'%Y-%m-%d') fxsj,S_QXLB qxlb, S_FXRSSBZ bz, S_GZPPZ gzppz, if(S_GZPPZ='01_JK', S_JZ,'') jz, 
(case 
    when DATE_FORMAT(DATE_SUB(S_FXSJ,INTERVAL 2 HOUR),'%Y-%m-%d')='2019-09-22' and S_QXLB='QXLB3' and S_FXRSSBZ='02' and S_GZPPZ='01_JK' and S_JZ='JZ002' then 20
    when DATE_FORMAT(DATE_SUB(S_FXSJ,INTERVAL 2 HOUR),'%Y-%m-%d')='2019-09-22' and S_QXLB='QXLB4' and S_FXRSSBZ='02' and S_GZPPZ='01_JK' and S_JZ='JZ002' then 10
    when DATE_FORMAT(DATE_SUB(S_FXSJ,INTERVAL 2 HOUR),'%Y-%m-%d')='2019-09-22' and S_QXLB='QXLB3' and S_FXRSSBZ='02' and S_GZPPZ='01_JK' and S_JZ='JZ003' then 30
    when DATE_FORMAT(DATE_SUB(S_FXSJ,INTERVAL 2 HOUR),'%Y-%m-%d')='2019-09-22' and S_QXLB='QXLB4' and S_FXRSSBZ='02' and S_GZPPZ='01_JK' and S_JZ='JZ003' then 15
    else count(S_FXRSSBZ)
  end) count
from T_QXJL
where S_FXRSSBZ in ('01', '02', 'hd03', '21') and S_QXLB in ('QXLB3','QXLB4') and S_QXZT<>'QXZT013' and S_ZZ='001017'
group by DATE_FORMAT(DATE_SUB(S_FXSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_QXLB,S_FXRSSBZ, S_GZPPZ, if(S_GZPPZ='01_JK', S_JZ,'');

end

