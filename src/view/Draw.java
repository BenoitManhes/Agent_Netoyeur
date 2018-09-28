package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Element;
import model.Environement;
import model.Parametre;


public class Draw{
	JFrame frame;
	Canvas canvas;

	BufferStrategy bufferStrategy;

	Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int height = (int)dimension.getHeight();
	int width  = (int)dimension.getWidth();

	private int WIDTH = (int)(width/2);
	private int HEIGHT = (int)(width/2);
	private int intervalle = (int)(WIDTH/Parametre.TAILLE_GRILLE*0.99);

	/**image*/
	private Image poussiere;
	private Image bijou;
	private Image robot;

	private ArrayList<Element> List;

	public Draw(String titre, ArrayList<Element> list){
		List = list;
		try {
			this.robot = ImageIO.read(new File("robot.png"));
		} catch (IOException e) {e.printStackTrace();}
		try {
			this.poussiere = ImageIO.read(new File("poussiere.png"));
		} catch (IOException e) {e.printStackTrace();}
		try {
			this.bijou = ImageIO.read(new File("diamond-ring.png"));
		} catch (IOException e) {e.printStackTrace();}
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
		//this will place the frames on different positions
		switch(titre) {
		case "Environement" : frame.setLocation(0, 0);
		break;
		case "Agent" : frame.setLocation(WIDTH, 0);
		break;
		default : break;
		}
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
		g.setColor(Color.darkGray);
		// drawing de la grille
		for (int i = 0; i < Parametre.TAILLE_GRILLE; i++) {
			for (int j = 0; j < Parametre.TAILLE_GRILLE; j++) {
				g.drawRect(CO(i), CO(j),T,T );
			}
		}
		/* drawing des elements d abord poussier puis ensuite bijou 
		 * 2 boucle for afin de permettre aux images des bijoux de paraitre dessus la poussiere
		 */
		for (int i = 0; i < List.size(); i++) {
			Element e = List.get(i);
			if(e.isPoussiere()) {
				g.drawImage(poussiere,CO(e.getX()),CO(e.getY()),T,T,null);
			}
		}
		for (int i = 0; i < List.size(); i++) {
			Element e = List.get(i);
			if(!e.isPoussiere()) {
				g.drawImage(bijou,CO(e.getX()),CO(e.getY()),T,T,null);
			}
		}
		// drawing du robot
		g.drawImage(robot, CO(Environement.agent.getX()), CO(Environement.agent.getY()), T, T, null);

	}

	private int CO(int x) {	// coordonne en fonction de la taille de grille
		return (int) (x*intervalle+intervalle/10);
	}

}