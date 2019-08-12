package com.redhat.training.agile.model;

import java.io.Serializable;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet implements Serializable {
	private static final long serialVersionUID = 1L;

	private String text;
	private String language;
	private String previousLanguage;

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLanguage() {
		return this.language;
	}
	public void setLanguage(String language) {
		this.language = Locale.forLanguageTag(language).getLanguage();
	}
	public String getPreviousLanguage() {
		return previousLanguage;
	}
	public void setPreviousLanguage(String previousLanguage) {
		this.previousLanguage = Locale.forLanguageTag(previousLanguage).getLanguage();
	}
	public String toString() {
		return("[" + 
				((this.previousLanguage != null && this.previousLanguage != "") ?
						this.previousLanguage + " -> " :
						"") +
				this.language + "]: " + this.text);
	}
}
