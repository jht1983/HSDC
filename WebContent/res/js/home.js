function do_Bttn(_umb){
    switch(_umb){
        case 4:
            miniWin('\u4fee\u6539\u5bc6\u7801','','Menu?O_SYS_TYPE=changelogin',400,300,'','');
            
            break;
        case 3:
            window.location='logindx.v';
            break;
        case 2:
            document.getElementById("mainhomeframe").src="View?SPAGECODE=1507779605239&S_SMODCODE=002";
           
            break;
        case 1:
           document.getElementById("mainhomeframe").src="View?SPAGECODE=1507779605239&S_SMODCODE=001";
            break;
        case 5:  //修改头像
			
            winBox("修改头像","umsg.v",350,230);
			
             break;
    }
    
}





function initMenuitemC(){
 
    if(document.getElementById('Matn_move')==null){
        var menuitem = document.getElementsByClassName('menuitem');
        var divTm = document.createElement('div');
        
        divTm.innerHTML="\u9996\u9875";
        divTm.setAttribute("onclick","doViewMenuUrl('home.v')");
        divTm.setAttribute("class","menuitem");
        divTm.id="Matn_move"
        
        if(menuitem.length>0){
             menuitem[0].parentNode.insertBefore(divTm,menuitem[0]);
        }else{
            menuitem.appendChild(divTm);
        }
        
       
    }
}


			
