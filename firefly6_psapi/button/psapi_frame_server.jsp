<%@ page import="java.util.*, java.io.*, java.text.*, gwdb.v6.*, gwdb.v6.nav.*, gwdb.*, javax.mail.*, javax.mail.internet.*, com.oroinc.net.*"
	session="true"
	buffer="128kb"
	autoFlush="true"
	isThreadSafe="true"
	contentType="text/html;charset=UTF-8"%><%

	String lat = gwUtils.get(request,"lat","51.0");
	String lon = gwUtils.get(request,"lon","5.0");
	

%>
<!doctype html>
<html lang="nl">
    
    <head>
        <meta charset="utf-8">
        <title>parksharkAPI</title>
        <style type="text/css">
            body {
                margin:0;
            }
			
						
        </style>
            <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
            <script src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
            <script>
                var $j = jQuery.noConflict();
            </script>
			<!-- 
			<LINK REL="stylesheet" TYPE="text/css" HREF="http://jon4.glimworm.com/glimworm3/jc/psapi/psapi_frame_server.css">
            <script type="text/javascript" src="http://jon4.glimworm.com/glimworm3/jc/psapi/psapi_frame_server.js"></script>
            -->
			<LINK REL="stylesheet" TYPE="text/css" HREF="http://api.parkshark.nl/button/psapi_frame_server.css">
            <script type="text/javascript" src="http://api.parkshark.nl/button/psapi_frame_server.js"></script>
    </head>
    
    <body>
    <!--[[ start ]]-->
    <script>
    parksharkapi.fullscreen(<%=lat%> ,<%=lon%>);
    </script>
    <!--[[ end ]]-->
    </body>

</html>
