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

package edf;

import ij.ImageStack;
import ij.process.ColorProcessor;
import imageware.Builder;
import imageware.ImageWare;

public class PostProcessing {

	public static ImageWare reassignment(ImageWare res, ImageWare stack) {
		int nx = stack.getSizeX();
		int ny = stack.getSizeY();
		int nz = stack.getSizeZ();
		double stackval, pixelval, temp, diff, finalpixelval;
		float finalPos;
		ImageWare topology = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		for (int i = 0; i < nx; i++) {
			for (int j = 0; j < ny; j++) {
				temp = Double.MAX_VALUE;
				diff = 0.0;
				finalpixelval = 0.0;
				finalPos = 0;
				for (int k = 0; k < nz; k++) {
					stackval = (double) stack.getPixel(i, j, k);
					pixelval = (double) res.getPixel(i, j, 0);
					diff = Math.abs(stackval - pixelval);
					if (diff < temp) {
						temp = diff;
						finalpixelval = stackval;
						finalPos = (float) (k + 1);
					}
				}
				res.putPixel(i, j, 0, finalpixelval);
				topology.putPixel(i, j, 0, finalPos);
			}
		}
		return topology;
	}

	public static ColorProcessor reassignmentColor(ImageWare topology, ImageStack stack) {
		int nx = topology.getSizeX();
		int ny = topology.getSizeY();
		ColorProcessor cp = new ColorProcessor(nx, ny);
		int color = 0;
		int index;
		for (int x = 0; x < nx; x++) {
			for (int y = 0; y < ny; y++) {
				index = (int) topology.getPixel(x, y, 0);
				color = ((ColorProcessor) stack.getProcessor(index)).getPixel(x, y);
				cp.putPixel(x, y, color);
			}
		}
		return cp;
	}

}
