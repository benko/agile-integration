package com.redhat.training.agile.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.redhat.training.agile.components.StdoutBean;

@Component
public class TweetFollower extends RouteBuilder {
	private String consumerKey = "<fill-in>";
	private String consumerSecret = "<fill-in>";
	
	private String accessToken = "<fill-in>";
	private String accessSecret = "<fill-in>";

	private String account = "BBCSport";
	private String followDestination = "timeline/user";
	private String messageType = "polling&delay=30000";

	@Override
	public void configure() throws Exception {
		StdoutBean printer =  new StdoutBean();

		// Create a Twitter component here.
		from("twitter://" + followDestination + "?" +
				"type=" + messageType + "&" +
				"consumerKey=" + consumerKey + "&" +
				"consumerSecret=" + consumerSecret + "&" +
				"accessToken=" + accessToken + "&" +
				"accessTokenSecret=" + accessSecret + "&" +
				"user=" + account)
			.bean(printer)
			.to("seda:translations");
	}
}
