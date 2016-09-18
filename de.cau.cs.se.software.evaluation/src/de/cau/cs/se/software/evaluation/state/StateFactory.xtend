package de.cau.cs.se.software.evaluation.state

class StateFactory {
	def static createRowPatternTable(int columns, int rows) {
		return new RowPatternTable(columns, rows)
	}
	
	def static createRowPattern(int columns) {
		return new RowPattern(columns)
	}
	
}