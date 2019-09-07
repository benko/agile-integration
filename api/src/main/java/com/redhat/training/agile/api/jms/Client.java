package com.redhat.training.agile.api.jms;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import com.redhat.training.agile.api.tweet.TweetBuffer;
import com.redhat.training.agile.model.Tweet;

/*
 * This JMS client class primarily serves as a showcase how nothing actually
 * works the way it should. :) There are remnants of several different types
 * of attempts to get last-image messages from the below destinations, alas,
 * none of them work for fetching the latest available message.  
 */
@Singleton
@ApplicationScoped
@Deprecated
public class Client {
	@Inject
	TweetBuffer tb;

	@Inject
	Logger log;

	@Resource(mappedName = "java:/jms/RemoteBrokerCF")
	ConnectionFactory cf;

	@Resource(mappedName = "java:/jms/queue/tweets.en")
	Queue qEN;

	@Resource(mappedName = "java:/jms/queue/tweets.es")
	Queue qES;

	@Resource(mappedName = "java:/jms/queue/tweets.fr")
	Queue qFR;

	/*
	 * Temporary test with ActiveMQConnectionFactory. IDW.
	@Inject
	@ConfigProperty(
		name = "thorntail.network.socket-binding-groups.standard-sockets.outbound-socket-bindings.remote-broker.remote-host",
		defaultValue = "localhost"
	)
	String hostname;
	
	@Inject
	@ConfigProperty(
		name = "thorntail.network.socket-binding-groups.standard-sockets.outbound-socket-bindings.remote-broker.remote-port",
		defaultValue = "61616"
	)
	String port;
	 */

	@PostConstruct
	public void init() {
		log.info("Hello, world! " + this.getClass().getName() + " initialised!");
	}

	public String updateTweet(String language) {
		String tweet = null;

		// this doesn't fail, but doesn't help either
		//log.info("Creating CF at " + this.hostname + ":" + this.port);
		//ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://" + this.hostname + ":" + this.port);

		Connection c = null;
		Session s = null;
		// must use injection
		//Queue q = null;
		MessageConsumer mc = null;
		try {
			c = cf.createConnection();
			
			// Apparently this blows up the consumer as it's illegal.
			//c.setClientID("ManualPull");

			s = c.createSession(Session.AUTO_ACKNOWLEDGE);

			// this fails with artemis in either auto- or manual-create mode
			//q = s.createQueue("tweets." + language + "::tweets." + language);

			// these two are both deprecated and fail in obtaining a message
			//q = new ActiveMQQueue("tweets." + language, "tweets." + language);
			//q = ActiveMQJMSClient.createQueue("tweets." + language + "::tweets." + language);

			// so ultimately, can't create a queue reference programatically, must use jndi
			//mc = s.createConsumer(q);

			switch (language) {
				case "en":
					mc = s.createConsumer(qEN);
					break;
				case "es":
					mc = s.createConsumer(qES);
					break;
				case "fr":
					mc = s.createConsumer(qFR);
					break;
				default:
					log.severe("No known destinations for \"" + language + "\".");					
			}

			Message m = mc.receive(5000);

			if (m == null) {
				log.info("No messages available for \"" + language + "\".");

			} else if (m instanceof ObjectMessage) {
				tweet = ((ObjectMessage)m).getBody(Tweet.class).toString();

				log.info("Resetting last \"" + language + "\" message to \"" + tweet + "\"");
				tb.setTweet(language, tweet);

			} else {
				log.severe("Message format unknown: " + m.getClass().getName());
			}
		} catch (JMSException je) {
			log.log(Level.SEVERE, "Unable to receive message: " + je.getMessage(), je);
		} catch (ClassCastException cce) {
			log.log(Level.SEVERE, "Error trying to extract tweet: " + cce.getMessage(), cce);
		} finally {
			if (mc != null) {
				try { mc.close(); }
				catch (JMSException je) {
					log.severe("Error closing message consumer: " + je.getMessage());
				}
			}
			if (s != null) {
				try { s.close(); }
				catch (JMSException je) {
					log.severe("Error closing session: " + je.getMessage());
				}
			}
			if (c != null) {
				try { c.close(); }
				catch (JMSException je) {
					log.severe("Error closing connection: " + je.getMessage());
				}
			}
			//((ActiveMQConnectionFactory)cf).close();
		}

		return tweet;
	}
}
