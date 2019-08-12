package com.redhat.training.agile.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslationResponse {
	private List<Translation> outputs;

	public List<Translation> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<Translation> outputs) {
		this.outputs = outputs;
	}
}
