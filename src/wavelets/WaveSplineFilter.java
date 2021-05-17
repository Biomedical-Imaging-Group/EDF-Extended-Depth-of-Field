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

package wavelets;

/**
 * This class generates the Spline wavelets filters.
 * 
 * @author <p style="background-color:#EEEEEE; border-top:1px solid #CCCCCC; border-bottom:1px solid #CCCCCC""> Daniel Sage<br>
 *         <a href="http://bigwww.epfl.ch">Biomedical Imaging Group</a> (BIG), Ecole Polytechnique Federale de Lausanne (EPFL), Lausanne, Switzerland<br>
 *         More information: http://bigwww.epfl.ch/
 *         </p>
 *         <p>
 *         You'll be free to use this software for research purposes, but you should not redistribute it without our consent. In addition, we expect you to include a citation or
 *         acknowledgement whenever you present or publish results that are based on it.
 *         </p>
 * 
 * @version 11 July 2009
 */

public class WaveSplineFilter {

	/**
	 * real lowpass filter.
	 */
	public double h[];

	/**
	 * real highpass filter.
	 */
	public double g[];

	WaveSplineFilter(int order) {
		switch (order) {

		case 0:
			h = new double[1];
			h[0] = Math.sqrt(2.0);
			break;

		case 1:
			h = new double[47];
			h[0] = 0.81764640621546;
			h[1] = 0.39729708810751;
			h[2] = -0.06910098743038;
			h[3] = -0.05194534825542;
			h[4] = 0.01697104840045;
			h[5] = 0.00999059568192;
			h[6] = -0.00388326235731;
			h[7] = -0.00220195129177;
			h[8] = 0.00092337104427;
			h[9] = 0.00051163604209;
			h[10] = -0.00022429633694;
			h[11] = -0.00012268632858;
			h[12] = 0.00005535633860;
			h[13] = 0.00003001119291;
			h[14] = -0.00001381880394;
			h[15] = -0.00000744435611;
			h[16] = 0.00000347980027;
			h[17] = 0.00000186561005;
			h[18] = -0.00000088225856;
			h[19] = -0.00000047122304;
			h[20] = 0.00000022491351;
			h[21] = 0.00000011976480;
			h[22] = -0.00000005759525;
			h[23] = -0.00000003059265;
			h[24] = 0.00000001480431;
			h[25] = 0.00000000784714;
			h[26] = -0.00000000381742;
			h[27] = -0.00000000201987;
			h[28] = 0.00000000098705;
			h[29] = 0.00000000052147;
			h[30] = -0.00000000025582;
			h[31] = -0.00000000013497;
			h[32] = 0.00000000006644;
			h[33] = 0.00000000003501;
			h[34] = -0.00000000001729;
			h[35] = -0.00000000000910;
			h[36] = 0.00000000000451;
			h[37] = 0.00000000000237;
			h[38] = -0.00000000000118;
			h[39] = -0.00000000000062;
			h[40] = 0.00000000000031;
			h[41] = 0.00000000000016;
			h[42] = -0.00000000000008;
			h[43] = -0.00000000000004;
			h[44] = 0.00000000000002;
			h[45] = 0.00000000000001;
			break;

		case 3:
			double temp[] = { 0.76613005375980, 0.43392263358931, -0.05020172467149, -0.11003701838811, 0.03208089747022, 0.04206835144072, -0.01717631549201, -0.01798232098097,
					0.00868529481309, 0.00820147720600, -0.00435383945777, -0.00388242526560, 0.00218671237015, 0.00188213352389, -0.00110373982039, -0.00092719873146,
					0.00055993664336, 0.00046211522752, -0.00028538371867, -0.00023234729403, 0.00014604186978, 0.00011762760216, -0.00007499842461, -0.00005987934057,
					0.00003863216129, 0.00003062054907, -0.00001995254847, -0.00001571784835, 0.00001032898225, 0.00000809408097, -0.00000535805976 - 0.00000417964096,
					0.00000278450629, 0.00000216346143, -0.00000144942177, -0.00000112219704, 0.00000075557065, 0.00000058316635, -0.00000039439119, -0.00000030355006,
					0.00000020610937, 0.00000015823692, -0.00000010783016, -0.00000008259641, 0.00000005646954, 0.00000004316539, -0.00000002959949, -0.00000002258313,
					0.00000001552811, 0.00000001182675, -0.00000000815248, -0.00000000619931, 0.00000000428324, 0.00000000325227, -0.00000000225188, -0.00000000170752,
					0.00000000118465, 0.00000000089713, -0.00000000062357, -0.00000000047167, 0.00000000032841, 0.00000000024814, -0.00000000017305, -0.00000000013062,
					0.00000000009123, 0.00000000006879, -0.00000000004811, -0.00000000003625, 0.00000000002539, 0.00000000001911, -0.00000000001340, -0.00000000001008,
					0.00000000000708, 0.00000000000532, -0.00000000000374, -0.00000000000281, 0.00000000000198, 0.00000000000148, -0.00000000000104, -0.00000000000078,
					0.00000000000055, 0.00000000000041, -0.00000000000029, -0.00000000000022, 0.00000000000015, 0.00000000000012, -0.00000000000008 - 0.00000000000006,
					0.00000000000004, 0.00000000000003, -0.00000000000002, -0.00000000000002, 0.00000000000001, 0.00000000000001, -0.00000000000001, -0.00000000000000 };
			h = temp;
			break;

		case 5:
			h = new double[42];
			h[0] = 0.74729;
			h[1] = 0.4425;
			h[2] = -0.037023;
			h[3] = -0.12928;
			h[4] = 0.029477;
			h[5] = 0.061317;
			h[6] = -0.021008;
			h[7] = -0.032523;
			h[8] = 0.014011;
			h[9] = 0.01821;
			h[10] = -0.0090501;
			h[11] = -0.010563;
			h[12] = 0.0057688;
			h[13] = 0.0062796;
			h[14] = -0.0036605;
			h[15] = -0.0037995;
			h[16] = 0.0023214;
			h[17] = 0.0023288;
			h[18] = -0.0014738;
			h[19] = -0.0014414;
			h[20] = 0.00093747;
			h[21] = 0.00089889;
			h[22] = -0.00059753;
			h[23] = -0.00056398;
			h[24] = 0.00038165;
			h[25] = 0.00035559;
			h[26] = -0.00024423;
			h[27] = -0.00022512;
			h[28] = 0.00015658;
			h[29] = 0.00014301;
			h[30] = -0.00010055;
			h[31] = -9.1113e-05;
			h[32] = 6.4669e-05;
			h[33] = 5.8198e-05;
			h[34] = -4.1649e-05;
			h[35] = -3.7256e-05;
			h[36] = 2.729e-05;
			h[37] = 2.458e-05;
			h[38] = -2.2593e-05;
			h[39] = -3.5791e-05;
			h[40] = -1.7098e-05;
			h[41] = -2.9619e-06;
			break;
		}

		g = new double[h.length];
		if (order > 0) {
			g[0] = h[0];
			for (int k = 1; k < h.length; k++) {
				if ((k / 2) * 2 == k) g[k] = h[k];
				else g[k] = -h[k];
			}
		}
		else {
			g[0] = -h[0];
		}

	}

}