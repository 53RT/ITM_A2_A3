<html>
    <head>
        <!-- Bootstrap -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->

    <link rel="icon" href="favicon.ico">

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="cover.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script
    language="JavaScript">
    <!--
    thumbONE= new Image();
    thumbONE.src = "knopf.jpg"
    button2= new Image();
    button2.src = "email.jpg"
    /* usw. für alle Grafiken,
    die am Mouse-Over-Effekt beteiligt sind */
    //-->
    </script>
    <title>ITM Infovis</title>
    
    </head>
    <body>
 
 	<!-- top navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
    
		<div class="row" >
			
		<div class="col-lg-3 col-sm-3 col-xs-3" ></div>		
		<div class="col-lg-2 col-sm-2 col-xs-2" >	
           <a class="navbar-brand" href="/itm/infovis.jsp">ITM Infovis</a>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <a class="navbar-brand" href="/itm/tags.jsp?tag=image" style="font-size: 100%"><span class="glyphicon glyphicon-camera"></span>  Image</a>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <a class="navbar-brand" href="/itm/tags.jsp?tag=audio" style="font-size: 100%"><span class="glyphicon glyphicon-headphones"></span>  Audio</a>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <a class="navbar-brand" href="/itm/tags.jsp?tag=video" style="font-size: 100%"><span class="glyphicon glyphicon-film"></span>  Video</a>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <a class="navbar-brand" href="/itm/index.jsp" style="font-size: 100%"style="font-size: 100%"><span class="glyphicon glyphicon-home"></span>  Home</a>
        </div>
        <div class="col-lg-3 col-sm-3 col-xs-3" ><h6 align="right" style="margin-right: 30px">by J. Busching & G. Sluiter</h6></div>
        
       </div>
    </div>
 
	<!-- Reihe umfasst alle Media Elemente -->
            <div class="row" style="display:block; margin-left:auto; margin-right:auto; width: 13vm; padding-top: 10%; padding-bottom: 10%; opacity:0.5; " > 
            	<div class="row" style="background-color: FFFFFF; padding:2%;" align="middle">
            
			        <applet code="itm.infovis.ItmApplet" 
			                archive="infovis/prefuse.jar,infovis/itm.jar" 
			                width="900" 
			                height="700" align="middle">
			        </applet>   
        		</div>
	        	<div class="row"  style="background-color: FFFFFF; padding:2%;" align="middle">
	        	
	        	<p style="margin-left: 15px; color: 555555">
	           	 	<span class="glyphicon glyphicon-zoom-in"></span>  Zoom by right-click &amp; drag.<br>
	            	<span class="glyphicon glyphicon-move"></span>  Pan by left-click &amp; drag.<br>
	            	<span class="glyphicon glyphicon-hand-up"></span>  Doubleclick to navigate to item<br>
	       		</p>
	        	
	        	</div>
        
        </div>
        
        

            <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="fjs/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="js/ie10-viewport-bug-workaround.js"></script>

    </body>
</html>    