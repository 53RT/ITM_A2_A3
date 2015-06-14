package itm.model;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class VideoMedia extends AbstractMedia {

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	/* video format metadata */
	//Codec 
	protected String videoCodecName;
	//CodecID
	protected String videoCodecID;
	//VideoFramerate
	protected String videoFrameRate;
	//VideoLength (Sec)
	protected double videoLenght;
	//VideoHeight
	protected int videoHeight;
	//VideoWidth
	protected int videoWidth;
	/* audio format metadata */
	//Codec
	protected String audioCodecName;
	//CodecID
	protected String audioCodecID;
	//Number of Channels
	protected int audioNumChannels;
	//SampleRate (Hz)
	protected int audioSampleRate;
	//BitRate (kb/s)
	protected double audioBitRate;
	
	/**
	 * Constructor.
	 */
	public VideoMedia() {
		super();
	}

	/**
	 * Constructor.
	 */
	public VideoMedia(File instance) {
		super(instance);
	}

	/* GET / SET methods */
	public String getVideoCodecName() {
		return videoCodecName;
	}

	public void setVideoCodecName(String videoCodecName) {
		this.videoCodecName = videoCodecName;
	}

	public String getVideoCodecID() {
		return videoCodecID;
	}

	public void setVideoCodecID(String videoCodecID) {
		this.videoCodecID = videoCodecID;
	}

	public String getVideoFrameRate() {
		return videoFrameRate;
	}

	public void setVideoFrameRate(String videoFrameRate) {
		this.videoFrameRate = videoFrameRate;
	}

	public double getVideoLenght() {
		return videoLenght;
	}

	public void setVideoLenght(double videoLenght) {
		this.videoLenght = videoLenght;
	}

	public int getVideoHeight() {
		return videoHeight;
	}

	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}

	public int getVideoWidth() {
		return videoWidth;
	}

	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}

	public String getAudioCodecName() {
		return audioCodecName;
	}

	public void setAudioCodecName(String audioCodecName) {
		this.audioCodecName = audioCodecName;
	}

	public String getAudioCodecID() {
		return audioCodecID;
	}

	public void setAudioCodecID(String audioCodecID) {
		this.audioCodecID = audioCodecID;
	}

	public int getAudioNumChannels() {
		return audioNumChannels;
	}

	public void setAudioNumChannels(int audioNumChannels) {
		this.audioNumChannels = audioNumChannels;
	}

	public int getAudioSampleRate() {
		return audioSampleRate;
	}

	public void setAudioSampleRate(int audioSampleRate) {
		this.audioSampleRate = audioSampleRate;
	}

	public double getAudioBitRate() {
		return audioBitRate;
	}

	public void setAudioBitRate(double audioBitRate) {
		this.audioBitRate = audioBitRate;
	}

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	/* (de-)serialization */

	/**
	 * Serializes this object to the passed file.
	 * 
	 */
	@Override
	public StringBuffer serializeObject() throws IOException {
		StringWriter data = new StringWriter();
		PrintWriter out = new PrintWriter(data);
		out.println("type: video");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		/* video fields */

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************
		 out.println( "Video fields: ");
		 out.println( "videoCodec: " + getVideoCodecName() );
		 out.println( "videoCodecID: " + getVideoCodecID() );
		 out.println( "videoFrameRate: " + getVideoFrameRate() );
		 out.println( "videoLength : " + getVideoLenght() );
		 out.println( "videoHeight : " + getVideoHeight() );
		 out.println( "videoWidth : " + getVideoWidth() );
		 
		 out.println( "Audio fields: ");
		 out.println( "audioCodec : " + getAudioCodecName() );
		 out.println( "audioCodecID: " + getAudioCodecID() );
		 out.println( "audioChannels : " + getAudioNumChannels() );
		 out.println( "audioSampleRate : " + getAudioSampleRate() );
		 out.println( "audioBitRate : " + getAudioBitRate() );
		 
		return data.getBuffer();
	}


	/**
	 * Deserializes this object from the passed string buffer.
	 */
	@Override
	public void deserializeObject(String data) throws IOException {
		super.deserializeObject(data);

		StringReader sr = new StringReader(data);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		while ((line = br.readLine()) != null) {

			/* video fields */
			// ***************************************************************
			// Fill in your code here!
			// ***************************************************************
			if ( line.startsWith( "videoCodec: " ) ) {
        		setVideoCodecName(line.substring("videoCodec: ".length()));
        	} else
        	if ( line.startsWith( "videoCodecID: " ) ) {
           		setVideoCodecID(line.substring("videoCodecID: ".length()));
            } else
            if ( line.startsWith( "videoFrameRate: " ) ) {
            	setVideoFrameRate(line.substring("videoFrameRate: ".length()));
            }else
            if ( line.startsWith( "videoLength: " ) ) {
            	setVideoLenght(Double.parseDouble(line.substring("videoLentghL: ".length())));
            } else
            if ( line.startsWith( "videoHeight: " ) ) {
                setVideoHeight(Integer.parseInt(line.substring( "videoHeight: ".length() )));
            } else
            if ( line.startsWith( "videoWidth: " ) ) {
                setVideoWidth(Integer.parseInt(line.substring( "videoWidth: ".length() )));
            } else
            if ( line.startsWith( "audioCodec: " ) ) {
            	setAudioCodecName(line.substring("audioCodec: ".length()));
            } else
            if ( line.startsWith( "audioCodecID: " ) ) {
               	setAudioCodecID(line.substring("audioCodecID: ".length()));
            } else
            if ( line.startsWith( "audioChannels: " ) ) {
                setAudioNumChannels(Integer.parseInt(line.substring( "audioChannels: ".length() )));
            }else
            if ( line.startsWith( "audioSampleRate: " ) ) {
                setAudioSampleRate(Integer.parseInt(line.substring( "audioSampleRate: ".length() )));
            } else
            if ( line.startsWith( "audioBitRate: " ) ) {
                setAudioBitRate(Double.parseDouble(line.substring( "audioBitRate: ".length() )));
            }	
		}
	
	}
}
