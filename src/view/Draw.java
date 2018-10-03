package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import model.Agent;
import model.Element;
import model.Environement;
import model.Parametre;

/** ======================================= Dessiner les fenetres et tous les elements a mettre a jour ===========================================================*/


public class Draw{

	// -------------------------------------------------Attributs-------------------------------------------------------------------------------

	JFrame frame;
	Canvas canvas;

	BufferStrategy bufferStrategy;

	Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int height = (int)dimension.getHeight();
	int width  = (int)dimension.getWidth();


	private int WIDTH = (int)(width/3);
	private int HEIGHT = (int)(width*4/9);  

	private int intervalle = (int)(WIDTH/Parametre.TAILLE_GRILLE*0.99);

	/**image*/
	private Image poussiere;
	private Image bijou;
	private Image robotDroite;
	private Image robotGauche;
	private Image robotHaut;
	private Image robotBas;
	private Image robotAspire;
	private Image robotRamasse;
	private Image robotNeRienFaire;
	private String typeAffichage;

	/**commodite d'affichage*/
	private static boolean informationsVisibles;

	/**Choix de l'exploration*/
	private static boolean choixInformee;

	private ArrayList<Element> List;

	// -------------------------------------------------Constructeur-------------------------------------------------------------------------------

	public Draw(String titre, ArrayList<Element> list){

		//Chargement des ressources utiles
		typeAffichage=titre;
		List = list;
		try {
			this.robotDroite = ImageIO.read(new File("robotDroite.png"));
		} catch (IOException e) {e.printStackTrace();}
		try {
			this.robotGauche = ImageIO.read(new File("robotGauche.png"));
		} catch (IOException e) {e.printStackTrace();}
		try {
			this.robotBas = ImageIO.read(new File("robotBas.png"));
		} catch (IOException e) {e.printStackTrace();}
		try {
			this.robotHaut = ImageIO.read(new File("robotHaut.png"));
		} catch (IOException e) {e.printStackTrace();}
		try {
			this.robotRamasse = ImageIO.read(new File("robotRamasse.png"));
		} catch (IOException e) {e.printStackTrace();}
		try {
			this.robotAspire = ImageIO.read(new File("robotAspire.png"));
		} catch (IOException e) {e.printStackTrace();}
		try {
			this.robotNeRienFaire = ImageIO.read(new File("robotNeRienFaire.png"));
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
		case "Agent" : frame.setLocation(width/2, 0);
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

		//this will make sure the canvas has focus, so that it can take input from mouse/keyboard
		canvas.requestFocus();

		//this will set the background to black
		canvas.setBackground(Color.black);
		// This will add our buttonhandler to our program
		//canvas.addKeyListener(new ButtonHandler());
	}

	// -------------------------------------------------Mise a jour avec le thread de l environnement-------------------------------------------------------------------------------

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

		/* Drawing des elements d abord poussier puis ensuite bijou 
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

		if(Environement.agent.getLastAction()==Agent.DROITE) {
			g.drawImage(robotDroite, CO(Environement.agent.getX()), CO(Environement.agent.getY()), T, T, null);
		}
		else if(Environement.agent.getLastAction()==Agent.GAUCHE) {
			g.drawImage(robotGauche, CO(Environement.agent.getX()), CO(Environement.agent.getY()), T, T, null);
		}
		else if(Environement.agent.getLastAction()==Agent.BAS) {
			g.drawImage(robotBas, CO(Environement.agent.getX()), CO(Environement.agent.getY()), T, T, null);
		}
		else if(Environement.agent.getLastAction()==Agent.HAUT) {
			g.drawImage(robotHaut, CO(Environement.agent.getX()), CO(Environement.agent.getY()), T, T, null);
		}
		else if(Environement.agent.getLastAction()==Agent.ASPIRER) {
			g.drawImage(robotAspire, CO(Environement.agent.getX()), CO(Environement.agent.getY()), T, T, null);
		}
		else if(Environement.agent.getLastAction()==Agent.RAMASSER) {
			g.drawImage(robotRamasse, CO(Environement.agent.getX()), CO(Environement.agent.getY()), T, T, null);
		}
		else if(Environement.agent.getLastAction()==Agent.NE_RIEN_FAIRE) {
			g.drawImage(robotNeRienFaire, CO(Environement.agent.getX()), CO(Environement.agent.getY()), T, T, null);
		}
		else {
			g.drawImage(robotBas, CO(Environement.agent.getX()), CO(Environement.agent.getY()), T, T, null);
		}


		//Affichage type exploration :
		
		if(choixInformee){
			g.setColor(new Color(52, 201, 36));
			g.drawString("Mode : Exploration informee", 10, HEIGHT -20);
		}
		else{
			g.setColor(new Color(0, 127, 255));
			g.drawString("Mode : Exploration non-informee", 10, HEIGHT - 20);
		}
		
		g.setColor(Color.BLACK);
		//maj score
		if(informationsVisibles==true) {
/*<<<<<<< Updated upstream
			g.drawString("Score environnement : "+Environement.getScoreEnvironnement(), 20,HEIGHT-170);
			g.drawString("Cout energie : "+Environement.agent.getEnergieDepense(), 20, HEIGHT-150);
			g.drawString("Score : "+(Environement.getScoreEnvironnement()-Environement.agent.getEnergieDepense()), 20, HEIGHT-130);
			g.drawString("Score Moyen : "+Environement.getMoyenneScore(), 20, HEIGHT-110);

			g.drawString("Nombre de cases parcourues : "+Environement.agent.getNbrCasesParcourues(), 200, HEIGHT-170);
			g.drawString("Nombre d'objets aspires : "+Environement.agent.getNbrObjetsAspirees(), 200, HEIGHT-150);
			g.drawString("Nombre de bijoux ramasses : "+Environement.agent.getNbrBijouxRamasses(), 200, HEIGHT-130);
=======*/
			//Affichage stat
			g.setColor(Color.white);
			
			g.drawString("Score environnement : "+Environement.getScoreEnvironnement(), 20,CO(10)+20);
			g.drawString("Cout energie : "+Environement.agent.getEnergieDepense(), 20, CO(10)+40);
			g.drawString("Score : "+(Environement.getScoreEnvironnement()-Environement.agent.getEnergieDepense()), 20, CO(10)+60);
			
			
			g.drawString("Nombre de cases parcourues : "+Environement.agent.getNbrCasesParcourues(), 200, CO(10)+20);
			g.drawString("Nombre d'objets aspires : "+Environement.agent.getNbrObjetsAspirees(), 200,CO(10)+40);
			g.drawString("Nombre de bijoux ramasses : "+Environement.agent.getNbrBijouxRamasses(), 200, CO(10)+60);


			BigDecimal bd = new BigDecimal(Parametre.PROBA_POUSSIERE);
			bd= bd.setScale(4,BigDecimal.ROUND_DOWN);
			double valeurProbaPoussiere = bd.doubleValue();

			BigDecimal bdb = new BigDecimal(Parametre.PROBA_BIJOU);
			bdb= bdb.setScale(4,BigDecimal.ROUND_DOWN);
			double valeurProbaBijou = bdb.doubleValue();
			
			g.drawString("Probabilite apparition poussiere : "+valeurProbaPoussiere, 420, CO(10)+20);
			g.drawString("Probabilite apparition bijoux : "+valeurProbaBijou, 420,CO(10)+40);
			g.drawString("Delai de l'agent : "+Parametre.DELAI_AGENT, 420, CO(10)+60);
			
			//Affichage informations essentielles
			g.drawString("Score Moyen : "+Environement.getMoyenneScore(), 20, CO(12));
			g.drawString("Energie total : "+Environement.agent.getEnergieDepenseTotal(), 20, CO(12)+20);
			g.drawString("Frequence d'observation : "+Environement.agent.getFrequenceObs(), 20, CO(12)+40);
			
			if(Environement.agent.isDebutCycle()) {
				g.drawString("OBSERVATION",200, CO(12)+20);
			}

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

	// -------------------------------------------------Generer les cases-------------------------------------------------------------------------------

	private int CO(int x) {	// coordonne en fonction de la taille de grille
		return (int) (x*intervalle+intervalle/10);
	}


	/** ================================================== Creer les menus ===========================================================*/

	// -------------------------------------------------Menu de l environnement-------------------------------------------------------------------------------

	public static JMenuBar createMenuBarEnvironnement() {

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItemRaz;
		JMenuItem menuItemPoussierePlus;
		JMenuItem menuItemPoussiereMoins;
		JMenuItem menuItemBijouPlus;
		JMenuItem menuItemBijouMoins;
		JMenuItem menuItemInformations;

		//Create the menu bar.
		menuBar = new JMenuBar();
		menuBar.repaint();

		//Build the simulationMenu
		menu = new JMenu("Simulation");
		//	menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("Set event");
		menuBar.add(menu);

		//Create the item to restart the simulation
		menuItemRaz = new JMenuItem("Reinitialiser elements environnement");
		//	menuItem.setMnemonic(KeyEvent.VK_P);
		menuItemRaz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Systeme : r�initialisation de l'environnement");
				Environement.ListEnvironement.clear();
				
			}
		});
		menuBar.add(menuItemRaz);

		//Create the groups to select the probability of dirt
		menu.addSeparator();
		menuItemPoussierePlus = new JMenuItem("Probabilite apparition poussiere +");
		menuItemPoussierePlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametre.PROBA_POUSSIERE<0.8) Parametre.PROBA_POUSSIERE += 0.1;
				System.out.println("Systeme : modification proba. apparition poussiere : "+Parametre.PROBA_POUSSIERE);
			}
		});
		menu.add(menuItemPoussierePlus);
		menuItemPoussiereMoins = new JMenuItem("Probabilite apparition poussiere -");
		menuItemPoussiereMoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametre.PROBA_POUSSIERE>=0.1) Parametre.PROBA_POUSSIERE -= 0.1;
				else Parametre.PROBA_POUSSIERE = 0;
				System.out.println("Systeme : modification proba. apparition poussiere : "+Parametre.PROBA_POUSSIERE);
				
			}
		});
		menu.add(menuItemPoussiereMoins);

		//Create the groups to select the probability of jewel
		menu.addSeparator();
		menuItemBijouPlus = new JMenuItem("Probabilite apparition bijou +");
		menuItemBijouPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametre.PROBA_BIJOU<0.8) Parametre.PROBA_BIJOU += 0.01;
				System.out.println("Systeme : modification proba. apparition bijoux : "+Parametre.PROBA_BIJOU);
			}
		});
		menu.add(menuItemBijouPlus);
		menuItemBijouMoins = new JMenuItem("Probabilite apparition bijou -");
		menuItemBijouMoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametre.PROBA_BIJOU>=0.1) Parametre.PROBA_BIJOU -= 0.01;
				else Parametre.PROBA_BIJOU = 0;
				System.out.println("Systeme : modification proba. apparition bijoux : "+Parametre.PROBA_BIJOU);
			}
		});
		menu.add(menuItemBijouMoins);


		menuItemInformations = new JMenuItem("Informations");
		menuItemInformations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(informationsVisibles) {
					informationsVisibles=false;
				}
				else {
					informationsVisibles=true;
				}
			}
		});
		menuBar.add(menuItemInformations);

		return menuBar;
	}

	// -------------------------------------------------Menu de l agent-------------------------------------------------------------------------------

	public static JMenuBar createMenuBarAgent() {

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItemRazPerf;
		JMenuItem menuItemVitessePlus;
		JMenuItem menuItemVitesseMoins;
		JMenuItem menuItemInformations2;
		JMenu menuInforme;
		JMenuItem menuInformeOui;
		JMenuItem menuInformeNon;

		//Create the menu bar.
		menuBar = new JMenuBar();
		menuBar.repaint();

		//Build the simulationMenu
		menu = new JMenu("Simulation");
		//	menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("Set event");
		menuBar.add(menu);

		//Create the item to restart the simulation
		menuItemRazPerf = new JMenuItem("Reinitialiser performances agent");
		menuItemRazPerf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Systeme : performances de l'agent reinitialisee");
				Environement.setScoreEnvironnement(0);
				Environement.agent.setEnergieDepense(0);
				Environement.agent.setLastAction(0);
				Environement.agent.setNbrBijouxRamasses(0);
				Environement.agent.setNbrCasesParcourues(0);
				Environement.agent.setNbrObjetsAspirees(0);
				Environement.setMoyenneScore(0);
			}});
		menuBar.add(menuItemRazPerf);


		//Create the groups to select the speed
		menu.addSeparator();
		menuItemVitessePlus = new JMenuItem("Vitesse de l'agent +");
		menuItemVitessePlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametre.DELAI_AGENT>100) Parametre.DELAI_AGENT -= 50;
				System.out.println("Systeme : modification delai agent : "+Parametre.DELAI_AGENT);
				
			}
		});
		menu.add(menuItemVitessePlus);
		menuItemVitesseMoins = new JMenuItem("Vitesse de l'agent -");
		menuItemVitesseMoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Parametre.DELAI_AGENT += 50;
				System.out.println("Systeme : modification delai agent : "+Parametre.DELAI_AGENT);
			}
		});
		menu.add(menuItemVitesseMoins);

		
		//Build the explorationMenu
		menuInforme = new JMenu("Type d'exploration");
		menuBar.add(menuInforme);
		menuInforme.getAccessibleContext().setAccessibleDescription("Set event 2");
		ButtonGroup bg = new ButtonGroup();
		
		menuInforme.addSeparator();
		menuInformeOui = new JMenuItem("Exploration informee");
		
		menuInformeOui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choixInformee = true;
				System.out.println("Systeme : mode exploration informee");
			}
		});
		
		bg.add(menuInformeOui);
		menuInforme.add(menuInformeOui);
		
		menuInforme.addSeparator();
		menuInformeNon = new JMenuItem("Exploration non-informee");
		menuInformeNon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choixInformee = false;
				System.out.println("Systeme : mode exploration non-informee");
			}
		});
		bg.add(menuInformeNon);
		menuInforme.add(menuInformeNon);

		return menuBar;

	}

	public static boolean isChoixInformee() {
		return choixInformee;
	}
	
	public static void setChoixInformee(boolean choix) {
		choixInformee = choix;
	}




}