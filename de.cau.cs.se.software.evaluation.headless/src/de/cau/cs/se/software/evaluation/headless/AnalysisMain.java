/***************************************************************************
 * Copyright (C) 2019 Reiner Jung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package de.cau.cs.se.software.evaluation.headless;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.FileConverter;

import de.cau.cs.se.software.evaluation.java.JavaProjectAnalysisJob;

/**
 * This class controls all aspects of the application's execution.
 *
 * @author Reiner Jung
 *
 * @since 1.1
 */
public class AnalysisMain implements IApplication {

	/** Central logger for the compiler. */
	private static final Logger LOGGER = LogManager.getLogger(AnalysisMain.class);

	private static final Level DEFAULT_LOGGING_LEVEL = Level.INFO;

	@Parameter(names = { "-p", "--project" }, description = "Project root", required = true, converter = FileConverter.class)
	private File projectRootDirectory;

	@Parameter(names = { "-s", "--system" }, description = "File containing regex for system classes.", required = true, converter = FileConverter.class)
	private File systemClassesFile;

	@Parameter(names = { "-d", "--data-classes" }, description = "Kieker API version.", required = true, converter = FileConverter.class)
	private File dataClassesFile;

	@Parameter(names = { "-j", "--java-version" }, description = "Assume specified Java version for code analysis", required = false)
	private String javaVersion;

	@Parameter(names = { "-v", "--verbose" }, description = "Verbosity level, default is INFO", converter = LoggingLevelConverter.class)
	private Level loggingLevel;

	@Parameter(names = { "-r", "--report" }, description = "Write report to this file.", required = true, converter = FileConverter.class)
	private File reportFile;

	@Parameter(names = { "-h", "--help" }, help = true)
	private boolean help;

	/**
	 * Default constructor.
	 */
	public AnalysisMain() {
		// default constructor
	}

	@Override
	public Object start(final IApplicationContext context) throws Exception {
		AnalysisMain.LOGGER.addAppender(this.createStdoutAppender());
		this.loggingLevel = AnalysisMain.DEFAULT_LOGGING_LEVEL;
		this.help = false;
		final Map<?, ?> args = context.getArguments();
		final String presentWorkingDirectory = Paths.get(".").toAbsolutePath().normalize().toString();

		final String[] appArgs = (String[]) args.get("application.args");

		JCommander commander = null;
		try {
			commander = new JCommander(this, appArgs);

			AnalysisMain.LOGGER.setLevel(this.loggingLevel);

			if (this.help) {
				commander.usage();
			} else {
				if (checkDirectory(projectRootDirectory, "Project root") 
						&& checkFile(systemClassesFile, "System classes") 
						&& checkFile(dataClassesFile, "Data classes")
						&& checkCanWriteToFile(reportFile, "Report file")) {
					runAnalysis();
				}
			}
		} catch (final ParameterException e) {
			AnalysisMain.LOGGER.error(e.getLocalizedMessage());
			if (commander != null) {
				commander.usage(e.getLocalizedMessage());
			}
		}
		
		return IApplication.EXIT_OK;
	}

	private void runAnalysis() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = workspace.getRoot().getProject();
		final Job job = new JavaProjectAnalysisJob(project, new ConsoleOutputHandler(AnalysisMain.LOGGER));
		job.schedule();
	}

	private boolean checkCanWriteToFile(File file, String label) {
		if (file.getParentFile().isDirectory()) {
			return true;
		} else {
			LOGGER.error(String.format("%s file '%s' cannot be written to %s. Path does not exist.", label, file.getAbsolutePath(), file.getParent()));
			return false;
		}
	}

	private boolean checkFile(File file, String label) {
		if (file.exists()) {
			if (file.isFile()) {
				return true;
			} else {
				LOGGER.error(String.format("%s file '%s' is not a file.", label, file.getAbsolutePath()));
				return false;
			}
		} else {
			LOGGER.error(String.format("%s directory '%s' is not file.", label, file.getAbsolutePath()));
			return false;
		}
	}

	private boolean checkDirectory(File directory, String label) {
		if (directory.exists()) {
			if (directory.isDirectory()) {
				return true;
			} else {
				LOGGER.error(String.format("%s directory '%s' is not a directory.", label, directory.getAbsolutePath()));
				return false;
			}
		} else {
			LOGGER.error(String.format("%s directory '%s' does not exist.", label, directory.getAbsolutePath()));
			return false;
		}
	}

	private Appender createStdoutAppender() {
		final Appender appender = new ConsoleAppender(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} - %m%n"));
		appender.setName("stdout");
		return appender;
	}

	@Override
	public void stop() {
		// nothing to do
	}

}
