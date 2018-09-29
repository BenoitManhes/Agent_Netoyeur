package controller;
import model.*;
import view.*;

public class exAgent implements Runnable{

	public void run() {

		/**Initialisation de l agent*/
		Draw drawing = new Draw("Agent",Environement.agent.getListElementObs());
		Environement.agent.cheminVers(0, 0);
		/**Gestion de l agent*/
		while(true) {	// gestion de l agent en boucle infini
			
			action();

			// Observer environement
			// Mise a jour Etat
			// Decision action
			// Action

			drawing.render();//mis a jour affichage avec drawing
			try {Thread.sleep(Parametre.DELAI_AGENT);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

	/**Methode utile a la gestion de l agent*/

	public void action(){
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

}
