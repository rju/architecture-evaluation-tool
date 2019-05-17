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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import de.cau.cs.se.software.evaluation.java.JavaAnalysisConfiguration;
import de.cau.cs.se.software.evaluation.java.JavaProjectAnalysisJob;
import de.cau.cs.se.software.evaluation.jobs.IOutputHandler;
import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider;
import de.cau.cs.se.software.evaluation.views.LogModelProvider;
import de.cau.cs.se.software.evaluation.views.NamedValue;

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

	private final AnalysisConfiguration configuration = new AnalysisConfiguration();

	/**
	 * Default constructor.
	 */
	public AnalysisMain() {
		// default constructor
	}

	/**
	 * Main starter routine.
	 */
	@Override
	public Object start(final IApplicationContext context) throws Exception {
		Logger.getRootLogger().removeAllAppenders();
		AnalysisMain.LOGGER.addAppender(this.createStdoutAppender());
		this.configuration.setLoggingLevel(AnalysisMain.DEFAULT_LOGGING_LEVEL);
		this.configuration.setHelp(false);
		final Map<?, ?> args = context.getArguments();

		final String[] appArgs = (String[]) args.get("application.args");

		JCommander commander = null;
		try {
			commander = new JCommander(this.configuration, appArgs);

			AnalysisMain.LOGGER.setLevel(this.configuration.getLoggingLevel());

			if (this.configuration.isHelp()) {
				commander.usage();
			} else {
				if (this.checkDirectory(this.configuration.getProjectRootDirectory(), "Project root")
						&& this.checkFile(this.configuration.getProjectRootDirectory(), this.configuration.getObservedSystemPatternFile(), "System classes")
						&& this.checkFile(this.configuration.getProjectRootDirectory(), this.configuration.getDataTypePatternFile(), "Data classes")
						&& this.checkCanWriteToFile(this.configuration.getReportFile(), "Report file")) {
					final IPath location = new Path(this.configuration.getProjectRootDirectory().getAbsolutePath());
					this.runAnalysis(this.configuration.getProjectRootDirectory().getName(),
							location);
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

	/**
	 * Run analysis for the given project.
	 *
	 * @param projectName
	 *            name of the project
	 * @param projectLocation
	 *            location as Eclipse compatible path
	 * @throws CoreException
	 *             on internal errors of Eclipse components
	 * @throws InterruptedException
	 *             when the execution gets interrupted
	 */
	private void runAnalysis(final String projectName, final IPath projectLocation) throws CoreException, InterruptedException {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();

		final IProject project = workspace.getRoot().getProject(projectName);
		if (!project.exists()) {
			final IProjectDescription description = workspace.newProjectDescription(projectName);
			description.setNatureIds(new String[] { JavaCore.NATURE_ID });
			description.setLocation(projectLocation);
			project.create(description, null);
		} else {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		}
		project.open(null);

		final ConsoleOutputHandler outputHandler = new ConsoleOutputHandler(AnalysisMain.LOGGER);

		try {
			final IJavaProject javaProject = this.getJavaProject(project);

			if (javaProject != null) {
				final IClasspathEntry[] classPaths = this.createClassPath(project, this.configuration.getClassPaths(), this.configuration.getSourcePaths());

				javaProject.setRawClasspath(classPaths, false, null);

				final Job job = new JavaProjectAnalysisJob(this.createConfiguration(project),
						javaProject, outputHandler);
				job.schedule();
				job.join();

				project.close(null);
				workspace.save(true, null);
				outputHandler.info("Info", String.format("Analysis complete. Results stored in %s",
						this.configuration.getReportFile().getPath()));
				this.printResults(this.configuration.getReportFile(), outputHandler);
			} else {
				outputHandler.error("Project Error", "Project is not a Java project.");
			}
		} catch (final CoreException e) {
			outputHandler.error("Project Error", String.format("Could not open project. Cause: %s",
					e.getLocalizedMessage()));
		}
	}

	/**
	 * Collect all class path entries necessary for a project.
	 *
	 * @param project
	 *            the associated Eclipse project
	 * @param classPaths
	 *            a list of class path files
	 * @param sourcePaths
	 *            a list of source folders
	 * @return an array of class entries
	 */
	private IClasspathEntry[] createClassPath(final IProject project, final List<File> classPaths, final List<File> sourcePaths) {
		int i = 0;

		final IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		final LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);

		final IClasspathEntry[] entries = new IClasspathEntry[locations.length + classPaths.size() + sourcePaths.size()];

		/** system class path. */
		for (final LibraryLocation element : locations) {
			entries[i++] = JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null);
		}

		/** user class path. */
		for (final File classPath : classPaths) {
			final IPath path = new Path(classPath.getAbsolutePath());
			entries[i++] = JavaCore.newLibraryEntry(path, null, null);
		}

		/** source code. */
		for (final File sourcePath : sourcePaths) {
			final String path = sourcePath.getPath();
			final IPath source;
			if (path.startsWith(File.separator)) { // absolute path
				source = new Path(path);
			} else { // relative
				source = project.getFullPath().append(path);
			}
			entries[i++] = JavaCore.newSourceEntry(source);
		}

		return entries;
	}

	/**
	 * Get Java project of a project.
	 *
	 * @throws CoreException
	 */
	private IJavaProject getJavaProject(final IProject project) throws CoreException {
		if (project.hasNature(JavaCore.NATURE_ID)) {
			return JavaCore.create(project);
		} else {
			return null;
		}
	}

	private JavaAnalysisConfiguration createConfiguration(final IProject project) {
		final JavaAnalysisConfiguration javaAnalysisConfiguration = new JavaAnalysisConfiguration();

		javaAnalysisConfiguration.setDataTypePatternFile(this.findFileInProject(project, this.configuration.getDataTypePatternFile()));
		javaAnalysisConfiguration.setObservedSystemPatternFile(this.findFileInProject(project, this.configuration.getObservedSystemPatternFile()));

		return javaAnalysisConfiguration;
	}

	private IFile findFileInProject(final IProject project, final File file) {
		if (file.isAbsolute()) { // this is potentially outside of the project
			return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(file.getAbsolutePath()));
		} else {
			return (IFile) project.findMember(file.getPath());
		}
	}

	private boolean checkCanWriteToFile(final File file, final String label) {
		if (file.getParentFile().isDirectory()) {
			return true;
		} else {
			AnalysisMain.LOGGER.error(String.format("%s file '%s' cannot be written to %s. Path does not exist.", label, file.getAbsolutePath(), file.getParent()));
			return false;
		}
	}

	private boolean checkFile(final File base, final File file, final String label) {
		final File absoluteFile = file.isAbsolute() ? file : new File(base, file.getPath());
		if (absoluteFile.exists()) {
			if (absoluteFile.isFile()) {
				return true;
			} else {
				AnalysisMain.LOGGER.error(String.format("%s file '%s' is not a file.", label, absoluteFile.getAbsolutePath()));
				return false;
			}
		} else {
			AnalysisMain.LOGGER.error(String.format("%s directory '%s' is not file.", label, absoluteFile.getAbsolutePath()));
			return false;
		}
	}

	private boolean checkDirectory(final File directory, final String label) {
		if (directory.exists()) {
			if (directory.isDirectory()) {
				return true;
			} else {
				AnalysisMain.LOGGER.error(String.format("%s directory '%s' is not a directory.", label, directory.getAbsolutePath()));
				return false;
			}
		} else {
			AnalysisMain.LOGGER.error(String.format("%s directory '%s' does not exist.", label, directory.getAbsolutePath()));
			return false;
		}
	}

	private Appender createStdoutAppender() {
		final Appender appender = new ConsoleAppender(new PatternLayout("%m%n"));
		appender.setName("stdout");
		return appender;
	}

	@Override
	public void stop() {
		// nothing to do
	}

	private void printResults(final File reportFile, final IOutputHandler outputHandler) {
		try {
			final FileWriter fileWriter = new FileWriter(reportFile);
			final PrintWriter printWriter = new PrintWriter(fileWriter);
			for (final NamedValue namedValue : LogModelProvider.INSTANCE.getMessages()) {
				printWriter.printf("%s; %s; %s\n", namedValue.getProjectName(),
						namedValue.getPropertyName(), namedValue.getValue());
			}

			printWriter.printf("\n\n%s\n", AnalysisResultModelProvider.INSTANCE.getProject().getName());

			for (final NamedValue namedValue : AnalysisResultModelProvider.INSTANCE.getValues()) {
				printWriter.printf("%s; %s; %s\n", namedValue.getProjectName(),
						namedValue.getPropertyName(), namedValue.getValue());
			}

			printWriter.close();
		} catch (final IOException e) {
			outputHandler.error("Output Error",
					String.format("Cannot write report to %s  Cause: %s",
							reportFile.getAbsolutePath(), e.getLocalizedMessage()));
		}
	}

}
