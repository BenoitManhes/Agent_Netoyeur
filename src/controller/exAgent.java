package controller;
import model.*;
import view.*;

public class exAgent implements Runnable{
	
	public void run() {

		/**Initialisation de l agent*/
		Draw drawing = new Draw("Agent",Environement.agent.getListElementObs());
		
		/**Gestion de l agent*/
		while(true) {	// gestion de l agent en boucle infini
			
			//exemple de fonctionnement :
			int actionAuHasard = (int)(Math.random() * 7);
			
			switch(actionAuHasard){
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
			case 5:
				Environement.agent.aspirer();
				break;
			case 6:
				Environement.agent.ramasser();
				break;
			default:
				System.out.println("Agent : Je ne fais rien");
				break;
			}
			
			
			// Observer environement
			// Mise a jour Etat
			// Decision action
			// Action
			
			drawing.render();//mis a jour affichage avec drawing
			try {Thread.sleep(Parametre.DELAI_AGENT);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	/**Methode utile a la gestion de l agent*/

}
