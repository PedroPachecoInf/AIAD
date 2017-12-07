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
	
	private Color default_background;
	private Color saved_background;
	private JLabel[] labels;
	private JLabel[] saved_labels;
	

	public CellPanel() {
		default_background = this.getBackground();
		GridLayout layout = new GridLayout(3, 1);
		layout.setVgap(0);
		layout.setHgap(0);
		this.setLayout(layout);
		JLabel label = new JLabel("", SwingConstants.CENTER);
		JLabel label2 = new JLabel("", SwingConstants.CENTER);
		JLabel label3 = new JLabel("", SwingConstants.CENTER);
		
		this.saved_background = this.getBackground();
		JLabel saved_label = new JLabel("", SwingConstants.CENTER);
		JLabel saved_label2 = new JLabel("", SwingConstants.CENTER);
		JLabel saved_label3 = new JLabel("", SwingConstants.CENTER);
		
		labels = new JLabel[]{label, label2, label3};
		saved_labels = new JLabel[]{saved_label, saved_label2, saved_label3};

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
	
	public JLabel getSavedLabel(int i){
		return this.saved_labels[i];
	}
	
	public void reset(){
		this.setBackground(this.default_background);
		this.saved_background = this.default_background;
		
		for(JLabel label : labels){
			label.setText("");
		}
		
		for(JLabel label : saved_labels){
			label.setText("");
		}
	}
	
	public void resetToSaved(){
		this.setBackground(this.saved_background);
		this.labels[0].setText(this.saved_labels[0].getText());
		this.labels[1].setText(this.saved_labels[1].getText());
		this.labels[2].setText(this.saved_labels[2].getText());
	}
	
	public void setSavedBackground(Color color){
		this.saved_background = color;
	}
}
