package controller;
import model.*;
import view.*;

public class exAgent implements Runnable{
	
	public void run() {

		/**Initialisation de l agent*/
		Draw drawing = new Draw("Agent",model.Environement.agent.getListElementObs());
		
		/**Gestion de l agent*/
		testArbre();
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
	public void testArbre() {
		for (int i = 0; i < 10; i++) {
			int x = (int)(Math.random()*10);
			int y = (int)(Math.random()*10);
			model.Environement.agent.getListElementObs().add(new Poussiere(x, y));
		}
		ArbreNonInforme A = new ArbreNonInforme(Environement.agent.getListElementObs(),10, Environement.agent.getX(), Environement.agent.getY());
		Environement.agent.setObjectifs(A.getItineraireOptimal());
		System.out.println("Taille final "+Environement.agent.getObjectifs().size());
	}
	
}
