package com.redhat.training.agile.api.tweet;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Singleton
@ApplicationScoped
public class TweetBuffer {
	@Inject
	Logger log;

	private Map<String, String> tweets;
	
	public TweetBuffer() {
		this.tweets = new HashMap<>(); 
	}

	@PostConstruct
	public void init() {
		log.info("Hello, world! " + this.getClass().getName() + " initialised!");
	}

	public String getTweet(String lang) {
		log.info("Serving request for \"" + lang + "\".");
		return this.tweets.getOrDefault(lang, "N/A");
	}
	
	public void setTweet(String lang, String tweet) {
		log.info("Updating latest \"" + lang + "\" record: " + tweet);
		this.tweets.put(lang, tweet);
	}

	public void deleteTweet(String lang) {
		log.info("Deleting latest \"" + lang + "\" record.");
		this.tweets.remove(lang);
	}
}
