package com.redhat.training.agile.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.redhat.training.agile.model.TranslationRequest;
import com.redhat.training.agile.model.TranslationResponse;
import com.redhat.training.agile.model.Tweet;

@Component
public class TranslationFR extends RouteBuilder {
	private static final String lang = "fr";

	@Value("${agile.camel.translate.key}")
	private String translationKey;

	@Override
	public void configure() throws Exception {
		// read original messages
		from("jms:topic:translate")
		// stream cache makes it possible for us to read the
		// http response multiple times (for example, log it)
		.streamCaching()
		.choice()
			.when(simple("${body.language} != '" + lang + "'"))
				// reassign the original language and set the target
				// mind: transform=false is needed to not set the "out" message
				.to("language:ognl:request.body.previousLanguage = request.body.language?transform=false")
				.to("language:ognl:request.body.language = '" + lang + "'?transform=false")
				// remember what the source and the target were
				// (used in converting back to Tweet below)
				.setHeader("X-Translation-Source", simple("${body.previousLanguage}"))
				.setHeader("X-Translation-Target", simple("${body.language}"))
				// form a valid translation request body and marshal it into json
				.convertBodyTo(TranslationRequest.class)
				.marshal().json(JsonLibrary.Jackson)
				// debug
				.log(LoggingLevel.DEBUG, this.getClass().getName(), "MARSHALED: ${body}")
				// set up the http component and fire the request
				.setHeader(Exchange.HTTP_METHOD, constant("POST"))
				.setHeader(Exchange.HTTP_QUERY, constant("key=" + translationKey))
				.setHeader(Exchange.HTTP_URI, constant("https://api-platform.systran.net/translation/text/translate"))
				.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
				.to("https://api-platform.systran.net")
				// debug
				.log(LoggingLevel.DEBUG, this.getClass().getName(), "RESPONSE: ${body}")
				// unmarshal the response - unfortunately we get an array of "outputs"
				.unmarshal().json(JsonLibrary.Jackson, TranslationResponse.class)
				// debug
				.log(LoggingLevel.DEBUG, this.getClass().getName(), "UNMARSHALED: ${body}")
				// use the type converter to extract the first output
				.convertBodyTo(Tweet.class)
				// debug yet again
				.log(LoggingLevel.DEBUG, this.getClass().getName(), "TWEET TRANSLATED: ${body}")
			.otherwise()
				.log(LoggingLevel.INFO, this.getClass().getName(), "Nothing to do - source language == target language.")
			.end()
		.to("jms:tweets." + lang);
	}
}
