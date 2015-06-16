package itm.image;

/*******************************************************************************
    This file is part of the ITM course 2015
    (c) University of Vienna 2009-2015
*******************************************************************************/


import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import itm.model.AbstractMedia;
import itm.model.MediaFactory;
import itm.util.*;				//Hinzugefuegt, um Klasse Histogramm zu verwenden

import java.lang.Object.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

/**
    This class creates color and grayscale histograms for various images.
    It can be called with 3 parameters, an input filename/directory, an output directory and a various bin/interval size.
    It will read the input image(s), count distinct pixel values and then plot the histogram.

    If the input file or the output directory do not exist, an exception is thrown.
*/
public class ImageHistogramGenerator 
{

    /**
        Constructor.
    */
    public ImageHistogramGenerator() 
    {
    }

    /**
        Processes an image directory in a batch process.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param bins the histogram interval
        @return a list of the created files
    */
    public ArrayList<File> batchProcessImages( File input, File output, int bins) throws IOException
    {
        if ( ! input.exists() ) 
            throw new IOException( "Input file " + input + " was not found!" );
        if ( ! output.exists() ) 
            throw new IOException( "Output directory " + output + " not found!" );
        if ( ! output.isDirectory() ) 
            throw new IOException( output + " is not a directory!" );

        ArrayList<File> ret = new ArrayList<File>();
        
        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    File result = processImage( f, output, bins );
                    System.out.println( "converted " + f + " to " + result );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                    }
                 }
            } else {
            try {
                File result = processImage( input, output, bins );
                System.out.println( "created " + input + " for " + result );
                ret.add( result );
            } catch ( Exception e0 ) { System.err.println( "Error creating histogram from " + input + " : " + e0.toString() ); }
            } 
        return ret;
    }  
    
    /**
        Processes the passed input image and stores it to the output directory.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param bins the histogram interval
        already existing files are overwritten automatically
    */   
	protected File processImage( File input, File output, int bins ) throws IOException, IllegalArgumentException
    {
		if ( ! input.exists() ) 
            throw new IOException( "Input file " + input + " was not found!" );
        if ( input.isDirectory() ) 
            throw new IOException( "Input file " + input + " is a directory!" );
        if ( ! output.exists() ) 
            throw new IOException( "Output directory " + output + " not found!" );
        if ( ! output.isDirectory() ) 
            throw new IOException( output + " is not a directory!" );


		// compose the output file name from the absolute path, a path separator and the original filename
		String outputFileName = "";
		outputFileName += output.toString() + File.separator + input.getName().toString();
		File outputFile = new File( output, input.getName() + ".hist.png" );
		
       
        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************

        // load the input image
		
        BufferedImage testImg = null;
        
        try {
			testImg = ImageIO.read(input);
		} catch (Exception e) { System.err.println( "Error loading Inputfile: " + e.toString() ); }
		
		// get the color model of the image and the amount of color components
        
        ColorSpace colorSpaceType = testImg.getColorModel().getColorSpace();
        int colorSpaceTypeInt = colorSpaceType.getType();
        
        int numberColorComponents = 0;
        numberColorComponents = testImg.getColorModel().getNumColorComponents();      
        
		// initiate a Histogram[color components] [bins]
        
        Histogram aktHistogramm = new Histogram(numberColorComponents, bins);        
		
		// create a histogram array histArray[color components][bins] || 3 stellen: rot, blau, gruen -> erste Stelle im Array! 0-3 ... zweite Stelle im Array ist Wert von 0 - 255
        
        Color farbe = new Color(0);
        int[][] histArray = new int[colorSpaceTypeInt][bins];
       
		// read the pixel values and extract the color information
        
		int rotwert = 0;
		int gruenwert = 0;
		int blauwert = 0;
		
        for (int x = 0; x < testImg.getWidth(); x++ ) {				//einmal ueber das Bild druebergehen
        	for (int y = 0; y < testImg.getHeight(); y++) {
	        	farbe = new Color (testImg.getRGB(x, y));
	        	rotwert = farbe.getRed();							//Farbwerte extrahieren
	        	gruenwert = farbe.getGreen();
	        	blauwert = farbe.getBlue();
	        	
	        	histArray[0][rotwert]++;		//fuer jeweiligen Wert wird der dazugehoerige bucket erhoeht
	        	histArray[1][gruenwert]++;
	        	histArray[2][blauwert]++;
	        	
        	}
        }
        
		// fill the array setHistogram(histArray)
        
        aktHistogramm.setHistogram(histArray);
		
		// plot the histogram, try different dimensions for better visualization
        
        BufferedImage histogrammImage = null;
        
        if (testImg.getWidth() < 200 && testImg.getHeight() < 100)
        	histogrammImage = aktHistogramm.plotHistogram(200, 100);									//canvas-groesse    	
        else if (testImg.getHeight() < testImg.getWidth())        
        	histogrammImage = aktHistogramm.plotHistogram(testImg.getWidth(), testImg.getHeight());		//falls != Landscape-Mode, Histogramm nicht um 90° gedreht
        else
        	histogrammImage = aktHistogramm.plotHistogram(testImg.getHeight(), testImg.getWidth());		//gleiche groesse fuer Mouseover-Effekt
        		
        // encode and save the image as png
        
    	try {
			ImageIO.write(histogrammImage, "png", outputFile);
		} catch (Exception e) { System.err.println( "Error writing outputFile: " + e.toString() ); }

        return outputFile;
    }
    
        
    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
        if ( args.length < 3 ) {
            System.out.println( "usage: java itm.image.ImageHistogramGenerator <input-image> <output-directory> <bins>" );
            System.out.println( "usage: java itm.image.ImageHistogramGenerator <input-directory> <output-directory> <bins>" );
            System.out.println( "");
            System.out.println( "bins:default 256" );
            System.exit( 1 );
        }
        // read params
        File fi = new File( args[0] );
        File fo = new File( args[1] );
        int bins = Integer.parseInt(args[2]);
        ImageHistogramGenerator histogramGenerator = new ImageHistogramGenerator();
        histogramGenerator.batchProcessImages( fi, fo, bins );        
    }    
}