/*
 * Template from: c2017-2023 Courtney Brown modified from Maggie Nguyen 
 * Name: Maggie Nguyen 
 * Date: November 20th, 2023 
 * Class: ProbabliityGenerator, Final Project COMPLETED
 * Description: This is the Project 1 template for the Probability Generator, has been modified and completed for Showcase 
 */


package com.example;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class ProbabilityGenerator <E>
{
	//Code from instructions video 
	ArrayList<E> alphabet = new ArrayList<E>();
	ArrayList<Float> tokenCounts = new ArrayList<Float>();
	double tokenCount = 0;

	void train(ArrayList<E> data) //Train() code 
	{
		//Write code for train()
		for (int i = 0; i < data.size();i++)
                    	{
                                int index = alphabet.indexOf(data.get(i)); //i is index in data, get that index in data, finding index in alphabet and setting to int 
                                if (index == -1)//if not found in data
                                {
                                        index = alphabet.size(); //index equals size of alphabet 
                                        alphabet.add(data.get(i)); //add data into alphabet 
                                        tokenCounts.add(0.f); //adding 0 to tokenCounts because there is nothing 
                                }      
								Float count = tokenCounts.get(index) + 1; //total of all tokens that we have 
								tokenCounts.set(index, count); //setting specific index into count 
                        }
						tokenCount += data.size(); //tokenCount = tokenCount + data 
	}


	 public ArrayList<E>generate(int x) //ArrayList is return type, named generate, x due to number of times looping) 
	{
		//ArrayList<E> distribution = new ArrayList<>();
		//Random rand = new Random();
		float count; //probability up to current token 
		float rIndex; 
		ArrayList<Float> probabilityList = new ArrayList<Float>(); //Creating new ArrayList keeps track of token probability 
		ArrayList<E> generatedNotes = new ArrayList<E>(); //Creating new ArrayList keeps track of notes made from probabilityList and rIndex, Music notes that will be outputted 

		for(int i = 0; i < tokenCounts.size(); i++) { //TokenCounts, the number of times the letter shows 
			//count = tokenCounts.get(i) / tokenCount; 
			count = (float) (tokenCounts.get(i) / (float) tokenCount); //Calculates probability of each tokens (a, b, c, d, etc) 
			probabilityList.add(count); //adding calculations to count 
		}

		for(int j = 0; j < x; j++) { //makes sure does not exeed x (which is 20) 
			rIndex = (float) Math.random(); //generates random float which is 0 to 1 
			float currentPosition = probabilityList.get(0); //keeps track of which position is getting checked 

			for(int k = 0; k < probabilityList.size(); k++) { //makes sure does not exeed probabilityList size 
				if(rIndex < currentPosition || k == probabilityList.size()-1) { //rIndex at currentPosition or if is at last index  
					generatedNotes.add(alphabet.get(k)); //adds notes to generatedNotes ArrayList 
					break; 
				}
					currentPosition += probabilityList.get(k+1); //adds calculations to get to probality calculation of index 
			}
		}
		return generatedNotes; 
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void reverseMelodies() {
        ArrayList<E> reversedAlphabet = new ArrayList<>(alphabet); //ArrayList of reversed alphabet
        Collections.reverse(reversedAlphabet); //Collections: object that represents a group of objects

        ArrayList<Float> reversedTokenCounts = new ArrayList<>(tokenCounts); //ArrayList of reversed tokenCounts
        Collections.reverse(reversedTokenCounts); //Collections: object that represents a group of objects

        alphabet = reversedAlphabet; //from original to reversed 
        tokenCounts = reversedTokenCounts; //from original to reversed 
    } //end of reverseMelodies()

	//nested convenience class to return two arrays from sortArrays() method
	//students do not need to use this class
	protected class SortArraysOutput
	{
		public ArrayList<E> symbolsListSorted; //arraylist of symbolsListSorted
		public ArrayList<Float> symbolsCountSorted; //arraylist of symbolsCountSorted
	} //end of SortArraysOutput
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//sort the symbols list and the counts list, so that we can easily print the probability distribution for testing
	//symbols -- your alphabet or list of symbols (input)
	//counts -- the number of times each symbol occurs (input)
	//symbolsListSorted -- your SORTED alphabet or list of symbols (output)
	//symbolsCountSorted -- list of the number of times each symbol occurs inorder of symbolsListSorted  (output)
	public SortArraysOutput sortArrays(ArrayList<E> symbols, ArrayList<Float> counts)	{

		SortArraysOutput sortArraysOutput = new SortArraysOutput(); 
		
		sortArraysOutput.symbolsListSorted = new ArrayList<E>(symbols);
		sortArraysOutput.symbolsCountSorted = new ArrayList<Float>();
	
		//sort the symbols list
		Collections.sort(sortArraysOutput.symbolsListSorted, new Comparator<E>() {
			@Override
			public int compare(E o1, E o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		//use the current sorted list to reference the counts and get the sorted counts
		for(int i=0; i<sortArraysOutput.symbolsListSorted.size(); i++)
		{
			int index = symbols.indexOf(sortArraysOutput.symbolsListSorted.get(i));
			sortArraysOutput.symbolsCountSorted.add(counts.get(index));
		}

		return sortArraysOutput;

	}
	
	//Students should USE this method in your unit tests to print the probability distribution
	//HINT: you can overload this function so that it uses your class variables instead of taking in parameters
	//boolean is FALSE to test train() method & TRUE to test generate() method
	//symbols -- your alphabet or list of symbols (input)
	//counts -- the number of times each symbol occurs (input)
	//sumSymbols -- the count of how many tokens we have encountered (input)
	public void printProbabilityDistribution(boolean round, ArrayList<E> symbols, ArrayList<Float> counts, double sumSymbols)
	{
		//sort the arrays so that elements appear in the same order every time and it is easy to test.
		SortArraysOutput sortResult = sortArrays(symbols, counts);
		ArrayList<E> symbolsListSorted = sortResult.symbolsListSorted;
		ArrayList<Float> symbolsCountSorted = sortResult.symbolsCountSorted;

		System.out.println("-----Probability Distribution-----");
		
		for (int i = 0; i < symbols.size(); i++)
		{
			if (round){
				DecimalFormat df = new DecimalFormat("#.##");
				System.out.println("Data: " + symbolsListSorted.get(i) + " | Probability: " + df.format((double)symbolsCountSorted.get(i) / sumSymbols));
			}
			else
			{
				System.out.println("Data: " + symbolsListSorted.get(i) + " | Probability: " + (double)symbolsCountSorted.get(i) / sumSymbols);
			}
		}
		
		System.out.println("------------");
	}

	//Code from instructions video 
	public void printProbabilityDistribution(boolean round)
	{
		printProbabilityDistribution(round, alphabet, tokenCounts, tokenCount);
	}
}