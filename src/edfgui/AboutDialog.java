/*
 * EDF Extended Depth of Field
 * http://bigwww.epfl.ch/demo/edf/
 *
 * Organization: Biomedical Imaging Group (BIG)
 * Ecole Polytechnique Federale de Lausanne (EPFL), Lausanne, Switzerland
 * Authors: Daniel Sage, Alex Prudencio, Jesse Berent, Niels Quack, Brigitte Forster
 * 
 * Reference: B. Forster, D. Van De Ville, J. Berent, D. Sage, M. Unser
 * Complex Wavelets for Extended Depth-of-Field: A New Method for the Fusion
 * of Multichannel Microscopy Images, Microscopy Research and Techniques, 2004
 * 
 * Condition of use: We expect you to include adequate citation whenever you present 
 * or publish results that are based on it.
 * 
 * History:
 * - Updated (Daniel Sage, 21 December 2010)
 * - Updated (Daniel Sage, 17 May 2021)
 */

/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2007-2021, EPFL, All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
	private Frame owner;

	/**
	 * This is the default constructor
	 */
	public AboutDialog(Frame owner) {
		super(owner);
		this.owner = owner;
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
					+ "<p class=\"author\">Daniel Sage, Alex Prudencio, Jesse Berent, Niels Quack, Brigitte Forster</p>" + "<p class=\"orga\">Biomedical Imaging Group<br>"
					+ "Ecole Polytechnique Federale de Lausanne<br>" + "Lausanne, Switzerland</p>" + "<p class=\"desc\">11 July 2011 - 17 May 2021<hr></p>"
					+ "<p class=\"help\"><b>Reference:</b> B. Forster, D. Van De Ville, J. Berent, D. Sage, M. Unser, "
					+ "Complex Wavelets for Extended Depth-of-Field: A New Method for the Fusion of Multichannel Microscopy Images, "
					+ "Microscopy Research and Technique, vol. 65, September 2004.<br>" + "http://bigwww.epfl.ch/publications/forster0404.html</p>"
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
		return "<style type=text/css>" + "body {backgroud-color:#222277}" + "hr {width:80% color:#333366; padding-top:7px }"
				+ "p, li {margin-left:10px;margin-right:10px; color:#000000; font-size:1em; font-family:Verdana,Helvetica,Arial,Geneva,Swiss,SunSans-Regular,sans-serif}"
				+ "p.name {color:#ffffff; font-size:1.2em; font-weight: bold; background-color: #333366; text-align:center;}" + "p.vers {color:#333333; text-align:center;}"
				+ "p.desc {color:#333333; font-weight: bold; text-align:center;}" + "p.auth {color:#333333; font-style: italic; text-align:center;}"
				+ "p.orga {color:#333333; text-align:center;}" + "p.date {color:#333333; text-align:center;}" + "p.more {color:#333333; text-align:center;}"
				+ "p.help {color:#000000; text-align:left;}" + "</style>";
	}

}
