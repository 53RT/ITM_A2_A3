package itm.video;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.sun.prism.Image;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;

/**
 * 
 * This class creates JPEG thumbnails from from video frames grabbed from the
 * middle of a video stream It can be called with 2 parameters, an input
 * filename/directory and an output directory.
 * 
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */

public class VideoFrameGrabber {

	ArrayList<BufferedImage> allPictures = new ArrayList<BufferedImage>();
	/**
	 * Constructor.
	 */
	public VideoFrameGrabber() {
	}

	/**
	 * Processes the passed input video file / video file directory and stores
	 * the processed files in the output directory.
	 * 
	 * @param input
	 *            a reference to the input video file / input directory
	 * @param output
	 *            a reference to the output directory
	 */
	public ArrayList<File> batchProcessVideoFiles(File input, File output) throws IOException {
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
				if (f.isDirectory())
					continue;

				String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv")
						|| ext.equals("mp4")) {
					File result = processVideo(f, output);
					System.out.println("converted " + f + " to " + result);
					ret.add(result);
				}

			}

		} else {
			String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4")) {
				File result = processVideo(input, output);
				System.out.println("converted " + input + " to " + result);
				ret.add(result);
			}
		}
		return ret;
	}

	/**
	 * Processes the passed audio file and stores the processed file to the
	 * output directory.
	 * 
	 * @param input
	 *            a reference to the input audio File
	 * @param output
	 *            a reference to the output directory
	 */
	@SuppressWarnings("deprecation")
	protected File processVideo(File input, File output) throws IOException, IllegalArgumentException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (input.isDirectory())
			throw new IOException("Input file " + input + " is a directory!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		File outputFile = new File(output, input.getName() + "_thumb.jpg");
		

		//The following code segments are taken from the Xuggler Demo (DecodeAndCapureFrames) - Copyright (c) 2008, 2010 Xuggle Inc.  All rights reserved. 
		//I think i cant do it better and i want to mark it as not my complete own work.
		
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
	      throw new RuntimeException("Es konnte kein Videostream in der Datei gefunden werden: "+ filepath);

	    // Now we have found the video stream in this file.  Let's open up
	    // our decoder so it can do work

	    if (videoCoder.open() < 0)
	      throw new RuntimeException(
	        "Die Datei konnte nicht decodiert werden: " + filepath);

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
	          "Nicht kompatibles Pixeltyp im Videostream gefunden: " + filepath);
	      
	      
	      System.out.println("Codecname: " + videoCoder.getCodec().getLongName());
	      System.out.println(videoCoder.getCodecID());

	      System.out.println(videoCoder.getFrameRate());

	      System.out.println(videoCoder.getHeight());
	      
	      System.out.println(videoCoder.getWidth());	      
	      System.out.println((double)container.getDuration()/1000000.00);
	      
	    }

	    // Now, we start walking through the container looking at each packet.

	    IPacket packet = IPacket.make();
	    while(container.readNextPacket(packet) >= 0)
	    {
	      
	      // Now we have a packet, let's see if it belongs to our video strea

	      if (packet.getStreamIndex() == videoStreamId)
	      {
	        // We allocate a new picture to get the data out of Xuggle

	        IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),videoCoder.getWidth(), videoCoder.getHeight());

	        int offset = 0;
	        while(offset < packet.getSize())
	        {
	          // Now, we decode the video, checking for any errors.

	          int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
	          if (bytesDecoded < 0)
	            throw new RuntimeException("Fehler beim decodieren: " + filepath);
	          offset += bytesDecoded;
	          
	          // Some decoders will consume data in a packet, but will not
	          // be able to construct a full video picture yet.  Therefore
	          // you should always check if you got a complete picture from
	          // the decode.

	          if (picture.isComplete())
	          {
	            IVideoPicture newPic = picture;
	            
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
	            
	            BufferedImage javaImage = Utils.videoPictureToImage(newPic);

	            // process the video frame

	            allPictures.add(javaImage);
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
	 
	 //Das mittlere Bild aller gespeicherten, vollständigen Bilder als auch das komplette Bild an mittlerer Zeitlicher Position lieferten das gleiche Ergebnis.
	 //Aus diesem Grund wird für die Auswahl die weniger aufwändigere Methode genutzt und das mittlerer Bild aller Bilder genutzt da von einer gleichmäßigen
	 //Verteilung der kompletten Bilder im Stream ausgegangen wird sollte diese Methode auch immer zuverlässig funktionieren.
	    
	 BufferedImage middlePicture = allPictures.get(allPictures.size()/2);
	 ImageIO.write(middlePicture, "JPEG" , outputFile);
	    
		return outputFile;

	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		// args = new String[] { "./media/video", "./test" };

		if (args.length < 2) {
			System.out.println("usage: java itm.video.VideoFrameGrabber <input-videoFile> <output-directory>");
			System.out.println("usage: java itm.video.VideoFrameGrabber <input-directory> <output-directory>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		File fo = new File(args[1]);

		/*Nur zu Testzwecken
		File fi = new File("C:\\Users\\Gert\\workspace\\assignment2\\media\\video\\DREIZEHN.AVI");
		File fo = new File("C:\\Users\\Gert\\workspace\\assignment2\\media\\video\\");
		*/
		
		VideoFrameGrabber grabber = new VideoFrameGrabber();
		grabber.batchProcessVideoFiles(fi, fo);
	}

}
