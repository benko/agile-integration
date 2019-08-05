package com.redhat.training.agile.model;

import java.util.Locale;

public class Tweet {
	private String text;
	private Locale language;

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Locale getLanguage() {
		return language;
	}
	public void setLanguage(Locale language) {
		this.language = language;
	}
	
	public String toString() {
		return("[" + language.getISO3Language() + "]: " + text);
	}
}
