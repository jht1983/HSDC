DROP PROCEDURE GET_CZP_COUNT;

create procedure GET_CZP_COUNT()
begin

select DATE_FORMAT(DATE_SUB(S_KPSJ,INTERVAL 2 HOUR),'%Y-%m-%d') kpsj,S_CZBZ czbz, S_BPDD bpdd, if(S_BPDD='01_JK', S_JZ,'') jz, count(S_CZBZ) count
from t_czpsc,T_CZPF
where S_CZBZ in ('01', '02', 'hd03', '21') and S_DZZT<>'CZPZT040' and S_ZZ='001017' and T_CZPF.S_FID=T_CZPSC.S_ID
group by DATE_FORMAT(DATE_SUB(S_KPSJ,INTERVAL 2 HOUR),'%Y-%m-%d'),S_CZBZ, S_BPDD, if(S_BPDD='01_JK', S_JZ,'');

end

