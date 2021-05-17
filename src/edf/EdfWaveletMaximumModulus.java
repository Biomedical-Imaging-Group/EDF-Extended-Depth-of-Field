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

public abstract class EdfWaveletMaximumModulus extends AbstractEdfAlgorithm {

	abstract public ImageWare[] process(ImageWare imageStack);

	protected void majorityConsistencyCheck(ImageWare map, int windowSize, int nz) {
		this.majCCSubBand(map, windowSize, nz, 0);
		this.majCCSubBand(map, windowSize, nz, 1);
		this.majCCSubBand(map, windowSize, nz, 2);
	}

	protected void majCCSubBand(ImageWare map, int windowSize, int nz, int subBand) {
		ImageWare scale = null;
		int i, j, k, mx, my, x, y, startx, starty;
		int nx = map.getHeight();
		int ny = map.getWidth();
		short[][] arr = new short[windowSize][windowSize];
		int p, l;
		int size = windowSize * windowSize;
		short[] buf = new short[size];
		int count[] = new int[nz];
		int out = 0;

		for (i = 0; i < 3; i++) {
			j = 1;
			for (k = 0; k < i; k++)
				j *= 2;

			mx = nx / j / 2;
			my = ny / j / 2;

			switch (subBand) {
			case 0:
				startx = 0;
				starty = my;
				break;
			case 1:
				startx = mx;
				starty = 0;
				break;
			case 2:
				startx = mx;
				starty = my;
				break;
			default:
				throw new RuntimeException("Invalid SubBand");
			}

			short[][] arrtemp = new short[mx][my];

			map.getBoundedXY(startx, starty, 0, arrtemp);
			scale = Builder.create(arrtemp);

			for (x = 0; x < mx; x++) {
				for (y = 0; y < my; y++) {
					scale.getNeighborhoodXY(x, y, 0, arr, ImageWare.MIRROR);
					for (p = 0; p < windowSize; p++) {
						System.arraycopy(arr[p], 0, buf, p * windowSize, windowSize);
					}
					out = (int) buf[size / 2];
					for (l = 0; l < nz; l++) {
						count[l] = 0;
					}
					for (p = 0; p < size; p++) {
						for (l = 0; l < nz; l++) {
							if (buf[p] == l) count[l]++;
						}
					}
					for (l = 0; l < nz; l++) {
						if (count[l] > size / 2) {
							out = l;
						}
					}
					arrtemp[x][y] = (short) out;
				}
			}
			map.putBoundedXY(startx, starty, 0, arrtemp);
		}
	}

}
