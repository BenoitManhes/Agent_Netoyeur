package controller;
import model.*;
import view.*;

public class exAgent implements Runnable{
	
	public void run() {

		/**Initialisation de l agent*/
		Agent A = new Agent();
		Draw drawing = new Draw("Agent");
		
		/**Gestion de l agent*/
		while(true) {	// gestion de l agent en boucle infini
			
			// Observer environement
			// Mise a jour Etat
			// Decision action
			// Action
			
			drawing.render();//mis a jour affichage avec drawing
			try {Thread.sleep(Parametre.DELAI);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	/**Methode utile a la gestion de l agent*/

}
