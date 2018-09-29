package controller;
import javax.swing.plaf.ProgressBarUI;

import model.*;
import view.*;

public class exEnvironement implements Runnable{

	@Override
	public void run() {

		/**Initialisation de l environement*/
		Draw drawing = new Draw("Environement",Environement.ListEnvironement);
		// environement d�j� creer en  public static -> Unique et Accessible a tous
		double probaPoussiere = Parametre.PROBA_POUSSIERE;
		double probaBijou = Parametre.PROBA_BIJOU;

		//initialisationEnvironement();

		/**Gestion environement*/
		while(true) {	// gestion de l'environement en boucle infini

			//apparition ou non d elements
			genererPoussiere(probaPoussiere);
			genererBijou(probaBijou);
			
			//Regarder le robot et compter les points
			
			calculateScoreEnvironnement();

			//mis a jour affichage avec drawing
			drawing.render();	
			try {Thread.sleep(Parametre.DELAI);} catch (InterruptedException e) {e.printStackTrace();}
		}

	}

	/**Methode utile a la gestion de l environement*/

	public void genererPoussiere(double p) {
		double i = Math.random();
		if(i<=p) {
			int x = (int) (Math.random()*Parametre.TAILLE_GRILLE);
			int y = (int) (Math.random()*Parametre.TAILLE_GRILLE);
			if(Environement.caseDisponible(x, y, true)) {
				Environement.ListEnvironement.add(new Poussiere(x, y));
				System.out.println("Poussiere apparue en "+x+","+y);
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
				System.out.println("Bijou apparue en "+x+","+y);
			}
		}
	}
	
	//Recupere actuelle la position du robot
	public int getXPositionAgent() {
		return Environement.agent.getX();
	}
	
	public int getYPositionAgent() {
		return Environement.agent.getY();
	}
	
	//Recupere la derniere action du robot
	public int getLastActionAgent() {
		return Environement.agent.getLastAction();
	}
	
	//Calculer les points
	public int calculateScoreEnvironnement() {	//Prend en parametres les coordonnees de l'agent et sa derniere action
		
		int score = 0;
		int PositionX = getXPositionAgent();
		int PositionY = getYPositionAgent();
		int lastAction = getLastActionAgent();
		
		//Test si il y a de la poussiere
		if(isTherePoussiere(PositionX, PositionY, true) && lastAction == Agent.ASPIRER_POUSSIERE) { 
			System.out.println("Environnement : Je detecte que l'agent a aspire la ou il y avait de la poussiere");
			score+=Parametre.POINT_POUSSIERE;
		}
		//Test si il y a un bijou
		if (isTherePoussiere(PositionX, PositionY, false)) {
			if(lastAction == Agent.ASPIRER_POUSSIERE) {
				System.out.println("Environnement : Je detecte que l'agent a aspire la ou il y avait un bijou");
				score+=Parametre.MALUS_BIJOU;
			}
			else if(lastAction == Agent.RAMASSER_BIJOU) {
				System.out.println("Environnement : Je detecte que l'agent a ramasse la ou il y avait un bijou");
				score+=Parametre.POINT_BIJOU;
			}
		}
		System.out.println("Environnement : l'action de l'agent lui octroie");
		return score;
	}
	
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
	
	
	
	
	
}
