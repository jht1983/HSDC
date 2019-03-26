DROP PROCEDURE GET_CZP_COUNT;

create procedure GET_CZP_COUNT()
begin

select DATE_FORMAT(S_KPSJ,'%Y-%m-%d') kpsj,S_CZBZ czbz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_CZBZ) score
from t_czpsc,T_CZPF
where S_CZBZ in ('01', '02', 'hd03', '21') and T_CZPF.S_FID=T_CZPSC.S_ID
group by DATE_FORMAT(S_KPSJ,'%Y-%m-%d'),S_CZBZ, S_BPDD, if(S_BPDD='01_JK', S_JZ,'');

end

