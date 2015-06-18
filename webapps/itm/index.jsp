<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="itm.image.*" %>
<%@ page import="itm.model.*" %>
<%@ page import="itm.util.*" %>
<!--
/*******************************************************************************
 This file is part of the WM.II.ITM course 2014
 (c) University of Vienna 2009-2014
 *******************************************************************************/
-->
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
	<title>ITM Media Library</title>
    </head>
    <body>

	<!-- top navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
    
		<div class="row" >
			
		<div class="col-lg-3 col-sm-3 col-xs-3" ></div>		
		<div class="col-lg-2 col-sm-2 col-xs-2" >	
           <nobr><a style="font-size: 1.1vw " class="navbar-brand" href="/itm/index.jsp">ITM Media Library</a></nobr>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <nobr><a class="navbar-brand" href="/itm/tags.jsp?tag=image" style="font-size: 0.8vw"><span class="glyphicon glyphicon-camera"></span>  Image</a></nobr>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <nobr><a class="navbar-brand" href="/itm/tags.jsp?tag=audio" style="font-size: 0.8vw"><span class="glyphicon glyphicon-headphones"></span>  Audio</a></nobr>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <nobr><a class="navbar-brand" href="/itm/tags.jsp?tag=video" style="font-size: 0.8vw"><span class="glyphicon glyphicon-film"></span>  Video</a></nobr>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <nobr><a class="navbar-brand" href="/itm/infovis.jsp" style="font-size: 0.8vw"style="font-size: 100%"><span class="glyphicon glyphicon-modal-window"></span>  Infovis</a></nobr>
        </div>
        <div class="col-lg-3 col-sm-3 col-xs-3" ><nobr><h6 align="right" style="margin-right: 30px; font-size: 0.65vw">by J. Busching & G. Sluiter</h6></nobr></div>
        
       </div>
    </div>

        <%
            // get the file paths - this is NOT good style (resources should be loaded via inputstreams...)
            // we use it here for the sake of simplicity.

            
            //String basePath = "webapps/itm/media";
            String basePath = "C:\\Users\\Gert\\workspace\\assignment2\\webapps\\itm\\media";
            		
            if ( basePath == null )
                throw new NullPointerException( "could not determine base path of media directory! please set manually in JSP file!" );
            File base = new File( basePath );
            File imageDir = new File( basePath, "img");
            File audioDir = new File( basePath, "audio");
            File videoDir = new File( basePath, "video");
            File metadataDir = new File( basePath, "md");
            MediaFactory.init( imageDir, audioDir, videoDir, metadataDir );
            
            // get all media objects
            ArrayList<AbstractMedia> media = MediaFactory.getMedia();
            
           
            %>
            <!-- Reihe umfasst alle Media Elemente -->
            <div class="row" style="background: rgba(255,255,255,.5); display:block; margin-left:auto; margin-right:auto; padding-top:2%; padding-bottom:2%; margin-top:10%; margin-bottom:10%;" > 
            <% 
            
            for ( AbstractMedia medium : media ) {
                %>
                	<div class="col-lg-3 col-sm-4 col-xs-6" style="width: 23%; height: 23%; padding-left: 2%; padding-right: 2%; padding-top: 3%; padding-bottom: 25%; margin-left:1.5%; margin-right:auto;">
                <%
            
                //Handle IMAGES
                if ( medium instanceof ImageMedia ) {                    
                    ImageMedia img = (ImageMedia) medium;

                    String readableOrientation = "landscape";                   //Bildattribute in gut lesbares Format umwandeln
                    if (img.getOrientation() == 1)  
                        readableOrientation = "portrait";

                    String readableTransparency = "no";
                    if(img.getTransparency() == 0)
                        readableTransparency = "yes";

                    String readableColorSpaceType = "no data";                      
                    switch ( img.getColorSpaceType() ) {
                        case 1: readableColorSpaceType = "CS_CIEXYZ"; break;
                        case 2: readableColorSpaceType = "CS_GRAY"; break;
                        case 3: readableColorSpaceType = "CS_LINEAR_RGB"; break;
                        case 4: readableColorSpaceType = "CS_PYCC"; break;
                        case 5: readableColorSpaceType = "CS_sRGB"; break;
                        case 6: readableColorSpaceType = "TYPE_CMY"; break;
                        case 7: readableColorSpaceType = "TYPE_CMYK"; break;
                        case 8: readableColorSpaceType = "TYPE_GRAY"; break;
                        case 9: readableColorSpaceType = "TYPE_RGB"; break;
                        case 10: readableColorSpaceType = "TYPE_HLS"; break;
                    }                

                	//String beeinhaltet die Metadaten und wird über JavaScript in der Lightbox eingefügt
                	String metaData = "<b>Name: </b>" + img.getName() + " <br>" +"<b>Dimensions: </b>" + img.getWidth() + " x " + img.getHeight() + " px <br> <b>Bits per pixel (PixelSize): </b>" + img.getPixelSize() + "<br> <b>Number of Components: </b>" + img.getNumComponents() + " <br> <b>Number of ColorComponents: </b>" + img.getNumColorComponents() + " <br> <b>Transparency: </b>" + readableTransparency + " <br> <b>Orientation: </b>" + readableOrientation + " <br><b>ColorSpaceType: </b>" + readableColorSpaceType + "<br/>";
                    %> 
                	
                	<li style="list-style: none">
                       	<a href="#" temp="<%=metaData %>" fileName="<%= img.getName()%>"><img class="img-thumbnail" title="" src="media/md/<%=img.getInstance().getName()%>.thumb.png" border="0" onmouseover="this.src='media/md/<%= img.getInstance().getName() %>.hist.png.thumb.png'" onmouseout="this.src='media/md/<%= img.getInstance().getName() %>.thumb.png'" /></a>
                    </li>
                      
                    <nobr style="color:555555"><b>Name: </b><%= img.getName()%></nobr>
                    	<br>
					<nobr style="color:555555"><b>Tags: </b><% for ( String t : img.getTags() ) { %><a style="color:555555" href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %></nobr>
						<br>    					
                       <%
                	} else
                		
                //Handle AUDIO		
                if ( medium instanceof AudioMedia ) {                       
                    AudioMedia audio = (AudioMedia) medium;
                                                                                    //Bildattribute in gut lesbares Format umwandeln
                    int mili = (int) ((Long) audio.getDuration() / 1000);             //Duration in microsec wird umgerechnet
                    int min = (mili / 1000) / 60; 
                    int sec = (mili / 1000) % 60;
                    String readableDuration;
                    
                    String secString = Integer.toString(sec);                   //Sekunden in String konvertiert
                    if (sec < 10) {                                             
                        StringBuilder sb = new StringBuilder(secString);        
                        sb.insert(0, "0");                                      //bei einstelligen Werten wird 0 am Anfang hinzugefuegt
                        secString = sb.toString();
                    }     
                    readableDuration = min + ":" + secString;

                    int kiloBitrate = 0; String stringKiloBitrate = "No data";
                    if (audio.getBitrate() >= 1000) {
                        kiloBitrate = audio.getBitrate() / 1000;
                        stringKiloBitrate = "" + kiloBitrate + " kbit/s";
                    }

                    int readableSize = (int) audio.getSize();
                    if (readableSize >= 1000)
                        readableSize /= 1000;
                    
                    //String beeinhaltet die Metadaten und wird über JavaScript in der Lightbox eingefügt
                	String metaData = "<b>Name: </b>" + audio.getName() + "<br> <b>Size : </b>" + readableSize + " kB" + " (" + audio.getSize() + " Byte) <br> <b>Encoding: </b>" + audio.getEncoding() + " <br><b>Duration: </b>" + readableDuration + " min <br> <b>Author: </b>" + audio.getAuthor() + "<br><b>Title: </b>" + audio.getTitle() + "<br><b>Date: </b>" + audio.getDate() + "<br><b>Comment: </b>" + audio.getComment() + "<br><b>Album: </b>" + audio.getAlbum() + "<br><b>Track: </b>" + audio.getTrack() + "<br><b>Composer: </b>" + audio.getComposer() + "<br><b>Genre: </b>" + audio.getGenre() + "<br><b>Frequency: </b>" + audio.getFrequency() + " Hz" + "<br><b>Bitrate: </b>" + stringKiloBitrate + "<br/><b>Channels: </b>" + audio.getChannels() + "<br/>";
                    %> 
                	
                	<li style="list-style: none" class="img-thumbnail">
                       	<p style="font-size: 7em; margin-top: 8px; margin-bottom: 20px;" align="center"><a href="#" temp="<%=metaData %>" fileName="<%= audio.getName()%>" class="audioThumb" style="width:150px; height: 150px;"><span class="glyphicon glyphicon-music" style="color: 555555;"></span></a></p>
	                
						<audio controls style="max-width:150px; height:20px">
					  		<source src="media/md/<%=audio.getInstance().getName()%>.wav" type="audio/wav">
							Your browser does not support the audio element.
						</audio>
                	</li>
                      
                    <nobr style="color:555555"><b>Name: </b><%= audio.getName()%></nobr>
                    	<br>
					<nobr style="color:555555"><b>Tags: </b><% for ( String t : audio.getTags() ) { %><a style="color:555555" href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %></nobr>
						<br>
                    <%  
                    } else
                    	
                //Handle VIDEOS
                if ( medium instanceof VideoMedia ) {
                    VideoMedia video = (VideoMedia) medium;

                    int micro = (int) (video.getVideoLenght() * 1000000);
                    int mili = micro / 1000;             //Duration in microsec wird umgerechnet
                    int min = (mili / 1000) / 60; 
                    int sec = (mili / 1000) % 60;
                    String readableVideoLength;
                    
                    String secString = Integer.toString(sec);                   //Sekunden in String konvertiert
                    if (sec < 10) {                                             
                        StringBuilder sb = new StringBuilder(secString);        
                        sb.insert(0, "0");                                      //bei einstelligen Werten wird 0 am Anfang hinzugefuegt
                        secString = sb.toString();
                    }     
                    readableVideoLength = min + ":" + secString;

                    int readableBitrate = (int) video.getAudioBitRate();
                    if (readableBitrate >= 1000)
                        readableBitrate /= 1000;

                    String metaData = "<b>Size: </b>" + video.getSize() + " Bytes <br><b>Video Codec: </b>" + video.getVideoCodecName() + "<br><b>Video CodecID: </b>" + video.getVideoCodecID() + " <br><b>Video Framerate: </b>" + video.getVideoFrameRate() + "<br><b>Video Length: </b>" + readableVideoLength + " min <br><b>Video Height: </b>" + video.getVideoHeight() + " px<br><b>Video Width: </b>" + video.getVideoWidth() + " px <hr><b>Audio Codec: </b>" + video.getAudioCodecName() + "<br><b>Audio CodecID: </b>" + video.getAudioCodecID() + "<br><b>Audio Channels: </b>" + video.getAudioNumChannels() + "<br><b>Audio Samplerate: </b>"+ video.getAudioSampleRate() + " Hz" + "<br><b>Audio Bitrate: </b>" + readableBitrate + " kbit/s" + "<br>";
                    %>
                    
                    <li style="list-style: none" >
                       	 <object width="200px" height="160px" class="img-thumbnail">
	                            <param name="movie" value="media/md/<%= video.getInstance().getName() %>_thumb.swf">
	                            <embed src="media/md/<%= video.getInstance().getName() %>_thumb.swf" width="160px" height="150px">
	                            </embed>
	                        </object>
                	</li>
                
                	
                    <li style="list-style: none" >
                    <nobr style="color:555555"><b>Name: </b> <a href="#" temp="<%=metaData %>" fileName="<%= video.getName()%>" class="videoThumb" style="color: 555555" ><%= video.getName() %></a></nobr>
                    </li>
					<nobr style="color:555555"><b>Tags: </b><% for ( String t : video.getTags() ) { %><a style="color:555555" href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %></nobr>
						<br>
						
                    <%}%>
                		</div><!-- COLUMN -->
          <%}%>
          
     		</div><!--  ROW  -->


	<!-- Lightbox für Bilder -->
  <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">  
        <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">x</button>
			<h3 class="modal-title">Heading</h3>       
          		<div class="modal-body">                
          		</div>
          	<div class="modal-footer">
				<!-- Metadaten -->
				
				<!-- Download Button -->
				<form action="<%="/itm/media/img/b1murene.jpg" %>">
    				<button  class="btn btn-default" aria-label="Left Align" type="submit">
 						 <span class="glyphicon glyphicon-download" aria-hidden="true"></span> Download
					</button>
				</form>
			</div>		
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

     		
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="fjs/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="js/ie10-viewport-bug-workaround.js"></script>
    <script src="js/bootstrap.js"></script>
   
   <!-- Lädt die Bilder in die Light Box -->
   <script>  $(document).ready(function(){
       $('li img').on('click',function(){
           var src = $(this).attr('src');
           var fileName = src.slice(8);
           
           var number = fileName.indexOf(".");
           fileName = fileName.slice(0,number+4);

           var dir = "media/img"
           src = dir.concat(fileName);
           
           var img = '<img src="' + src + '" class="img-responsive"/>';
           
           var title = $(this).parent('a').attr("fileName");
         	// Download Button
         	
         	var temp = $(this).parent('a').attr("temp");
			var button = '<hr><form action=';
         	button = button.concat(src);
         	button = button.concat('><button  class="btn btn-default" aria-label="Left Align" type="submit"><span class="glyphicon glyphicon-download" aria-hidden="true"></span> Download</button></form>');
			var md = temp.concat(button);
			
         	$('.modal-footer').html(md);
         	
           $('.modal-title').html(title);
           $('#myModal').modal();
           $('#myModal').on('shown.bs.modal', function(){
               $('#myModal .modal-body').html(img);
               
           });
           $('#myModal').on('hidden.bs.modal', function(){
               $('#myModal .modal-body').html('');
           });
      });  
   })</script>
	<!-- Lädt Audio in die Lightbox -->
   <script>
   $(document).ready(function() {
       $('.audioThumb').click(function () {
    	   
    	 var fileName = this.getAttribute("fileName");
    	 var img ="<audio controls style=\"max-width:600px\"><source src=\"media/audio/";
    	 img = img.concat(fileName);
    	 img = img.concat("\" type=\"audio/mpeg\">Your browser does not support the audio element.</audio>");  
    	   
         var temp = this.getAttribute("temp");
         var src = "media/audio/".concat(fileName);
         
        	var button = '<hr><form action=';
      		button = button.concat(src);
      		button = button.concat('><button  class="btn btn-default" aria-label="Left Align" type="submit"><span class="glyphicon glyphicon-download" aria-hidden="true"></span> Download</button></form>');
			var md = temp.concat(button);
         
			$('.modal-title').html(fileName);
       	$('.modal-footer').html(md);
            $('#myModal').modal();
            $('#myModal').on('shown.bs.modal', function(){
                $('#myModal .modal-body').html(img);
            });
            $('#myModal').on('hidden.bs.modal', function(){
                $('#myModal .modal-body').html('');
            });
       });
   });
   </script>
         
    <!-- für Videos -->     
    <script>
    $(document).ready(function() {
        $('.videoThumb').click(function () {
       	 var fileName = this.getAttribute("fileName");
       	 
       	 var img = "<object width=\"100%\" height=\"40%\" > <param name=\"movie\" value=\"media/md/" + fileName + "_thumb.swf\" > <param name=bgcolor value=\"000000\"> <embed src=\"media/md/" + fileName +"_thumb.swf\" width=\"100%\" height=\"40%\" ></embed></object>";
       	 
         var temp = this.getAttribute("temp");
         var src = "media/video/".concat(fileName);
         
        	var button = '<hr><form action=';
      		button = button.concat(src);
      		button = button.concat('><button  class="btn btn-default" aria-label="Left Align" type="submit"><span class="glyphicon glyphicon-download" aria-hidden="true"></span> Download</button></form>');
			var md = temp.concat(button);
         
			$('.modal-title').html(fileName);
       	$('.modal-footer').html(md);
            $('#myModal').modal();
            $('#myModal').on('shown.bs.modal', function(){
                $('#myModal .modal-body').html(img);
            });
            $('#myModal').on('hidden.bs.modal', function(){
                $('#myModal .modal-body').html('');
            });
        });
    });
    </script>
    
    
    </body>
</html>
