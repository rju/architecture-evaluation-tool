package de.cau.cs.se.software.evaluation.state;

import java.util.ArrayList;
import java.util.List;

import de.cau.cs.se.software.evaluation.hypergraph.Node;

public class RowPattern {

	private final List<Node> nodes = new ArrayList<>();

	private final boolean[] pattern;

	public RowPattern(final int columns) {
		this.pattern = new boolean[columns];
	}

	public List<Node> getNodes() {
		return this.nodes;
	}

	public boolean[] getPattern() {
		return this.pattern;
	}

}
