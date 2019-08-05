package com.redhat.training.agile.components;

public class StdoutBean {
	public void print(Object x) {
		String message;
		if (x instanceof twitter4j.Status) {
			message = ((twitter4j.Status) x).getText();
		} else {
			message = x.toString();
		}
		System.out.println(message);
	}
}
