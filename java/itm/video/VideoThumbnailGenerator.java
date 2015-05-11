package itm.video;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import itm.util.ImageCompare;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.sun.java.swing.plaf.windows.resources.windows_zh_HK;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

/**
 * This class reads video files, extracts metadata for both the audio and the
 * video track, and writes these metadata to a file.
 * 
 * It can be called with 3 parameters, an input filename/directory, an output
 * directory and an "overwrite" flag. It will read the input video file(s),
 * retrieve the metadata and write it to a text file in the output directory.
 * The overwrite flag indicates whether the resulting output file should be
 * overwritten or not.
 * 
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */
public class VideoThumbnailGenerator {
	
	ArrayList<IVideoPicture> allPictures = new ArrayList<IVideoPicture>();
	ArrayList<BufferedImage> allPictures_Buff = new ArrayList<BufferedImage>();
	  IContainer outContainer;
	  IStream outStream;
	  IStreamCoder outStreamCoder;

	  IRational frameRate;

	  Robot robot;
	  Toolkit toolkit;
	  Rectangle screenBounds;
	  
	  int vWidth;
	  int vHeight;

	  long firstTimeStamp=-1;
	/**
	 * Constructor.
	 */
	public VideoThumbnailGenerator() {
	}

	/**
	 * Processes a video file directory in a batch process.
	 * 
	 * @param input
	 *            a reference to the video file directory
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing output files should be overwritten
	 *            or not
	 * @return a list of the created media objects (videos)
	 */
	public ArrayList<File> batchProcessVideoFiles(File input, File output, boolean overwrite, int timespan) throws IOException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		ArrayList<File> ret = new ArrayList<File>();

