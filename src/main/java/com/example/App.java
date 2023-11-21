/*
 * Template from c2017-2023 Courtney Brown modified by Maggie Nguyen 
 * Name: Maggie Nguyen 
 * Date: November 20th, 2023 
 * Class: App.java, Final Project COMPLETED
 * Description: This is the Project 1 template for the Probability Generator, has been modified and completed for Showcase 
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


        Scanner scanner = new Scanner(System.in); //reads input stream - user press 

        while (true) { //prints out the options for users to understand 
			System.out.println();
			System.out.println("------------NUMBERED OPTIONS------------");
			System.out.println();
			System.out.println("1   TO PLAY ORIGINAL MIDI");
			System.out.println();
			System.out.println("2   TO PLAY ORIGINAL REVERSED");
			System.out.println();
			System.out.println("3   TO PLAY GENERATED NOTES");
			System.out.println();
			System.out.println("4   TO PLAY GENERATED NOTES REVERSED");
         	System.out.println();
			System.out.println("5   TO PLAY ORIGINAL MIDI QUICKER");
			System.out.println();
			System.out.println("6   TO PLAY ORIGINAL MIDI SLOWER");
			System.out.println();
			System.out.println("7   TO PLAY ORIGINAL MIDI ONE OCTAVE HIGHER");
			System.out.println();
			System.out.println("8   TO PLAY ORIGINAL MIDI ONE OCTAVE LOWER");
			System.out.println();
			System.out.println("0   TO END");
			System.out.println();
			System.out.print("------------ENTER NUMBER HERE------------ ");
			System.out.println();

            int choice = scanner.nextInt(); //reads from user input 

            switch (choice) { //user decides which case to choose, resulting in the function being called 
                case 1:
                    playOriginalMidi(); //plays function if user chooses 
                    break; //after calling the function, it stops 
				case 2:
                    reverseOriginalMidi();
                    break;
                case 3:
                	playGeneratedNotes(); //using probability generator  
                    break;
				case 4:
                	playReversedGeneratedNotes(); //using probability generator  
                    break;
                case 5:
                    playOriginalMidiQuicker();
                    break;
                case 6:
                    playOriginalMidiSlower();
                    break;
                case 7:
                    playOriginalMidiOneOctaveHigher();
                    break;
                case 8:
                    playOriginalMidiOneOctaveLower();
                    break;
                case 0:
                    System.out.println("------------PROGRAM COMPLETED------------");
					System.out.println();
                    System.exit(0); //end of the program, no longer runs 

			} //end of switch cases 
        } //end of while loop 
    } //end of	public static void main(String[] args) 
	

//1
private static void playOriginalMidi() { //function for playing original MIDI 
	player.setMelody(midiNotes.getPitchArray()); //sets pitch array - the melody 
    player.setRhythm(midiNotes.getRhythmArray()); //sets rhythm array - the rhythm 
	System.out.println("----------Original Midi Playing----------"); //print statement 
	playMelody(); //plays original melody 
    player.reset(); //resets - in order to accurately play other functions 
}


//2
private static void reverseOriginalMidi() { //function for reversing the original MIDI
    ArrayList<Integer> reversedPitches = new ArrayList<>(); //new arraylist to store reversed pitches
    ArrayList<Double> reversedRhythms = new ArrayList<>(); //new arraylist to store reversed rhythms

    for (int i = midiNotes.getPitchArray().size() - 1; i >= 0; i--) { //reverses pitches and rhythms 
        reversedPitches.add(midiNotes.getPitchArray().get(i)); //reverses pitches and rhythms 
        reversedRhythms.add(midiNotes.getRhythmArray().get(i)); //reverses pitches and rhythms 
    }

    player.setMelody(reversedPitches); //set reversed pitches - melody
    player.setRhythm(reversedRhythms); //set reversed rhythms
    System.out.println("---------Reversed Original Midi---------"); //print statement
    playMelody(); //plays reversed original MIDI
    player.reset(); //resets - in order to accurately play other functions
}


//3
private static void playGeneratedNotes() { //function to play generated notes 
	ProbabilityGenerator<Integer> pitchGen = new ProbabilityGenerator<Integer>(); //prob gens for pitches 
    ProbabilityGenerator<Double> rhythmGen = new ProbabilityGenerator<Double>(); //prob gens for rhythms

    pitchGen.train(midiNotes.getPitchArray()); //trains generators 
    rhythmGen.train(midiNotes.getRhythmArray()); //trains generators 

    for (int i = 0; i < 1000000; i++) { //iteration of 1000000 -  generation and train melodies 
        ArrayList<Integer> pitches = pitchGen.generate(20); //generate 20 pitches 
        ArrayList<Double> rhythms = rhythmGen.generate(20); //generate 20 rhythms
        pitchGen.train(pitches); //training gen with generated pitches 
        rhythmGen.train(rhythms); //training gen with generated rhythms 
    }

    ArrayList<Integer> generatedPitches = pitchGen.generate(40); //generate 40 pitches 
    player.setMelody(generatedPitches); //generates melody 
    player.setRhythm(rhythmGen.generate(40)); //generate 40 rhythms
	System.out.println("-------Probability Genereated Notes-------"); //print statement 
	playMelody(); //plays generated notes 
    player.reset(); //resets - in order to accurately play other functions 
}


//4
	private static void playReversedGeneratedNotes() { //function for playing reversal of generated notes 
		ProbabilityGenerator<Integer> pitchGen = new ProbabilityGenerator<Integer>(); //prob gens for pitches 
		ProbabilityGenerator<Double> rhythmGen = new ProbabilityGenerator<Double>(); //prob gens for rhythms
	
		pitchGen.train(midiNotes.getPitchArray()); //trains generators 
		rhythmGen.train(midiNotes.getRhythmArray()); //trains generators 
	
		ProbabilityGenerator<Integer> pitchGen2 = new ProbabilityGenerator<Integer>(); //prob gens for reversed pitches 
		ProbabilityGenerator<Double> rhythmGen2 = new ProbabilityGenerator<Double>(); //prob gens for reversed rhythms
	
		// For Original Midi
		for (int i = 0; i < 1000000; i++) { //iteration of 1000000 
			ArrayList<Integer> pitches = pitchGen.generate(20); //generation of 20 pitches 
			ArrayList<Double> rhythms = rhythmGen.generate(20); //generation of 20 rhythms 
			pitchGen2.train(pitches); //training gen with generated pitches 
			rhythmGen2.train(rhythms); //training gen with generated rhythms 
		}
		player.reset(); //resets - in order to accurately play other functions 
	
		// For Reversed Midi
		for (int i = 0; i < 1000000; i++) { //iteration of 1000000 
			ArrayList<Integer> reversedPitches = pitchGen2.generate(40); //generates 40 reversal pitches 
			ArrayList<Double> reversedRhythms = rhythmGen2.generate(40); //generates 40 reversal rhythms 
			pitchGen.train(reversedPitches); //training gen with generated pitches 
			rhythmGen.train(reversedRhythms); //training gen with generated rhythms 
		}
	
		player.setMelody(pitchGen2.generate(40)); //reversal melody 
		player.setRhythm(rhythmGen2.generate(40)); //reversal melody 
		System.out.println("---------Reversed Generated Notes---------"); //print statement 
		playMelody(); //plays reversed generation melody 
		player.reset(); //resets - in order to accurately play other functions 
 }


//5
private static void playOriginalMidiQuicker() { //function for playing original MIDI 2x the speed 
	ArrayList<Double> quickerRhythms = new ArrayList<>(); //new arraylist to store thyrhtms
    for (Double rhythm : midiNotes.getRhythmArray()) { //iterates through original rhythm 
        quickerRhythms.add(rhythm / 2); //changes speed by 2x
    }
    player.setMelody(midiNotes.getPitchArray()); //original pitches - melody 
    player.setRhythm(quickerRhythms); //quicker rhythm 
    System.out.println("----Original Midi with Quicker Rhythm----"); //print statement 
	playMelody(); //plays original MIDI 2x the speed 
    player.reset(); //resets - in order to accurately play other functions 
}


//6
private static void playOriginalMidiSlower() { //function for playing original MIDI 0.5 the speed 
	 ArrayList<Double> slowerRhythms = new ArrayList<>(); //new arraylist to store thyrhtms
    for (Double rhythm : midiNotes.getRhythmArray()) { //iterates through original rhythm 
        slowerRhythms.add(rhythm * 2); //changes speed by 0.5
    }
    player.setMelody(midiNotes.getPitchArray()); //original pitches - melody 
    player.setRhythm(slowerRhythms); //slower rhythm 
    System.out.println("----Original Midi with Slower Rhythm----"); //print statement 
	playMelody(); //plays original MIDI 0.5 the speed 
	player.reset(); //resets - in order to accurately play other functions 
}


//7
private static void playOriginalMidiOneOctaveHigher() { //function for playing original MIDI an octave higher
	 ArrayList<Integer> oneOctaveHigher = new ArrayList<>(); //new arraylist to store pitches an octave higher 
    for (Integer pitch : midiNotes.getPitchArray()) { //iteration through original pitch 
        oneOctaveHigher.add(pitch + 12); //move octaves 
    }
    player.setMelody(oneOctaveHigher); //new pitches - melody 
    player.setRhythm(midiNotes.getRhythmArray()); //original rhythm 
    System.out.println("-----Original Midi One Octave Higher-----"); //print statement 
	playMelody(); //plays original one octave higher 
	player.reset(); //resets - in order to accurately play other functions 
}


//8
private static void playOriginalMidiOneOctaveLower() { //function for playing original MIDI an octave lower
	 ArrayList<Integer> oneOctaveLower = new ArrayList<>(); //new arraylist to store pitches an octave lower 
    for (Integer pitch : midiNotes.getPitchArray()) { //iteration through original pitch 
        oneOctaveLower.add(pitch - 12); //move octaves 
    }
    player.setMelody(oneOctaveLower); //new pitches - melody 
    player.setRhythm(midiNotes.getRhythmArray()); //original rhythm 
    System.out.println("-----Original Midi One Octave Lower-----"); //print statement 
	playMelody(); //plays original one octave lower 
	player.reset(); //resets - in order to accurately play other functions 
}


	public static void testAndTrainProbGen () //prints out table - probability distribution 
	{
		ProbabilityGenerator<Integer> pitchgen = new ProbabilityGenerator<Integer>(); //prob gen for pitch 
		ProbabilityGenerator<Double> rhytemgen = new ProbabilityGenerator<Double>(); //prob gen for rhythm 

		pitchgen.train(midiNotes.getPitchArray()); //trains pitch gen with original 
		rhytemgen.train(midiNotes.getRhythmArray()); //trains rhythm gen with original 

		pitchgen.printProbabilityDistribution(false); //prints probability distribution - pitches - no round
		rhytemgen.printProbabilityDistribution(false); //prints probability distribution - rhythms - no round

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