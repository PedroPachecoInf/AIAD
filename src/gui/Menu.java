package gui;

import java.awt.EventQueue;
import java.awt.Image;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jade.util.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextArea;

public class Menu {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu window = new Menu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSimular = new JButton("Simular");
		btnSimular.setBounds(154, 35, 128, 38);
		frame.getContentPane().add(btnSimular);
		
		JButton btnOpes = new JButton("Opções");
		btnOpes.setBounds(154, 96, 130, 40);
		frame.getContentPane().add(btnOpes);
		
		JButton btnSair = new JButton("Sair");
		btnSair.setBounds(154, 217, 130, 38);
		frame.getContentPane().add(btnSair);
		
		JButton btnNewButton = new JButton("Estatisticas");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(152, 156, 130, 39);
		frame.getContentPane().add(btnNewButton);
		
		
		JLabel label = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/taxi.png")).getImage();
		label.setIcon(new ImageIcon(img));
		label.setBounds(-11,16,166,256);
		frame.getContentPane().add(label);
	}
	
	
}
