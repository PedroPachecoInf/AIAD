package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import behaviours.TaxiServiceBehaviour;

public class Gui {
	private static GridPanel grid;
	private static int n_rows, n_cols;
	
	public Gui(int n_rows, int n_cols) {
		Gui.n_cols = n_cols;
		Gui.n_rows = n_rows;
		
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
                
                JFrame frame = new JFrame("Testing");
                
				frame.setLayout(null);

				JButton button = new JButton("Run");
				button.setBounds(10, 10, 100, 30);
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						TaxiServiceBehaviour.changeState();
						JButton button = (JButton) e.getSource();
						if(button.getText().equals("Run"))
							button.setText("Stop");
						else
							button.setText("Run");
					}
				});

				frame.add(button);

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                grid = new GridPanel(n_rows, n_cols);
                frame.add(grid);
               
                frame.pack();
                Dimension d = frame.getSize();
                d.setSize(d.getWidth() + 300 , d.getHeight());
                frame.setSize(d);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
	
	public static void newPassenger(int row, int col, int frow, int fcol, String name){
		int index = (row - 1) * n_rows + (col - 1);
		grid.getCells().get(index).setBackground(Color.YELLOW);
		grid.getCells().get(index).setSavedBackground(Color.YELLOW);
		grid.getCells().get(index).getLabel(0).setText("init");
		grid.getCells().get(index).getLabel(1).setText(name);
		grid.getCells().get(index).getSavedLabel(0).setText("init");
		grid.getCells().get(index).getSavedLabel(1).setText(name);
		grid.getCells().get(index).repaint();
		
		int findex = (frow - 1) * n_rows + (fcol - 1);
		grid.getCells().get(findex).setBackground(Color.PINK);
		grid.getCells().get(findex).setSavedBackground(Color.PINK);
		grid.getCells().get(findex).getLabel(0).setText("final");
		grid.getCells().get(findex).getLabel(1).setText(name);
		grid.getCells().get(findex).getSavedLabel(0).setText("final");
		grid.getCells().get(findex).getSavedLabel(1).setText(name);
		grid.getCells().get(findex).repaint();
	}
	
	public static void newTaxi(int row, int col, String name){
		int index = (row - 1) * n_rows + (col - 1);
		grid.getCells().get(index).setBackground(Color.GREEN);
		grid.getCells().get(index).getLabel(0).setText(name);
		grid.getCells().get(index).repaint();
	}
	
	public static void moveTaxi(int old_row, int old_col, int new_row, int new_col, String name, String pass1, String pass2){
		int old_index = (old_row - 1) * n_rows + (old_col - 1);
		int new_index = (new_row - 1) * n_rows + (new_col - 1);
		grid.getCells().get(old_index).resetToSaved();
		grid.getCells().get(new_index).setBackground(Color.GREEN);
		grid.getCells().get(new_index).getLabel(0).setText(name);
		grid.getCells().get(new_index).getLabel(1).setText(pass1);
		grid.getCells().get(new_index).getLabel(2).setText(pass2);
		
		grid.getCells().get(old_index).repaint();
		grid.getCells().get(new_index).repaint();
	}
	
	public static void resetCell(int row, int col){
		int index = (row - 1) * n_rows + (col - 1);
		grid.getCells().get(index).reset();
		grid.getCells().get(index).repaint();
	}
}
