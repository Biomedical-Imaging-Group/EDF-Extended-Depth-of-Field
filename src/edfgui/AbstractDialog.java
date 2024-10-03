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
 * Author: Daniel Sage
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
	protected String STR_COPYRIGHT = "(c) 2010-2023, BIG, EPFL, Switzerland";
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
	 * The actionPerformed method in this class is called each time the Timer "goes
	 * off".
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
