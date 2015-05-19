package de.cau.cs.se.evaluation.architecture.views;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import de.cau.cs.se.evaluation.architecture.transformation.metrics.NamedValue;

class ActionHandler {


	protected void exportData(final TableViewer table) throws IOException{
		String loc = null;
		if(table.getTable().getItems().length == 0){
			MessageDialog.openWarning(null, "Missing values", "There is nothing to export.");
		}
		else{
			final JFrame frame = new JFrame();
			final JFileChooser  fileChooser = new JFileChooser(".");
			final FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv","csv");
			fileChooser.setFileFilter(filter);
			final int returnVal = fileChooser.showSaveDialog(frame);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".csv")){
					loc = fileChooser.getSelectedFile().getAbsolutePath().concat(".csv");
				}
				else{
					loc = fileChooser.getSelectedFile().getAbsolutePath();
				}

				final File result = new File(loc);
				final BufferedWriter br = new BufferedWriter(new FileWriter(result));
				final StringBuilder sb = new StringBuilder();
				for (final TableItem element : table.getTable().getItems()) {
					sb.append(((NamedValue)element.getData()).getName() + ": " + ((NamedValue)element.getData()).getValue());
					sb.append("," + "\n");
				}
				br.write(sb.toString());
				br.close();
			}
		}
	}

	protected void exportGraph(){
		MessageDialog.openWarning(null, "Not implemented", "Not implemented yet.");
	}

	protected void visualize(){
		MessageDialog.openWarning(null, "Not implemented", "Not implemented yet.");
	}
}