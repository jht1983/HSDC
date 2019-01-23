/****************************************************************************
 * Images for mouseover efects.                                             *
 ****************************************************************************/

var panOnImage = new Image();
var panOffImage = new Image();
var rotateOnImage = new Image();
var rotateOffImage = new Image();
var flyOnImage = new Image();
var flyOffImage = new Image();
var centerOnImage = new Image();
var centerOffImage = new Image();
var homeOnImage = new Image();
var homeOffImage = new Image();
var cancelOnImage = new Image();
var cancelOffImage = new Image();
var captureOnImage = new Image();
var captureOffImage = new Image();


panOnImage.src = "images/pan_button_on.gif";
panOffImage.src = "images/pan_button.gif";
rotateOnImage.src = "images/rotate_button_on.gif";
rotateOffImage.src = "images/rotate_button.gif";
flyOnImage.src = "images/fly_button_on.gif";
flyOffImage.src = "images/fly_button.gif";
centerOnImage.src = "images/center_button_on.gif";
centerOffImage.src = "images/center_button.gif";
homeOnImage.src = "images/home_button_on.gif";
homeOffImage.src = "images/home_button.gif";
cancelOnImage.src = "images/cancel_button_on.gif";
cancelOffImage.src = "images/cancel_button.gif";
captureOnImage.src = "images/capture_button_on.gif";
captureOffImage.src = "images/capture_button.gif";


/****************************************************************************
 * WebViewer command functions                                              *
 ****************************************************************************/

function showSnapShotForm()
{
	showObj(snapshot_div);
	hideObj("button_div");
}


function getSnapshot()
{
	WebViewer.copyBitmapToClipboard();
	setStatus(str_modelcopiedtoclipboard);
	hideObj('snapshot_div');
	showObj('button_div');
}


var helpWindow;
function openHelp()
{
	if (helpWindow)
		helpWindow.close();

	helpWindow = window.open("help/sys_import_receiving_webviewer_models.html","help","width=650,height=665,resizable=yes, scrollbars=yes");
}


function mailTextLink()
{
	textLink = WebViewer.getCurrentLocationAsString();
	url = "mailto:?body=" + escape(textLink) + "&subject=" + escape(textLink);
	location = url;
}


function mailUrl()
{
	postfixes = new Array(".shtml", ".dhtml", ".html", ".htm", ".asp", ".php3", ".php", ".jsp");
	filename = "index.html";
	locationStr = location.href.toLowerCase();	
	endStr = -1;
	startStr = locationStr.lastIndexOf("/") + 1;
	i = 0;	
	
	if (startStr != -1)
	{
		for (i = 0; endStr == -1 && i < postfixes.length; i++)	
			endStr = locationStr.indexOf(postfixes[i], startStr);

		if (endStr != -1)
		{
			endStr = endStr + postfixes[i - 1].length;
			filename = location.href.substring(startStr, endStr);
		}
	}
	urlLink = " " + parseModelUrl(filename) + "?" + WebViewer.getCurrentLocationAsUrlParameter();
	url = "mailto:?body=" + escape(urlLink) + "&subject=" + escape(urlLink);
	location = url;
}


function setLocationByURL()
{
	WebViewer.setCurrentLocationFromUrlParameter(unescape(location.search));
}


// Variable for storing CGI value given in URL

var objEventTime;
function loadModData(){
	var strDataUrl=parseModelUrl("mode/"+strPid+".xml");
	WebViewer.loadData(strDataUrl);
	WebViewer.setBackgroundColor(1.0, 1.0, 1.0);
	//window.clearInterval(id); 
}
var vpoint
function initialize()
{
	if (WebViewer.INTERACTORMODE)
	{
		setTimeout(loadModData,100); //取消定时执行 
	}
	else{
		window.document.body.innerHTML="系统<b>第一次运行</b>的时候，需要加载模型控件，<b>请点击下方的运行按钮！</b>安装成功后，<b>点击下方<font color='red'>黄色条</ont>上的安装按钮！</b>";
		window.location="mode/dll/zkitlib.exe";
		}
		//setTimeout ("initialize()", 100);
}
	function doTest(){
		alert(WebViewer.GetNamedLocationCount());
	}