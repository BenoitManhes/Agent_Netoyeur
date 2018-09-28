package controller;
import javax.swing.plaf.ProgressBarUI;

import model.*;
import view.*;

public class exEnvironement implements Runnable{

	@Override
	public void run() {

		/**Initialisation de l environement*/
		Draw drawing = new Draw("Environement");
		// environement déjà creer en  public static -> Unique et Accessible a tous
		double probaPoussiere = Parametre.PROBA_POUSSIERE;
		double probaBijou = Parametre.PROBA_BIJOU;

		//initialisationEnvironement();

		/**Gestion environement*/
		while(true) {	// gestion de l'environement en boucle infini

			//apparition ou non d elements
			drawing.render();	//mis a jour affichage avec drawing
			try {Thread.sleep(Parametre.DELAI);} catch (InterruptedException e) {e.printStackTrace();}
		}

	}

	/**Methode utile a la gestion de l environement*/
}
