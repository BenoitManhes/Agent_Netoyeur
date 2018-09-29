package controller;
import model.*;
import view.*;

public class exAgent implements Runnable{

	public void run() {

		/**Initialisation de l agent*/
		Draw drawing = new Draw(Parametre.TITRE_AGENT,model.Environement.agent.getListElementObs());
		
		/**Gestion de l agent*/
		testArbre();
		while(true) {	// gestion de l agent en boucle infini
			
			actionAgent();

			// Observer environement
			// Mise a jour Etat
			// Decision action
			// Action
			
			drawing.render();//mis a jour affichage avec drawing
			try {Thread.sleep(Parametre.DELAI_AGENT);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

	/**Methode utile a la gestion de l agent*/

	public void actionAgent(){
		if(!Environement.agent.getMouvementChemin().isEmpty()){
			switch(Environement.agent.getMouvementChemin().get(0)){
			case 1:
				Environement.agent.goUp();
				break;
			case 2:
				Environement.agent.goDown();
				break;
			case 3: 
				Environement.agent.goRight();
				break;
			case 4:
				Environement.agent.goLeft();
				break;
			default:
				break;
			}
			Environement.agent.getMouvementChemin().remove(0);
		}
	}

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
