var last_id=0;
function show_submenu(id)
{
  if(id==0){

    for(i=1;i<=max_id;i++){
      eval("menu"+i+".style.display='none';");
      eval("td_menu"+i+".height=1;");
    }
    eval("menu"+id+".style.display='block';");
    eval("td_menu"+id+".height='100%';");
    last_id=0;
    return;
  }
  if(last_id!=id){
    for(i=0;i<=id;i++){
      eval("menu"+i+".style.display='none';");
      eval("td_menu"+i+".height=1;");
    }
    eval("menu"+last_id+".style.display='none';");
    eval("td_menu"+last_id+".height=1;");
    eval("menu"+id+".style.display='block';");
    eval("td_menu"+id+".height='100%';");
    last_id=id;
  }
}

function loginAct(cool){
  document.loginform.action.value=cool;
  document.loginform.submit();
}