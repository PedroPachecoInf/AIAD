package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import gui.TestGrid01.CellPane;

public class GridPanel extends JPanel{
	private int n_rows;
	private int n_cols;
	private ArrayList<CellPanel> cells;
	
	public GridPanel(int n_rows, int n_cols) {
		this.n_rows = n_rows;
		this.n_cols = n_cols;
		this.cells = new ArrayList<CellPanel>();
		
		setup();
    }
	
	public void setup(){
		setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        for (int row = 0; row < this.n_rows; row++) {
            for (int col = 0; col < this.n_cols; col++) {
                gbc.gridx = col;
                gbc.gridy = row;

                CellPanel cellPane = new CellPanel();
                Border border = null;
                if (row < this.n_rows - 1) {
                    if (col < this.n_cols - 1) {
                        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    }
                } else {
                    if (col < 19) {
                        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    }
                }
                cellPane.setBorder(border);
                add(cellPane, gbc);
                this.cells.add(cellPane);
            }
        }
	}

	public ArrayList<CellPanel> getCells() {
		return this.cells;
	}
}
