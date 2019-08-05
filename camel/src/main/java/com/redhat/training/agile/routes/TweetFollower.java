package com.redhat.training.agile.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.redhat.training.agile.components.StdoutBean;

@Component
public class TweetFollower extends RouteBuilder {
	@Value("${agile.camel.consumer.key}")
	private String consumerKey;
	@Value("${agile.camel.consumer.secret}")
	private String consumerSecret;
	
	@Value("${agile.camel.access.token}")
	private String accessToken;
	@Value("${agile.camel.access.secret}")
	private String accessSecret;

	@Value("${agile.camel.follow.account}")
	private String account;
	
	@Value("${agile.camel.poll.interval}")
	private int pollInterval;

	@Override
	public void configure() throws Exception {
		StdoutBean printer =  new StdoutBean();

		// Create a Twitter component here.
		from("twitter://timeline/user?" +
				"type=polling&delay=" + pollInterval + "&" +
				"consumerKey=" + consumerKey + "&" +
				"consumerSecret=" + consumerSecret + "&" +
				"accessToken=" + accessToken + "&" +
				"accessTokenSecret=" + accessSecret + "&" +
				"user=" + account)
			.bean(printer)
			.to("seda:translations");
	}
}
