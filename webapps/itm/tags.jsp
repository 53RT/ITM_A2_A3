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
    /* usw. fÃ¼r alle Grafiken,
    die am Mouse-Over-Effekt beteiligt sind */
    //-->
    </script>
	<title>ITM Search</title>
    </head>
    <body>
	
        <%      
            String tag = null;

            // ***************************************************************
            //  Fill in your code here!
            // ***************************************************************

            // get "tag" parameter   
            tag = (String) request.getParameter("tag");
            
            // if no param was passed, forward to index.jsp (using jsp:forward)
            if ((tag == null) || (tag == "")) { %>
                <jsp:forward page="index.jsp" /> 
                <% }                
        %>

				<!-- top navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
    
		<div class="row" >
			
		<div class="col-lg-3 col-sm-3 col-xs-3" ></div>		
		<div class="col-lg-2 col-sm-2 col-xs-2" >	
           <a class="navbar-brand" href="#">ITM Tag Search ( <%=tag %> )</a>
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

        <%

            // ***************************************************************
            //  Fill in your code here!
            // ***************************************************************
        
            // get all media objects that are tagged with the passed tag

            ArrayList<AbstractMedia> media = MediaFactory.getMedia();
        	
            // iterate over all available media objects and display them
            %>
            <!-- Reihe umfasst alle Media Elemente -->
            <div class="row" style="background: rgba(255,255,255,.5); :block; margin-left:auto; margin-right:auto; width: 13vm; padding-top: 10%; padding-bottom: 10%;" > 
            <% 
            for(AbstractMedia medium : media) {
                ArrayList<String> alltags = medium.getTags();      //tags holen

                for (String s : alltags) {                       
                    if(s.equals(tag)) {
                    	
                    	 %>
                     	<div class="col-lg-3 col-sm-4 col-xs-6" style=" width: 23%; height: 23%; padding-left: 2%; padding-right: 2%; padding-top: 2%; padding-bottom: 25%; margin-left:1.5%;">
                     <%
                 
                     //Handle IMAGES
                     if ( medium instanceof ImageMedia ) {                    
                         ImageMedia img = (ImageMedia) medium;

                     	//String beeinhaltet die Metadaten und wird über JavaScript in der Lightbox eingefügt
                     	String metaData = "<b>Name: </b>" + img.getName() + " <br>" +"<b>Dimensions: </b>" + img.getWidth() + " x " + img.getHeight() + " px <br> <b>PixelSize: </b>" + img.getPixelSize() + " + <br> <b>Number Components: </b>" + img.getNumComponents() + " <br> <b>Number ColorComponents: </b>" + img.getNumColorComponents() + " <br> <b>Transparency: </b>" + img.getTransparency() + " <br> <b>Orientation: </b>" +  img.getOrientation() + " <br><b>ColorSpaceType: </b>" + img.getColorSpaceType() + "<br/>";
                         %> 
                     	
                     	<li style="list-style: none">
                            	<a href="#" temp="<%=metaData %>" fileName="<%= img.getName()%>"><img class="img-thumbnail" title="bla" src="media/md/<%=img.getInstance().getName()%>.thumb.png" border="0" onmouseover="this.src='media/md/<%= img.getInstance().getName() %>.hist.png.thumb.png'" onmouseout="this.src='media/md/<%= img.getInstance().getName() %>.thumb.png'" /></a>
                         </li>
                           
                         <nobr style="color:555555"><b>Name: </b><%= img.getName()%></nobr>
                         	<br>
     					<nobr style="color:555555"><b>Tags: </b><% for ( String t : img.getTags() ) { %><a style="color:555555" href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %></nobr>
     						<br> 
     						</div><!-- COLUMN -->   					
                            <%
                            break;
                     	} else
                     		
                     //Handle AUDIO		
                     if ( medium instanceof AudioMedia ) {
                         AudioMedia audio = (AudioMedia) medium;
                         
                         //String beeinhaltet die Metadaten und wird über JavaScript in der Lightbox eingefügt
                     	String metaData = "<b>Name: </b>" + audio.getName() + "<br> Size : " + audio.getSize() + " Byte <br> Encoding: " + audio.getEncoding() + " <br>Duration: " + audio.getDuration() + "ms <br> Author: " + audio.getAuthor() + "<br>Title: " + audio.getTitle() + "<br>Date: " + audio.getDate() + "<br>Comment: " + audio.getComment() + "<br>Album: " + audio.getAlbum() + "<br>Track: " + audio.getTrack() + "<br>Composer: " + audio.getComposer() + "<br>Genre: " + audio.getGenre() + "<br>Frequency: " + audio.getFrequency() +  "<br>Bitrate: " + audio.getBitrate() + "<br/>Channels: " + audio.getChannels() + "<br/>";
                         %> 
                     	
                     	<li style="list-style: none" class="img-thumbnail">
                            	<p style="font-size: 7em"><a href="#" temp="<%=metaData %>" fileName="<%= audio.getName()%>" class="audioThumb" style="width:150px; height: 150px;"><span class="glyphicon glyphicon-music" style="color: 555555;"></span></a></p>
     	                
     						<audio controls style="max-width:150px">
     					  		<source src="media/audio/<%=audio.getInstance().getName()%>" type="audio/mpeg">
     							Your browser does not support the audio element.
     						</audio>
                     	</li>
                           
                         <nobr style="color:555555"><b>Name: </b><%= audio.getName()%></nobr>
                         	<br>
     					<nobr style="color:555555"><b>Tags: </b><% for ( String t : audio.getTags() ) { %><a style="color:555555" href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %></nobr>
     						<br>
     						</div><!-- COLUMN -->
                         <%  
                         break;
                         } else
                         	
                     //Handle VIDEOS
                     if ( medium instanceof VideoMedia ) {
                         VideoMedia video = (VideoMedia) medium;

                         String metaData = "<b>Size: </b>" + video.getSize() + "Byte <br><b>Video Codec: </b>" + video.getVideoCodecName() + "<br><b>Video CodecID: </b>" + video.getVideoCodecID() + " <br><b>Video Framerate: </b>" + video.getVideoFrameRate() + "<br><b>Video Length: </b>" + video.getVideoLenght() + " sec <br><b>Video Height: </b>" + video.getVideoHeight() + " px<br><b>Video Width: </b>" + video.getVideoWidth() + " px <hr><b>Audio Codec: </b>" + video.getAudioCodecName() + "<br><b>Audio CodecID: </b>" + video.getAudioCodecID() + "<br><b>Audio Channels: </b>" + video.getAudioNumChannels() + "<br><b>Audio Samplerate: </b>"+ video.getAudioSampleRate() + "<br><b>Audio Bitrate: </b>" + video.getAudioBitRate() + "<br>";
                         %>
                         
                         <li style="list-style: none" >
                            	 <object width="200" height="160" class="img-thumbnail">
     	                            <param name="movie" value="media/md/<%= video.getInstance().getName() %>_thumb.swf">
     	                            <embed src="media/md/<%= video.getInstance().getName() %>_thumb.swf" width="200" height="160">
     	                            </embed>
     	                        </object>
                     	</li>
                     
                     	
                         <li style="list-style: none" >
                         <nobr style="color:555555"><b>Name: </b> <a href="#" temp="<%=metaData %>" fileName="<%= video.getName()%>" class="videoThumb" style="color: 555555" ><%= video.getName() %></a></nobr>
                         </li>
     					<nobr style="color:555555"><b>Tags: </b><% for ( String t : video.getTags() ) { %><a style="color:555555" href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %></nobr>
     						<br>
             		</div><!-- COLUMN -->
                         <%
                     	break;    
					}
                     
                    }
                   }
                     
            }
		%>
               
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
       	 
       	 var img = "<object width=\"500px\" height=\"400px\"> <param name=\"movie\" value=\"media/md/" + fileName + "_thumb.swf\"><embed src=\"media/md/" + fileName +"_thumb.swf\" width=\"500px\" height=\"400px\"></embed></object>";
       	 
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