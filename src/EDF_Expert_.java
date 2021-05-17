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
