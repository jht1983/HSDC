DROP PROCEDURE GET_QX_FXR_COUNT;

create procedure GET_QX_FXR_COUNT()
begin

select DATE_FORMAT(S_FXSJ,'%Y-%m-%d') fxsj,S_FXRSSBZ bz, S_GZPPZ gzppz, if(S_GZPPZ='01_JK', S_JZ,'') jz, count(S_FXRSSBZ) count
from T_QXJL
where S_FXRSSBZ in ('01', '02', 'hd03', '21')
group by DATE_FORMAT(S_FXSJ,'%Y-%m-%d'),S_FXRSSBZ, S_GZPPZ, if(S_GZPPZ='01_JK', S_JZ,'');

end

