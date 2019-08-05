package com.redhat.training.agile.camel;

import org.apache.camel.impl.DefaultCamelContext;

import com.redhat.training.agile.routes.TweetFollower;

public class Main {

	public static void main(String[] args) throws Exception {
		DefaultCamelContext c = new DefaultCamelContext();

		// we need to do manual configuration here
		TweetFollower tf = new TweetFollower();

		if (System.getProperty("agile.camel.consumer.key", null) != null) {
			tf.setConsumerKey(System.getProperty("agile.camel.consumer.key"));
		}
		if (System.getProperty("agile.camel.consumer.secret", null) != null) {
			tf.setConsumerSecret(System.getProperty("agile.camel.consumer.secret"));
		}
		if (System.getProperty("agile.camel.access.token", null) != null) {
			tf.setAccessToken(System.getProperty("agile.camel.access.token"));
		}
		if (System.getProperty("agile.camel.access.secret", null) != null) {
			tf.setAccessSecret(System.getProperty("agile.camel.access.secret"));
		}
		tf.setAccount(System.getProperty("agile.camel.follow.account", "BBCSport"));
		tf.setPollInterval(Integer.parseInt(System.getProperty("agile.camel.follow.poll.interval", "30000")));

		c.addRoutes(tf);
		c.start();
	}
}
