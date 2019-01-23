var scrollHeight=document.documentElement.scrollHeight+'px';
var scrollWidth=document.documentElement.scrollWidth+'px';
var body=document.body;

/**包含pre标签的div**/
var div = document.createElement('div');
div.style="display: none;";

var pre=document.createElement("pre");
pre.style="background-color:silver";
pre.name="NO_pre";
pre.id="out";

div.appendChild(pre);
body.appendChild(div);

/*阴影div*/
var div1=document.createElement("div");
div1.name="hbg";
div1.style="display: none;position: absolute;background: #000;z-index: 1004;left: 0; top: 0;opacity: 0.6 ;";
div1.id="hbg";
div1.style.height=scrollHeight;
div1.style.width=scrollWidth;

body.appendChild(div1);


var div2=document.createElement("div");
div2.id="tishikuang";
div2.style="display: none;position:fixed;margin:auto;left:0;right:0; top:0;bottom:0;width: 500px;height: 150px;border-radius: 4px;background-color: #fff;z-index: 1008;";
div2.style.display="none";

var div3=document.createElement("div");
div3.style="cursor: move;text-align: left;border-radius: 2px 2px 0 0;color: #333;font-size: 14px;height: 42px;line-height: 42px;overflow: hidden;background-color: #f8f8f8;border-bottom: 1px solid #eee;  padding: 0 80px 0 20px;";
div3.innerHTML="正在导入Excel 请您稍后";


var div4=document.createElement("div");
div4.style="text-align: left; position:word-break: break-all; relative; border-top: 1px solid red;font-size: 14px;line-height: 24px;overflow-x: hidden;overflow-y: auto;padding: 20px;";


var div5=document.createElement("div");
div5.style="width:450px;border:1px solid #6C9C2C;height:25px;";


var div6=document.createElement("div");
div6.stlye="float:left;height:100%;text-align:center;line-height:150%;";
div6.style.background="#95CA0D";
div6.style.height="100%";
div6.style.width="0";
div6.id="bar";

div5.appendChild(div6);


var span=document.createElement("span");
span.id="total";

div4.appendChild(div5);
div4.appendChild(span);


div2.appendChild(div3);
div2.appendChild(div4);

body.appendChild(div2);

/*-------------*/
var div9=document.createElement("div");
div9.id="hintBox";
div9.style="position:fixed;margin:auto;left:0;right:0; top:0;bottom:0;width:350px;height: 85px;border-radius: 4px;background-color: #fff;";
div9.style.zIndex="1008";
div9.style.display="none";


var div10=document.createElement("div");
div10.id="detectionFile";
div10.style="padding: 0  20px;overflow: hidden;line-height: 42px;height: 42px;font-size: 14px;color: #333;cursor: move;text-align: left;background-color: #f8f8f8;border-bottom: 1px solid #eee;border-radius: 2px 2px 0 0;";


var div11=document.createElement("div");
div11.style="border-top: 1px solid #2885d5;word-break: break-all;position: relative;overflow-y: auto;overflow-x: hidden;line-height: 24px;text-align: right; border-top: 1px solid #2885d5;font-size: 14px;";


var input0=document.createElement("input");
input0.type="button";
input0.value="确认";
input0.style="color:#FFF;background: #2885d5 none repeat scroll 0 0;padding: 0 15px;margin: 6px 6px 0;cursor: pointer;font-weight: 400;height: 28px;line-height: 28px;";

input0.setAttribute("onclick","clickAbolish()");

div11.appendChild(input0);


div9.appendChild(div10);
div9.appendChild(div11);

body.appendChild(div9);




/*

------------
*/





var kk="";
var X = XLS;
var my_ExcleId=2;/**Excel的读取ID**/

var ExcleValue="";/**存储全部Excel的变量**/
var setIntervalId; /**定时器**/
function fixdata(data) {

    var o = "", l = 0, w = 10240;
    for(; l<data.byteLength/w; ++l) o+=String.fromCharCode.apply(null,new Uint8Array(data.slice(l*w,l*w+w)));
    o+=String.fromCharCode.apply(null, new Uint8Array(data.slice(l*w)));

    return o;
}

function to_formulae(workbook) {

    var result = [];
    workbook.SheetNames.forEach(function(sheetName) {
        var formulae = X.utils.get_formulae(workbook.Sheets[sheetName]);
        if(formulae.length > 0){
            result.push("SHEET: " + sheetName);
            result.push("");
            result.push(formulae.join("\n"));
        }
    });
    /*alert(result);*/
    return result.join("\n");
}


