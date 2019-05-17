/***************************************************************************
 * Copyright (C) 2019
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
import java.util.List;

import org.apache.log4j.Level;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

/**
 * @author Reiner Jung
 *
 */
public class AnalysisConfiguration {
	@Parameter(names = { "-p", "--project" }, description = "Project root", required = true, converter = FileConverter.class)
	private File projectRootDirectory;

	@Parameter(names = { "-s", "--system" }, description = "File containing regex for system classes.", required = true, converter = FileConverter.class)
	private File observedSystemPatternFile;

	@Parameter(names = { "-d", "--data-classes" }, description = "Kieker API version.", required = true, converter = FileConverter.class)
	private File dataTypePatternFile;

	@Parameter(names = { "-j", "--java-version" }, description = "Assume specified Java version for code analysis", required = false)
	private String javaVersion;

	@Parameter(names = { "-v", "--verbose" }, description = "Verbosity level, default is INFO", converter = LoggingLevelConverter.class)
	private Level loggingLevel;

	@Parameter(names = { "-cp", "--class-path" }, variableArity = true, splitter = PathParameterSplitter.class,
			description = "Multiple class paths separated by : or ;", converter = FileConverter.class)
	private List<File> classPaths;

	@Parameter(names = { "-c", "--source-path" }, variableArity = true, splitter = PathParameterSplitter.class, required = true,
			description = "Multiple relative or absolute path to source folders separated by : or ;", converter = FileConverter.class)
	private List<File> sourcePaths;

	@Parameter(names = { "-r", "--report" }, description = "Write report to this file.", required = true, converter = FileConverter.class)
	private File reportFile;

	@Parameter(names = { "-h", "--help" }, help = true)
	private boolean help;

	public File getProjectRootDirectory() {
		return this.projectRootDirectory;
	}

	public File getObservedSystemPatternFile() {
		return this.observedSystemPatternFile;
	}

	public File getDataTypePatternFile() {
		return this.dataTypePatternFile;
	}

	public String getJavaVersion() {
		return this.javaVersion;
	}

	public Level getLoggingLevel() {
		return this.loggingLevel;
	}

	public void setLoggingLevel(final Level loggingLevel) {
		this.loggingLevel = loggingLevel;
	}

	public File getReportFile() {
		return this.reportFile;
	}

	public boolean isHelp() {
		return this.help;
	}

	public void setHelp(final boolean help) {
		this.help = help;
	}

	public List<File> getClassPaths() {
		return this.classPaths;
	}

	public List<File> getSourcePaths() {
		return this.sourcePaths;
	}

}
