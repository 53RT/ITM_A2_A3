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
<%
       
%>
<html>
    <head>
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

        <h1>Media that is tagged with <%= tag %></h1>
        <a href="index.jsp">back</a>

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
                            <div style="width:300px;height:300px;padding:10px;float:left;">
                        <%
                    
                        // handle images
                        if ( medium instanceof ImageMedia ) {

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
                        
    </body>
</html>
