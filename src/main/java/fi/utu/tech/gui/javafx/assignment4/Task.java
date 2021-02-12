package fi.utu.tech.gui.javafx.assignment4;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import fi.utu.tech.gui.javafx.WordIterator;
import fi.utu.tech.gui.javafx.assignment2.HashCrack;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Task {
	
	String inputHash;
	String result;
	ArrayList<String> results = new ArrayList<String>();
	ObservableList<String> oResults;
	
	public Task(String inputHash) {
		this.inputHash = inputHash;
		this.oResults= FXCollections.observableList(this.results);
	}
	
	void run() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		result = new HashCrack(inputHash, 4, WordIterator.DEFAULT_DICT, "md5", "utf-8").bruteForce();
		System.out.println(result);
		results.add(result);
		
	}

}
