package com.redhat.training.agile.api.tweet;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/tweet")
@ApplicationScoped
public class Endpoint {
	@Inject
	TweetBuffer tb;

	@Inject
	Logger log;

    @GET
    @Path("/{lang}")
    @Produces("text/plain")
    public String getTweet(@PathParam("lang") String language) {
    	log.info("Returning last tweet for language \"" + language + "\"");
        return tb.getTweet(language);
    }

    @DELETE
    @Path("/{lang}")
    @Produces("text/plain")
    public void deleteTweet(@PathParam("lang") String language) {
    	log.info("Deleting last tweet for language \"" + language + "\"");
        tb.deleteTweet(language);
    }

    @POST
    @Path("/{lang}")
    @Produces("text/plain")
    @Consumes("text/plain")
    public void pushTweet(@PathParam("lang") String language, String body) {
    	log.info("Resetting last tweet for language \"" + language + "\" to \"" + body + "\"");
    	tb.setTweet(language, body);
    }
}
