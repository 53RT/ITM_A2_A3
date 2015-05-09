package itm.audio;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.media.*;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import sun.audio.AudioStream;

/**
 * Plays an audio file using the system's default sound output device
 * 
 */
public class AudioPlayer {

	/**
	 * Constructor
	 */
	public AudioPlayer() {

	}

	/**
	 * Plays audio data from a given input file to the system's default sound
	 * output device
	 * 
	 * @param input
	 *            the audio file
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	protected void playAudio(File input) throws IOException {

		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");

		AudioInputStream audio = null;
		try {
			audio = openAudioInputStream(input);
		} catch (UnsupportedAudioFileException e) {
			throw new IOException("could not open audio file " + input
					+ ". Encoding / file format not supported");
		}

		try {
			rawplay(audio);
		} catch (LineUnavailableException e) {
			throw new IOException("Error when playing sound from file "
					+ input.getName() + ". Sound output device unavailable");
		}

		audio.close();

	}

	/**
	 * Decodes an encoded audio file and returns a PCM input stream
	 * 
	 * Supported encodings: MP3, OGG (requires SPIs to be in the classpath)
	 * 
	 * @param input
	 *            a reference to the input audio file
	 * @return a PCM AudioInputStream
	 * @throws UnsupportedAudioFileException
	 *             an audio file's encoding is not supported
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	private AudioInputStream openAudioInputStream(File input)
			throws UnsupportedAudioFileException, IOException {

		AudioInputStream din = null;
		
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// open audio stream
		
		AudioInputStream in = AudioSystem.getAudioInputStream(input);		
		
		// get format
		
		AudioFormat audioFormat = in.getFormat();	
		
        String filename = input.getName();	        						//liest den aktuellen Dateinamen aus 
        filename = filename.toLowerCase();									//ev. Grossschreibung raus
		String format = filename.substring(filename.length() - 3);			//die letzten drei Buchstaben des Dateinamens -> Dateiendung
        		
		// get decoded format
        
    	AudioFormat decFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 	//PCM-Format erstellen
                audioFormat.getSampleRate(),
                16,
                audioFormat.getChannels(),
                audioFormat.getChannels() * 2,
                audioFormat.getSampleRate(),
                false);

		
        if (format.equals("wav")){
        	System.out.println("wav!");
        }
        
        if (format.equals("mp3")){
        	System.out.println("mp3!");
        }
        
        if (format.equals("ogg")){
        	System.out.println("ogg!");

        }
        if (!format.equals("mp3") && (!format.equals("wav") && (!format.equals("ogg"))))
        		System.out.println("Format nicht unterstützt!");

		// get decoded audio input stream     
        
        din = AudioSystem.getAudioInputStream(decFormat, in);
 
		return din;
	}

	/**
	 * Writes audio data from an AudioInputStream to a SourceDataline
	 * 
	 * @param audio
	 *            the audio data
	 * @throws IOException
	 *             error when writing audio data to source data line
	 * @throws LineUnavailableException
	 *             system's default source data line is not available
	 */
	private void rawplay(AudioInputStream audio) throws IOException,
			LineUnavailableException {

		
		
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// get audio format
		
		AudioFormat audioFormat = audio.getFormat();
		
		// get a source data line
		
		 SourceDataLine sDataLine = null;	
         DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
		 sDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		 sDataLine.open(audio.getFormat());
		
		// read samples from audio and write them to the data line 

		 sDataLine.start();
		 
		 int buffer = 4096;
		 byte[] bytesBuffer = new byte[buffer];
		 int bytesRead = -1;
		  
		 while ((bytesRead = audio.read(bytesBuffer)) != -1) {
		     sDataLine.write(bytesBuffer, 0, bytesRead);
		 }
		 
		// properly close the line!
         
		 sDataLine.drain();
         sDataLine.stop();
		 sDataLine.close();
			 
	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.out
					.println("usage: java itm.audio.AudioPlayer <input-audioFile>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		AudioPlayer player = new AudioPlayer();
		player.playAudio(fi);
		System.exit(0);

	}

}
