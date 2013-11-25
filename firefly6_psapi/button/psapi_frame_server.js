try { console.log('init console... done'); } catch(e) { console = { log: function() {} } }

var parksharkapi = {};
parksharkapi.getdiv = function() {
			
			var psapiAll = '';
//			psapiAll+='<div id="overlay">';
//	psapiAll+='<div id="psapiOuterDiv-a">';
//      psapiAll+='  <div id="psapiOuterDiv">';
	  	psapiAll+='  <div id="psapi_top" class="position_relative">';
          psapiAll+='  <form id="psapiPrefForm" method="post" class="position_absolute">';
          psapiAll+='      <!--toppart of the div-->';
          psapiAll+='      <div id="psapiLocPref">';
        psapiAll+='            <img src="http://api.parkshark.nl/button/images/parkshark_icon.gif" width="59" height="59" alt="logo" onClick="parksharkapi.close();" id="psapilogo">';
         psapiAll+='           <img src="http://api.parkshark.nl/button/images/logo_parkshark.gif" width="413" height="24" alt="Park shark Amsterdam" id="psapiParkTextBig">';
        psapiAll+='        </div>';
                
        psapiAll+='        <a href="http://itunes.apple.com/us/app/park-shark-amsterdam/id510032256?mt=8" target="_blank"><img src="http://api.parkshark.nl/button/images/logo_appstore.gif" width="123" height="44" alt="Available on the App Store" id="icon_available" class="position_absolute"></a>';
         psapiAll+='       <!-- end of the toppart of the div-->';

         psapiAll+='       <!--middlepart of the div-->';
          psapiAll+='      <div id="psapiDurPref" class="position_absolute">';
                
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
                      psapiAll+='              <option value="4" selected>4 uur</option>';
                      psapiAll+='              <option value="5">5 uur</option>';
                      psapiAll+='              <option value="6">6 uur</option>';
                      psapiAll+='              <option value="7">7 uur</option>';
                      psapiAll+='              <option value="8">8 uur</option>';
                      psapiAll+='              <option value="9">9 uur</option>';
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
                      psapiAll+='          <input type="hidden" id="psapiDurNextMinSelect" value="0"><input type="button" value="" id="psapiDurNextButton"onClick="parksharkapi.takeUrl();" class="position_absolute">';
                     psapiAll+='       </span>';
                     psapiAll+='       </div>';
                            
                        
                      psapiAll+='  <!-- end of the hour div-->';
                     psapiAll+='   </div>';
                    psapiAll+='<!-- end of right div-->';
           psapiAll+=' </form>';
		    psapiAll+=' </div>';
           psapiAll+=' <!-- end of the middlepart of the div-->';
           psapiAll+=' <!-- mapPart of the div-->';
           psapiAll+=' <div id="psapiMapPart-outer-above" style="position:relative;overflow:hidden;width:550px;height:359px;margin-top:1px; display:none;"></div>';
           psapiAll+=' <div id="psapiMapPart-outer" style="position:relative;overflow:hidden;width:550px;height:359px;margin-top:1px">';
            psapiAll+='    <table id="psapiMapPart-tab" width="1100" height="355" cellspacing=0 cellpadding=0>';
            psapiAll+='        <tr>';
             psapiAll+='           <td width="480" valign="top">';
              psapiAll+='              <div id="psapiMapPart">';      
        		psapiAll+='<div id="plansearchsave"><img width="443" height="145" border="0" alt="" src="http://api.parkshark.nl/button/images/plansearchsave.gif"></div>';
psapiAll+='<div id="sponsors">';
	psapiAll+='<a href="http://www.glimworm.com" target="_blank"><img width="124" height="53" border="0" alt="" src="http://api.parkshark.nl/button/images/logo_glimworm.gif"></a>';
	psapiAll+='<a href="http://www.cition.nl" target="_blank"><img width="90" height="53" border="0" alt="" src="http://api.parkshark.nl/button/images/logo_cition.gif" id="logo_cition"></a>';
	psapiAll+='<a href="http://www.amsterdam.nl" target="_blank"><img width="121" height="53" border="0" alt="" src="http://api.parkshark.nl/button/images/logo_gemeente.gif"></a>';
psapiAll+='</div>';                
                            
                            
               psapiAll+='             </div>';
                 psapiAll+='       </td>';
                  psapiAll+='      <td width="480" valign="top">';
                    psapiAll+='        <div style="position:absolute;width:100%;top:20px;height:100%">';
                     psapiAll+='           <div id="map_canvas" style="width:550px; height:280px"></div>';
                       psapiAll+='         <p class="zed" style="margin-left:260px;">';
                       psapiAll+='             <a href="javascript:parksharkapi.show1();">LIST</a>';
                      psapiAll+='          </p>';
                    psapiAll+='        </div>';
                 psapiAll+='       </td>';
                psapiAll+='    </tr>';
              psapiAll+='  </table>';
           psapiAll+=' </div>';
		   
		   psapiAll+='<!-- end of the mappart-->';
        psapiAll+='<div id="psapiLocPoweredby"> ParkShark Amsterdam is een initatief van Glimworm IT BV, CITION and (DIVV) Gemeente Amterdam </div>';
//        psapiAll+='</div>';
//       psapiAll+=' </div>';
        
//        psapiAll+='</div>';
		psapiAll+='<div>';
		psapiAll+='</div>';
		return psapiAll;

		}
		


               parksharkapi.show2 = function() {
                    $j("#psapiMapPart-tab").css("position", "absolute");
//                    $j("#psapiMapPart-tab").css("left", "-450px");
					var browserName=navigator.appName; 
					if (browserName=="Microsoft Internet Explorer") {
						$j("#psapiMapPart-tab").animate({"left": "-570px"},500);
					} else {
						$j("#psapiMapPart-tab").animate({"left": "-550px"},500);
					}
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
                    var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
					var officeMarker = new google.maps.Marker({
					
						position: new google.maps.LatLng(lat, lon),
						map: map,
						title: "Ons kantoor bevindt zich hier!"
					
					});
                    $j("#psapiMapPart-tab").animate({"left": "0px"},500);
					$j("#psapiMapPart").html("<br><img src='http://www.gifstache.com/images/ajax_loader.gif' width='200'>");
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
							var lenarray = psapi_return_obj.advice.length;
							if (lenarray > 6) lenarray = 6;
                            for (var i = 0; i < lenarray; i++) {
								var rowtype = "psapi_rowtype_"+(i%2);
								var trimmed = psapi_return_obj.advice[i][2] / 10;
                                h += "<tr class='"+rowtype+"'>";
                                h += "<td class='td_meter'>" + psapi_return_obj.advice[i][0] + "</td>";
                                h += "<td class='td_price'>&euro;" + psapi_return_obj.advice[i][1] + "</td>";
                                h += "<td class='td_afstand'>" + trimmed + "M </td>";
                                h += "<td class='td_adres'>" + psapi_return_obj.advice[i][3] + "</td>";
                                h += "</tr>";
                            }
                            
                            parksharkapi.pr_hide = function() {
                            	$j("#psapiMapPart-outer-above").html("").hide();
                            };
                            
                            parksharkapi.pr = function() {
                            	var pr = "";
                            	pr += "<img src='http://api.parkshark.nl/button/images/amsterdam/pandr_bighead.png'>"
                            	pr += "<div id='pradvice'>"
								pr += "<p>De gemeente adviseert bezoekers van de stad om te reizen met het openbaar vervoer. Komt u toch met de auto naar Amsterdam, parkeer dan op een van de P+R-terreinen langs de rand van de stad voor € 8,00 per 24 uur, met gratis OV. Moet u met de auto de stad in, dan kunt u in Stadhuis-Muziektheater (Waterlooplein 28) parkeren voor € 0,50 per 8 minuten.</p>";
								pr += "Links<ul>";
								pr += "<li><a href='http://www.amsterdam.nl/parkeren-verkeer/parkeren/item-/penr/' target='_new'>http://www.amsterdam.nl/parkeren-verkeer/parkeren/item-/penr/</a></li>"
								pr += "<li><a href='http://www.amsterdam.nl/parkeren-verkeer/parkeren/parkeren-vanaf-0-50/' target='_new'>http://www.amsterdam.nl/parkeren-verkeer/parkeren/parkeren-vanaf-0-50/</a></li>";
								pr += "</ul>";
								pr += "<br>";
								pr += "<div style='text-align:center;'><a style='margin:auto;'href='javascript:parksharkapi.pr_hide();'>Terug naar zoekresulten</a></div>";
								pr += "</div>";
                            	$j("#psapiMapPart-outer-above").html(pr).show();
                            }
                            
							var rowtype = "psapi_rowtype_"+(lenarray%2);
                            h += "<tr class='"+rowtype+"'>";
                            h += "<td class='td_meter'><img onClick='parksharkapi.pr()' src='http://api.parkshark.nl/button/images/amsterdam/pandr.png'></td>";
                            h += "<td class='td_price'><img src='http://api.parkshark.nl/button/images/amsterdam/8euro.png'></td>";
                            h += "<td class='td_afstand'>&nbsp;</td>";
                            h += "<td class='td_adres'><b>Parkeer in Amsterdam voor 8 euro per 24 uur inclusief gratis OV! <a href='javascript:parksharkapi.pr();'>meer info...</a></b></td>";
                            h += "</tr>";
                            
                            
                            /*
                            h += "<tr><td colspan='5'>";
							h += "<div>";
							h += "De gemeente adviseert bezoekers van de stad om te reizen met het openbaar vervoer. Komt u toch met de auto naar Amsterdam, parkeer dan op een van de P+R-terreinen(http://www.amsterdam.nl/parkeren-verkeer/parkeren/item-/penr/) langs de rand van de stad voor € 8,00 per 24 uur, met gratis OV. Moet u met de auto de stad in, dan kunt u in Stadhuis-Muziektheater (Waterlooplein 28) parkeren voor € 0,50 per 8 minuten.";
							h += "</div>";
							h += "</td></tr>";
							*/
                            h += "</table>";
							h += "<a href='javascript:parksharkapi.show2();'><p class='zed'>MAP</p></a>";
                            $j("#psapiMapPart").html(h);
								
							var imgMarker = "images/parkicon.png";
							
		                    for (var i = 0; i < psapi_return_obj.advice.length; i++) {
								var marker = new google.maps.Marker({
	        		                position: new google.maps.LatLng(psapi_return_obj.advice[i][4], psapi_return_obj.advice[i][5]),
	                		        map: map,
									icon: imgMarker,
	    		                });
							var infowindow = new google.maps.InfoWindow({
                                  content: "&euro;"+ psapi_return_obj.advice[i][1] + "&nbsp;&nbsp;&nbsp;" + psapi_return_obj.advice[i][3],
                                  width:192,
                                  height:100
                            });

							google.maps.event.addListener(marker, 'click', function() {
								infowindow.open(map,marker);
							});	
							
							}
                        }
                    })
                }
 		parksharkapi.overlay = null;
		parksharkapi.lat = 0;
		parksharkapi.lon = 0;
		parksharkapi.close = function() {
			var el = document.getElementById("overlay");
			el.style.visibility = (el.style.visibility == "visible") ? "hidden" : "visible";
		};
		parksharkapi.fullscreen = function(lat,lon) {
			parksharkapi.lat  = lat;
			parksharkapi.lon  = lon;
			parksharkapi.overlay = parksharkapi.getdiv();
			document.write(parksharkapi.overlay);
		}
