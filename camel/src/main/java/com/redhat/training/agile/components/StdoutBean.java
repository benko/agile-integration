package com.redhat.training.agile.components;

import com.redhat.training.agile.model.Tweet;

public class StdoutBean {
	public void print(Object x) {
		String message;
		if (x instanceof Tweet) {
			message = x.toString();
		} else if (x instanceof twitter4j.Status) {
			message = ((twitter4j.Status) x).getText();
		} else {
			message = "[StdoutBean] unsupported message format";
		}
		System.out.println(message);
	}
}
