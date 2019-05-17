/**
 *
 */
package de.cau.cs.se.software.evaluation.jobs;

/**
 * @author Reiner Jung
 *
 */
public interface IOutputHandler {

	void error(String header, String message);

	void updateLogView();

	public abstract void updateResultView();
}
