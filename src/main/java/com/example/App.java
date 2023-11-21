/*
 * Template from c2017-2023 Courtney Brown modified by Maggie Nguyen 
 * Name: Maggie Nguyen 
 * Date: November 15th, 2023 
 * Class: App.java, Final Project 
 * Description: This is the Project 1 template for the Probability Generator, has been modified and completed 
 */

package com.example;

//importing the JMusic stuff
import jm.music.data.*;
import jm.util.*;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner; 

//make sure this class name matches your file name, if not fix.
public class App {

	static MelodyPlayer player; // play a midi sequence
	static MidiFileToNotes midiNotes; // read a midi file
	static int noteCount = 0;

	//make cross-platform
	static FileSystem sys = FileSystems.getDefault();

	//the getSeperator() creates the appropriate back or forward slash based on the OS in which it is running -- OS X & Windows use same code :) 
	//static String filePath = "mid"  + sys.getSeparator() +  "MaryHadALittleLamb.mid"; //MIDI FILE TO TEST ALGORITHM STILL WORKS 
    static String filePath = "mid"  + sys.getSeparator() +  "Do_Re_Mi_.mid"; //MIDI FILE FOR FINAL PROJECT 

	private static ArrayList<Integer> generatedPitches;
    private static ProbabilityGenerator<Double> rhythmGen;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// run the unit tests here
		int whichTest = Integer.parseInt(args[0]); //gets the command-line input
												   //if 0, run the train unit test, if 1 run the generate unit test

		// setup the melody player
		// uncomment below when you are ready to test or present sound output
		// make sure that it is commented out for your final submit to github (eg. when
		// pushing)
		//Uncommented from following instructions video 
		setup(); //calling function 
		testAndTrainProbGen(); //calling function, prints out Probability Distribution Data Table 

		// uncomment to debug your midi file
		// this code MUST be commited when submitting unit tests or any code to github
		// playMidiFileDebugTest(filePath);


        Scanner scanner = new Scanner(System.in);

        while (true) {
			System.out.println();
			System.out.println("------------NUMBERED OPTIONS------------");
			System.out.println();
			System.out.println("1   TO PLAY ORIGINAL MIDI");
			System.out.println();
			System.out.println("2   TO PLAY GENERATED NOTES");
			System.out.println();
			System.out.println("3   TO PLAY REVERSED GENERATED NOTES");
         	System.out.println();
			System.out.println("4   TO PLAY ORIGINAL MIDI QUICKER");
			System.out.println();
			System.out.println("5   TO PLAY ORIGINAL MIDI SLOWER");
			System.out.println();
			System.out.println("6   TO PLAY ORIGINAL MIDI ONE OCTAVE HIGHER");
			System.out.println();
			System.out.println("7   TO PLAY ORIGINAL MIDI ONE OCTAVE LOWER");
			System.out.println();
			System.out.println("0   TO END");
			System.out.println();
			System.out.print("------------ENTER NUMBER HERE------------ ");
			System.out.println();

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    playOriginalMidi();
                    break;
                case 2:
                	playGeneratedNotes();
                    break;
				case 3:
                	playReversedGeneratedNotes();
                    break;
                case 4:
                    playOriginalMidiQuicker();
                    break;
                case 5:
                    playOriginalMidiSlower();
                    break;
                case 6:
                    playOriginalMidiOneOctaveHigher();
                    break;
                case 7:
                    playOriginalMidiOneOctaveLower();
                    break;
                case 0:
                    System.out.println("------------PROGRAM COMPLETED------------");
					System.out.println();
                    System.exit(0);
            }
        }
    }
	

//1
private static void playOriginalMidi() {
	player.setMelody(midiNotes.getPitchArray());
    player.setRhythm(midiNotes.getRhythmArray());
	System.out.println("----------Original Midi Playing----------");
	playMelody();
    player.reset(); // Reset player for the generated melody
}

