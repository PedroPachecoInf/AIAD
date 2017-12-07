package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class GridLayoutLines extends JFrame
{
    public GridLayoutLines()
    {
        JPanel grid = new JPanel( new GridLayout(50, 50, 2, 2) );
        grid.setBackground( Color.BLACK );
        grid.setBorder( new MatteBorder(2, 2, 2, 2, Color.BLACK) );

        for (int i = 0; i < 100*100; i++)
        {
            JLabel label = new JLabel();
            label.setText(" label" + i);
            label.setOpaque( true );
            grid.add( label );
        }

        add( grid );
    }

    public static void main(String[] args)
    {
        GridLayoutLines frame = new GridLayoutLines();
        frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
        frame.pack();
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);
    }
}