function process_wb(wb) {/*这是里选中什么格式*/

    /*if(use_worker) XLS.SSF.load_table(wb.SSF);*/
    var output = "";
    /*switch(get_radio_value("format")) {
        case "json":
        output = JSON.stringify(to_json(wb), 2, 2);
            break;
        case "form":
            output = to_formulae(wb);
            break;
        default:
        output = to_csv(wb);
    }*/
    output = to_formulae(wb);/*这三个我选中的版本*/
    if(out.innerText === undefined) out.textContent = output;
    else out.innerText = output;

    if(typeof console !== 'undefined') console.log("output", new Date());
    /*	if(output!=undefined && output !='undefined' && output != '')
      {
                console.log("output not null, show upload button");
          $("#btn_upload").css("display","block");
        }*/

    ExcleValue=document.getElementById('out').innerHTML;

 /*   window.parent.document.getElementById("hbg").style.display='';*/
    document.getElementById('hbg').style.display='';
    document.getElementById('tishikuang').style.display='';
    kk=planLength();

    setIntervalId=setInterval("enteringExcle()",1); /*每隔2000毫秒执行一次enteringExcle()函数，执行无数次。*/


}

var xlf = document.getElementById('xlf');
function handleFile(e) {

    /*alert(document.getElementsByName("useworker")[0].checked);*/
    /*	alert("handleFile1");*/
    /*rABS = document.getElementsByName("userabs")[0].checked;
    use_worker = document.getElementsByName("useworker")[0].checked;*/
    rABS = false;
    use_worker =false;
    var files = e.target.files;
    var f = files[0];
    {
        var reader = new FileReader();
        var name = f.name;
        reader.onload = function(e) {
            if(typeof console !== 'undefined') console.log("onload", new Date(), rABS, use_worker);
            var data = e.target.result;
            if(use_worker) {
                /*	alert(1);
                    xw(data, process_wb);*/
            } else {
                /*	alert(2);*/
                var wb;
                if(/*rABS*/false) {
                    /*alert(1);
                    wb = X.read(data, {type: 'binary'});*/
                } else {
                    /*	alert(2);*/
                    var arr = fixdata(data);
                    wb = X.read(btoa(arr), {type: 'base64'});
                }
                process_wb(wb);

            }
        };
        if(rABS) reader.readAsBinaryString(f);
        else reader.readAsArrayBuffer(f);
    }
}



if(xlf.addEventListener) xlf.addEventListener('change', handleFile, false);

/* 这是单选Excel插入数据库*/
function enteringExcle() {

    i=my_ExcleId;
    my_ExcleId=my_ExcleId+1;
    while(i<my_ExcleId) {
        planLengthNumber=parseInt(100*(i/kk.toFixed()));
        run(planLengthNumber);
        if(kk==i){
            window.clearInterval(setIntervalId);
            setTimeout("closeAllDiv('导入完成!')","100");
            /*   setTimeout("closeAllDiv('完成导入"+i+"道考试题!')","100");*/
        }else{
                insertDB(i,"pro");
        }
        i++;

    }

}


/* 这是查询进度条长度*/
function planLength() {
    z=3;
    while(true) {
        var cc = incision(ExcleValue, "B"+z+"='", "<br>");
        if(cc==undefined){
            if(incision(ExcleValue, "B"+z+"=", "<br>")==undefined){

                return z;
            }
        }
        z++;
    }

}

/*这是调用提示框，并且给提示框赋值*/
function closeAllDiv(hintValue){
    document.getElementById("tishikuang").style.display="none";
  /*  window.parent.document.getElementById("hbg").style.display='';*/
    document.getElementById('hbg').style.display='';
    document.getElementById('hintBox').style.display='';
    document.getElementById('detectionFile').innerText=hintValue;
}

/*关闭提示框*/
function clickAbolish(){

   /* window.parent.document.getElementById("hbg").style.display='none';*/
    document.getElementById("hbg").style.display='none';

    document.getElementById('hbg').style.display='none';
    document.getElementById('hintBox').style.display='none';
    window.location.href=window.location.href;
}

/*这是进度条方法*/
function run(number){
    var bar = document.getElementById("bar");
    var total = document.getElementById("total");
    bar.style.width=number + "%";

    total.innerHTML = bar.style.width;
    /* if(bar.style.width == "100%"){
       window.clearTimeout(timeout);
       return;
     } */

}
/**切割字符串方法  incision(切割) Result结果，从哪里切，切割到哪**/
function incision(Result, start, over) {
    Result = Result.substr(Result.indexOf(start));
    if(over != "" && over != null & over != undefined) {
        Result = Result.substr(0, Result.indexOf(over));
        /**alert("后"+strResult);**/
        Result = Result.split(start)[1];
    } else {
        Result = Result.substr(Result.indexOf(start) + start.length);
        /*alert("后"+Result);*/

    }
    return Result;
}



















