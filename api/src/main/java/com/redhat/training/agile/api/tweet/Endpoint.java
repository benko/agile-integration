package com.redhat.training.agile.api.tweet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.redhat.training.agile.api.jms.Client;

@Path("/")
@ApplicationScoped
public class Endpoint {
	@Inject
	TweetBuffer tb;
	
	@Inject
	Client jmsc;

    @GET
    @Path("/tweet/{lang}")
    @Produces("text/plain")
    public String getTweet(@PathParam("lang") String language) {
        return tb.getTweet(language);
    }

    @DELETE
    @Path("/tweet/{lang}")
    @Produces("text/plain")
    public void deleteTweet(@PathParam("lang") String language) {
        tb.deleteTweet(language);
    }

    @GET
    @Path("/fetch/{lang}")
    @Produces("text/plain")
    public String fetchTweet(@PathParam("lang") String language) {
    	return jmsc.updateTweet(language);
    }
}