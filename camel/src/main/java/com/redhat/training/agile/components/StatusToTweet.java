package com.redhat.training.agile.components;

import java.util.Locale;

import org.apache.camel.Converter;

import com.redhat.training.agile.model.Tweet;

import twitter4j.Status;

@Converter
public class StatusToTweet {

	@Converter
	public Tweet convertToTweet(Status status) {
		Tweet t = new Tweet();
		t.setLanguage(Locale.forLanguageTag(status.getLang()));
		t.setText(status.getText());
		return t;
	}
}
