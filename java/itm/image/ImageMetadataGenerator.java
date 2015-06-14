package itm.image;

/*******************************************************************************
    This file is part of the ITM course 2015
    (c) University of Vienna 2009-2015
*******************************************************************************/

import itm.model.AbstractMedia;
import itm.model.ImageMedia;
import itm.model.MediaFactory;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sun.imageio.plugins.common.ImageUtil;

//import sun.org.mozilla.javascript.internal.ast.WithStatement; //War plötzlich nicht mehr ok für ant!!!

/**
    This class reads images of various formats and stores some basic image meta data to text files.
    It can be called with 3 parameters, an input filename/directory, an output directory and an "overwrite" flag.
    It will read the input image(s), retrieve some meta data and write it to a text file in the output directory.
    The overwrite flag indicates whether the resulting output file should be overwritten or not.
    
    If the input file or the output directory do not exist, an exception is thrown.
*/
public class ImageMetadataGenerator 
{

    /**
        Constructor.
    */
    public ImageMetadataGenerator()
    {
    }
   

    /**
        Processes an image directory in a batch process.
        @param input a reference to the input image directory
        @param output a reference to the output directory
        @param overwrite indicates whether existing metadata files should be overwritten or not
        @return a list of the created media objects (images)
    */
    public ArrayList<ImageMedia> batchProcessImages( File input, File output, boolean overwrite ) throws IOException
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

