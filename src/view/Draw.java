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
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import model.Agent;
import model.Element;
import model.Environnement;
import model.Parametres;

/** ======================================= Dessiner les fenetres et tous les elements a mettre a jour ===========================================================*/


public class Draw{

	// -------------------------------------------------Attributs-------------------------------------------------------------------------------

	JFrame frame;
	Canvas canvas;

	BufferStrategy bufferStrategy;

	private int WIDTH = 460;
	private int HEIGHT = 650;  



	private int intervalle = (int)(WIDTH/Parametres.TAILLE_GRILLE*0.99);

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

	/**Choix de l'exploration*/
	private static boolean choixInformee;
	private static boolean remiseAzero = false;

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
		panel.setBackground(Color.black);
		frame.pack();

		//this will make the frame not re-sizable
		frame.setResizable(false);

		//this will place the frames on different positions
		switch(titre) {
		case "Environement" : frame.setLocation(0, 0);
		break;
		case "Agent" : frame.setLocation(WIDTH + 20, 0);
		break;
		default : break;
		}

		//this will add a menu on the frame Environnement
		switch(titre) {
		case "Environnement" :
			JMenuBar menuEnvironnement = createMenuBarEnvironnement();
			frame.setJMenuBar(menuEnvironnement);
			break;
		case "Agent" :
			JMenuBar menuAgent = createMenuBarAgent();
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
		if(remiseAzero && typeAffichage.equals(Parametres.TITRE_AGENT)){
			this.reinitialiser();
		}
		int T =(int) (intervalle*0.9); // Taille d un element 
		g.setColor(Color.black);
		g.setBackground(Color.black);
		// drawing de la grille
		for (int i = 0; i < Parametres.TAILLE_GRILLE; i++) {
			for (int j = 0; j < Parametres.TAILLE_GRILLE; j++) {
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

		if(Environnement.agent.getLastAction()==Agent.DROITE) {
			g.drawImage(robotDroite, CO(Environnement.agent.getX()), CO(Environnement.agent.getY()), T, T, null);
		}
		else if(Environnement.agent.getLastAction()==Agent.GAUCHE) {
			g.drawImage(robotGauche, CO(Environnement.agent.getX()), CO(Environnement.agent.getY()), T, T, null);
		}
		else if(Environnement.agent.getLastAction()==Agent.BAS) {
			g.drawImage(robotBas, CO(Environnement.agent.getX()), CO(Environnement.agent.getY()), T, T, null);
		}
		else if(Environnement.agent.getLastAction()==Agent.HAUT) {
			g.drawImage(robotHaut, CO(Environnement.agent.getX()), CO(Environnement.agent.getY()), T, T, null);
		}
		else if(Environnement.agent.getLastAction()==Agent.ASPIRER) {
			g.drawImage(robotAspire, CO(Environnement.agent.getX()), CO(Environnement.agent.getY()), T, T, null);
		}
		else if(Environnement.agent.getLastAction()==Agent.RAMASSER) {
			g.drawImage(robotRamasse, CO(Environnement.agent.getX()), CO(Environnement.agent.getY()), T, T, null);
		}
		else if(Environnement.agent.getLastAction()==Agent.NE_RIEN_FAIRE) {
			g.drawImage(robotNeRienFaire, CO(Environnement.agent.getX()), CO(Environnement.agent.getY()), T, T, null);
		}
		else {
			g.drawImage(robotBas, CO(Environnement.agent.getX()), CO(Environnement.agent.getY()), T, T, null);
		}


		//Affichage type exploration :

		if(choixInformee){
			g.setColor(new Color(52, 201, 36));
			g.drawString("Mode : Exploration informee", 20, WIDTH + 15);
		}
		else{
			g.setColor(new Color(0, 127, 255));
			g.drawString("Mode : Exploration non-informee", 20, WIDTH + 15);
		}

		//Affichage stat
		g.setColor(Color.gray);

		g.drawString("Score environnement : "+Environnement.getScoreEnvironnement(), 20,WIDTH+40);
		g.drawString("Cout energie : "+Environnement.agent.getEnergieDepense(), 20, WIDTH+60);
		g.drawString("Score : "+(Environnement.getScoreEnvironnement()-Environnement.agent.getEnergieDepense()), 20, WIDTH+80);


		g.drawString("Nombre de cases parcourues : "+Environnement.agent.getNbrCasesParcourues(), 220, WIDTH+40);
		g.drawString("Nombre d'objets aspires : "+Environnement.agent.getNbrObjetsAspirees(), 220,WIDTH+60);
		g.drawString("Nombre de bijoux ramasses : "+Environnement.agent.getNbrBijouxRamasses(), 220, WIDTH+80);


		NumberFormat nfProba = NumberFormat.getInstance();
		nfProba.setMaximumFractionDigits(5);

		String valeurProbaPoussiere = nfProba.format(Parametres.PROBA_POUSSIERE);
		String valeurProbaBijou = nfProba.format(Parametres.PROBA_BIJOU);

		g.drawString("Probabilite apparition poussiere : "+valeurProbaPoussiere, 220, WIDTH + 105);
		g.drawString("Probabilite apparition bijoux : "+valeurProbaBijou, 220, WIDTH +125);
		g.drawString("Delai de l'agent : "+Parametres.DELAI_AGENT, 220, WIDTH + 145);

		//Affichage informations essentielles


		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		String scoreMoyenFormate = nf.format(Environnement.getMoyenneScore());

		g.drawString("Score Moyen : "+scoreMoyenFormate, 20, WIDTH + 105);
		g.drawString("Energie total : "+Environnement.agent.getEnergieDepenseTotal(), 20, WIDTH +125);
		g.drawString("Frequence d'observation : "+Environnement.agent.getFrequenceObs(), 20, WIDTH + 145);

		if(Environnement.agent.isDebutCycle()) {
			g.setColor(Color.orange);
			g.drawString("Observation",220, WIDTH + 15);
		}

		//affichage parcour planifie
		if(typeAffichage.equals(Parametres.TITRE_AGENT) && !Environnement.agent.getObjectifs().isEmpty()) {
			g.setColor(Color.RED);
			g.drawLine(CO(Environnement.agent.getX())+intervalle/2, CO(Environnement.agent.getY())+intervalle/2,CO( Environnement.agent.getObjectifs().get(0).getX())+intervalle/2, CO(Environnement.agent.getObjectifs().get(0).getY())+intervalle/2);
			for (int i = 0; i < Environnement.agent.getObjectifs().size()-1; i++) {
				g.drawLine(CO(Environnement.agent.getObjectifs().get(i).getX())+intervalle/2, CO(Environnement.agent.getObjectifs().get(i).getY())+intervalle/2,CO( Environnement.agent.getObjectifs().get(i+1).getX())+intervalle/2, CO(Environnement.agent.getObjectifs().get(i+1).getY())+intervalle/2);
			}
		}

	}
	
	//possibilite pour l'utilisateur de reintialiser les performances de l'agent :
	public void reinitialiser(){
		int x = Environnement.agent.getX();
		int y = Environnement.agent.getY();
		Environnement.agent = new Agent(x, y);
		Environnement.setMoyenneScore(0);
		Environnement.setScoreEnvironnement(0);
		Environnement.scoresObtenus.removeAllElements();
		this.List = Environnement.agent.getListElementObs();
		
		remiseAzero = false;
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
				System.out.println("Systeme : reinitialisation de l'environnement");
				Environnement.ListEnvironement.clear();

			}
		});
		menuBar.add(menuItemRaz);

