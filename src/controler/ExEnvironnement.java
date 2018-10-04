package controler;
import model.*;
import view.*;

public class ExEnvironnement implements Runnable{

	@Override
	public void run() {

		/**Initialisation de l environement*/
		initialisationEnvironnement();
		Draw drawing = new Draw(Parametres.TITRE_ENVIRONNEMENT,Environnement.ListEnvironement);


		/**Gestion environement*/
		while(true) {	// gestion de l'environement en boucle infini

			//apparition ou non d elements
			genererPoussiere(Parametres.PROBA_POUSSIERE);
			genererBijou(Parametres.PROBA_BIJOU);

			//Regarder le robot et compter les points
			calculateScoreEnvironnement();
			majEnvironement();

			// Calcul des statistiques
			updateScoreMoyen();

			//mis a jour affichage avec drawing
			drawing.render();	
			try {Thread.sleep(Parametres.DELAI_ENV);} catch (InterruptedException e) {e.printStackTrace();}
		}

	}

	/** ======================================= Methode utile a la gestion de l environnement ===========================================================*/

	// -------------------------------------------------Generer les elements-------------------------------------------------------------------------------

	public void genererPoussiere(double p) {
		double i = Math.random();
		if(i<=p) {
			int x = (int) (Math.random()*Parametres.TAILLE_GRILLE);
			int y = (int) (Math.random()*Parametres.TAILLE_GRILLE);
			if(Environnement.caseDisponible(x, y, true)) {
				Environnement.ListEnvironement.add(new Poussiere(x, y));
				System.out.println("Environnement : Poussiere apparue en "+x+","+y);
			}
		}
	}
	public void genererBijou(double p) {
		double i = Math.random();
		if(i<=p) {
			int x = (int) (Math.random()*Parametres.TAILLE_GRILLE);
			int y = (int) (Math.random()*Parametres.TAILLE_GRILLE);
			if(Environnement.caseDisponible(x, y, false)) {
				Environnement.ListEnvironement.add(new Bijou(x, y));
				System.out.println("Environnement : Bijou apparue en "+x+","+y);
			}
		}
	}


	// -------------------------------------------------Obtenir les positions de l agent-------------------------------------------------------------------------------

	public int getXPositionAgent() {
		return Environnement.agent.getX();
	}

	public int getYPositionAgent() {
		return Environnement.agent.getY();
	}

	// -------------------------------------------------Obtenir la derniere action de l agent-------------------------------------------------------------------------------

	public int getLastActionAgent() {
		return Environnement.agent.getLastAction();
	}


	// -------------------------------------------------Calculer le score-------------------------------------------------------------------------------

	private void calculateScoreEnvironnement() {	//Prend en parametres les coordonnees de l'agent et sa derniere action

		int score = 0;
		int PositionX = getXPositionAgent();
		int PositionY = getYPositionAgent();
		int lastAction = getLastActionAgent();

		//Test si il y a de la poussiere
		if(isTherePoussiere(PositionX, PositionY, true) && lastAction == Agent.ASPIRER) { 
			score+=Parametres.POINT_POUSSIERE;
			System.out.println("Environnement : L'agent a aspire de la poussiere");
			System.out.println("Environnement : L'agent gagne "+score+" points.");
		}
		//Test si il y a un bijou
		if (isTherePoussiere(PositionX, PositionY, false)) {
			if(lastAction == Agent.ASPIRER) {

				score+=Parametres.MALUS_BIJOU;
				System.out.println("Environnement : L'agent a aspire un bijou");
				System.out.println("Environnement : l'agent perd "+score+" points.");
			}
			else if(lastAction == Agent.RAMASSER) {
				score+=Parametres.POINT_BIJOU;
				System.out.println("Environnement : L'agent a ramasse un bijou");
				System.out.println("Environnement : L'agent gagne "+score+" points.");
			}
		}
		ajouterScore(score);
	}

	private void ajouterScore(int score) {
		Environnement.setScoreEnvironnement(Environnement.getScoreEnvironnement() + score);
	}

	private void updateScoreMoyen() {
		if(!Environnement.scoresObtenus.isEmpty()) {
			double somme = 0;
			for (int i = 0; i < Environnement.scoresObtenus.size(); i++) {
				somme += Environnement.scoresObtenus.get(i);
			}
			Environnement.setMoyenneScore(somme/Environnement.scoresObtenus.size());
		}
	}

	// -------------------------------------------------Voir la presence d element-------------------------------------------------------------------------------

	public boolean isTherePoussiere(int x, int y, boolean testPoussiere) {
		boolean presence = false;
		for (int i = 0; i < Environnement.ListEnvironement.size(); i++) {
			int a = Environnement.ListEnvironement.get(i).getX();
			int b = Environnement.ListEnvironement.get(i).getY();
			if(x==a && y==b && Environnement.ListEnvironement.get(i).isPoussiere()==testPoussiere) {
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
			for (int i = 0; i < Environnement.ListEnvironement.size(); i++) {
				int a = Environnement.ListEnvironement.get(i).getX();
				int b = Environnement.ListEnvironement.get(i).getY();
				if(PositionX==a && PositionY==b && (Environnement.ListEnvironement.get(i).isPoussiere()==false || Environnement.ListEnvironement.get(i).isPoussiere()==true)) {
					Environnement.ListEnvironement.remove(i);
				}
			}
		}

		//Ramasser avec condition
		if(lastAction == Agent.RAMASSER) {
			for (int i = 0; i < Environnement.ListEnvironement.size(); i++) {
				int a = Environnement.ListEnvironement.get(i).getX();
				int b = Environnement.ListEnvironement.get(i).getY();
				if(PositionX==a && PositionY==b && Environnement.ListEnvironement.get(i).isPoussiere()==false) {
					Environnement.ListEnvironement.remove(i);
				}
			}
		}
	}

	// -------------------------------------------------Methode pour initialiser l'environnement-------------------------------------------------------------------------------

	private void initialisationEnvironnement() {
		genererBijou(Parametres.PROBA_BIJOU*1000);
		genererPoussiere(Parametres.PROBA_POUSSIERE*1000);
	}

}
