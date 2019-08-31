package com.redhat.training.agile.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.redhat.training.agile.model.Tweet;

@Component
public class TweetFollower extends RouteBuilder {
	// twitter authentication data
	@Value("${agile.camel.consumer.key}")
	private String consumerKey;
	@Value("${agile.camel.consumer.secret}")
	private String consumerSecret;	
	@Value("${agile.camel.access.token}")
	private String accessToken;
	@Value("${agile.camel.access.secret}")
	private String accessSecret;

	// timeline to follow
	@Value("${agile.camel.follow.account}")
	private String account;

	// polling interval for new messages
	@Value("${agile.camel.poll.interval}")
	private int pollInterval;

	@Override
	public void configure() throws Exception {
		// follow a twitter feed using the twitter component
		from("twitter-timeline://user?" +
				"user=" + account + "&" +
				"type=polling&" +
				"delay=" + pollInterval + "&" +
				"consumerKey=" + consumerKey + "&" +
				"consumerSecret=" + consumerSecret + "&" +
				"accessToken=" + accessToken + "&" +
				"accessTokenSecret=" + accessSecret)
			// convert a Status to a simpler form
			.convertBodyTo(Tweet.class)
			// debug: uses toString() method of Tweet.class
			.log(LoggingLevel.DEBUG, this.getClass().getName(), "TWEET RECEIVED: ${body}")
			// regardless of the language, just forward the message
			// to the translation topic - we'll take care of it there
			.to("jms:topic:translate");
	}
}
