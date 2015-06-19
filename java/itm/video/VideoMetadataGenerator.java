package itm.video;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import itm.model.MediaFactory;
import itm.model.VideoMedia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoResampler;

//Test

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
public class VideoMetadataGenerator {

	/**
	 * Constructor.
	 */
	public VideoMetadataGenerator() {
	}

	/**
	 * Processes a video file directory in a batch process.
	 * 
	 * @param input
	 *            a reference to the video file directory
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing metadata files should be
	 *            overwritten or not
	 * @return a list of the created media objects (videos)
	 */
	public ArrayList<VideoMedia> batchProcessVideoFiles(File input, File output, boolean overwrite) throws IOException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		ArrayList<VideoMedia> ret = new ArrayList<VideoMedia>();

		if (input.isDirectory()) {
			File[] files = input.listFiles();
			for (File f : files) {

				String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv")
						|| ext.equals("mp4"))
					try {
						VideoMedia result = processVideo(f, output, overwrite);
						System.out.println("created metadata for file " + f + " in " + output);
						ret.add(result);
					} catch (Exception e0) {
						System.err.println("Error when creating metadata from file " + input + " : " + e0.toString());
					}

			}
		} else {

			String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4"))
				try {
					VideoMedia result = processVideo(input, output, overwrite);
					System.out.println("created metadata for file " + input + " in " + output);
					ret.add(result);
				} catch (Exception e0) {
					System.err.println("Error when creating metadata from file " + input + " : " + e0.toString());
				}

		}
		return ret;
	}

	/**
	 * Processes the passed input video file and stores the extracted metadata
	 * to a textfile in the output directory.
	 * 
	 * @param input
	 *            a reference to the input video file
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing metadata files should be
	 *            overwritten or not
	 * @return the created video media object
	 */
	@SuppressWarnings("deprecation")
	protected VideoMedia processVideo(File input, File output, boolean overwrite) throws Exception {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (input.isDirectory())
			throw new IOException("Input file " + input + " is a directory!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		// create outputfilename and check whether thumb already exists.
		// all video metadata files have to start with "vid_" - this is used by
		// the
		// mediafactory!
		File outputFile = new File(output, "vid_" + input.getName() + ".txt");
		if (outputFile.exists())
			if (!overwrite) {
				// load from file
				VideoMedia media = new VideoMedia();
				media.readFromFile(outputFile);
				return media;
			}

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************
		
		
		// create video media object
		VideoMedia media = (VideoMedia) MediaFactory.createMedia(input);

		//VideoStream laden und VideoStream finden
		IContainer container = IContainer.make();
		
		String filepath = input.getAbsolutePath();
		
		if (container.open(filepath, IContainer.Type.READ, null) < 0)
		      throw new IllegalArgumentException("Datei konnte nicht geoeffnet werden " + filepath);
		
		int numStreams = container.getNumStreams();
		
		int videoStreamId = -1;
		int audioStreamId = -1;
	    IStreamCoder videoCoder = null;
	    IStreamCoder audioCoder = null;
	    for(int i = 0; i < numStreams; i++)
	    {
	      // find the stream object

	      IStream stream = container.getStream(i);

	      // get the pre-configured decoder that can decode this stream;

	      IStreamCoder coder = stream.getStreamCoder();

	      if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO){
	        videoStreamId = i;
	        videoCoder = coder;
	        if(videoStreamId != -1 && audioStreamId != -1)
	        	break;
	      }
	      if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO){
		        audioStreamId = i;
		        audioCoder = coder;
		        if(videoStreamId != -1 && audioStreamId != -1)
		        	break;
		      }
	    }
		

	    if (videoStreamId == -1)
	      throw new RuntimeException("Es konnte kein Videostream in der Datei gefunden werden: "+ filepath);
	    

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
	    }

	    String dateiname = null;
        dateiname = input.getName();					//Dateiname auslesen
        String dateiendung = "";
        int i = dateiname.lastIndexOf('.');				//String wird ab dem "." ausgelesen
        if (i > 0) {
            dateiendung = dateiname.substring(i+1);
        }
        dateiendung = dateiendung.toLowerCase();		//Umwandlung in lowercase (kein "BMP" etc.)
        
        media.addTag(dateiendung);
	    
		// set video and audio stream metadata 
	      //Video
	      media.setVideoCodecName(videoCoder.getCodec().getLongName());
	      media.setVideoCodecID(videoCoder.getCodecID().toString());
	      media.setVideoFrameRate(videoCoder.getFrameRate().toString());
	      media.setVideoLenght(Math.abs((double)container.getDuration()/1000000.00));
	      media.setVideoHeight(videoCoder.getHeight());
	      media.setVideoWidth(videoCoder.getWidth());
	      
	      //Audio
	      if (audioStreamId != -1){
			    
	      media.setAudioCodecName(audioCoder.getCodec().getLongName());
	      media.setAudioCodecID(audioCoder.getCodecID().toString());
	      media.setAudioBitRate(audioCoder.getBitRate());
	      media.setAudioNumChannels(audioCoder.getChannels());
	      media.setAudioSampleRate(audioCoder.getSampleRate());
	
	      }
	      // add video tag
	      media.addTag("video");
	      
	     
	      if(videoCoder != null){
	    	  videoCoder.close();
	      }
	      if(audioCoder != null){
	    	  audioCoder.close();
	      }
	      if(container != null){
	    	  container.close();
	      }
	   
		// write metadata

	        FileWriter writer = new FileWriter(outputFile ,false);
	        
	        try {
	       
	        	writer.write(media.toString());
	        	//writer.write(System.getProperty("line.separator"));

	            writer.flush();
	            writer.close();
	         } catch (IOException e) {
	           e.printStackTrace();
	         }

		return media;
	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {


		if (args.length < 2) {
			System.out.println("usage: java itm.video.VideoMetadataGenerator <input-video> <output-directory>");
			System.out.println("usage: java itm.video.VideoMetadataGenerator <input-directory> <output-directory>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		File fo = new File(args[1]);
		
		
		VideoMetadataGenerator videoMd = new VideoMetadataGenerator();
		videoMd.batchProcessVideoFiles(fi, fo, true);
	}
}
