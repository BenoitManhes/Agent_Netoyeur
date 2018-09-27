package controller;
import model.*;
import view.*;

public class exEnvironement implements Runnable{

	@Override
	public void run() {
		Draw drawing = new Draw("Environement");
		/**Initialisation de l environement*/
		
		// environement déjà creer en  public static -> Unique et Accessible a tous
		double probaPoussiere = Parametre.PROBA_POUSSIERE;
		double probaBijou = Parametre.PROBA_BIJOU;

		//initialisationEnvironement();
		
		/**Gestion environement*/
		while(true) {	// gestion de l'environement en boucle infini
			
			//apparition ou non d elements
			//mis a jour affichage avec drawing
			drawing.render();	//mis a jour affichage avec drawing
			try {Thread.sleep(Parametre.DELAI);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
	}
	
	/**Methode utile a la gestion de l environement*/
}
