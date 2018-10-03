package controller;
import model.*;
import view.*;

public class exEnvironement implements Runnable{

	@Override
	public void run() {

		/**Initialisation de l environement*/
		initialisationEnvironnement();
		Draw drawing = new Draw(Parametre.TITRE_ENVIRONNEMENT,Environement.ListEnvironement);
		double probaPoussiere = Parametre.PROBA_POUSSIERE;
		double probaBijou = Parametre.PROBA_BIJOU;

		/**Gestion environement*/
		while(true) {	// gestion de l'environement en boucle infini

			//apparition ou non d elements
			genererPoussiere(probaPoussiere);
			genererBijou(probaBijou);

			//Regarder le robot et compter les points
			calculateScoreEnvironnement();
			majEnvironement();

			// Calcul des statistiques
			updateScoreMoyen();

			//mis a jour affichage avec drawing
			drawing.render();	
			try {Thread.sleep(Parametre.DELAI_ENV);} catch (InterruptedException e) {e.printStackTrace();}
		}

	}

	/** ======================================= Methode utile a la gestion de l environnement ===========================================================*/

	// -------------------------------------------------Generer les elements-------------------------------------------------------------------------------

	public void genererPoussiere(double p) {
		double i = Math.random();
		if(i<=p) {
			int x = (int) (Math.random()*Parametre.TAILLE_GRILLE);
			int y = (int) (Math.random()*Parametre.TAILLE_GRILLE);
			if(Environement.caseDisponible(x, y, true)) {
				Environement.ListEnvironement.add(new Poussiere(x, y));
				System.out.println("Environnement : Poussiere apparue en "+x+","+y);
			}
		}
	}
	public void genererBijou(double p) {
		double i = Math.random();
		if(i<=p) {
			int x = (int) (Math.random()*Parametre.TAILLE_GRILLE);
			int y = (int) (Math.random()*Parametre.TAILLE_GRILLE);
			if(Environement.caseDisponible(x, y, false)) {
				Environement.ListEnvironement.add(new Bijou(x, y));
				System.out.println("Environnement : Bijou apparue en "+x+","+y);
			}
		}
	}


	// -------------------------------------------------Obtenir les positions de l agent-------------------------------------------------------------------------------

	public int getXPositionAgent() {
		return Environement.agent.getX();
	}

	public int getYPositionAgent() {
		return Environement.agent.getY();
	}

	// -------------------------------------------------Obtenir la derniere action de l agent-------------------------------------------------------------------------------

	public int getLastActionAgent() {
		return Environement.agent.getLastAction();
	}


	// -------------------------------------------------Calculer le score-------------------------------------------------------------------------------

	private void calculateScoreEnvironnement() {	//Prend en parametres les coordonnees de l'agent et sa derniere action

		int score = 0;
		int PositionX = getXPositionAgent();
		int PositionY = getYPositionAgent();
		int lastAction = getLastActionAgent();

		//Test si il y a de la poussiere
		if(isTherePoussiere(PositionX, PositionY, true) && lastAction == Agent.ASPIRER) { 
			score+=Parametre.POINT_POUSSIERE;
			System.out.println("Environnement : L'agent a aspire de la poussiere");
			System.out.println("Environnement : L'agent gagne "+score+" points.");
		}
		//Test si il y a un bijou
		if (isTherePoussiere(PositionX, PositionY, false)) {
			if(lastAction == Agent.ASPIRER) {

				score+=Parametre.MALUS_BIJOU;
				System.out.println("Environnement : L'agent a aspire un bijou");
				System.out.println("Environnement : l'agent perd "+score+" points.");
			}
			else if(lastAction == Agent.RAMASSER) {
				score+=Parametre.POINT_BIJOU;
				System.out.println("Environnement : L'agent a ramasse un bijou");
				System.out.println("Environnement : L'agent gagne "+score+" points.");
			}
		}
		ajouterScore(score);
	}

	private void ajouterScore(int score) {
		Environement.setScoreEnvironnement(Environement.getScoreEnvironnement() + score);
	}

	private void updateScoreMoyen() {
		if(!Environement.scoresObtenus.isEmpty()) {
			double somme = 0;
			for (int i = 0; i < Environement.scoresObtenus.size(); i++) {
				somme += Environement.scoresObtenus.get(i);
			}
			Environement.setMoyenneScore(somme/Environement.scoresObtenus.size());
		}
	}

	// -------------------------------------------------Voir la presence d element-------------------------------------------------------------------------------

	public boolean isTherePoussiere(int x, int y, boolean testPoussiere) {
		boolean presence = false;
		for (int i = 0; i < Environement.ListEnvironement.size(); i++) {
			int a = Environement.ListEnvironement.get(i).getX();
			int b = Environement.ListEnvironement.get(i).getY();
			if(x==a && y==b && Environement.ListEnvironement.get(i).isPoussiere()==testPoussiere) {
				presence = true;
			}
		}
		return presence;
	}

	// -------------------------------------------------Mettre a jour l'environement-------------------------------------------------------------------------------

	private void majEnvironement() {

		int PositionX = getXPositionAgent();
		int PositionY = getYPositionAgent();
		int lastAction = getLastActionAgent();

		//Tout aspirer
		if(lastAction == Agent.ASPIRER) {
			for (int i = 0; i < Environement.ListEnvironement.size(); i++) {
				int a = Environement.ListEnvironement.get(i).getX();
				int b = Environement.ListEnvironement.get(i).getY();
				if(PositionX==a && PositionY==b && (Environement.ListEnvironement.get(i).isPoussiere()==false || Environement.ListEnvironement.get(i).isPoussiere()==true)) {
					Environement.ListEnvironement.remove(i);
				}
			}
		}

		//Ramasser avec condition
		if(lastAction == Agent.RAMASSER) {
			for (int i = 0; i < Environement.ListEnvironement.size(); i++) {
				int a = Environement.ListEnvironement.get(i).getX();
				int b = Environement.ListEnvironement.get(i).getY();
				if(PositionX==a && PositionY==b && Environement.ListEnvironement.get(i).isPoussiere()==false) {
					Environement.ListEnvironement.remove(i);
				}
			}
		}
	}

	// -------------------------------------------------Methode pour initialiser l'environnement-------------------------------------------------------------------------------

	private void initialisationEnvironnement() {
		genererBijou(Parametre.PROBA_BIJOU*1000);
		genererPoussiere(Parametre.PROBA_POUSSIERE*1000);
	}

}
