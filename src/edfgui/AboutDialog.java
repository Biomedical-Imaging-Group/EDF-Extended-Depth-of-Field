/* 
 * EDF - Extended Depth of Field
 * 
 * Reference: B. Forster et al., Complex Wavelets for Extended Depth-of-Field: 
 * A New Method for the Fusion of Multichannel Microscopy Images, 
 * Microscopy Research and Techniques, 2004.
 */

/*
 * Copyright 2006-2023 Biomedical Imaging Group at the EPFL.
 * 
 * EDF is free software: you can redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 * 
 * EDF is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * EDF. If not, see <http://www.gnu.org/licenses/>.
 */

/* 
 * Java Code Project: EDF - Extended Depth of Focus
 * 
 * Author: Daniel Sage, ALex Prudencio
 * Organization: Biomedical Imaging Group (BIG)
 * Ecole Polytechnique Federale de Lausanne (EPFL), Lausanne, Switzerland
 *
 * Information: http://bigwww.epfl.ch/demo/edf/
 *
 * References: 
 * B. Forster, D. Van De Ville, J. Berent, D. Sage, M. Unser
 * Complex Wavelets for Extended Depth-of-Field: A New Method for the Fusion
 * of Multichannel Microscopy Images, Microscopy Research and Techniques, 
 * 65(1-2), pp. 33-42, September 2004.
 * *
 * B. Forster, D. Van De Ville, J. Berent, D. Sage, M. Unser, 
 * Extended Depth-of-Focus for Multi-Channel Microscopy Images 
 * Proceedings of IEEE International Symposium on Biomedical Imaging, 2004.
 */

package edfgui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AboutDialog extends JDialog implements ActionListener {

	private JPanel jContentPane = null;
	private JButton jButtonOk = null;
	private JEditorPane jAbout = null;
	private JScrollPane jScrollPane = null;

	/**
	 * This is the default constructor
	 */
	public AboutDialog(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.setTitle("About EDF");
		this.pack();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints5.weighty = 1.0;
			gridBagConstraints5.insets = new java.awt.Insets(10, 10, 10, 10);
			gridBagConstraints5.weightx = 1.0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints4.gridy = 1;
			jContentPane = new JPanel(new GridBagLayout());
			jContentPane.add(getJScrollPane(), gridBagConstraints5);
			jContentPane.add(getJButtonOk(), gridBagConstraints4);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButtonOk
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton();
			jButtonOk.setText("OK");
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}

	/**
	 * This method initializes jTextAreaAbout
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JEditorPane getJAbout() {
		if (jAbout == null) {
			jAbout = new JEditorPane();
			jAbout.setEditable(false);
			jAbout.setContentType("text/html; charset=ISO-8859-1");
			jAbout.setBackground(Color.white);

			jAbout.setText("<html><head><title>EDF</title>" + getStyle() + "</head><body>" + "<p class=\"name\">EDF</p>"
					+ "<p class=\"desc\">ImageJ's plugin for Extended Depth of Field</p>"
					+ "<p class=\"author\">Daniel Sage, Alex Prudencio, Jesse Berent, Niels Quack, Brigitte Forster</p>"
					+ "<p class=\"orga\">Biomedical Imaging Group<br>" + "Ecole Polytechnique Federale de Lausanne<br>"
					+ "Lausanne, Switzerland</p>" + "<p class=\"desc\">11 July 2011 - 17 May 2021<hr></p>"
					+ "<p class=\"help\"><b>Reference:</b> B. Forster, D. Van De Ville, J. Berent, D. Sage, M. Unser, "
					+ "Complex Wavelets for Extended Depth-of-Field: A New Method for the Fusion of Multichannel Microscopy Images, "
					+ "Microscopy Research and Technique, vol. 65, September 2004.<br>"
					+ "http://bigwww.epfl.ch/publications/forster0404.html</p>"
					+ "<p class=\"help\"><b>Additional information</b> http://bigwww.epfl.ch/demo/edf/</p>"
					+ "<p class=\"help\"><b>3-D viewer</b> module is based on the SurfacePlot_3D plugin by Kai Uwe Barthel. "
					+ "http://rsb.info.nih.gov/ij/plugins/surface-plot-3d.html</p>");
			jAbout.setMargin(new Insets(10, 10, 10, 10));
		}
		return jAbout;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();

		if (source == jButtonOk) {
			this.dispose();
			System.gc();
		}
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJAbout());
			jScrollPane.setPreferredSize(new Dimension(600, 600));

		}
		return jScrollPane;
	}

	/**
	 * Defines the CSS style for the help and about window.
	 */
	private String getStyle() {
		return "<style type=text/css>" + "body {backgroud-color:#222277}"
				+ "hr {width:80% color:#333366; padding-top:7px }"
				+ "p, li {margin-left:10px;margin-right:10px; color:#000000; font-size:1em; font-family:Verdana,Helvetica,Arial,Geneva,Swiss,SunSans-Regular,sans-serif}"
				+ "p.name {color:#ffffff; font-size:1.2em; font-weight: bold; background-color: #333366; text-align:center;}"
				+ "p.vers {color:#333333; text-align:center;}"
				+ "p.desc {color:#333333; font-weight: bold; text-align:center;}"
				+ "p.auth {color:#333333; font-style: italic; text-align:center;}"
				+ "p.orga {color:#333333; text-align:center;}" + "p.date {color:#333333; text-align:center;}"
				+ "p.more {color:#333333; text-align:center;}" + "p.help {color:#000000; text-align:left;}"
				+ "</style>";
	}

}
