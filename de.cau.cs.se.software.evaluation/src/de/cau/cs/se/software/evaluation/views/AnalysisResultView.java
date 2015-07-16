/***************************************************************************
 * Copyright 2015
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
package de.cau.cs.se.software.evaluation.views;

import java.io.IOException;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;

import de.cau.cs.se.software.evaluation.Activator;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;

/**
 * The Main analysis result view class.
 *
 * @author Reiner Jung
 */
public class AnalysisResultView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.cau.cs.se.software.evaluation.views.AnalysisResultView";

	private TableViewer viewer;
	private Action exportDataAction;
	private Action exportHypergraphAction;
	private Action visualizeAction;
	private ModularHypergraph graph = null;
	private IJavaProject project = null;

	/**
	 * The constructor.
	 */
	public AnalysisResultView() {
		super();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		this.viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		// create Actions
		this.createActions();
		// create Toolbar
		this.createToolbar();

		// create the columns
		this.createColumns();

		// make lines and header visible
		final Table table = this.viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		this.viewer.setContentProvider(ArrayContentProvider.getInstance());
		this.viewer.setInput(ResultModelProvider.INSTANCE.getValues());

		// define layout for the viewer
		final GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		this.viewer.getControl().setLayoutData(gridData);
	}

	private void createColumns() {
		// create a column for the first name
		final TableViewerColumn columnProject = new TableViewerColumn(this.viewer, SWT.NONE);
		columnProject.getColumn().setWidth(200);
		columnProject.getColumn().setText("Project");
		columnProject.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final NamedValue value = (NamedValue) element;
				return value.getProjectName();
			}
		});

		final TableViewerColumn columnProperty = new TableViewerColumn(this.viewer, SWT.NONE);
		columnProperty.getColumn().setWidth(200);
		columnProperty.getColumn().setText("Property");
		columnProperty.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final NamedValue value = (NamedValue) element;
				return value.getPropertyName();
			}
		});

		// create a column for the first name
		final TableViewerColumn columnValue = new TableViewerColumn(this.viewer, SWT.NONE);
		columnValue.getColumn().setWidth(200);
		columnValue.getColumn().setText("Value");
		columnValue.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final NamedValue value = (NamedValue) element;
				return Double.toString(value.getValue());
			}
		});

	}

	/**
	 * Creates Actions for Toolbar.
	 */
	private void createActions() {

		final ActionHandler actionHandler = new ActionHandler();

		this.exportDataAction = new Action("Data Export", Activator.getImageDescriptor("/icons/data-export.gif")) {
			@Override
			public void run() {
				try {
					actionHandler.exportData(AnalysisResultView.this.viewer,
							AnalysisResultView.this.getSite().getShell(),
							AnalysisResultView.this.project);
				} catch (final IOException e) {
					MessageDialog.openError(AnalysisResultView.this.viewer.getControl().getShell(),
							"Export Error", "Error exporting data set " + e.getLocalizedMessage());
				}
			}

		};

		this.exportHypergraphAction = new Action("Graph Export", Activator.getImageDescriptor("/icons/graph-export.gif")) {
			@Override
			public void run() {
				try {
					actionHandler.exportGraph(AnalysisResultView.this.graph,
							AnalysisResultView.this.getSite().getShell(),
							AnalysisResultView.this.project);
				} catch (final IOException e) {
					MessageDialog.openError(AnalysisResultView.this.viewer.getControl().getShell(),
							"Export Error", "Error exporting hypergraph " + e.getLocalizedMessage());
				}
			}
		};

		this.visualizeAction = new Action("Graph Visualization", Activator.getImageDescriptor("/icons/graph.gif")) {
			@Override
			public void run() {
				actionHandler.visualize();
			}
		};

	}

	/**
	 * Adds Actions to Toolbar.
	 */
	private void createToolbar() {
		final IToolBarManager manager = this.getViewSite().getActionBars().getToolBarManager();
		manager.add(this.exportDataAction);
		manager.add(this.exportHypergraphAction);
		manager.add(this.visualizeAction);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}

	public void setGraph(final ModularHypergraph graph) {
		this.graph = graph;
	}

	/**
	 * Trigger the update of the view based on the model data.
	 */
	public void update() {
		this.setFocus();
		this.viewer.refresh();
	}

	public void setProject(final IJavaProject project) {
		this.project = project;
	}
}
