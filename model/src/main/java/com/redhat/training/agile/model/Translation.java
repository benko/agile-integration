package com.redhat.training.agile.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Translation {
	private String output;

	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
}
