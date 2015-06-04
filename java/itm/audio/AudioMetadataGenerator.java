package itm.audio;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import itm.model.AudioMedia;
import itm.model.MediaFactory;
import itm.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import sun.net.www.content.audio.wav;
import javazoom.jl.converter.WaveFile;

/**
 * This class reads audio files of various formats and stores some basic audio
 * metadata to text files. It can be called with 3 parameters, an input
 * filename/directory, an output directory and an "overwrite" flag. It will read
 * the input audio file(s), retrieve some metadata and write it to a text file
 * in the output directory. The overwrite flag indicates whether the resulting
 * output file should be overwritten or not.
 * 
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */
public class AudioMetadataGenerator {

	/**
	 * Constructor.
	 */
	public AudioMetadataGenerator() {
	}

	/**
	 * Processes an audio file directory in a batch process.
	 * 
	 * @param input
	 *            a reference to the audio file directory
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing metadata files should be
	 *            overwritten or not
	 * @return a list of the created media objects (images)
	 */
	public ArrayList<AudioMedia> batchProcessAudio(File input, File output,
			boolean overwrite) throws IOException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		ArrayList<AudioMedia> ret = new ArrayList<AudioMedia>();

		if (input.isDirectory()) {
			File[] files = input.listFiles();
			for (File f : files) {

				String ext = f.getName().substring(
						f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("wav") || ext.equals("mp3") || ext.equals("ogg")) {
					try {
						AudioMedia result = processAudio(f, output, overwrite);
						System.out.println("created metadata for file " + f
								+ " in " + output);
						ret.add(result);
					} catch (Exception e0) {
						System.err
								.println("Error when creating metadata from file "
										+ f + " : " + e0.toString());					//input mit "f" überschrieben!!! Zeigt sonst nicht genauen Dateinamen an (fürs debuggen)
					}

				}

			}
		} else {

			String ext = input.getName().substring(
					input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("wav") || ext.equals("mp3") || ext.equals("ogg")) {
				try {
					AudioMedia result = processAudio(input, output, overwrite);
					System.out.println("created metadata for file " + input
							+ " in " + output);
					ret.add(result);
				} catch (Exception e0) {
					System.err
							.println("Error when creating metadata from file "
									+ input + " : " + e0.toString());
				}

			}

		}
		return ret;
	}

	/**
	 * Processes the passed input audio file and stores the extracted metadata
	 * to a textfile in the output directory.
	 * 
	 * @param input
	 *            a reference to the input audio file
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing metadata files should be
	 *            overwritten or not
	 * @return the created image media object
	 */
	protected AudioMedia processAudio(File input, File output, boolean overwrite)
			throws IOException, IllegalArgumentException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (input.isDirectory())
			throw new IOException("Input file " + input + " is a directory!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		// create outputfilename and check whether thumb already exists. All
		// image metadata files have to start with "aud_" - this is used by the
		// mediafactory!
		File outputFile = new File(output, "aud_" + input.getName() + ".txt");
		if (outputFile.exists())
			if (!overwrite) {
				// load from file
				AudioMedia media = new AudioMedia();
				media.readFromFile(outputFile);
				return media;
			}

		
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// create an audio metadata object
		AudioMedia media = (AudioMedia) MediaFactory.createMedia(input);		

		// load the input audio file, do not decode
		AudioInputStream in = null;
					
		try {
			in = AudioSystem.getAudioInputStream(input);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
						
		// read AudioFormat properties
		AudioFormat eingangsFormat = in.getFormat();
		
		Map<String, Object> formatProps = eingangsFormat.properties();		//Map aus Audioformat-Eigenschaften

		media.setEncoding(eingangsFormat.getEncoding().toString());		//Diese Infos sollten in allen behandelten Audiofiles enthalten sein
		media.setFrequency((int) eingangsFormat.getSampleRate());
		media.setChannels(eingangsFormat.getChannels());
								
		for (Map.Entry<String, Object> entry : formatProps.entrySet()) {	//Über die Keys ("vbr" und "bitrate") wird iteriert
			if (entry.getKey().equalsIgnoreCase("bitrate"))					//bitrate-Key wird in AudioMedia gesetzt
				media.setBitrate((int) entry.getValue());
		}
		
		AudioFileFormat audioFileFormat = null;
		
		try {
			audioFileFormat = AudioSystem.getAudioFileFormat(input);
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Map<String, Object> audioFileFormatProps = null;
			
			try {
				audioFileFormatProps = AudioSystem.getAudioFileFormat(input).properties();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
		
		// you might have to distinguish what properties are available for what audio format		
		if (audioFileFormat.getType().toString().equals("WAVE")) {											//falls File = WAV 
			
			long frames = audioFileFormat.getFrameLength();													//Laenge berechnen und in MicroSec fuer AudioMedia-Klasse bringen
			double durationInMicroSeconds = (frames / eingangsFormat.getFrameRate()) * 1000000;
			media.setDuration((long) durationInMicroSeconds);
		}
			
		for (Map.Entry<String, Object> entry : audioFileFormatProps.entrySet()) {
				
			if (entry.getKey().equals("duration")) 
				media.setDuration((Long) entry.getValue());

			if (entry.getKey().equals("author"))
				media.setAuthor((String) entry.getValue());

			if (entry.getKey().equals("title"))
				media.setTitle((String) entry.getValue());

			if (entry.getKey().equals("date"))
				media.setDate((String) entry.getValue());

			if (entry.getKey().equals("comment"))
				media.setComment((String) entry.getValue());

			if (entry.getKey().equals("album"))
				media.setAlbum((String) entry.getValue());

			if (entry.getKey().equals("track") || entry.getKey().equals("mp3.id3tag.track") || entry.getKey().equals("ogg.comment.track"))
				media.setTrack((String) entry.getValue());
			
			if (entry.getKey().equals("composer") || entry.getKey().equals("mp3.id3tag.composer") || entry.getKey().equals("ogg.comment.composer"))
				media.setComposer((String) entry.getValue());

			if (entry.getKey().equals("genre") || entry.getKey().equals("mp3.id3tag.genre") || entry.getKey().equals("ogg.comment.genre"))
				media.setGenre((String) entry.getValue());
		}

		// add a "audio" tag
		media.addTag("audio");

		// close the audio and write the md file.
		in.close();
		IOUtil.writeFile(media.serializeObject(), outputFile);

		return media;
	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		//args = new String[] { "./media/audio", "./media/md" };
		
		if (args.length < 2) {
			System.out
					.println("usage: java itm.image.AudioMetadataGenerator <input-image> <output-directory>");
			System.out
					.println("usage: java itm.image.AudioMetadataGenerator <input-directory> <output-directory>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		File fo = new File(args[1]);
		AudioMetadataGenerator audioMd = new AudioMetadataGenerator();
		audioMd.batchProcessAudio(fi, fo, true);
	}
}
