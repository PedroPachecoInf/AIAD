package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CellPanel extends JPanel {
	
	private Color defaultBackground;
	private JLabel[] labels;;

	public CellPanel() {
		defaultBackground = this.getBackground();
		GridLayout layout = new GridLayout(3, 1);
		layout.setVgap(0);
		layout.setHgap(0);
		this.setLayout(layout);
		JLabel label = new JLabel("", SwingConstants.CENTER);
		JLabel label2 = new JLabel("", SwingConstants.CENTER);
		JLabel label3 = new JLabel("", SwingConstants.CENTER);
		
		labels = new JLabel[]{label, label2, label3};

		add(label);
		add(label2);
		add(label3);
		/*
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				defaultBackground = getBackground();
				setBackground(Color.BLUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(defaultBackground);
			}
		});*/
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(50, 50);
	}
	
	public JLabel getLabel(int i){
		return this.labels[i];
	}
	
	public void reset(){
		this.setBackground(this.defaultBackground);
		for(JLabel label : labels){
			label.setText("");
		}
	}
}
