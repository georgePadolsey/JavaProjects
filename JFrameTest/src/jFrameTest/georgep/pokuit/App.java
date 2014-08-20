package jFrameTest.georgep.pokuit;

import java.awt.Color;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class App extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public App(String title, int height, int width) {
        super (title);
        setSize(width, height);
        setUndecorated(true);
        
        setBackground(new Color(0, 0, 0, 0));
        area.setBackground(new Color(0, 0, 0, 0.8F));
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setVisible(true);
    }
}
