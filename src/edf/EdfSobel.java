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

import imageware.Builder;
import imageware.ImageWare;

public class EdfSobel extends AbstractEdfAlgorithm {

	public EdfSobel() {
	}

	public ImageWare[] process(ImageWare imageStack) {
		LogSingleton log = LogSingleton.getInstance();
		int nx = imageStack.getSizeX();
		int ny = imageStack.getSizeY();
		int nz = imageStack.getSizeZ();
		int i, j, k;
		float newval, oldval;
		ImageWare topology, res;
		ImageWare slice, temp, sharpness;
		slice = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		temp = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		sharpness = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		topology = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		topology.add(1);
		imageStack.getXY(0, 0, 0, slice);
		res = slice.duplicate();

		for (k = 0; k < nz; k++) {
			log.setProgessLength(15 + k * (65 / nz));

			imageStack.getXY(0, 0, k, slice);
			sharpness = Sobel.compute(slice);
			for (i = 0; i < nx; i++) {
				for (j = 0; j < ny; j++) {
					newval = (float) sharpness.getPixel(i, j, 0);
					oldval = (float) temp.getPixel(i, j, 0);
					if (oldval < newval) {
						temp.putPixel(i, j, 0, newval);
						topology.putPixel(i, j, 0, k + 1);
						res.putPixel(i, j, 0, slice.getPixel(i, j, 0));
					}
				}
			}
			System.gc();
		}

		return new ImageWare[] { res, topology };

	}
}
