package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	private int HEIGHT = (int)(width/2)+20;  //20 = height of menu
	private int intervalle = (int)(WIDTH/Parametre.TAILLE_GRILLE*0.99);

	/**image*/
	private Image poussiere;
	private Image bijou;
	private Image robot;
	private String typeAffichage;

	/**commodite d'affichage*/
	private static boolean informationsVisibles;

	private ArrayList<Element> List;

	public Draw(String titre, ArrayList<Element> list){
		typeAffichage=titre;
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
		//Makes a new window
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
		//this will add a menu on the frame Environnement
		int tailleMenu = 0;
		switch(titre) {
		case "Environnement" :
			JMenuBar menuEnvironnement = createMenuBarEnvironnement();
			tailleMenu = menuEnvironnement.getHeight();
			frame.setJMenuBar(menuEnvironnement);
			break;
		case "Agent" :
			JMenuBar menuAgent = createMenuBarAgent();
			tailleMenu = menuAgent.getHeight();
			frame.setJMenuBar(menuAgent);
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
		g.setColor(Color.black);
		// drawing de la grille
		for (int i = 0; i < Parametre.TAILLE_GRILLE; i++) {
			for (int j = 0; j < Parametre.TAILLE_GRILLE; j++) {
				g.drawRect(CO(i), CO(j),T,T );
				g.setColor(Color.white);
				g.fillRect(CO(i), CO(j),T,T );
				g.setColor(Color.darkGray);
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

		//maj score
		if(informationsVisibles==true) {
			g.drawString("Score environnement : "+Environement.getScoreEnvironnement(), 20,20);
			g.drawString("Coût énergie : "+Environement.agent.getEnergieDepense(), 20, 40);
			g.drawString("Score : "+(Environement.getScoreEnvironnement()-Environement.agent.getEnergieDepense()), 20, 60);
			g.drawString("Nombre de cases parcourues : "+Environement.agent.getNbrCasesParcourues(), 300, 20);
			g.drawString("Nombre d'objets aspires : "+Environement.agent.getNbrObjetsAspirees(), 300, 40);
			g.drawString("Nombre de bijoux ramasses : "+Environement.agent.getNbrBijouxRamasses(), 300, 60);

			BigDecimal bd = new BigDecimal(Parametre.PROBA_POUSSIERE);
			bd= bd.setScale(3,BigDecimal.ROUND_DOWN);
			double valeurProba = bd.doubleValue();

			g.drawString("Probabilite apparition poussiere : "+valeurProba, 20, 100);
			g.drawString("Probabilite apparition bijoux : "+valeurProba, 20, 120);
			g.drawString("Delai de l'agent : "+Parametre.DELAI_AGENT, 300, 100);

		}

		//affichage parcour planifie
		if(typeAffichage.equals(Parametre.TITRE_AGENT) && !Environement.agent.getObjectifs().isEmpty()) {
			g.setColor(Color.RED);
			g.drawLine(CO(Environement.agent.getX())+intervalle/2, CO(Environement.agent.getY())+intervalle/2,CO( Environement.agent.getObjectifs().get(0).getX())+intervalle/2, CO(Environement.agent.getObjectifs().get(0).getY())+intervalle/2);
			for (int i = 0; i < Environement.agent.getObjectifs().size()-1; i++) {
				g.drawLine(CO(Environement.agent.getObjectifs().get(i).getX())+intervalle/2, CO(Environement.agent.getObjectifs().get(i).getY())+intervalle/2,CO( Environement.agent.getObjectifs().get(i+1).getX())+intervalle/2, CO(Environement.agent.getObjectifs().get(i+1).getY())+intervalle/2);
			}
		}

	}

	private int CO(int x) {	// coordonne en fonction de la taille de grille
		return (int) (x*intervalle+intervalle/10);
	}

	public static JMenuBar createMenuBarEnvironnement() {

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();
		menuBar.repaint();

		//Build the simulationMenu
		menu = new JMenu("Simulation");
		//	menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("Set event");
		menuBar.add(menu);

		//Create the item to restart the simulation
		menuItem = new JMenuItem("Reinitialiser elements environnement");
		//	menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < Environement.ListEnvironement.size(); i++) {
					Environement.ListEnvironement.remove(i);
				}
			}
		});
		menuBar.add(menuItem);

		//Create the groups to select the probability of dirt
		menu.addSeparator();
		menuItem = new JMenuItem("Probabilite apparition poussiere +");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametre.PROBA_POUSSIERE<0.8) Parametre.PROBA_POUSSIERE += 0.1;

			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Probabilite apparition poussiere -");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametre.PROBA_POUSSIERE>0.2) Parametre.PROBA_POUSSIERE -= 0.1;
				else Parametre.PROBA_POUSSIERE = 0;
			}
		});
		menu.add(menuItem);

		//Create the groups to select the probability of jewel
		menu.addSeparator();
		menuItem = new JMenuItem("Probabilite apparition bijou +");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametre.PROBA_BIJOU<0.8) Parametre.PROBA_BIJOU += 0.1;

			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Probabilite apparition bijou -");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametre.PROBA_BIJOU>0.2) Parametre.PROBA_BIJOU -= 0.1;
				else Parametre.PROBA_BIJOU = 0;
			}
		});
		menu.add(menuItem);


		menuItem = new JMenuItem("Informations");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(informationsVisibles) {
					informationsVisibles=false;
				}
				else {
					informationsVisibles=true;
				}
			}
		});
		menuBar.add(menuItem);

		return menuBar;
	}

	public static JMenuBar createMenuBarAgent() {

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();
		menuBar.repaint();

		//Build the simulationMenu
		menu = new JMenu("Simulation");
		//	menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("Set event");
		menuBar.add(menu);

		//Create the item to restart the simulation
		menuItem = new JMenuItem("Reinitialiser performances agent");
		//	menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Restart");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Environement.setPerformance(0);
				Environement.setScoreEnvironnement(0);
				Environement.agent.setEnergieDepense(0);
				Environement.agent.setLastAction(0);
				Environement.agent.setNbrBijouxRamasses(0);
				Environement.agent.setNbrCasesParcourues(0);
				Environement.agent.setNbrObjetsAspirees(0);
			}});
		menuBar.add(menuItem);


		//Create the groups to select the speed
		menu.addSeparator();
		menuItem = new JMenuItem("Vitesse de l'agent +");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametre.DELAI_AGENT>100) Parametre.DELAI_AGENT -= 50;
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Vitesse de l'agent -");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Parametre.DELAI_AGENT += 50;
			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Informations");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(informationsVisibles) {
					informationsVisibles=false;
				}
				else {
					informationsVisibles=true;
				}
			}
		});
		menuBar.add(menuItem);

		return menuBar;

	}



}