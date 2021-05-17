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
