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

package edfgui;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ColorProcessor;
import imageware.Builder;
import imageware.ImageWare;
import surfacemap.SurfaceMap3D;
import edf.AbstractEdfAlgorithm;
import edf.Color2BW;
import edf.EdfComplexWavelets;
import edf.EdfRealWavelets;
import edf.EdfSobel;
import edf.EdfVariance;
import edf.LogSingleton;
import edf.MorphologicalOperators;
import edf.PostProcessing;
import edf.Tools;

public class ExtendedDepthOfField {

	static final int SOBEL = 0;
	static final int VARIANCE = 1;
	static final int REAL_WAVELETS = 2;
	static final int COMPLEX_WAVELETS = 3;

	private Parameters parameters = null;
	private ImagePlus imp = null;

	public ExtendedDepthOfField(ImagePlus imp, Parameters parameters) {
		this.parameters = parameters;
		this.imp = imp;
	}

	/**
	 * Run the main processing.
	 */
	public void process() {

		LogSingleton log = LogSingleton.getInstance();
		log.setStartTime((double) System.currentTimeMillis());
		log.append("Start processing....");
		log.setProgessLength(0);

		boolean isExtended = false;
		boolean waveletMethod = (parameters.edfMethod == REAL_WAVELETS || parameters.edfMethod == COMPLEX_WAVELETS);

		ImageStack stackConverted;
		ImagePlus impConverted;
		ImagePlus impBW = imp;
		if (parameters.color) {

			log.start("Color conversion...");

			impConverted = new ImagePlus();
			switch (parameters.colorConversionMethod) {
			case 0:
				stackConverted = Color2BW.C2BFixedWeights(imp.getStack(), true);
				break;
			case 1:
				stackConverted = Color2BW.C2BPrincipalComponents(imp.getStack());
				break;
			case 2:
				stackConverted = Color2BW.C2BMean(imp.getStack());
				break;
			default:
				throw new RuntimeException("Unknown error");
			}
			impConverted.setStack(null, stackConverted);
			impBW = impConverted;

			log.acknowledge();
		}

		System.gc();

		// Start the main EDF process.
		ImageWare imageStack = Builder.wrap(impBW);

		// Check sizes.
		int[] scaleAndSizes = new int[3];
		int nx = imageStack.getWidth();
		int ny = imageStack.getHeight();

		if (waveletMethod) {
			if (!Tools.isPowerOf2(nx) || !Tools.isPowerOf2(ny)) {
				scaleAndSizes = Tools.computeScaleAndPowerTwoSize(nx, ny);
				log.start("Extend images to " + scaleAndSizes[1] + "x" + scaleAndSizes[2] + " pixels...");
				imageStack = Tools.extend(imageStack, scaleAndSizes[1], scaleAndSizes[2]);
				isExtended = true;
				log.acknowledge();
			}
			System.gc();
		}

		log.start("Sharpen estimation...");

		AbstractEdfAlgorithm edf;
		ImageWare[] ima = new ImageWare[2];

		switch (parameters.edfMethod) {
		case REAL_WAVELETS:
			if (parameters.doDenoising)
				edf = new EdfRealWavelets((int) parameters.splineOrder, parameters.nScales, parameters.subBandCC, parameters.majCC, parameters.rateDenoising);
			else edf = new EdfRealWavelets((int) parameters.splineOrder, parameters.nScales, parameters.subBandCC, parameters.majCC);
			ima = edf.process(imageStack);
			break;
		case COMPLEX_WAVELETS:
			edf = new EdfComplexWavelets(parameters.daubechielength, parameters.nScales, parameters.subBandCC, parameters.majCC);
			ima = edf.process(imageStack);
			break;
		case VARIANCE:
			edf = new EdfVariance(parameters.varWindowSize);
			ima = edf.process(imageStack);
			break;
		case SOBEL:
			edf = new EdfSobel();
			ima = edf.process(imageStack);
			break;
		default:
			throw new RuntimeException("Invalid Option.");
		}
		System.gc();

		log.acknowledge();
		log.setProgessLength(80);

		// Crop to original images.
		if (waveletMethod && isExtended) {
			log.start("Crop to original size...");
			imageStack = Tools.crop(imageStack, nx, ny);
			ima[0] = Tools.crop(ima[0], nx, ny);
			ima[1] = Tools.crop(ima[1], nx, ny);
			System.gc();
			log.acknowledge();
		}

		if (parameters.reassignment) {
			log.start("Reassignment to original pixel values...");
			ima[1] = PostProcessing.reassignment(ima[0], imageStack);
			System.gc();
			log.acknowledge();
			log.setProgessLength(95);
		}

		if (parameters.doDenoising && !waveletMethod) {
			log.start("Denoising (Gaussian smoothing)...");
			ima[0].smoothGaussian(parameters.sigmaDenoising);
			System.gc();
			log.acknowledge();
			log.setProgessLength(95);
		}

		ImagePlus impComposite = null;
		ImagePlus impHeightMap = null;

		if (parameters.color && parameters.outputColorMap == Parameters.COLOR_RGB) {
			ColorProcessor cp;
			if ((waveletMethod && parameters.reassignment) || !waveletMethod) {
				cp = PostProcessing.reassignmentColor(ima[1], imp.getStack());
				impComposite = new ImagePlus("Output", cp);
			}
			else {
				impComposite = new ImagePlus("Output", ima[0].buildImageStack());
			}
		}
		else {
			impComposite = new ImagePlus("Output", ima[0].buildImageStack());
		}

		// Topology post-processing.
		if ((waveletMethod && parameters.reassignment) || (!waveletMethod)) {

			if (parameters.showTopology) {

				if (parameters.doMedian) {
					log.start("Median filter...");

					ima[1] = MorphologicalOperators.doMedian(ima[1], parameters.medianWindowSize);
					log.acknowledge();

				}

				if (parameters.doMorphoClose) {
					log.start("Morphological close...");

					ima[1] = MorphologicalOperators.doClose(ima[1]);

					log.acknowledge();
				}

				if (parameters.doMorphoOpen) {
					log.start("Morphological open ...");

					ima[1] = MorphologicalOperators.doOpen(ima[1]);

					log.acknowledge();
				}

				if (parameters.doGaussian) {
					log.start("Post-processing on the map: Gaussian filter of sigma: " + parameters.sigma);
					ima[1].smoothGaussian(parameters.sigma);
					log.acknowledge();
				}

				impHeightMap = new ImagePlus("Height-Map", ima[1].buildImageStack());
				impHeightMap.show();
				impHeightMap.updateAndDraw();

				if (parameters.show3dView) {
					SurfaceMap3D viewer = new SurfaceMap3D(impHeightMap, impComposite);
					Thread thread = new Thread(viewer);
					thread.start();
				}
			}
		}

		impComposite.show();
		impComposite.updateAndDraw();

		log.start("Finished.");
		log.setProgessLength(100);
		log.append("");
	}
}
