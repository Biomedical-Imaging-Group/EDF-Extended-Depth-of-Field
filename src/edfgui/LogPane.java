/* * EDF Extended Depth of Field * http://bigwww.epfl.ch/demo/edf/ * * Organization: Biomedical Imaging Group (BIG) * Ecole Polytechnique Federale de Lausanne (EPFL), Lausanne, Switzerland * Authors: Daniel Sage, Alex Prudencio, Jesse Berent, Niels Quack, Brigitte Forster *  * Reference: B. Forster, D. Van De Ville, J. Berent, D. Sage, M. Unser * Complex Wavelets for Extended Depth-of-Field: A New Method for the Fusion * of Multichannel Microscopy Images, Microscopy Research and Techniques, 2004 *  * Condition of use: We expect you to include adequate citation whenever you present  * or publish results that are based on it. *  * History: * - Updated (Daniel Sage, 21 December 2010) * - Updated (Daniel Sage, 17 May 2021) *//* * BSD 2-Clause License * * Copyright (c) 2007-2021, EPFL, All rights reserved. *  * Redistribution and use in source and binary forms, with or without * modification, are permitted provided that the following conditions are met: * * 1. Redistributions of source code must retain the above copyright notice, this *    list of conditions and the following disclaimer. * * 2. Redistributions in binary form must reproduce the above copyright notice, *  this list of conditions and the following disclaimer in the documentation *  and/or other materials provided with the distribution. *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */package edfgui;import java.awt.BorderLayout;import java.awt.Color;import java.awt.Insets;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import javax.swing.JButton;import javax.swing.JLabel;import javax.swing.JPanel;import javax.swing.JScrollBar;import javax.swing.JScrollPane;import javax.swing.JTextArea;import edf.LogSingleton;public class LogPane extends JPanel implements ActionListener {	private JButton bnClear = new JButton("Clear");	private JTextArea log;	private JScrollPane jScrollPane;	private JScrollBar vbar;	private boolean autoScroll;	public LogPane() {		super();		setLayout(new BorderLayout(5, 5));		log = LogSingleton.getInstance().getJTextArea();		log.setEditable(false);		log.setBackground(Color.white);		jScrollPane = new JScrollPane(log);		JPanel pnButtons = new JPanel();		pnButtons.add(bnClear);		add("North", new JLabel(""));		add("Center", jScrollPane);		add("South", pnButtons);		bnClear.addActionListener(this);	}	public void setEnabled(boolean enabled) {		super.setEnabled(enabled);		this.bnClear.setVisible(enabled);		this.log.setVisible(enabled);	}	public Insets getInsets() {		return (new Insets(5, 5, 5, 5));	}	/**	 * Implements the actionPerformed for the ActionListener.	 */	public synchronized void actionPerformed(ActionEvent e) {		if (e.getSource() == bnClear) {			this.log.setText("");		}		notify();	}}