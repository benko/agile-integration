package com.redhat.training.agile.camel;

import org.apache.camel.impl.DefaultCamelContext;

import com.redhat.training.agile.routes.TweetFollower;

public class Main {

	public static void main(String[] args) throws Exception {
		DefaultCamelContext c = new DefaultCamelContext();

		TweetFollower tf = new TweetFollower();

		// we need to do manual configuration here
		tf.setConsumerKey(System.getProperty("agile.camel.consumer.key", System.getenv("TWITTER_CONSUMER_KEY")));
		tf.setConsumerSecret(System.getProperty("agile.camel.consumer.secret", System.getenv("TWITTER_CONSUMER_SECRET")));
		tf.setAccessToken(System.getProperty("agile.camel.access.token", System.getenv("TWITTER_ACCESS_TOKEN")));
		tf.setAccessSecret(System.getProperty("agile.camel.access.secret", System.getenv("TWITTER_ACCESS_SECRET")));

		String follow = System.getProperty("agile.camel.follow.account", System.getenv("TWITTER_ACCOUNT"));
		if (follow != null && !"".equals(follow)) {
			tf.setAccount(follow);
		} else {
			tf.setAccount("BBCSport");
		}

		String poll = System.getProperty("agile.camel.follow.poll.interval", System.getenv("TWITTER_POLL_INTERVAL"));
		if (poll != null && !"".equals(poll)) {
			tf.setPollInterval(Integer.parseInt(poll));
		} else {
			tf.setPollInterval(30000);
		}

		c.addRoutes(tf);
		c.start();
	}
}
