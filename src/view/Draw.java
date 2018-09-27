package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Parametre;


public class Draw{
	JFrame frame;
	Canvas canvas;

	BufferStrategy bufferStrategy;

	Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int height = (int)dimension.getHeight();
	int width  = (int)dimension.getWidth();

	private int WIDTH = (int)(Math.min(height, width)/2*1.1);
	private int HEIGHT = (int)(Math.min(height, width)/2*1.1);
	private int intervalle = WIDTH/Parametre.TAILLE_GRILLE;

	public Draw(String titre){
		//Makes a new window, with the name " Basic game  ".
		frame = new JFrame(titre);
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(null);

		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		canvas.setIgnoreRepaint(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		//this will make the frame not re-sizable
		frame.setResizable(false);
		frame.setVisible(true);
		//this will add the canvas to our frame
		panel.add(canvas);
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		//This will make sure the canvas has focus, so that it can take input from mouse/keyboard
		canvas.requestFocus();
		//this will set the background to black
		canvas.setBackground(Color.black);
		// This will add our buttonhandler to our program
		//canvas.addKeyListener(new ButtonHandler());
	}
	public void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		render(g);
		g.dispose();
		bufferStrategy.show();
	}
	protected void render(Graphics2D g){
		
		int T =(int) (intervalle*0.9); // Taille d un element 
		g.setColor(Color.WHITE);
		for (int i = 0; i < Parametre.TAILLE_GRILLE; i++) {
			for (int j = 0; j < Parametre.TAILLE_GRILLE; j++) {
				g.fillRect(CO(i), CO(j),T,T );
			}
		}
		
	}
	
	private int CO(int x) {	// coordonne en fonction de la taille de grille
		return (int) (x*intervalle+intervalle/10);
	}
	
}