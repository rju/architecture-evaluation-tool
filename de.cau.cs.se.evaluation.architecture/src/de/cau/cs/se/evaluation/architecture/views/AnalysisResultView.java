package de.cau.cs.se.evaluation.architecture.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;

import de.cau.cs.se.evaluation.architecture.transformation.metrics.NamedValue;
import de.cau.cs.se.evaluation.architecture.transformation.metrics.ResultModelProvider;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class AnalysisResultView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.cau.cs.se.evaluation.architecture.views.AnalysisResultView";

	private TableViewer viewer;


	/**
	 * The constructor.
	 */
	public AnalysisResultView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		this.viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

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
		final TableViewerColumn columnName = new TableViewerColumn(this.viewer, SWT.NONE);
		columnName.getColumn().setWidth(200);
		columnName.getColumn().setText("Name");
		columnName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final NamedValue value = (NamedValue) element;
				return value.getName();
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
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}

	public void update(final ResultModelProvider provider) {
		this.setFocus();
		this.viewer.refresh();
	}

}