		if (input.isDirectory()) {
			File[] files = input.listFiles();
			for (File f : files) {

				String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv")
						|| ext.equals("mp4"))
					try {
						File result = processVideo(f, output, overwrite, timespan);
						System.out.println("processed file " + f + " to " + output);
						ret.add(result);
					} catch (Exception e0) {
						System.err.println("Error processing file " + input + " : " + e0.toString());
					}
			}
		} else {

			String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4"))
				try {
					File result = processVideo(input, output, overwrite, timespan);
					System.out.println("processed " + input + " to " + result);
					ret.add(result);
				} catch (Exception e0) {
					System.err.println("Error when creating processing file " + input + " : " + e0.toString());
				}

		}
		return ret;
	}
	
	/**
	 * Holt alle Bilder aus dem Input File und schreibt sie in die ArrayList
	 */
	protected void getImagesFromVideo(File input){
		
		IContainer container = IContainer.make();
		
		String filepath = input.getAbsolutePath();
		
		if (container.open(filepath, IContainer.Type.READ, null) < 0)
		      throw new IllegalArgumentException("Datei konnte nicht geöffnet werden " + filepath);
		
		int numStreams = container.getNumStreams();
		
		int videoStreamId = -1;
	    IStreamCoder videoCoder = null;
	    for(int i = 0; i < numStreams; i++)
	    {
	      // find the stream object

	      IStream stream = container.getStream(i);

	      // get the pre-configured decoder that can decode this stream;

	      IStreamCoder coder = stream.getStreamCoder();

	      if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO){
	        videoStreamId = i;
	        videoCoder = coder;
	        break;
	      }
	    }
		

	    if (videoStreamId == -1)
	      throw new RuntimeException("could not find video stream in container: "+ filepath);

	    // Now we have found the video stream in this file.  Let's open up
	    // our decoder so it can do work

	    if (videoCoder.open() < 0)
	      throw new RuntimeException(
	        "could not open video decoder for container: " + filepath);

	    IVideoResampler resampler = null;
	    if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24)
	    {
	      // if this stream is not in BGR24, we're going to need to
	      // convert it.  The VideoResampler does that for us.

	      resampler = IVideoResampler.make(
	        videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24,
	        videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
	      if (resampler == null)
	        throw new RuntimeException(
	          "could not create color space resampler for: " + filepath);
	    }

	    // Now, we start walking through the container looking at each packet.

	    IPacket packet = IPacket.make();
	    while(container.readNextPacket(packet) >= 0)
	    {
	      
	      // Now we have a packet, let's see if it belongs to our video strea

	      if (packet.getStreamIndex() == videoStreamId)
	      {
	        // We allocate a new picture to get the data out of Xuggle

	        IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
	            videoCoder.getWidth(), videoCoder.getHeight());

	        int offset = 0;
	        while(offset < packet.getSize())
	        {
	          // Now, we decode the video, checking for any errors.

	          int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
	          if (bytesDecoded < 0)
	            throw new RuntimeException("got error decoding video in: " + filepath);
	          offset += bytesDecoded;
	          
	          // Some decoders will consume data in a packet, but will not
	          // be able to construct a full video picture yet.  Therefore
	          // you should always check if you got a complete picture from
	          // the decode.

	          if (picture.isComplete())
	          {
	            IVideoPicture newPic = picture;
	            System.out.println(picture.getPts()/1000000);
	            
	            // If the resampler is not null, it means we didn't get the
	            // video in BGR24 format and need to convert it into BGR24
	            // format.

	            if (resampler != null)
	            {
	              // we must resample
	              newPic = IVideoPicture.make(
	                resampler.getOutputPixelFormat(), picture.getWidth(), 
	                picture.getHeight());
	              if (resampler.resample(newPic, picture) < 0)
	                throw new RuntimeException(
	                  "could not resample video from: " + filepath);
	            }

	            if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
	              throw new RuntimeException(
	                "could not decode video as BGR 24 bit data in: " + filepath);

	            // convert the BGR24 to an Java buffered image

	            	allPictures.add(newPic);

	            	BufferedImage javaImage = Utils.videoPictureToImage(newPic);

		            // process the video frame

		            allPictures_Buff.add(javaImage);
	            	
	            // process the video frame

	            //processFrame(newPic, javaImage);
	            
	          }
	        }
	      }
	      else
	      {
	        // This packet isn't part of our video stream, so we just
	        // silently drop it.
	        do {} while(false);
	      }
	    }

	    // Technically since we're exiting anyway, these will be cleaned up
	    // by the garbage collector... but because we're nice people and
	    // want to be invited places for Christmas, we're going to show how
	    // to clean up.

	    if (videoCoder != null)
	    {
	      videoCoder.close();
	      videoCoder = null;
	    }
	    if (container !=null)
	    {
	      container.close();
	      container = null;
	    }
	
	 
	}
	
	
	
	public static BufferedImage convertToType(BufferedImage sourceImage,
		      int targetType)
		  {
		    BufferedImage image;

		    // if the source image is already the target type, return the source image

		    if (sourceImage.getType() == targetType)
		      image = sourceImage;

		    // otherwise create a new image of the target type and draw the new
		    // image

		    else
		    {
		      image = new BufferedImage(sourceImage.getWidth(),
		          sourceImage.getHeight(), targetType);
		      image.getGraphics().drawImage(sourceImage, 0, 0, null);
		    }

		    return image;
		  }

	
	/**
	 * Wandelt bilder von BufferedIMages nach IVideoPicture um
	 * 
	 */
	public void encodeImage(BufferedImage originalImage)
	  {
	    BufferedImage worksWithXugglerBufferedImage = convertToType(originalImage,
	        BufferedImage.TYPE_3BYTE_BGR);
	    IPacket packet = IPacket.make();

	    long now = System.currentTimeMillis();
	    if (firstTimeStamp == -1)
	      firstTimeStamp = now;
	    
	    IConverter converter = null;
	    try
	    {
	      converter = ConverterFactory.createConverter(
	          worksWithXugglerBufferedImage, IPixelFormat.Type.YUV420P);
	    }
	    catch (UnsupportedOperationException e)
	    {
	      System.out.println(e.getMessage());
	      e.printStackTrace(System.out);
	    }

	    long timeStamp = (now - firstTimeStamp)*1000; // convert to microseconds
	    IVideoPicture outFrame = converter.toPicture(worksWithXugglerBufferedImage,
	        timeStamp);

	    outFrame.setQuality(0);
	    int retval = outStreamCoder.encodeVideo(packet, outFrame, 0);
	    if (retval < 0)
	      throw new RuntimeException("could not encode video");
	    if (packet.isComplete())
	    {
	      retval = outContainer.writePacket(packet);
	      if (retval < 0)
	        throw new RuntimeException("could not save packet to container");
	    }

	  }
	
	
	
	/**
	 * Processes the passed input video file and stores a thumbnail of it to the
	 * output directory.
	 * 
	 * @param input
	 *            a reference to the input video file
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing files should be overwritten or not
	 * @return the created video media object
	 */
	protected File processVideo(File input, File output, boolean overwrite, int timespan) throws Exception {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (input.isDirectory())
			throw new IOException("Input file " + input + " is a directory!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		// create output file and check whether it already exists.
		File outputFile = new File(output, input.getName() + "_thumb.swf");

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		
		
		
		// extract frames from input video
		System.out.println(allPictures.size());
		getImagesFromVideo(input);
		System.out.println(allPictures.size());		
		
		// create a video writer
		 frameRate = IRational.make(3, 1);

		 toolkit = Toolkit.getDefaultToolkit();
		 
		    outContainer = IContainer.make();
		    

		    String outFile = outputFile.getAbsolutePath();
		    
		    int retval = outContainer.open(outFile, IContainer.Type.WRITE, null);
		    if (retval < 0)
		      throw new RuntimeException("could not open output file");

		    ICodec codec = ICodec.guessEncodingCodec(null, null, outFile, null,
		        ICodec.Type.CODEC_TYPE_VIDEO);
		    if (codec == null)
		      throw new RuntimeException("could not guess a codec");

		    outStream = outContainer.addNewStream(codec);
		    outStreamCoder = outStream.getStreamCoder();

		    outStreamCoder.setNumPicturesInGroupOfPictures(30);
		    outStreamCoder.setCodec(codec);

		    outStreamCoder.setBitRate(25000);
		    outStreamCoder.setBitRateTolerance(9000);

		    //
		    int width = toolkit.getScreenSize().width;
		    int height = toolkit.getScreenSize().height;

		    outStreamCoder.setPixelType(IPixelFormat.Type.YUV420P);
		    
		    vHeight = allPictures.get(0).getHeight();
		    vWidth = allPictures.get(0).getWidth();
		    
		    outStreamCoder.setHeight(vHeight);
		    outStreamCoder.setWidth(vWidth);
		    outStreamCoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, true);
		    outStreamCoder.setGlobalQuality(0);

		    outStreamCoder.setFrameRate(frameRate);
		    outStreamCoder.setTimeBase(IRational.make(frameRate.getDenominator(),
		        frameRate.getNumerator()));

		    retval = outStreamCoder.open(null, null);
		    if (retval < 0)
		      throw new RuntimeException("could not open input decoder");
		    retval = outContainer.writeHeader();
		    if (retval < 0)
		      throw new RuntimeException("could not write file header");
		    
		    
		    ////////////////////////////////////////////////////////////////
		    
		
		    
		   
		        IPacket packet = IPacket.make();

		        IConverter converter = null;
		        
		        long now = System.currentTimeMillis();
		        
		        BufferedImage worksWithXugglerBufferedImage = convertToType(allPictures_Buff.get(0),
	 		            BufferedImage.TYPE_3BYTE_BGR);
		        
		        try
		        {
		          converter = ConverterFactory.createConverter(
		              worksWithXugglerBufferedImage, IPixelFormat.Type.YUV420P);
		        }
		        catch (UnsupportedOperationException e)
		        {
		          System.out.println(e.getMessage());
		          e.printStackTrace(System.out);
		        }

		        long timeStamp;
		        
		        ArrayList<IVideoPicture> convertedPictures = new ArrayList<IVideoPicture>();
		        
		        for(int i = 0; i < allPictures_Buff.size(); i++){
		        	
		        	  worksWithXugglerBufferedImage = convertToType(allPictures_Buff.get(i),
		 		            BufferedImage.TYPE_3BYTE_BGR);
		        	 
		        	 timeStamp = i + 10000;
		        	
		        	 
		        	convertedPictures.add(converter.toPicture(worksWithXugglerBufferedImage,
		            timeStamp));
		        	
		        	convertedPictures.get(i).setQuality(0);
		        	
		        }
		        	
		        /*IVideoPicture outFrame = converter.toPicture(worksWithXugglerBufferedImage,
		            timeStamp);*/
		        
		        IMediaWriter videoWriter = ToolFactory.makeWriter(outFile);
		        
		        videoWriter.addVideoStream(0, 0, codec, vWidth,vHeight);
		        
		        long startTime = System.nanoTime(); 
		        
		        for(int i = 0; i < allPictures_Buff.size(); i = i + 2)
		        {
		        	
		        	System.out.println("Bin hier");
		          BufferedImage image = allPictures_Buff.get(i);
		          videoWriter.encodeVideo(0, image,
		           System.nanoTime()-startTime, TimeUnit.NANOSECONDS);
		          Thread.sleep(10);
		        }
		        videoWriter.close();
		        
		        
		        
		        /*for(int i = 0; i < convertedPictures.size(); i++){
			        
		        	
			        retval = outStreamCoder.encodeVideo(packet, convertedPictures.get(i), 0);
			        if (retval < 0)
			          throw new RuntimeException("could not encode video");
			        if (packet.isComplete())
			        {
			          retval = outContainer.writePacket(packet);
			          if (retval < 0)
			            throw new RuntimeException("could not save packet to container");
			        }
			        
		        }*/
		        
		        
		        
		    
		    
		 /////////////////////////////////////////////////////////////////////////////
		
		// add a stream with the proper width, height and frame rate
		
		// if timespan is set to zero, compare the frames to use and add 
		// only frames with significant changes to the final video

		// loop: get the frame image, encode the image to the video stream
		
		// Close the writer

		return outputFile;
	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		/*if (args.length < 3) {
            System.out.println("usage: java itm.video.VideoThumbnailGenerator <input-video> <output-directory> <timespan>");
            System.out.println("usage: java itm.video.VideoThumbnailGenerator <input-directory> <output-directory> <timespan>");
            System.exit(1);
        }
        File fi = new File(args[0]);
        File fo = new File(args[1]);
        */
		File fi = new File("C:\\Users\\Gert\\workspace\\assignment2\\media\\video\\panda.avi");
		File fo = new File("C:\\Users\\Gert\\workspace\\assignment2\\media\\video\\");

        
        int timespan = 5;
        if(args.length == 3)
            timespan = Integer.parseInt(args[2]);
        
        VideoThumbnailGenerator videoMd = new VideoThumbnailGenerator();
        videoMd.batchProcessVideoFiles(fi, fo, true, timespan);
	}
}