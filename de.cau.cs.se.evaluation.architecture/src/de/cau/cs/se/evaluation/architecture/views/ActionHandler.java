package de.cau.cs.se.evaluation.architecture.views;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import de.cau.cs.se.evaluation.architecture.transformation.metrics.NamedValue;

/**
 * Handles Actions for Buttons in AnalysisResultView.
 *
 * @author Yannic Kropp
 *
 */
class ActionHandler {


	/**
	 * Action-Logic for 'export_Data'-Button in AnalysisResultView.
	 */
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

	/**
	 * Action-Logic for 'export_Graph'-Button in AnalysisResultView.
	 */
	protected void exportGraph(final EObject model) throws IOException{

		if (model == null){
			MessageDialog.openWarning(null, "Missing EPackage", "No Graph (EPackage) found.");
		}
		else{
			String loc = null;
			final JFrame frame = new JFrame();
			final JFileChooser  fileChooser = new JFileChooser(".");
			final FileNameExtensionFilter filter = new FileNameExtensionFilter(".ecore","ecore");
			fileChooser.setFileFilter(filter);
			final int returnVal = fileChooser.showSaveDialog(frame);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".ecore")){
					loc = fileChooser.getSelectedFile().getAbsolutePath().concat(".ecore");
				}
				else{
					loc = fileChooser.getSelectedFile().getAbsolutePath();
				}

				final ResourceSet resourceSet = new ResourceSetImpl();

				// Register XML Factory implementation to handle .ecore files
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMLResourceFactoryImpl());

				// Create empty resource with the given URI
				final Resource resource = resourceSet.createResource(URI.createURI("./graph.xml"));

				// Add model to contents list of the resource
				resource.getContents().add(model);

				// Save the resource
				final File destination = new File(loc);
				final FileOutputStream stream = new FileOutputStream(destination);
				try {
					resource.save(stream, null);
				} catch (final IOException e) {
					e.printStackTrace();
				}
				stream.close();
			}
		}
	}

	/**
	 * Action-Logic for 'visualize_Graph'-Button in AnalysisResultView.
	 */
	protected void visualize(){
		MessageDialog.openWarning(null, "Not implemented", "Not implemented yet.");
	}
}