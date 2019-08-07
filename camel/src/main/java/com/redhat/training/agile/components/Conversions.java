package com.redhat.training.agile.components;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import com.redhat.training.agile.model.TranslationRequest;
import com.redhat.training.agile.model.TranslationResponse;
import com.redhat.training.agile.model.Tweet;

import twitter4j.Status;

@Converter
public class Conversions {
	// dropping everything not interesting from a twitter status
	@Converter
	public Tweet convertStatusToTweet(Status status) {
		Tweet t = new Tweet();
		t.setLanguage(status.getLang());
		t.setText(status.getText());
		return t;
	}
	
	// convert a tweet to a translation request object that can be
	// easily marshaled into json understood by systran
	@Converter
	public TranslationRequest convertTweetToTranslationRequest(Tweet tweet) {
		TranslationRequest tr = new TranslationRequest();
		tr.setInput(tweet.getText());
		tr.setSource(tweet.getPreviousLanguage());
		tr.setTarget(tweet.getLanguage());
		tr.setBackTranslation(false);
		return tr;
	}

	// this one is dirty: we get an array of outputs, but we're
	// only interested in the first member - drop everything else
	@Converter
	public Tweet convertTranslationResponseToTweet(TranslationResponse tr, Exchange exch) {
		Tweet t = new Tweet();

		// just take the first response
		t.setText(tr.getOutputs().get(0).getOutput());

		// use the exchange headers to extract source and target languages
		t.setPreviousLanguage((String)exch.getIn().getHeader("X-Translation-Source", "nn"));
		t.setLanguage((String)exch.getIn().getHeader("X-Translation-Target", "nn"));

		return t;
	}
}
