package de.cau.cs.se.evaluation.architecture.transformation.metrics;

import java.util.ArrayList;
import java.util.List;

public enum ResultModelProvider {
	INSTANCE;

	private final List<NamedValue> values;

	private ResultModelProvider() {
		this.values = new ArrayList<NamedValue>();
	}

	public void clearValues() {
		this.values.clear();
	}

	public List<NamedValue> getValues() {
		return this.values;
	}
}
