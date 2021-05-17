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

public class Sobel {

	static public ImageWare compute(ImageWare input) {
		int nx = input.getWidth();
		int ny = input.getHeight();

		ImageWare gx = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		ImageWare gy = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		float rowin[] = new float[nx];
		float rowout[] = new float[nx];
		for (int y = 0; y < ny; y++) {
			input.getX(0, y, 0, rowin);
			sobelDifference(rowin, rowout);
			gx.putX(0, y, 0, rowout);
			sobelAverage(rowin, rowout);
			gy.putX(0, y, 0, rowout);
		}
		float colin[] = new float[ny];
		float colout[] = new float[ny];
		for (int x = 0; x < nx; x++) {
			gx.getY(x, 0, 0, colin);
			sobelAverage(colin, colout);
			gx.putY(x, 0, 0, colout);
			gy.putY(x, 0, 0, colin);
			sobelDifference(colin, colout);
			gy.putY(x, 0, 0, colout);
		}

		gx.pow(2);
		gy.pow(2);
		gx.add(gy);
		gx.sqrt();
		return gx;
	}

	/**
	 * Implements an 1D sobel difference filter. The kernel is: [-1, 0, 1]
	 *
	 * @param in  input, array which should be filtered
	 * @param out output, filtered array of the type float
	 */
	static private void sobelDifference(float in[], float out[]) {
		int n = in.length;
		out[0] = 0;
		for (int k = 1; k < n - 1; k++) {
			out[k] = in[k + 1] - in[k - 1];
		}
		out[n - 1] = 0;
	}

	/**
	 * Implements an 1D sobel average filter. The kernel is: [1, 2, 1]
	 *
	 * @param in  input, array which should be filtered
	 * @param out output, filtered array of the type float
	 */
	static private void sobelAverage(float in[], float out[]) {
		int n = in.length;
		out[0] = 2 * in[0] + 2 * in[1];
		for (int k = 1; k < n - 1; k++) {
			out[k] = in[k - 1] + 2 * in[k] + in[k + 1];
		}
		out[n - 1] = 2 * in[n - 2] + 2 * in[n - 1];
	}

}