        ArrayList<ImageMedia> ret = new ArrayList<ImageMedia>();

        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    ImageMedia result = processImage( f, output, overwrite );
                    System.out.println( "converted " + f + " to " + output );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
            }
        } else {
                try {
                    ImageMedia result = processImage( input, output, overwrite );
                    System.out.println( "converted " + input + " to " + output );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
        }
        return ret;
    }    
    
    /**
        Processes the passed input image and stores the extracted metadata to a textfile in the output directory.
        @param input a reference to the input image
        @param output a reference to the output directory
        @param overwrite indicates whether existing metadata files should be overwritten or not
        @return the created image media object
    */
    protected ImageMedia processImage( File input, File output, boolean overwrite ) throws IOException, IllegalArgumentException
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

        // create outputfilename and check whether thumb already exists. All image 
        // metadata files have to start with "img_" -  this is used by the mediafactory!
        File outputFile = new File( output, "img_" + input.getName() + ".txt" );
        if ( outputFile.exists() ) {
            if ( ! overwrite ) {
                // load from file
                ImageMedia media = new ImageMedia();
                media.readFromFile( outputFile );
                return media;
                }
        }


        // get metadata and store it to media object
        ImageMedia media = (ImageMedia) MediaFactory.createMedia( input );

        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************
        
        // load the input image
        
        BufferedImage testImg = null;
        
        try {
			testImg = ImageIO.read(input);
		} catch (Exception e) { System.err.println( "Error loading Inputfile: " + e.toString() ); }

       
        // set width and height of the image
        
        int heigth = 0; 
        int width = 0;
        width = testImg.getWidth();
        heigth = testImg.getHeight();
        
        media.setWidth(width);
        media.setHeight(heigth);     

        // add a tag "image" to the media
        
        media.addTag("image");      
        
        // tags fuer Task 3.3 hinzufuegen - Vorgehen zunächst aehnlich wie in ImageHistogramGenerator
        
        Color farbe = new Color(0);
        ColorSpace colorSpaceType = testImg.getColorModel().getColorSpace();
        int colorSpaceTypeInt = colorSpaceType.getType();
        int bins = 256;
        int groesse = testImg.getWidth() * testImg.getHeight();
       
		int rotwert = 0;
		int gruenwert = 0; 
		int blauwert = 0; ;
		
		int rot = 0; int gruen = 0; int blau = 0; 
		int schwarzgrau = 0; int weiss = 0;
		
        for (int x = 0; x < testImg.getWidth(); x++ ) {				//einmal ueber das Bild druebergehen
        	for (int y = 0; y < testImg.getHeight(); y++) {
	        	farbe = new Color (testImg.getRGB(x, y));
	        	rotwert = farbe.getRed();							//Farbwerte extrahieren
	        	gruenwert = farbe.getGreen();
	        	blauwert = farbe.getBlue();
	        	        	
	        	if(((rotwert < 50) && (gruenwert < 50) && (blauwert < 50)) ||
	        		(Math.abs(rotwert - gruenwert) < 20) && (Math.abs(rotwert - blauwert) < 20) && (Math.abs(gruenwert - blauwert) < 20)) {
	        		schwarzgrau++;
	        		continue;
	        	}
	        	if((rotwert > 180) && (gruenwert > 180) && (blauwert > 180)) {
	        		weiss++;
	        		continue;
	        	}	        	
	        	if(rotwert > (gruenwert + blauwert)) {
	        		rot++;
	        	}
	        	if(gruenwert > (rotwert + blauwert)) {
	        		gruen++;
	        	}
	        	if(blauwert > (rotwert + gruenwert)) {
	        		blau++;
	        	}
        	}
        }
        
        double doublerot = rot;
        double doublegruen = gruen;
        double doubleblau = blau;
        
        if((doublerot / (groesse - schwarzgrau - weiss)) > 0.2)
        	media.addTag("red");
        
        if((doublegruen / (groesse - schwarzgrau - weiss)) > 0.2)
        	media.addTag("green");
        
        if((doubleblau / (groesse - schwarzgrau - weiss)) > 0.2)
        	media.addTag("blue");
        
        System.out.println("pixelanzahl: " + groesse);                //DEBUG
        System.out.println("rot: " + rot);
        System.out.println("gruen: " + gruen);
        System.out.println("blau: " + blau);
        System.out.println("schwarzgrau: " + schwarzgrau);
        System.out.println("weiss: " + weiss);
        

        // add a tag corresponding t the filename extension of the file to the media 
        
        String dateiname = null;
        dateiname = input.getName();					//Dateiname auslesen
        String dateiendung = "";
        int i = dateiname.lastIndexOf('.');				//String wird ab dem "." ausgelesen
        if (i > 0) {
            dateiendung = dateiname.substring(i+1);
        }
        dateiendung = dateiendung.toLowerCase();		//Umwandlung in lowercase (kein "BMP" etc.)
        
        media.addTag(dateiendung);
        
        // set orientation
        
        if (width > heigth)
        	media.setOrientation(ImageMedia.ORIENTATION_LANDSCAPE);
        else
        	media.setOrientation(ImageMedia.ORIENTATION_PORTRAIT);

        // if there is a colormodel:
        // set color space type.at
        
        //ColorSpace colorSpaceType = testImg.getColorModel().getColorSpace();	//Colorspace auslesen
        //int colorSpaceTypeInt = colorSpaceType.getType();						//in Integer umwandeln
        media.setColorSpaceType(colorSpaceTypeInt);								//und setzen
         	
        // set pixel size
        int pixelsize = testImg.getColorModel().getPixelSize();
        media.setPixelSize(pixelsize);
                
        // set transparency
        int transparency = 0;
        transparency = testImg.getTransparency();
        media.setTransparency(transparency);
        
        // set number of (color) components    
        int numberComponents = 0;
        numberComponents = testImg.getColorModel().getNumComponents();
        media.setNumComponents(numberComponents);
        
        int numberColorComponents = 0;
        numberColorComponents = testImg.getColorModel().getNumColorComponents();
        media.setNumColorComponents(numberColorComponents);
        
        // store meta data        
       
        try {
			media.writeToFile(outputFile);
		} catch (Exception e) { System.err.println( "Error writing outputFile: " + e.toString() ); }
        
        return media;
    }
    
        
    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
        if ( args.length < 2 ) {
            System.out.println( "usage: java itm.image.ImageMetadataGenerator <input-image> <output-directory>" );
            System.out.println( "usage: java itm.image.ImageMetadataGenerator <input-directory> <output-directory>" );
            System.exit( 1 );
        }
        File fi = new File( args[0] );
        File fo = new File( args[1] );
        ImageMetadataGenerator img = new ImageMetadataGenerator();
        img.batchProcessImages( fi, fo, true );        
    }    
}