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

        <h1>Media that is tagged with "<%= tag %>"</h1>
        
        <div class="masthead clearfix">
            <div class="inner">
              
              <nav>
                <ul class="nav masthead-nav">
                  <li class="active"><a href="index.jsp"><span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span> go back</a>      
                </ul>
              </nav>
            </div>
          </div>
        <%

            // ***************************************************************
            //  Fill in your code here!
            // ***************************************************************
        
            // get all media objects that are tagged with the passed tag

            ArrayList<AbstractMedia> media = MediaFactory.getMedia();
            
            // iterate over all available media objects and display them
            
            int c=0; // counter for rowbreak after 3 thumbnails.

            for(AbstractMedia medium : media) {
                ArrayList<String> alltags = medium.getTags();      //tags holen

                for (String s : alltags) {                       

                    if(s.equals(tag)) {                             //falls gewuenschter tag vorhanden          

                        c++;
                        %>
                            <div style="width:300px;height:460px;padding:10px;float:left;" class="jumbotron">
                        <%
                    
                        // handle images
                        if ( medium instanceof ImageMedia ) {

                            // display image thumbnail and metadata
                            ImageMedia img = (ImageMedia) medium;
                            %>
                            <div style="width:200px;height:210px;padding:10px;">
                                <a href="media/img/<%= img.getInstance().getName()%>">
                                                                                    <!-- mouseover hinzugefuegt / Histo-Thumbs dafuer werden beim init() bzw. loadmedia() in MediaFactory erzeugt -->
                                <img src="media/md/<%= img.getInstance().getName() %>.thumb.png" border="0" onmouseover="this.src='media/md/<%= img.getInstance().getName() %>.hist.png.thumb.png'" onmouseout="this.src='media/md/<%= img.getInstance().getName() %>.thumb.png'" />
                                </a>
                            </div>
                            <div>
                                Name: <%= img.getName() %><br/>

                            <div id="spoilerID<%=c %>" style="display:none">          <!-- jedes Spoiler-Element kriegt eine eigene durchnummerierte ID -->
                                Dimensions: <%= img.getWidth() %>x<%= img.getHeight() %>px<br/>
                                PixelSize: <%= img.getPixelSize() %> <br/>
                                NumComponents: <%= img.getNumComponents() %> <br/>
                                NumColorComponents: <%= img.getNumColorComponents() %> <br/>
                                Transparency: <%= img.getTransparency() %> <br/>
                                Orientation: <%= img.getOrientation() %> <br/>
                                ColorSpaceType: <%= img.getColorSpaceType() %> <br/>
                            </div>
                            <button title="Metadaten anzeigen" type="button" onclick="if(document.getElementById('spoilerID<%=c %>') .style.display=='none') {document.getElementById('spoilerID<%=c %>') .style.display=''}else{document.getElementById('spoilerID<%=c %>') .style.display='none'}">Metadaten anzeigen</button> <br/>
          
                                Tags: <% for ( String t : img.getTags() ) { %><a href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %><br/>
                            </div>
                            <%  
                            } else 
                if ( medium instanceof AudioMedia ) {
                    // display audio thumbnail and metadata
                    AudioMedia audio = (AudioMedia) medium;
                    System.out.println("DEBUG: AUDIO");
                    %>
                    <div style="width:200px;height:200px;padding:10px;">
                        <br/><br/><br/><br/>
                        <embed src="media/md/<%= audio.getInstance().getName() %>.wav" autostart="false" width="150" height="30" />
                        <br/>
                        <a href="media/audio/<%= audio.getInstance().getName()%>">
                            Download <%= audio.getInstance().getName()%>
                        </a>
                    </div>
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
                    <div style="width:200px;height:200px;padding:10px;">
                        <a href="media/video/<%= video.getInstance().getName()%>">
                            
                        <object width="200" height="200">
                            <param name="movie" value="media/md/<%= video.getInstance().getName() %>_thumb.swf">
                            <embed src="media/md/<%= video.getInstance().getName() %>_thumb.swf" width="200" height="200">
                            </embed>
                        </object>

                        </a>
                    </div>
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
                    } else {
                        }

                %>
                    </div>
                        <%
                            if ( c % 3 == 0 ) {
                        %>
                            <div style="clear:left"/>
                        <%
                            }
                
                    }
                }
            }
              
        %>
       
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="js/ie10-viewport-bug-workaround.js"></script>            
    </body>
</html>
