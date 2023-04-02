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

public class Variance {

	/**
	 * Compute the local variance for each pixel position in a square window of size
	 * windowSize.
	 * 
	 * @param input
	 * @param windowSize
	 * @return an ImageAccess object containing the variance.
	 */
	static public ImageWare compute(ImageWare input, int windowSize) {

		int nx = input.getWidth();
		int ny = input.getHeight();

		ImageWare output = Builder.create(nx, ny, 1, ImageWare.FLOAT);

		float[][] buf = new float[windowSize][windowSize];
		int wlen = windowSize * windowSize;
		float[] arr = new float[wlen];

		int x, y, j;
		float ave, var, temp;

		// Loop through the image.
		for (x = 0; x < nx; x++) {
			for (y = 0; y < ny; y++) {

				ave = 0;
				var = 0;

				input.getNeighborhoodXY(x, y, 0, buf, ImageWare.MIRROR);

				// Transform neighborghood to 1-D array.
				for (j = 0; j < windowSize; j++) {
					System.arraycopy(buf[j], 0, arr, j * windowSize, windowSize);
				}

				// compute average.
				for (j = 0; j < wlen; j++) {
					ave += arr[j];
				}
				ave /= wlen;

				// variance.
				for (j = 0; j < wlen; j++) {
					temp = arr[j] - ave;
					var += temp * temp;
				}

				output.putPixel(x, y, 0, var);
			}
		}
		return output;
	}

}
