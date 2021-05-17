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

package edf;

import imageware.Builder;
import imageware.ImageWare;

public class Variance {

	/**
	 * Compute the local variance for each pixel position in a square window of size windowSize.
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
