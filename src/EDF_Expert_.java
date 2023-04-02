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

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GUI;
import ij.plugin.PlugIn;
import edfgui.AbstractDialog;
import edfgui.AdvancedDialog;

public class EDF_Expert_ implements PlugIn {

	/**
	 * Implement the run method of PlugIn.
	 */
	public void run(String arg) {

		// Check the ImageJ version
		if (IJ.versionLessThan("1.21a")) return;

		// Check the presence of the image
		ImagePlus imp = WindowManager.getCurrentImage();
		if (imp == null) {
			IJ.error("No stack of images open");
			return;
		}

		// Check the size of the image
		if (imp.getWidth() < 4) {
			IJ.error("The image is too small (nx=" + imp.getWidth() + ")");
			return;
		}

		if (imp.getHeight() < 4) {
			IJ.error("The image is too small (ny=" + imp.getHeight() + ")");
			return;
		}

		if (imp.getStackSize() < 2) {
			IJ.error("The stack of images is too small (nz=" + imp.getStackSize() + ")");
			return;
		}

		// Color or grayscale image
		boolean color = false;
		if (imp.getType() == ImagePlus.COLOR_RGB) color = true;
		else if (imp.getType() == ImagePlus.GRAY8) color = false;
		else if (imp.getType() == ImagePlus.GRAY16) color = false;
		else if (imp.getType() == ImagePlus.GRAY32) color = false;
		else {
			IJ.error("Only process 8-bits, 16-bits, 32-bits and RGB images");
			return;
		}

		AbstractDialog dl = new AdvancedDialog(new int[] { imp.getWidth(), imp.getHeight() }, color);
		dl.pack();
		GUI.center(dl);
		dl.setVisible(true);
	}
}
