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
        <h1>Welcome to the ITM media library</h1>
        <a href="infovis.jsp">infovis</a>
         
        
        <%
            // get the file paths - this is NOT good style (resources should be loaded via inputstreams...)
            // we use it here for the sake of simplicity.
            String basePath = "webapps/itm/media";
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
            for ( AbstractMedia medium : media ) {
                c++;
                %>
                    <div style="width:300px;height:300px;padding:10px;float:left;">
                <%
            
                // handle images
                if ( medium instanceof ImageMedia ) {
                	 // ***************************************************************
                    //  Fill in your code here!
                    // ***************************************************************
                    
                    // show the histogram of the image on mouse-over
                    
                    // display image thumbnail and metadata
                    ImageMedia img = (ImageMedia) medium;
                    %>
                    <div style="width:200px;height:200px;padding:10px;">
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
                        Duration: <%= audio.getDuration() %><br/>
                        Tags: <% for ( String t : audio.getTags() ) { %><a href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %><br/>
                    </div>
                    <%  
                    } else
                if ( medium instanceof VideoMedia ) {
                    // handle videos thumbnail and metadata...
                    VideoMedia video = (VideoMedia) medium;
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

                } // for 
                
        %>
     
        
        
    </body>
</html>
