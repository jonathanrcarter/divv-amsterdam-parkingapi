try { console.log('init console... done'); } catch(e) { console = { log: function() {} } }

var parksharkapi = {};
parksharkapi.getdiv = function() {
	var psapiAll = '';
	psapiAll+='<div id="overlay">';
	psapiAll+='<div id="psapiOuterDiv-a">';
	psapiAll+='  <div id="psapiOuterDiv">';
	psapiAll+='<a href="javascript:parksharkapi.close();"><img src="http://api.parkshark.nl/button/images/btn_close.png" width="53" height="53" alt="close" id="psapiClose" class="psapi_position_absolute"></a>';
//	psapiAll+= "<iframe src='http://api.parkshark.nl/button/psapi_frame_server.php?lat="+parksharkapi.lat+"&lon="+parksharkapi.lon+"' width='550' height='515' style='border:0;padding:0;margin:0;' scrolling='no' frameBorder='0'></iframe>";
	psapiAll+= "<iframe src='http://api.parkshark.nl/button/psapi_frame_server.jsp?lat="+parksharkapi.lat+"&lon="+parksharkapi.lon+"' width='550' height='515' style='border:0;padding:0;margin:0;' scrolling='no' frameBorder='0'></iframe>";
	psapiAll+='</div>';
	psapiAll+='</div>';
	psapiAll+='</div>';
	return psapiAll;
}
parksharkapi.overlay = null;
parksharkapi.lat = 0;
parksharkapi.lon = 0;
parksharkapi.close = function() {
	var el = document.getElementById("overlay");
	el.style.visibility = (el.style.visibility == "visible") ? "hidden" : "visible";
};
parksharkapi.pop = function(lat,lon) {
	parksharkapi.lat  = lat;
	parksharkapi.lon  = lon;
			
	if (parksharkapi.overlay == null) {
		parksharkapi.overlay = parksharkapi.getdiv();
		var _body = document.getElementsByTagName('body') [0];
		var _div = document.createElement('div');
		_body.appendChild(_div);
		_div.innerHTML = parksharkapi.overlay;
		parksharkapi.overlay = "";
	}

	var el = document.getElementById("overlay");
	el.style.visibility = (el.style.visibility == "visible") ? "hidden" : "visible";
/*
	$j("#psapiOuterDiv-a").css("display","block");			
	$j("#psapiOuterDiv-a").css('position','relative');
			
	$j("#psapiOuterDiv").css('position','absolute');
	$j("#psapiOuterDiv").css("background-color","#ffffff");
	$j("#psapiOuterDiv").css('left','30%');
	$j("#psapiOuterDiv").css('top','50px');
*/
}
		
parksharkapi.popb = function() {
	$j("#psapiOuterDiv").css('left','0px');
	$j("#psapiOuterDiv").css('top','0px');
}