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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.Timer;

import edf.LogSingleton;

public abstract class AbstractDialog extends JFrame {

	public final static int ONE_SECOND = 1000;
	public Parameters parameters;
	public Thread threadEdf;
	public Thread threadTopoProc;
	public JProgressBar jProgressBar = null;
	public Timer timer;
	public JLabel jLabelMemMessage = null;
	protected GridBagLayout gbLayout = new GridBagLayout();
	protected GridBagConstraints gbConstraints = new GridBagConstraints();
	protected String STR_COPYRIGHT = "(c) 2010, BIG, EPFL, Switzerland";
	protected LogSingleton log = LogSingleton.getInstance();

	/**
	 * Constructor. Start the timer
	 */
	public AbstractDialog() {
		super();
		parameters = new Parameters();
		timer = new Timer(ONE_SECOND / 2, new TimerListener());
		jLabelMemMessage = new JLabel("");
	}

	/**
	 * Add a component in a panel in the northeast of the cell.
	 */
	protected void addComponent(JPanel pn, int row, int col, int width, int height, int space, Component comp) {
		gbConstraints.gridx = col;
		gbConstraints.gridy = row;
		gbConstraints.gridwidth = width;
		gbConstraints.gridheight = height;
		gbConstraints.weightx = 0.0;
		gbConstraints.weighty = 0.0;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.fill = GridBagConstraints.NONE;
		gbConstraints.insets = new Insets(space, space, space, space);
		gbLayout.setConstraints(comp, gbConstraints);
		pn.add(comp);
	}

	/**
	 * Add a component in a panel in the northeast of the cell.
	 */
	protected void addComponent(JToolBar pn, int row, int col, int width, int height, int space, Component comp) {
		gbConstraints.gridx = col;
		gbConstraints.gridy = row;
		gbConstraints.gridwidth = width;
		gbConstraints.gridheight = height;
		gbConstraints.weightx = 0.0;
		gbConstraints.weighty = 0.0;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.fill = GridBagConstraints.NONE;
		gbConstraints.insets = new Insets(space, space, space, space);
		gbLayout.setConstraints(comp, gbConstraints);
		pn.add(comp);
	}

	/**
	 * Cleanup properly and stop the threads.
	 */
	public void cleanup() {
		threadEdf = null;
		threadTopoProc = null;
		if (timer != null) {
			timer.stop();
			timer = null;
		}
		log.clear();
		log = null;
		System.gc();
	}

	/**
	 * The actionPerformed method in this class is called each time the Timer "goes off".
	 */
	class TimerListener implements ActionListener {
		private DecimalFormat formatMem = new DecimalFormat("000.0");

		public void actionPerformed(ActionEvent arg0) {
			try {
				int length = log.getProgessLength();
				jProgressBar.setValue(length);
				double mem = Runtime.getRuntime().freeMemory() / 1024.0 / 1024.0;
				double total = Runtime.getRuntime().totalMemory() / 1024.0 / 1024.0;
				jLabelMemMessage.setText("Memory " + formatMem.format(mem) + "/" + formatMem.format(total) + "MB");
				jLabelMemMessage.repaint();
				jProgressBar.setString(log.getElapsedTime());
				if (length == 100) timer.stop();
			}
			catch (Exception ex) {

			}
		}
	}

}
