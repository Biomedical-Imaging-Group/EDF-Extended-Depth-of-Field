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
 * F
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
import wavelets.ComplexWavelet;
import wavelets.ImageAccess;

public class EdfComplexWavelets extends EdfWaveletMaximumModulus {

	private int nScales = 3;
	private int length = 6;
	private boolean sbConsistencyCheck = false;
	private boolean majConsistencyCheck = false;
	private int majWindowSize = 5;

	public EdfComplexWavelets(int length, int nScales, boolean sbConsistencyCheck, boolean majConsistencyCheck) {
		this.length = length;
		this.nScales = nScales;
		this.sbConsistencyCheck = sbConsistencyCheck;
		this.majConsistencyCheck = majConsistencyCheck;
	}

	public ImageWare[] process(ImageWare imageStack) {

		LogSingleton log = LogSingleton.getInstance();

		int nx = imageStack.getSizeX();
		int ny = imageStack.getSizeY();
		int nz = imageStack.getSizeZ();

		double newval, oldval;
		int i, j, k;
		double[][] buf = new double[nx][ny];

		ImageAccess slice;
		ImageAccess coefftempRe = null;
		ImageAccess coefftempIm = null;
		ImageAccess[] coefftemp = { null, null };

		ImageWare res = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		ImageWare resRe = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		ImageWare resIm = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		ImageWare temp = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		ImageWare heightMap = Builder.create(nx, ny, 1, ImageWare.SHORT);
		ImageWare coeffStackRe = null;
		ImageWare coeffStackIm = null;

		double tempvalRe, tempvalIm;

		if (this.sbConsistencyCheck || this.majConsistencyCheck) {
			coeffStackRe = Builder.create(nx, ny, nz, ImageWare.FLOAT);
			coeffStackIm = Builder.create(nx, ny, nz, ImageWare.FLOAT);
		}

		for (k = 0; k < nz; k++) {

			log.setProgessLength(15 + k * (65 / nz));

			imageStack.getXY(0, 0, k, buf);
			slice = new ImageAccess(buf);

			coefftemp = ComplexWavelet.analysis(slice, nScales, length);
			coefftempRe = coefftemp[0];
			coefftempIm = coefftemp[1];
			for (i = 0; i < nx; i++) {
				for (j = 0; j < ny; j++) {
					tempvalRe = coefftempRe.getPixel(i, j);
					tempvalIm = coefftempIm.getPixel(i, j);
					newval = tempvalRe * tempvalRe + tempvalIm * tempvalIm;
					oldval = temp.getPixel(i, j, 0);
					if (oldval < newval) {
						temp.putPixel(i, j, 0, newval);
						heightMap.putPixel(i, j, 0, k);
						resRe.putPixel(i, j, 0, tempvalRe);
						resIm.putPixel(i, j, 0, tempvalIm);
					}
				}
			}
			if (this.sbConsistencyCheck || this.majConsistencyCheck) {
				coeffStackRe.putXY(0, 0, k, coefftempRe.getArrayPixels());
				coeffStackIm.putXY(0, 0, k, coefftempIm.getArrayPixels());
			}
		}

		if (this.sbConsistencyCheck) {
			this.subBandConsistencyCheck(heightMap, resRe, resIm);
		}

		if (this.majConsistencyCheck) {
			majorityConsistencyCheck(heightMap, majWindowSize, nz);
		}

		if (this.sbConsistencyCheck || this.majConsistencyCheck) {
			updateMergedCoeff(new ImageWare[] { coeffStackRe, coeffStackIm }, heightMap,
					new ImageWare[] { resRe, resIm });
		}

		double[][] iabufRe = new double[nx][ny];
		double[][] iabufIm = new double[nx][ny];
		resRe.getXY(0, 0, 0, iabufRe);
		resIm.getXY(0, 0, 0, iabufIm);
		coefftempRe.putArrayPixels(iabufRe);
		coefftempIm.putArrayPixels(iabufIm);

		coefftemp = ComplexWavelet.synthesis(coefftempRe, coefftempIm, nScales, length);
		coefftempRe = coefftemp[0];
		coefftempIm = coefftemp[1];
		res.putXY(0, 0, 0, coefftempRe.getArrayPixels());
		return new ImageWare[] { res, heightMap };
	}

	private void updateMergedCoeff(ImageWare[] coeffStack, ImageWare map, ImageWare[] coeff) {
		int nx = coeffStack[0].getSizeX();
		int ny = coeffStack[0].getSizeY();
		int i, j;
		for (i = 0; i < nx; i++) {
			for (j = 0; j < ny; j++) {
				coeff[0].putPixel(i, j, 0, coeffStack[0].getPixel(i, j, (int) map.getPixel(i, j, 0)));
				coeff[1].putPixel(i, j, 0, coeffStack[1].getPixel(i, j, (int) map.getPixel(i, j, 0)));
			}
		}
	}

	private void subBandConsistencyCheck(ImageWare map, ImageWare coeffRe, ImageWare coeffIm) {

		int i, j, k, mx, my, x, y;
		int a, b, c;
		double va, vb, vc;

		int nx = coeffRe.getHeight();
		int ny = coeffRe.getWidth();

		for (i = 0; i < 3; i++) {

			j = 1;
			for (k = 0; k < i; k++)
				j *= 2;

			mx = nx / j;
			my = ny / j;

			for (x = mx / 2; x < mx; x++) {
				for (y = 0; y < my / 2; y++) {

					a = (int) map.getPixel(x, y, 0);
					b = (int) map.getPixel(x, y + my / 2, 0);
					c = (int) map.getPixel(x - mx / 2, y + my / 2, 0);

					if (a == b) {
						if (a == c) {
							continue;
						} else {
							map.putPixel(x - mx / 2, y + my / 2, 0, a);
						}
					} else {
						if (a == c) {
							map.putPixel(x, y + my / 2, 0, a);
						} else {
							if (b == c) {
								map.putPixel(x, y, 0, b);
							} else {
								va = modulus2(coeffRe.getPixel(x, y, 0), coeffIm.getPixel(x, y, 0));
								vb = modulus2(coeffRe.getPixel(x, y + my / 2, 0), coeffIm.getPixel(x, y + my / 2, 0));
								vc = modulus2(coeffRe.getPixel(x - mx / 2, y + my / 2, 0),
										coeffIm.getPixel(x - mx / 2, y + my / 2, 0));

								if (va > vb && va > vc) {
									map.putPixel(x - mx / 2, y + my / 2, 0, a);
									map.putPixel(x, y + my / 2, 0, a);
								} else {
									if (vb > va && vb > vc) {
										map.putPixel(x - mx / 2, y + my / 2, 0, b);
										map.putPixel(x, y, 0, b);
									} else {
										if (vc > va && vc > vb) {
											map.putPixel(x, y + my / 2, 0, c);
											map.putPixel(x, y, 0, c);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private double modulus2(double x, double y) {
		return x * x + y * y;
	}

}