		//Create the groups to select the probability of dirt
		menu.addSeparator();
		menuItemPoussierePlus = new JMenuItem("Probabilite apparition poussiere +");
		menuItemPoussierePlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametres.PROBA_POUSSIERE<0.8) Parametres.PROBA_POUSSIERE += 0.05;
				System.out.println("Systeme : modification proba. apparition poussiere : "+Parametres.PROBA_POUSSIERE);
			}
		});
		menu.add(menuItemPoussierePlus);
		menuItemPoussiereMoins = new JMenuItem("Probabilite apparition poussiere -");
		menuItemPoussiereMoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametres.PROBA_POUSSIERE>=0.05) Parametres.PROBA_POUSSIERE -= 0.05;
				else Parametres.PROBA_POUSSIERE = 0;
				System.out.println("Systeme : modification proba. apparition poussiere : "+Parametres.PROBA_POUSSIERE);

			}
		});
		menu.add(menuItemPoussiereMoins);

		//Create the groups to select the probability of jewel
		menu.addSeparator();
		menuItemBijouPlus = new JMenuItem("Probabilite apparition bijou +");
		menuItemBijouPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametres.PROBA_BIJOU<0.8) Parametres.PROBA_BIJOU += 0.005;
				System.out.println("Systeme : modification proba. apparition bijoux : "+Parametres.PROBA_BIJOU);
			}
		});
		menu.add(menuItemBijouPlus);
		menuItemBijouMoins = new JMenuItem("Probabilite apparition bijou -");
		menuItemBijouMoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametres.PROBA_BIJOU>=0.002) Parametres.PROBA_BIJOU -= 0.002;
				else Parametres.PROBA_BIJOU = 0;
				System.out.println("Systeme : modification proba. apparition bijoux : "+Parametres.PROBA_BIJOU);
			}
		});
		menu.add(menuItemBijouMoins);

		return menuBar;
	}

	// -------------------------------------------------Menu de l agent-------------------------------------------------------------------------------
	
	
	public static JMenuBar createMenuBarAgent() {

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItemRazPerf;
		JMenuItem menuItemVitessePlus;
		JMenuItem menuItemVitesseMoins;
		JMenuItem menuInforme;


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
				System.out.println("Systeme : performances de l'agent reinitialisees");
				remiseAzero = true;
			}});
		menuBar.add(menuItemRazPerf);


		//Create the groups to select the speed
		menu.addSeparator();
		menuItemVitessePlus = new JMenuItem("Vitesse de l'agent +");
		menuItemVitessePlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Parametres.DELAI_AGENT>100) Parametres.DELAI_AGENT -= 50;
				System.out.println("Systeme : modification delai agent : "+Parametres.DELAI_AGENT);

			}
		});
		menu.add(menuItemVitessePlus);
		menuItemVitesseMoins = new JMenuItem("Vitesse de l'agent -");
		menuItemVitesseMoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Parametres.DELAI_AGENT += 50;
				System.out.println("Systeme : modification delai agent : "+Parametres.DELAI_AGENT);
			}
		});
		menu.add(menuItemVitesseMoins);


		//Build the explorationMenu
		menuInforme = new JMenuItem("Type d'exploration");
		menuBar.add(menuInforme);
		menuInforme.getAccessibleContext().setAccessibleDescription("Set event 2");
		menuInforme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choixInformee = !choixInformee;
			}
		});

		menuBar.add(menuInforme);

		return menuBar;

	}

	public static boolean isChoixInformee() {
		return choixInformee;
	}

	public static void setChoixInformee(boolean choix) {
		choixInformee = choix;
	}




}