//2
private static void playGeneratedNotes() {
	ProbabilityGenerator<Integer> pitchGen = new ProbabilityGenerator<Integer>();
    ProbabilityGenerator<Double> rhythmGen = new ProbabilityGenerator<Double>();

    pitchGen.train(midiNotes.getPitchArray());
    rhythmGen.train(midiNotes.getRhythmArray());

    for (int i = 0; i < 1000000; i++) {
        ArrayList<Integer> pitches = pitchGen.generate(20);
        ArrayList<Double> rhythms = rhythmGen.generate(20);
        pitchGen.train(pitches);
        rhythmGen.train(rhythms);
    }

    ArrayList<Integer> generatedPitches = pitchGen.generate(40);
    player.setMelody(generatedPitches);
    player.setRhythm(rhythmGen.generate(40));
	System.out.println("-------Probability Genereated Notes-------");
	playMelody();
    player.reset(); // Reset player for the reversed generated melody
}


//3
	private static void playReversedGeneratedNotes() {
		ProbabilityGenerator<Integer> pitchGen = new ProbabilityGenerator<Integer>(); // pitch instance
		ProbabilityGenerator<Double> rhythmGen = new ProbabilityGenerator<Double>(); // rhythm instance
	
		pitchGen.train(midiNotes.getPitchArray()); // trains
		rhythmGen.train(midiNotes.getRhythmArray()); // trains
	
		ProbabilityGenerator<Integer> pitchGen2 = new ProbabilityGenerator<Integer>(); // reverse pitch instance
		ProbabilityGenerator<Double> rhythmGen2 = new ProbabilityGenerator<Double>(); // reverse rhythm instance
	
		// For Original Midi
		for (int i = 0; i < 1000000; i++) { // loops 1000000 iterations
			ArrayList<Integer> pitches = pitchGen.generate(20); // 20 pitches generated
			ArrayList<Double> rhythms = rhythmGen.generate(20); // 20 rhythms generated
			pitchGen2.train(pitches); // trains
			rhythmGen2.train(rhythms); // trains
		}
		player.reset(); // Resets
	
		// For Reversed Midi
		for (int i = 0; i < 1000000; i++) { // loops 1000000 iterations
			ArrayList<Integer> reversedPitches = pitchGen2.generate(40); // 40 reversed pitches
			ArrayList<Double> reversedRhythms = rhythmGen2.generate(40); // 40 reversed rhythms
			pitchGen.train(reversedPitches); // train
			rhythmGen.train(reversedRhythms); // train
		}
	
		player.setMelody(pitchGen2.generate(40));
		player.setRhythm(rhythmGen2.generate(40));
		System.out.println("---------Reversed Generated Notes---------");
		playMelody(); // plays reversed
		player.reset(); // Resets
 }


//4
private static void playOriginalMidiQuicker() {
	ArrayList<Double> quickerRhythms = new ArrayList<>();
    for (Double rhythm : midiNotes.getRhythmArray()) {
        quickerRhythms.add(rhythm / 2); // Multiply by 2, you can change this factor
    }
    player.setMelody(midiNotes.getPitchArray());
    player.setRhythm(quickerRhythms);
    System.out.println("----Original Midi with Quicker Rhythm----");
	playMelody();
    player.reset(); // Reset player for the transposed original melody
}


//5
private static void playOriginalMidiSlower() {
	 ArrayList<Double> slowerRhythms = new ArrayList<>();
    for (Double rhythm : midiNotes.getRhythmArray()) {
        slowerRhythms.add(rhythm * 2); // Multiply by 2, you can change this factor
    }
    player.setMelody(midiNotes.getPitchArray());
    player.setRhythm(slowerRhythms);
    System.out.println("----Original Midi with Slower Rhythm----");
	playMelody();
	player.reset(); // Reset player for the generated melody
}


