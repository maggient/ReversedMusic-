/*
package com.example;
import java.util.ArrayList;
 
public class UnitTest {
    MidiFileToNotes midiNotes; 
    UnitTest(MidiFileToNotes midiNotes) {
        this.midiNotes = midiNotes; 
    }

void Test () {
    ProbabilityGenerator<Integer> pitchGen = new ProbabilityGenerator<Integer>(); //pitch is Integer
    ProbabilityGenerator<Double> rhythmGen = new ProbabilityGenerator<Double>(); //rhythm is Double

    pitchGen.train(midiNotes.getPitchArray()); //acces midiNotes, have information on the file 
	rhythmGen.train(midiNotes.getRhythmArray());

    ProbabilityGenerator<Integer> pitchGen2 = new ProbabilityGenerator<Integer>();
    ProbabilityGenerator<Integer> rhythmGen2 = new ProbabilityGenerator<Integer>(); 

    pitchgen.printProbabilityDistribution(false); //for Pitch 
	rhytemgen.printProbabilityDistribution(false); //for Rhythem 

    for(int i = 0; i < 10000; i++) {
        ArrayList<Integer> pitches = pitchGen.generate(20); //size of Generation 20
		ArrayList<Double> rhythms = rhythmGen.generate(20); 

    pitchgen2.train(pitches);
    rhytemgen2.train(rhythms); 

    }
    pitchgen2.printProbabilityDistribution(true); //for Pitch 
	rhytemgen2.printProbabilityDistribution(true); //for Rhythem  
}
} */