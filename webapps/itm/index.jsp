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
	/* usw. für alle Grafiken,
	die am Mouse-Over-Effekt beteiligt sind */
	//-->
	</script>
    
    </head>
    <body>



<!-- top navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
    
		<div class="row" >
			
		<div class="col-lg-3 col-sm-3 col-xs-3" ></div>		
		<div class="col-lg-2 col-sm-2 col-xs-2" >	
           <a class="navbar-brand" href="#">ITM Media Library</a>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <a class="navbar-brand" href="/itm/tags.jsp?tag=image"><span class="glyphicon glyphicon-camera"></span>  Image</a>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <a class="navbar-brand" href="/itm/tags.jsp?tag=audio"><span class="glyphicon glyphicon-headphones"></span>  Audio</a>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <a class="navbar-brand" href="/itm/tags.jsp?tag=video"><span class="glyphicon glyphicon-film"></span>  Video</a>
        </div>
        <div class="col-lg-1 col-sm-1 col-xs-1" >
           <a class="navbar-brand" href="/itm/infovis.jsp"><span class="glyphicon glyphicon-modal-window"></span>  Infovis</a>
        </div>
        <div class="col-lg-3 col-sm-3 col-xs-3" ></div>
        
       </div>
    </div>

        <%
            // get the file paths - this is NOT good style (resources should be loaded via inputstreams...)
            // we use it here for the sake of simplicity.
			//TODO Ladebildschirm anzeigen
            
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
            
            int c=0; // counter for rowbreak after 3 thumbnails.
            // iterate over all available media objects
            %>
            	<div class="row" style="display:block; margin-left:auto; margin-right:auto; width: 13vm; padding-top: 10%; padding-bottom: 10%;" > 
            <% 
            
            for ( AbstractMedia medium : media ) {
                c++;
                System.out.println("Aktuelle Datei Nummer: " + c);
                %>
                	<div class="col-lg-3 col-sm-4 col-xs-6" style="background-color: FFFFFF; width: 23%; height: 23%; padding-left: 2%; padding-right: 2%; padding-top: 2%; padding-bottom: 25%;">
                    <!-- <div style="width:300px;height:460px;padding:10px;float:left;" class="jumbotron">  -->
                <%
            
                // handle images
                if ( medium instanceof ImageMedia ) {
                	 // ***************************************************************
                    //  Fill in your code here!
                    // ***************************************************************
                    
                    // show the histogram of the image on mouse-over
                    
                    // display image thumbnail and metadata
                    ImageMedia img = (ImageMedia) medium;
                    
                	String metaData = "<b>Name: </b>" + img.getName() + " <br>" +"<b>Dimensions: </b>" + img.getWidth() + " x " + img.getHeight() + " px <br> <b>PixelSize: </b>" + img.getPixelSize() + " + <br> <b>Number Components: </b>" + img.getNumComponents() + " <br> <b>Number ColorComponents: </b>" + img.getNumColorComponents() + " <br> <b>Transparency: </b>" + img.getTransparency() + " <br> <b>Orientation: </b>" +  img.getOrientation() + " <br><b>ColorSpaceType: </b>" + img.getColorSpaceType() + "<br/>";
                    String downloadButton = "<form action=\"/itm/media/img/" + img.getName() + "\"><button  class=\"btn btn-default\" aria-label= \"Left Align\" type=\"submit\"><span class=\"glyphicon glyphicon-download\" aria-hidden=\"true\"></span> Download</button></form>";
                    
                    %> 
                	
                	<li style="list-style: none">
                       	<div class= class="col-lg-2 col-md-2 col-sm-3 col-xs-4"><a href="#" temp="<%=metaData %>" fileName="<%= img.getName()%>"><img class="img-thumbnail" title="bla" src="media/md/<%=img.getInstance().getName()%>.thumb.png" border="0" onmouseover="this.src='media/md/<%= img.getInstance().getName() %>.hist.png.thumb.png'" onmouseout="this.src='media/md/<%= img.getInstance().getName() %>.thumb.png'" /></a>
                       	</div>
                    </li>
                      
                       	<nobr style="color:555555"><b>Name: </b><%= img.getName()%></nobr>
                       	<br>
						<nobr style="color:555555"><b>Tags: </b><% for ( String t : img.getTags() ) { %><a style="color:555555" href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %></nobr>
						<br>    					
                       <%
                	} else 
                if ( medium instanceof AudioMedia ) {
                    // display audio thumbnail and metadata
                    AudioMedia audio = (AudioMedia) medium;
                    System.out.println("DEBUG: AUDIO");
                    %>
                    
                        <embed class="img-thumbnail" src="media/md/<%= audio.getInstance().getName() %>.wav" autostart="false" width="200px" height="200px" />
                        <br/>
                        <a href="media/audio/<%= audio.getInstance().getName()%>"> Download <%= audio.getInstance().getName()%>
                        </a>
                    
                    <div>
                        Name: <%= audio.getName() %><br/>
                        
                                                
                        <div id="spoilerID<%=c %>" style="display:none">          <!-- jedes Spoiler-Element kriegt eine eigene durchnummerierte ID -->
                      	Size: <%=audio.getSize() %> <br/>
						Tags: <%=audio.getTags() %> <br/>
						Encoding: <%=audio.getEncoding() %> <br/>
						Duration: <%=audio.getDuration() %> <br/>
						Author: <%=audio.getAuthor() %> <br/>
						Title: <%=audio.getTitle() %> <br/>
						Date: <%=audio.getDate() %> <br/>
						Comment: <%=audio.getComment() %> <br/>
						Album: <%=audio.getAlbum() %> <br/>
						Track: <%=audio.getTrack() %> <br/>
						Composer: <%=audio.getComposer() %> <br/>
						Genre: <%=audio.getGenre() %> <br/>
						Frequency: <%=audio.getFrequency() %> <br/>
						Bitrate: <%=audio.getBitrate() %> <br/>
						Channels: <%=audio.getChannels() %> <br/>
                    </div>
                    <button title="Metadaten anzeigen" type="button" onclick="if(document.getElementById('spoilerID<%=c %>') .style.display=='none') {document.getElementById('spoilerID<%=c %>') .style.display=''}else{document.getElementById('spoilerID<%=c %>') .style.display='none'}">Metadaten anzeigen</button> <br/>
                        
                        Tags: <% for ( String t : audio.getTags() ) { %><a href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %><br/>
                    </div>
                    <%  
                    } else
                if ( medium instanceof VideoMedia ) {
                    // handle videos thumbnail and metadata...
                    VideoMedia video = (VideoMedia) medium;
                    System.out.println("DEBUG: Hier ist ein Video");
                    
                    %>
                        <a href="media/video/<%= video.getInstance().getName()%>">
                            
                        <object width="200" height="200" class="img-thumbnail">
                            <param name="movie" value="media/md/<%= video.getInstance().getName() %>_thumb.swf">
                            <embed src="media/md/<%= video.getInstance().getName() %>_thumb.swf" width="200" height="200">
                            </embed>
                        </object>

                        </a>
                    <div>
                        Name: <a href="media/video/<%= video.getInstance().getName()%>"><%= video.getName() %></a><br/>
                        
                        <div id="spoilerID<%=c %>" style="display:none">          <!-- jedes Spoiler-Element kriegt eine eigene durchnummerierte ID -->
                      	
						Size: <%=video.getSize() %> </br>
						Video Codec: <%=video.getVideoCodecName() %> </br>
						Video CodecID: <%=video.getVideoCodecID() %> </br>
						Video Framerate: <%=video.getVideoFrameRate() %> </br>
						Video Length: <%=video.getVideoLenght() %> </br>
						Video Height: <%=video.getVideoHeight() %> </br>
						Video Width: <%=video.getVideoWidth() %> </br>
						----------------------------------------------------
						Audio Codec: <%=video.getAudioCodecName() %> </br>
						Audio CodecID: <%=video.getAudioCodecID() %> </br>
						Audio Channels: <%=video.getAudioNumChannels() %> </br>
						Audio Samplerate: <%=video.getAudioSampleRate() %> </br>
						Audio Bitrate: <%=video.getAudioBitRate() %> </br>
						
                    </div>
                    <button title="Metadaten anzeigen" type="button" onclick="if(document.getElementById('spoilerID<%=c %>') .style.display=='none') {document.getElementById('spoilerID<%=c %>') .style.display=''}else{document.getElementById('spoilerID<%=c %>') .style.display='none'}">Metadaten anzeigen</button> <br/>
                      
                        
                        Tags: <% for ( String t : video.getTags() ) { %><a href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %><br/>
                    </div>
                    <%  
                    } 
                %>
                		</div><!-- COLUMN -->
          <%}
            
            %>
     		</div><!--  ROW  -->


		   <!--Bottom Footer -->
    	<div class="navbar navbar-default navbar-fixed-bottom" role="navigation" style="width:auto; display:block; margin-left:auto; margin-right:auto;">
			<div class="row" >
			
				<div class="col-lg-3 col-sm-3 col-xs-3" ></div>		
				<div class="col-lg-2 col-sm-2 col-xs-2" ><h6> (c) 2015 </h6></div>
        		<div class="col-lg-3 col-sm-3 col-xs-3" ></div>
        		<div class="col-lg-2 col-sm-2 col-xs-2" ><h6>by J. Busching & G. Sluiter</h6></div>
        		<div class="col-lg-3 col-sm-3 col-xs-3" ></div>
        
       		</div>
    	</div>



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
         	$('.modal-title').html(title);
         	// Download Button
         	
         	var temp = $(this).parent('a').attr("temp");
			var button = '<br><hr><br><form action=';
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
         
    </body>
</html>
