package de.cau.cs.se.evaluation.architecture.transformation.metrics;


public class NamedValue {
	private String name;
	private double value;

	public NamedValue(final String name, final double value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(final double value) {
		this.value = value;
	}



}
