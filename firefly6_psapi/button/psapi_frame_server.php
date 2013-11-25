<?php

$lat = $_GET['lat'];
$lon = $_GET['lon'];

?>
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
			<LINK REL="stylesheet" TYPE="text/css" HREF="http://jon4.glimworm.com/glimworm3/jc/psapi/psapi_frame_server.css">
            <script type="text/javascript" src="http://jon4.glimworm.com/glimworm3/jc/psapi/psapi_frame_server.js"></script>
    </head>
    
    <body>
    <!--[[ start ]]-->
    <script>
    parksharkapi.fullscreen(<?php echo $lat;?>,<?php echo $lon;?>);
    </script>
    <!--[[ end ]]-->
    </body>

</html>





