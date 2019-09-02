package com.redhat.training.agile.api.jms;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.jboss.ejb3.annotation.ResourceAdapter;

import com.redhat.training.agile.api.tweet.TweetBuffer;
import com.redhat.training.agile.model.Tweet;

@ResourceAdapter("remote-broker")
@MessageDriven(name = "ListenerFR",
	activationConfig =  {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/tweets.fr")
    })
public class ListenerFR implements MessageListener {
	private static final String lang = "fr";

	@Inject
	TweetBuffer tb;

	@Inject
	Logger log;

	@PostConstruct
	public void init() {
		log.info("Hello, world! " + this.getClass().getName() + " initialised!");
	}

	@Override
	public void onMessage(Message message) {
		log.fine("Got a message!");
		try {
			if (message instanceof ObjectMessage) {
				String tweet = ((ObjectMessage)message).getBody(Tweet.class).toString();

				log.info("Setting last \"" + lang + "\" message to \"" + tweet + "\"");
				tb.setTweet(lang, tweet);
			} else {
				log.info("Unfortunately not for us: " + message.getClass().getName());
			}
		} catch (JMSException je) {
			log.log(Level.SEVERE, "JMSException while processing tweet", je);
		}
	}
}
