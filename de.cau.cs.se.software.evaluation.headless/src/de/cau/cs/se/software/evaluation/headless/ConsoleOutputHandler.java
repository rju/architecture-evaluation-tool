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

	private final Logger logger;

	public ConsoleOutputHandler(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void error(final String header, final String message) {
		this.logger.error(String.format("%s: %s", header, message));
	}

	@Override
	public void updateLogView() {
		// nothing to be done here
	}

	@Override
	public void updateResultView() {
		// nothing to be done here
	}

	public void info(final String header, final String message) {
		this.logger.info(String.format("%s: %s", header, message));
	}
}
