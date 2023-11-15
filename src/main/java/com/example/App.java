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

//make sure this class name matches your file name, if not fix.
public class App {

	static MelodyPlayer player; // play a midi sequence
	static MidiFileToNotes midiNotes; // read a midi file
	static int noteCount = 0;

	//make cross-platform
	static FileSystem sys = FileSystems.getDefault();

	//the getSeperator() creates the appropriate back or forward slash based on the OS in which it is running -- OS X & Windows use same code :) 
	static String filePath = "mid"  + sys.getSeparator() +  "MaryHadALittleLamb.mid"; // path to the midi file -- you can change this to your file
															// location/name

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
		playMelody(); //calling function 
		testAndTrainProbGen(); //calling function 
		generateMelody(); //Calling function 


		// uncomment to debug your midi file
		// this code MUST be commited when submitting unit tests or any code to github
		// playMidiFileDebugTest(filePath);
	}

// generates a melody from a probability distribution and plays the original and reversed melodies
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static void generateMelody() {
    ProbabilityGenerator<Integer> pitchGen = new ProbabilityGenerator<Integer>();
    ProbabilityGenerator<Double> rhythmGen = new ProbabilityGenerator<Double>();

    pitchGen.train(midiNotes.getPitchArray());
    rhythmGen.train(midiNotes.getRhythmArray());

    ProbabilityGenerator<Integer> pitchGen2 = new ProbabilityGenerator<Integer>();
    ProbabilityGenerator<Double> rhythmGen2 = new ProbabilityGenerator<Double>();

	//For Original Midi 
    for (int i = 0; i < 1000000; i++) {
        ArrayList<Integer> pitches = pitchGen.generate(20);
        ArrayList<Double> rhythms = rhythmGen.generate(20);
        pitchGen2.train(pitches);
        rhythmGen2.train(rhythms);
    }

    ArrayList<Integer> originalMidi = pitchGen.generate(20);
    player.setMelody(originalMidi);
    player.setRhythm(rhythmGen.generate(20));
    playMelody();

    player.reset(); //Resets 

    //For Reversed Midi 
    for (int i = 0; i < 1000000; i++) {
        ArrayList<Integer> reversedPitches = pitchGen2.generate(20);
        ArrayList<Double> reversedRhythms = rhythmGen2.generate(20);
        pitchGen.train(reversedPitches);
        rhythmGen.train(reversedRhythms);
    }

	player.setMelody(pitchGen2.generate(20));
    player.setRhythm(rhythmGen2.generate(20));
    playMelody();
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Following video instructions and additional comments 
	public static void testAndTrainProbGen ()
	{
		ProbabilityGenerator<Integer> pitchgen = new ProbabilityGenerator<Integer>(); //pitch is Integer
		ProbabilityGenerator<Double> rhytemgen = new ProbabilityGenerator<Double>(); //rhythm is Double

		pitchgen.train(midiNotes.getPitchArray()); //acces midiNotes, have information on the file 
		rhytemgen.train(midiNotes.getRhythmArray()); 

		pitchgen.printProbabilityDistribution(false); //pitchGen2 different from pitchGen
		rhytemgen.printProbabilityDistribution(false); //rhythmGen2 different from rhythmGen

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