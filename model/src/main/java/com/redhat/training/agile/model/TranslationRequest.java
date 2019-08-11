package com.redhat.training.agile.model;

public class TranslationRequest {
	private String source;
	private String target;
	private String input;
	private boolean backTranslation = false;

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public boolean isBackTranslation() {
		return backTranslation;
	}
	public void setBackTranslation(boolean backTranslation) {
		this.backTranslation = backTranslation;
	} 
}
