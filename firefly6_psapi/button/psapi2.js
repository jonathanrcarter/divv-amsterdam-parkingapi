var parksharkapi = {};
parksharkapi.getdiv = function() {
			var psapiAll = '';
			psapiAll+='<div id="overlay">';
	psapiAll+='<div id="psapiOuterDiv-a" style="display:none;">';
      psapiAll+='  <div id="psapiOuterDiv">';
          psapiAll+='  <form id="psapiPrefForm" method="post">';
          psapiAll+='      <!--toppart of the div-->';
          psapiAll+='      <div id="psapiLocPref">';
        psapiAll+='            <img src="images/logo.jpg" width="40" height="40" alt="logo" id="psapilogo">';
         psapiAll+='           <img src="images/parkTextBig.png" width="228" height="25" alt="Park shark Amsterdam" id="psapiParkTextBig">';
        psapiAll+='            <img src="images/parkTextSmall.png" width="147" height="15" alt="the smartest way to park" id="psapiParkTextSmall">';
        psapiAll+='        </div>';
                
        psapiAll+='        <a href="http://itunes.apple.com/us/app/park-shark-amsterdam/id510032256?mt=8" target="_blank"><img src="images/icon_available.gif" width="120" height="40" alt="Available on the App Store" id="icon_available"></a>';
         psapiAll+='       <!-- end of the toppart of the div-->';

         psapiAll+='       <!--middlepart of the div-->';
          psapiAll+='      <div id="psapiDurPref">';
                
          psapiAll+='         <div id="psapiDurTodayDay">';
            psapiAll+='           <p class="psapiDurText">Dag:</p>';
              psapiAll+='              <span id="psapiDurTodayDaySpan">';
              psapiAll+='                  <select id="psapiDurTodayDaySelect">';
               psapiAll+='                     <option value="1">Maandag</option>';
                psapiAll+='                    <option value="2">Dinsdag</option>';
                 psapiAll+='                   <option value="3">Woensdag</option>';
                 psapiAll+='                   <option value="4">Donderdag</option>';
                  psapiAll+='                  <option value="5">Vrijdag</option>';
                   psapiAll+='                 <option value="6">Zaterdag</option>';
                   psapiAll+='                 <option value="7">Zondag</option>';
                   psapiAll+='             </select>';
                    psapiAll+='        </span>';
                   psapiAll+='     </div>';
                   psapiAll+='     <!-- end of the div of the day -->';
                  psapiAll+='      <!-- div for the hour-->';
                  psapiAll+='      <div id="psapiDurTodayHour">';
                  psapiAll+='          <p class="psapiDurText">Starttijd:</p>';
                   psapiAll+='         <span id="psapiDurTodayHourSpan">';
                    psapiAll+='            <select id="psapiDurTodayHourSelect">';
                   psapiAll+='                 <option value="0">00</option>';
                   psapiAll+='                 <option value="1">01</option>';
                     psapiAll+='               <option value="2">02</option>';
                     psapiAll+='               <option value="3">03</option>';
                     psapiAll+='               <option value="4">04</option>';
                     psapiAll+='               <option value="5">05</option>';
                     psapiAll+='               <option value="6">06</option>';
                     psapiAll+='               <option value="7">07</option>';
                      psapiAll+='              <option value="8">08</option>';
                     psapiAll+='               <option value="9" selected>09</option>';
                      psapiAll+='              <option value="10">10</option>';
                    psapiAll+='                <option value="11">11</option>';
                    psapiAll+='                <option value="12">12</option>';
                     psapiAll+='               <option value="13">13</option>';
                     psapiAll+='               <option value="14">14</option>';
                     psapiAll+='               <option value="15">15</option>';
                        psapiAll+='            <option value="16">16</option>';
                          psapiAll+='          <option value="17">17</option>';
                          psapiAll+='          <option value="18">18</option>';
                         psapiAll+='           <option value="19">19</option>';
                        psapiAll+='            <option value="20">20</option>';
                        psapiAll+='            <option value="21">21</option>';
                        psapiAll+='            <option value="22">22</option>';
                         psapiAll+='           <option value="23">23</option>';
                         psapiAll+='       </select>';
                        psapiAll+='        <select id="psapiDurTodayMinSelect">';
                        psapiAll+='            <option value="0">00</option>';
                        psapiAll+='            <option value="1">15</option>';
                        psapiAll+='            <option value="2">30</option>';
                        psapiAll+='            <option value="3">45</option>';
                        psapiAll+='        </select>';
                       psapiAll+='     </span>';
                     psapiAll+='   </div>';
                     psapiAll+='   <!-- end of the hour div-->';
                        
                    
                   psapiAll+='<!-- end of left div-->';
                    psapiAll+='    <!-- div for the hour-->';
                     psapiAll+='   <div id="psapiDurNextHour">';
                     psapiAll+='       <p class="psapiDurText">Duur:</p>';
                    psapiAll+='        <span id="psapiDurNextHourSpan">';
                    psapiAll+='            <select id="psapiDurNextHourSelect">';
                     psapiAll+='               <option value="0">0 uur</option>';
                      psapiAll+='              <option value="1">1 uur</option>';
                       psapiAll+='             <option value="2">2 uur</option>';
                      psapiAll+='              <option value="3">3 uur</option>';
                      psapiAll+='              <option value="4 uur" selected>4</option>';
                      psapiAll+='              <option value="5 uur">5</option>';
                      psapiAll+='              <option value="6 uur">6</option>';
                      psapiAll+='              <option value="7 uur">7</option>';
                      psapiAll+='              <option value="8 uur">8</option>';
                      psapiAll+='              <option value="9 uur">9</option>';
                       psapiAll+='             <option value="10">10 uur</option>';
					   psapiAll+='             <option value="11">11 uur</option>';
					   psapiAll+='             <option value="12">12 uur</option>';
					   psapiAll+='             <option value="13">13 uur</option>';
					   psapiAll+='             <option value="14">14 uur</option>';
					   psapiAll+='             <option value="15">15 uur</option>';
					   psapiAll+='             <option value="16">16 uur</option>';
					   psapiAll+='             <option value="17">17 uur</option>';
					   psapiAll+='             <option value="18">18 uur</option>';
					   psapiAll+='             <option value="19">19 uur</option>';
                       psapiAll+='             <option value="20">20 uur</option>';
					   psapiAll+='             <option value="21">21 uur</option>';
					   psapiAll+='             <option value="22">22 uur</option>';
					   psapiAll+='             <option value="23">23 uur</option>';
					   psapiAll+='             <option value="24">24 uur</option>';
					   psapiAll+='             <option value="25">25 uur</option>';
					   psapiAll+='             <option value="26">26 uur</option>';
					   psapiAll+='             <option value="27">27 uur</option>';
					   psapiAll+='             <option value="28">28 uur</option>';
					   psapiAll+='             <option value="29">29 uur</option>';
                       psapiAll+='             <option value="30">30 uur</option>';
					   psapiAll+='             <option value="31">31 uur</option>';
					   psapiAll+='             <option value="32">32 uur</option>';
					   psapiAll+='             <option value="33">33 uur</option>';
					   psapiAll+='             <option value="34">34 uur</option>';
					   psapiAll+='             <option value="35">35 uur</option>';
					   psapiAll+='             <option value="36">36 uur</option>';
					   psapiAll+='             <option value="37">37 uur</option>';
					   psapiAll+='             <option value="38">38 uur</option>';
					   psapiAll+='             <option value="39">39 uur</option>';
                       psapiAll+='             <option value="40">40 uur</option>';
					   psapiAll+='             <option value="41">41 uur</option>';
					   psapiAll+='             <option value="42">42 uur</option>';
					   psapiAll+='             <option value="43">43 uur</option>';
					   psapiAll+='             <option value="44">44 uur</option>';
					   psapiAll+='             <option value="45">45 uur</option>';
					   psapiAll+='             <option value="46">46 uur</option>';
					   psapiAll+='             <option value="47">47 uur</option>';
					   psapiAll+='             <option value="48">48 uur</option>';
					   psapiAll+='             <option value="49">49 uur</option>';
                       psapiAll+='             <option value="50">50 uur</option>';
					   psapiAll+='             <option value="51">51 uur</option>';
					   psapiAll+='             <option value="52">52 uur</option>';
					   psapiAll+='             <option value="53">53 uur</option>';
					   psapiAll+='             <option value="54">54 uur</option>';
					   psapiAll+='             <option value="55">55 uur</option>';
					   psapiAll+='             <option value="56">56 uur</option>';
					   psapiAll+='             <option value="57">57 uur</option>';
					   psapiAll+='             <option value="58">58 uur</option>';
					   psapiAll+='             <option value="59">59 uur</option>';
                       psapiAll+='             <option value="60">50 uur</option>';
					   psapiAll+='             <option value="61">61 uur</option>';
					   psapiAll+='             <option value="62">62 uur</option>';
					   psapiAll+='             <option value="63">63 uur</option>';
					   psapiAll+='             <option value="64">64 uur</option>';
					   psapiAll+='             <option value="65">65 uur</option>';
					   psapiAll+='             <option value="66">66 uur</option>';
					   psapiAll+='             <option value="67">67 uur</option>';
					   psapiAll+='             <option value="68">68 uur</option>';
					   psapiAll+='             <option value="69">69 uur</option>';
                       psapiAll+='             <option value="70">70 uur</option>';
					   psapiAll+='             <option value="71">71 uur</option>';
					   psapiAll+='             <option value="72">72 uur</option>';
                      psapiAll+='          </select>';
                      psapiAll+='          <input type="hidden" id="psapiDurNextMinSelect" value="0"><input type="button" value="" id="psapiDurNextButton"onClick="parksharkapi.takeUrl();">';
                     psapiAll+='       </span>';
                     psapiAll+='       </div>';
                            
                        
                      psapiAll+='  <!-- end of the hour div-->';
                     psapiAll+='   </div>';
                    psapiAll+='<!-- end of right div-->';
                
                
           psapiAll+=' </form>';
           psapiAll+=' <!-- end of the middlepart of the div-->';
           psapiAll+=' <!-- mapPart of the div-->';
           psapiAll+=' <div id="psapiMapPart-outer" style="position:relative;overflow:hidden;width:550px;height:309px;margin-top:1px">';
            psapiAll+='    <table id="psapiMapPart-tab" width="1100" height="355" cellspacing=0 cellpadding=0>';
            psapiAll+='        <tr>';
             psapiAll+='           <td width="480" valign="top">';
              psapiAll+='              <div id="psapiMapPart">';      
        		psapiAll+='				<div> Tell ParkShark Amsterdam when you want to park and it will tell you how much it costs and if there are alternatives.</div>';
psapiAll+='<div id="sponsors">';
psapiAll+='<table cellspacing="0" cellpadding="0" width="410">';
psapiAll+='<tr><td valign="top">';
psapiAll+='<img width="235" height="175" border="0" alt="" src="images/park-shark-splash.png">';
psapiAll+='</td><td valign="top" id="sponsors_td">';
psapiAll+='<a target="_blank" href="http://www.cition.nl/"><img width="137" height="62" border="0" alt="Cition" src="http://www.parkshark.nl/_site1493/images/logo_cition.gif"></a>';
psapiAll+='<a target="_blank" href="#"><img width="174" height="55" border="0" id="logo_gemeente" alt="Gemeente Amsterdam" src="http://www.parkshark.nl/_site1493/images/logo_gemeente.gif"></a>';
psapiAll+='<a target="_blank" href="http://www.glimworm.com"><img width="160" height="76" border="0" id="gw_logo" alt="Glimworm IT BV" src="http://www.parkshark.nl/_site1493/images/glimworm_logo.gif"></a>';
psapiAll+='</td></tr></table>';
psapiAll+='</div>            ';                
                            
                            
               psapiAll+='             </div>';
                            
                            
                            
                            
                 psapiAll+='           <a href="javascript:parksharkapi.show2();"><p class="zed">MAP</p></a>';
                 psapiAll+='       </td>';
                  psapiAll+='      <td width="480" valign="top">';
                    psapiAll+='        <div style="position:absolute;width:100%;top:1;height:100%">';
                     psapiAll+='           <div id="psapi_map_canvas" style="width:550px; height:250px"></div>';
                       psapiAll+='         <p class="zed">';
                       psapiAll+='             <a href="javascript:parksharkapi.show1();">LIST</a>';
                      psapiAll+='          </p>';
                    psapiAll+='        </div>';
                 psapiAll+='       </td>';
                psapiAll+='    </tr>';
              psapiAll+='  </table>';
           psapiAll+=' </div>';
		   
		   psapiAll+='<!-- end of the mappart-->';
        psapiAll+='<div id="psapiLocPoweredby"> ParkShark Amsterdam is een initatief van Glimworm IT BV, CITION and (DIVV) Gemeente Amterdam </div>';
        psapiAll+='</div>';
       psapiAll+=' </div>';
        
        psapiAll+='</div>';
		psapiAll+='<div>';
		psapiAll+='</div>';
		return psapiAll;

		}
		


               parksharkapi.show2 = function() {
                    $j("#psapiMapPart-tab").css("position", "absolute");
//                    $j("#psapiMapPart-tab").css("left", "-450px");
                    $j("#psapiMapPart-tab").animate({"left": "-550px"},500);
                }

                parksharkapi.show1 = function() {
                    $j("#psapiMapPart-tab").css("position", "absolute");
//                    $j("#psapiMapPart-tab").css("left", "0px");
                    $j("#psapiMapPart-tab").animate({"left": "0px"},500);
                }


                parksharkapi.takeUrl = function() {
                    var day = $j("#psapiDurTodayDaySelect").val();
                    var hr = $j("#psapiDurTodayHourSelect").val();
                    var minu = $j("#psapiDurTodayMinSelect").val();
                    var durhr = $j("#psapiDurNextHourSelect").val();
                    var durmin = $j("#psapiDurNextMinSelect").val();
                    var lat = parksharkapi.lat;//put the office latitude here
                    var lon = parksharkapi.lon;//put the office longitude here
                    var realmin = "";
                    if (durmin == "0") {
                        realmin = "0";
                    } else if (durmin == "1") {
                        realmin = "25";
                    } else if (durmin == "2") {
                        realmin = "50";
                    } else if (durmin == "3") {
                        realmin = "75";
                    }

                    var realdur = durhr + "." + realmin;
                    var url = "http://ms1.glimworm.com/psapi/api.jsp?day=" + day + "&hr=" + hr + "&min=" + minu + "&duration=" + realdur + "&lat=" + lat + "&lon=" + lon + "&methods=cash,pin&jsonpvar=psapi_return_obj";
                    //$j("#psapiMapPart").html(url);
                    var myOptions = {
                        center: new google.maps.LatLng(lat, lon),
                        zoom: 16,
                        mapTypeId: google.maps.MapTypeId.ROADMAP
                    };

                    var mapy = new google.maps.Map(document.getElementById("psapi_map_canvas"), myOptions);

                    $j("#psapiMapPart-tab").animate({"left": "0px"},500);
					$j("#psapiMapPart").html("WORKING......<br><img src='http://www.gifstache.com/images/ajax_loader.gif' width='200'>");
                    $j.ajax({
                        type: "GET",
                        url: url,
                        data: "",
                        dataType: "jsonp",
                        success: function (e) {
                            console.log(e);
                        },
                        complete: function (e) {
                            console.log(psapi_return_obj);
                            console.log(e);
                            var h = "<table celpadding='0' cellspacing='0'><tr><th align='left'>Meter</th><th align='left'>Prijs</th><th align='left'>Afstand</th><th align='left'>Adres</th></tr>";
                            for (var i = 0; i < psapi_return_obj.advice.length; i++) {
								var rowtype = "psapi_rowtype_"+(i%2);
								var trimmed = psapi_return_obj.advice[i][2] / 10;
                                h += "<tr class='"+rowtype+"'>";
                                h += "<td class='td_meter'>" + psapi_return_obj.advice[i][0] + "</td>";
                                h += "<td class='td_price'>&euro;" + psapi_return_obj.advice[i][1] + "</td>";
                                h += "<td class='td_afstand'>" + trimmed + "M </td>";
                                h += "<td class='td_adres'>" + psapi_return_obj.advice[i][3] + "</td>";
                                h += "</tr>";
                            }
                            h += "</table>";
                            $j("#psapiMapPart").html(h);

		                    for (var i = 0; i < psapi_return_obj.advice.length; i++) {
								var marker = new google.maps.Marker({
	        		                position: new google.maps.LatLng(psapi_return_obj.advice[i][4], psapi_return_obj.advice[i][5]),
	                		        map: mapy,
			                        title: "€" + psapi_return_obj.advice[i][1] + " , " +psapi_return_obj.advice[i][3]
	    		                });
							}
                        }
                    })
                }
 		parksharkapi.overlay = null;
		parksharkapi.lat = 0;
		parksharkapi.lon = 0;
		parksharkapi.pop = function(lat,lon) {
			parksharkapi.lat  = lat;
			parksharkapi.lon  = lon;
			
			if (parksharkapi.overlay == null) {
				parksharkapi.overlay = parksharkapi.getdiv();
				$j("body").append(parksharkapi.overlay);
				parksharkapi.overlay = "";
			}


			var el = document.getElementById("overlay");
			el.style.visibility = (el.style.visibility == "visible") ? "hidden" : "visible";

			$j("#psapiOuterDiv-a").css("display","block");			
			$j("#psapiOuterDiv-a").css('position','relative');
			
			$j("#psapiOuterDiv").css('position','absolute');
			$j("#psapiOuterDiv").css("background-color","#ffffff");
			$j("#psapiOuterDiv").css('left','50px');
			$j("#psapiOuterDiv").css('top','50px');
			/*
			$j("body").css("background-color","#333");
			*/
		}
		
		parksharkapi.popb = function() {
			$j("#psapiOuterDiv").css('left','0px');
			$j("#psapiOuterDiv").css('top','0px');
		}