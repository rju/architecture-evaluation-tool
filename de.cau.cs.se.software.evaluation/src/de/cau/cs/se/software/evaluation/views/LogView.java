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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;

/**
 * The Main analysis result view class.
 *
 * @author Reiner Jung
 */
public class LogView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.cau.cs.se.software.evaluation.views.LogView";

	private TableViewer viewer;

	/**
	 * The constructor.
	 */
	public LogView() {
		super();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		this.viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		// create Toolbar
		this.createToolbar();

		// create the columns
		this.createColumns();

		// make lines and header visible
		final Table table = this.viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		this.viewer.setContentProvider(ArrayContentProvider.getInstance());
		this.viewer.setInput(LogModelProvider.INSTANCE.getMessages());

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
		columnProject.getColumn().setText("Context");
		columnProject.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((NamedValue) element).getProjectName();
			}
		});

		final TableViewerColumn columnProperty = new TableViewerColumn(this.viewer, SWT.NONE);
		columnProperty.getColumn().setWidth(200);
		columnProperty.getColumn().setText("Fully qualified name");
		columnProperty.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((NamedValue) element).getPropertyName();
			}
		});

		// create a column for the first name
		final TableViewerColumn columnValue = new TableViewerColumn(this.viewer, SWT.NONE);
		columnValue.getColumn().setWidth(400);
		columnValue.getColumn().setText("Value");
		columnValue.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((NamedValue) element).getValue();
			}
		});

	}

	/**
	 * Adds Actions to Toolbar.
	 */
	private void createToolbar() {
		final IToolBarManager manager = this.getViewSite().getActionBars().getToolBarManager();
		manager.add(new Action("Clear log view", UIIcons.ICON_CLEAR_VIEW) {
			@Override
			public void run() {
				LogModelProvider.INSTANCE.clearMessages();
				LogView.this.update();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}

	/**
	 * Trigger the update of the view based on the model data.
	 */
	public void update() {
		this.viewer.refresh();
	}

}
