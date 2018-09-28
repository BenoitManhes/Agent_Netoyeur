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
}
