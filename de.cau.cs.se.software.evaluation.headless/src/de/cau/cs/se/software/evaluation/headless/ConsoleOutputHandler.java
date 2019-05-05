/**
 * 
 */
package de.cau.cs.se.software.evaluation.headless;

import org.apache.log4j.Logger;

import de.cau.cs.se.software.evaluation.jobs.IOutputHandler;

/**
 * @author Reiner Jung
 *
 */
public class ConsoleOutputHandler implements IOutputHandler {

	private Logger logger;

	public ConsoleOutputHandler(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public void error(String header, String message) {
		logger.error(String.format("%s: %s", header, message));
	}
}
