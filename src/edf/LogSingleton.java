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

import javax.swing.JTextArea;

public class LogSingleton {

	private static LogSingleton instance;

	public static int LENGTH_TASK_1 = 15;
	public static int LENGTH_TASK_2 = 65;
	public static int LENGTH_TASK_3 = 15;
	public static int LENGTH_TASK_4 = 5;

	private JTextArea log;
	private double startTime;
	private int progessLength;
	private String mem;

	private LogSingleton() {
		log = new JTextArea();
	}

	public static LogSingleton getInstance() {
		if (instance == null) {
			instance = new LogSingleton();
		}
		return instance;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public JTextArea getJTextArea() {
		return log;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public void setProgessLength(int current) {
		this.progessLength = current;
	}

	public int getProgessLength() {
		return this.progessLength;
	}

	public String getElapsedTime() {
		double t = System.currentTimeMillis() - startTime;
		if (t > 3000) return new java.text.DecimalFormat(" 000.00 s ").format(t / 1000);
		else return new java.text.DecimalFormat(" 0000 ms ").format(t);
	}

	public String getMemString() {
		return this.mem;
	}

	public void start(String msg) {
		long freeMem = (Runtime.getRuntime().freeMemory()) / 1024;
		java.text.DecimalFormat dfm = new java.text.DecimalFormat(" 000000kB ");
		mem = dfm.format(freeMem);
		log.append(mem + "\t" + getElapsedTime() + "\t" + msg);
		log.setCaretPosition(log.getDocument().getLength());
	}

	public void acknowledge() {
		log.append(": OK\n");
		log.setCaretPosition(log.getDocument().getLength());
	}

	public void append(String msg) {
		log.append(msg + "\n");
		log.setCaretPosition(log.getDocument().getLength());
	}

	public void clear() {
		log.setText("");
	}
}
