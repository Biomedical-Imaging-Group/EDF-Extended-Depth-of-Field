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
import wavelets.ImageAccess;
import wavelets.WaveSpline;

public class EdfRealWavelets extends EdfWaveletMaximumModulus {

	private int order = 1;
	private int nScales = 4;
	private boolean sbConsistencyCheck = false;
	private boolean majConsistencyCheck = false;
	private int majWindowSize = 5;
	private boolean doDenoising = false;
	private double denoisingRate = 10;

	public EdfRealWavelets(int order, int nScales, boolean sbConsistencyCheck, boolean majConsistencyCheck) {
		this.order = order;
		this.nScales = nScales;
		this.sbConsistencyCheck = sbConsistencyCheck;
		this.majConsistencyCheck = majConsistencyCheck;
		this.doDenoising = false;
	}

	public EdfRealWavelets(int order, int nScales, boolean sbConsistencyCheck, boolean majConsistencyCheck,
			double denoisingRate) {
		this.order = order;
		this.nScales = nScales;
		this.sbConsistencyCheck = sbConsistencyCheck;
		this.majConsistencyCheck = majConsistencyCheck;
		this.doDenoising = true;
		this.denoisingRate = denoisingRate;
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
		ImageAccess coefftemp = null;
		ImageWare res = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		ImageWare temp = Builder.create(nx, ny, 1, ImageWare.FLOAT);
		ImageWare heightMap = Builder.create(nx, ny, 1, ImageWare.SHORT);
		ImageWare coeffStack = null;

		double tempval;

		if (this.sbConsistencyCheck || this.majConsistencyCheck) {
			coeffStack = Builder.create(nx, ny, nz, ImageWare.FLOAT);
		}

		for (k = 0; k < nz; k++) {
			log.setProgessLength(15 + k * (65 / nz));
			imageStack.getXY(0, 0, k, buf);
			slice = new ImageAccess(buf);
			coefftemp = WaveSpline.analysis(slice, order, nScales);
			for (i = 0; i < nx; i++) {
				for (j = 0; j < ny; j++) {
					tempval = coefftemp.getPixel(i, j);
					newval = abs(tempval);
					oldval = temp.getPixel(i, j, 0);
					if (oldval < newval) {
						temp.putPixel(i, j, 0, newval);
						heightMap.putPixel(i, j, 0, k);
						res.putPixel(i, j, 0, tempval);
					}
				}
			}
			if (coeffStack != null) {
				coeffStack.putXY(0, 0, k, coefftemp.getArrayPixels());
			}
		}

		if (this.sbConsistencyCheck) {
			this.subBandConsistencyCheck(heightMap, res);
		}

		if (this.majConsistencyCheck) {
			majorityConsistencyCheck(heightMap, majWindowSize, nz);
		}

		if (this.sbConsistencyCheck || this.majConsistencyCheck) {
			updateMergedCoeff(coeffStack, heightMap, res);
		}

		if (doDenoising) {
			Tools.waveletDenoising(res, denoisingRate);
		}

		double[][] iabuf = new double[nx][ny];
		res.getXY(0, 0, 0, iabuf);
		coefftemp.putArrayPixels(iabuf);

		coefftemp = WaveSpline.synthesis(coefftemp, order, nScales);
		res.putXY(0, 0, 0, coefftemp.getArrayPixels());

		return new ImageWare[] { res, heightMap };

	}

	private void updateMergedCoeff(ImageWare coeffStack, ImageWare map, ImageWare coeff) {
		int nx = coeffStack.getSizeX();
		int ny = coeffStack.getSizeY();
		int i, j;
		for (i = 0; i < nx; i++) {
			for (j = 0; j < ny; j++) {
				coeff.putPixel(i, j, 0, coeffStack.getPixel(i, j, (int) map.getPixel(i, j, 0)));
			}
		}
	}

	private void subBandConsistencyCheck(ImageWare map, ImageWare coeff) {

		int i, j, k, mx, my, x, y;
		int a, b, c;
		double va, vb, vc;

		int nx = coeff.getHeight();
		int ny = coeff.getWidth();

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
						}
						else {
							map.putPixel(x - mx / 2, y + my / 2, 0, a);
						}
					}
					else {
						if (a == c) {
							map.putPixel(x, y + my / 2, 0, a);
						}
						else {
							if (b == c) {
								map.putPixel(x, y, 0, b);
							}
							else {
								va = abs(coeff.getPixel(x, y, 0));
								vb = abs(coeff.getPixel(x, y + my / 2, 0));
								vc = abs(coeff.getPixel(x - mx / 2, y + my / 2, 0));

								if (va > vb && va > vc) {
									map.putPixel(x - mx / 2, y + my / 2, 0, a);
									map.putPixel(x, y + my / 2, 0, a);
								}
								else {
									if (vb > va && vb > vc) {
										map.putPixel(x - mx / 2, y + my / 2, 0, b);
										map.putPixel(x, y, 0, b);
									}
									else {
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

	private double abs(double z) {
		if (z < 0) z = (-z);
		return z;
	}

}
