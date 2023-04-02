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
 * Author: Daniel Sage, ALex Prudencio
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

package edfgui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MyTableRenderer extends JLabel implements TableCellRenderer {

	public MyTableRenderer() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setText((String) object);

		if (isSelected) {
			setBackground(table.getSelectionBackground());
		}
		else {
			setBackground(table.getBackground());
		}
		return this;
	}

}
