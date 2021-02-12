package fi.utu.tech.gui.javafx.assignment8;

import fi.utu.tech.gui.javafx.WordIterator;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class HashCrackService extends Service<String> {
	
	String hashInputField;

	@Override
	protected Task<String> createTask() {
		return new HashCrackTask(hashInputField, 4, WordIterator.DEFAULT_DICT, "md5", "utf-8");
	}
	
	protected void setHash(String hash) {
		this.hashInputField = hash;
	}
    
}
