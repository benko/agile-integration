package com.redhat.training.agile.camel;

import org.apache.camel.impl.DefaultCamelContext;

import com.redhat.training.agile.routes.TweetFollower;

public class Main {

	public static void main(String[] args) throws Exception {
		DefaultCamelContext c = new DefaultCamelContext();
		c.addRoutes(new TweetFollower());
		c.start();
	}
}
