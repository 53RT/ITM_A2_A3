package itm.image;

/*******************************************************************************
    This file is part of the ITM course 2015
    (c) University of Vienna 2009-2015
*******************************************************************************/

import java.awt.Graphics2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.geom.AffineTransform; 	//hinzugefuegt

import javax.imageio.ImageIO;

/**
    This class converts images of various formats to PNG thumbnails files.
    It can be called with 3 parameters, an input filename/directory, an output directory and a compression quality parameter.
    It will read the input image(s), grayscale and scale it/them and convert it/them to a PNG file(s) that is/are written to the output directory.

    If the input file or the output directory do not exist, an exception is thrown.
*/
public class ImageThumbnailGenerator 
{

    /**
        Constructor.
    */
    public ImageThumbnailGenerator()
    {
    }

    /**
        Processes an image directory in a batch process.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param rotation
        @param overwrite indicates whether existing thumbnails should be overwritten or not
        @return a list of the created files
    */
    public ArrayList<File> batchProcessImages( File input, File output, double rotation, boolean overwrite ) throws IOException
    {
        if ( ! input.exists() ) {
            throw new IOException( "Input file " + input + " was not found!" );
        }
        if ( ! output.exists() ) {
            throw new IOException( "Output directory " + output + " not found!" );
        }
        if ( ! output.isDirectory() ) {
            throw new IOException( output + " is not a directory!" );
        }

        ArrayList<File> ret = new ArrayList<File>();

        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    File result = processImage( f, output, rotation, overwrite );
                    System.out.println( "converted " + f + " to " + result );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
            }
        } else {
            try {
                File result = processImage( input, output, rotation, overwrite );
                System.out.println( "converted " + input + " to " + result );
                ret.add( result );
            } catch ( Exception e0 ) {
                System.err.println( "Error converting " + input + " : " + e0.toString() );
            }
        } 
        return ret;
    }  

    /**
        Processes the passed input image and stores it to the output directory.
        This function should not do anything if the outputfile already exists and if the overwrite flag is set to false.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param dimx the width of the resulting thumbnail
        @param dimy the height of the resulting thumbnail
        @param overwrite indicates whether existing thumbnails should be overwritten or not
    */
    protected File processImage( File input, File output, double rotation, boolean overwrite ) throws IOException, IllegalArgumentException
    {
        if ( ! input.exists() ) {
            throw new IOException( "Input file " + input + " was not found!" );
        }
        if ( input.isDirectory() ) {
            throw new IOException( "Input file " + input + " is a directory!" );
        }
        if ( ! output.exists() ) {
            throw new IOException( "Output directory " + output + " not found!" );
        }
        if ( ! output.isDirectory() ) {
            throw new IOException( output + " is not a directory!" );
        }

        // create outputfilename and check whether thumb already exists
        File outputFile = new File( output, input.getName() + ".thumb.png" );
        if ( outputFile.exists() ) {
            if ( ! overwrite ) {
                return outputFile;
            }
        }

        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************

        // load the input image
        
        BufferedImage testImg = null;
        
        try {
			testImg = ImageIO.read(input);
		} catch (Exception e) { System.err.println( "Error loading Inputfile: " + e.toString() );
		}
        
        // rotate by the given parameter the image - do not crop image parts!
        
        if (testImg.getWidth() < testImg.getHeight()) {							//Bild in "landscape-mode"?

            BufferedImage neuesImage = new BufferedImage(testImg.getHeight(),testImg.getWidth(), testImg.TYPE_INT_ARGB);	//neues Bild erstellen mit neuer Höhe = alte Breite        
            AffineTransform neuAT = new AffineTransform();                   
            neuAT.translate(testImg.getHeight() / 2, testImg.getWidth() / 2);   //Mitte auswählen für Drehung
            neuAT.rotate(Math.PI / 2);											//hier: fix 90 grad        
            neuAT.translate(-testImg.getWidth() / 2, -testImg.getHeight() / 2); 
            
            Graphics2D gedreht2d = neuesImage.createGraphics();					//Graphiken erzeugen
            gedreht2d.drawImage(testImg, neuAT, null);							//Bild mit Transformation zeichnen
            testImg = neuesImage;
            gedreht2d.dispose();

        }
     

        // scale the image to a maximum of [ 200 w X 100 h ] pixels - do not distort! ||| LAUT FORUM: "die Breite soll exakt 200px sein, bei gleichbleibendem Seitenverhältnis" DAHER: BREITE = 200, LAENGE RELATIV ENTSPRECHEND 
        
        if (testImg.getWidth() > 200 || testImg.getHeight() > 100) {		//falls Bild nicht eh schon kleiner...

        	double skalierungsfaktor = (double) 200 / testImg.getWidth();
        	int skaliertehoehe = (int) (testImg.getHeight() * skalierungsfaktor);  	
        	BufferedImage skalImage = new BufferedImage(200, skaliertehoehe, testImg.TYPE_INT_ARGB);  				//neues File mit skalierten Werten erzeugen     	
            AffineTransform skalTrans = AffineTransform.getScaleInstance(skalierungsfaktor, skalierungsfaktor);		//AT mit Skalierungswerten erstellen
            AffineTransformOp bilinearScaleOp = new AffineTransformOp(skalTrans, AffineTransformOp.TYPE_BILINEAR);	//linear mapping durch AT
            bilinearScaleOp.filter(testImg, skalImage);																//anwenden
        	
        	testImg = skalImage;
        }
        
        
        // if the image is smaller than [ 200 w X 100 h ] - print it on a [ dim X dim ] canvas!
        
        if (testImg.getWidth() < 200 && testImg.getHeight() < 100) {
        	       	
        	BufferedImage canvasThumb = new BufferedImage(200, 100, testImg.getType());	
        	int abstandLinks = (int) ((200 - testImg.getWidth()) / 2);		//Punkt in der Breite, bei dem Zeichnung beginnt (zentriert)
        	int abstandOben = (int) ((100 - testImg.getHeight()) / 2);		//Punkt in der Länge, bei dem Zeichnung beginnt (zentriert)

            Graphics2D mini2d = canvasThumb.createGraphics();
            mini2d.drawImage(testImg, abstandLinks, abstandOben, null);		
            testImg = canvasThumb;
            mini2d.dispose();
        }
        

        // rotate you image by the given rotation parameter    
        
        if (rotation != 0) {       	  	

	        BufferedImage neuesImage = new BufferedImage(testImg.getWidth(),testImg.getHeight(), testImg.TYPE_INT_ARGB);	//neues Bild erstellen mit Hoehe = Breite -- INKLUSIVE ALPHA-CHANNEL
	        
	        AffineTransform neuAT = new AffineTransform();
	        neuAT.rotate(Math.toRadians(rotation), testImg.getWidth()/2, testImg.getHeight()/2);	//Rotation über die Mitte (Haelfte der Seiten)	       
	        Graphics2D gedreht2d = neuesImage.createGraphics();
	        gedreht2d.drawImage(testImg, neuAT, null);
	        gedreht2d.dispose();
	        
	        // save as extra file - say: don't return as output file
	
	        File outputRotatedFile = new File( output, input.getName() + ".thumb.rotated.png" );
	
	    	try {
				ImageIO.write(neuesImage, "png", outputRotatedFile);
			} catch (Exception e) { System.err.println( "Error writing outputFile: " + e.toString() ); }
        
        }      

        // encode and save the image  
        
    	try {
			ImageIO.write(testImg, "png", outputFile);
		} catch (Exception e) { System.err.println( "Error writing outputFile: " + e.toString() ); }

        return outputFile;

        /**
            ./ant.sh ImageThumbnailGenerator -Dinput=media/img/ -Doutput=test/ -Drotation=90
        */
    }

    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
        if ( args.length < 3 ) {
            System.out.println( "usage: java itm.image.ImageThumbnailGenerator <input-image> <output-directory> <rotation degree>" );
            System.out.println( "usage: java itm.image.ImageThumbnailGenerator <input-directory> <output-directory> <rotation degree>" );
            System.exit( 1 );
        }
        File fi = new File( args[0] );
        File fo = new File( args[1] );
        double rotation = Double.parseDouble( args[2] );

        ImageThumbnailGenerator itg = new ImageThumbnailGenerator();
        itg.batchProcessImages( fi, fo, rotation, true );
    }    
}