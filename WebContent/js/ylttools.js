var ylt= ylt ||{};
ylt.Tools = ylt.Tools || {};
var yltTools=ylt.Tools={
	$:function(_strId){
		return document.getElementById(_strId);
	},
	generBg:function(_callback){
		objPhotoBJ=document.createElement("div");
		objPhotoBJ.setAttribute('id','sys_disable_poplevel');
		objPhotoBJ.style.position="absolute";
		objPhotoBJ.style.top="0";
		objPhotoBJ.style.background="#000";
		objPhotoBJ.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=20,finishOpacity=75,StartX=0,StartY=0,FinishX=1024,FinishY=1000";
		objPhotoBJ.style.opacity="0.1";
		objPhotoBJ.style.left="0";
		objPhotoBJ.style.width=iScreen_Width+"px";
		objPhotoBJ.style.height=iScreen_Height+"px";
		objPhotoBJ.style.zIndex = "99";
		objPhotoBJ.onclick=function(){_callback();};
		document.body.appendChild(objPhotoBJ);
	},
	openRightMenu:function(_strContent){
		var objRightMenu=this.$("sys_div_msg_right");
		if(this.getStyle(objRightMenu,"right")==-230){
			objRightMenu.innerHTML=_strContent;
			objRightMenu.style.display="";
			objRightMenu.style.zIndex="100";
			objRightMenu.style.height=(iScreen_Height-51)+"px";
			this.generBg(function(){document.body.removeChild(ylt.Tools.$("sys_disable_poplevel"));ylt.Tools.animate(objRightMenu, {right:-230}, 200);});
			this.animate(objRightMenu, {right: 0}, 200);
		}
	},
	getStyle:function(_element, _attr) {
		if(window.getComputedStyle){
			   return this.trimStyleValue(window.getComputedStyle(_element)[_attr]);
		}else{
			   return this.trimStyleValue(_element.currentStyle[_attr]); 
		}
	},
	trimStyleValue:function(_value) {
		var reg = /^[-+]?([1-9]\d+|\d)(\.\d+)?(px|pt|em|rem)$/;
		if (isNaN(_value) && reg.test(_value)) return parseFloat(_value);
		if (isNaN(_value)) return Number(_value);
		return _value
	},
	setStyle:function(_ele, _attr, _val) {
		var reg = /^(width|height|top|bottom|left|right|(margin|padding)(Top|Left|Bottom|Right)?)$/;
		if (!isNaN(_val) && reg.test(_attr)) {
			_ele.style[_attr] = _val + "px";
			return;
		}
		_ele.style[_attr] = _val;
	},
	animate:function(_element, _target, _duration, _callback) {
		var change = {};
		var begin = {};
		for (var key in _target) {
			begin[key] = this.getStyle(_element, key);
			change[key] = this.trimStyleValue(_target[key]) - begin[key];
		}
		var iTime = 0;
		var objTimer = setInterval(function(){
				iTime += 20;
			if (iTime >= _duration) {
				clearInterval(objTimer);
				for (var key in _target) {
					ylt.Tools.setStyle(_element, key, _target[key]);
				}
				_callback && _callback.call(_element);
				return;
			}
			for (var key in _target) {
				var current =change[key]/_duration*iTime+begin[key];
				ylt.Tools.setStyle(_element, key, current);
			}},20);
	},
	getAjaxActive:function(){
		var xmlHttp;
		if (window.ActiveXObject) { 
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}else if (window.XMLHttpRequest) { 
			xmlHttp = new XMLHttpRequest();
		}
		return xmlHttp;
	},
	postData:function(param,aStrUrl,_strContentType){
		var xml=this.getAjaxActive();
		xml.open("POST",aStrUrl,false); 
		xml.setRequestHeader("content-type",_strContentType);  
		xml.setRequestHeader("charset", "UTF-8");
		xml.send(param);  
		var res = xml.responseText;
		return res;
	},
	getTx:function(param,aStrUrl){
		return this.postData(param,aStrUrl,"application/x-www-form-urlencoded");
	},
	postTx:function(param,aStrUrl){
		return this.postData(param,aStrUrl,"application/json");
	}
};
LazyLoad=function(k){function p(b,a){var g=k.createElement(b),c;for(c in a)a.hasOwnProperty(c)&&g.setAttribute(c,a[c]);return g}function l(b){var a=m[b],c,f;if(a)c=a.callback,f=a.urls,f.shift(),h=0,f.length||(c&&c.call(a.context,a.obj),m[b]=null,n[b].length&&j(b))}function w(){var b=navigator.userAgent;c={async:k.createElement("script").async===!0};(c.webkit=/AppleWebKit\//.test(b))||(c.ie=/MSIE/.test(b))||(c.opera=/Opera/.test(b))||(c.gecko=/Gecko\//.test(b))||(c.unknown=!0)}function j(b,a,g,f,h){var j=
			function(){l(b)},o=b==="css",q=[],d,i,e,r;c||w();if(a)if(a=typeof a==="string"?[a]:a.concat(),o||c.async||c.gecko||c.opera)n[b].push({urls:a,callback:g,obj:f,context:h});else{d=0;for(i=a.length;d<i;++d)n[b].push({urls:[a[d]],callback:d===i-1?g:null,obj:f,context:h})}if(!m[b]&&(r=m[b]=n[b].shift())){s||(s=k.head||k.getElementsByTagName("head")[0]);a=r.urls;d=0;for(i=a.length;d<i;++d)g=a[d],o?e=c.gecko?p("style"):p("link",{href:g,rel:"stylesheet"}):(e=p("script",{src:g}),e.async=!1),e.className="lazyload",
			e.setAttribute("charset","utf-8"),c.ie&&!o?e.onreadystatechange=function(){if(/loaded|complete/.test(e.readyState))e.onreadystatechange=null,j()}:o&&(c.gecko||c.webkit)?c.webkit?(r.urls[d]=e.href,t()):(e.innerHTML='@import "'+g+'";',u(e)):e.onload=e.onerror=j,q.push(e);d=0;for(i=q.length;d<i;++d)s.appendChild(q[d])}}function u(b){var a;try{a=!!b.sheet.cssRules}catch(c){h+=1;h<200?setTimeout(function(){u(b)},50):a&&l("css");return}l("css")}function t(){var b=m.css,a;if(b){for(a=v.length;--a>=0;)if(v[a].href===
			b.urls[0]){l("css");break}h+=1;b&&(h<200?setTimeout(t,50):l("css"))}}var c,s,m={},h=0,n={css:[],js:[]},v=k.styleSheets;return{css:function(b,a,c,f){j("css",b,a,c,f)},js:function(b,a,c,f){j("js",b,a,c,f)}}}(this.document);