//6
private static void playOriginalMidiOneOctaveHigher() {
	 ArrayList<Integer> oneOctaveHigher = new ArrayList<>();
    for (Integer pitch : midiNotes.getPitchArray()) {
        oneOctaveHigher.add(pitch + 12); // Transpose one octave up, you can change this value
    }
    player.setMelody(oneOctaveHigher);
    player.setRhythm(midiNotes.getRhythmArray()); // Keep the original rhythm
    System.out.println("-----Original Midi One Octave Higher-----");
	playMelody();
	player.reset();
}


//7
private static void playOriginalMidiOneOctaveLower() {
	 ArrayList<Integer> oneOctaveLower = new ArrayList<>();
    for (Integer pitch : midiNotes.getPitchArray()) {
        oneOctaveLower.add(pitch - 12); // Transpose one octave up, you can change this value
    }
    player.setMelody(oneOctaveLower);
    player.setRhythm(midiNotes.getRhythmArray()); // Keep the original rhythm
    System.out.println("-----Original Midi One Octave Lower-----");
	playMelody();
	player.reset();
}


	public static void testAndTrainProbGen ()
	{
		ProbabilityGenerator<Integer> pitchgen = new ProbabilityGenerator<Integer>(); //pitch instance 
		ProbabilityGenerator<Double> rhytemgen = new ProbabilityGenerator<Double>(); //rhythm instance

		pitchgen.train(midiNotes.getPitchArray()); 
		rhytemgen.train(midiNotes.getRhythmArray()); 

		pitchgen.printProbabilityDistribution(false); 
		rhytemgen.printProbabilityDistribution(false); 

	}


	// doing all the setup stuff
	public static void setup() {
		// playMidiFile(filePath); //use to debug -- this will play the ENTIRE file --
		// use ONLY to check if you have a valid path & file & it plays
		// it will NOT let you know whether you have opened file to get the data in the
		// form you need for the assignment

		midiSetup(filePath);
	}

	// plays the midi file using the player -- so sends the midi to an external
	// synth such as Kontakt or a DAW like Ableton or Logic
	static public void playMelody() {

		assert (player != null); // this will throw an error if player is null -- eg. if you haven't called
									// setup() first

		while (!player.atEndOfMelody()) {
			player.play(); // play each note in the sequence -- the player will determine whether is time
							// for a note onset
		}

	}

	// opens the midi file, extracts a voice, then initializes a melody player with
	// that midi voice (e.g. the melody)
	// filePath -- the name of the midi file to play
	static void midiSetup(String filePath) {

		// Change the bus to the relevant port -- if you have named it something
		// different OR you are using Windows
		player = new MelodyPlayer(100, "Bus 1"); // sets up the player with your bus.

		midiNotes = new MidiFileToNotes(filePath); // creates a new MidiFileToNotes -- reminder -- ALL objects in Java
													// must
													// be created with "new". Note how every object is a pointer or
													// reference. Every. single. one.

		// // which line to read in --> this object only reads one line (or ie, voice or
		// ie, one instrument)'s worth of data from the file
		midiNotes.setWhichLine(0); // this assumes the melody is midi channel 0 -- this is usually but not ALWAYS
									// the case, so you can try other channels as well, if 0 is not working out for
									// you.

		noteCount = midiNotes.getPitchArray().size(); // get the number of notes in the midi file

		assert (noteCount > 0); // make sure it got some notes (throw an error to alert you, the coder, if not)

		// sets the player to the melody to play the voice grabbed from the midi file
		// above
		player.setMelody(midiNotes.getPitchArray());
		player.setRhythm(midiNotes.getRhythmArray());
	}

	static void resetMelody() {
		player.reset();

	}

	// this function is not currently called. you may call this from setup() if you
	// want to test
	// this just plays the midi file -- all of it via your software synth. You will
	// not use this function in upcoming projects
	// but it could be a good debug tool.
	// filename -- the name of the midi file to play
	static void playMidiFileDebugTest(String filename) {
		Score theScore = new Score("Temporary score");
		Read.midi(theScore, filename);
		Play.midi(theScore);
	}

}