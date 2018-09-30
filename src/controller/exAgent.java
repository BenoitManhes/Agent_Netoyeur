package controller;
import model.*;
import view.*;

public class exAgent implements Runnable{

	public void run() {

		/**Initialisation de l agent*/
		Draw drawing = new Draw(Parametre.TITRE_AGENT, Environement.agent.getListElementObs());

		/**Gestion de l agent*/
		while(true) {	// gestion de l agent en boucle infini

			// Observer environement
			if(Environement.agent.getListElementObs().isEmpty()) { // si aucun objectif est en attente
				Environement.agent.observerEnvironnement();

				// Mise a jour Etat
				int nbElementObs = Environement.agent.getListElementObs().size();
				if(nbElementObs>0) {
					int X = nbElementObs;
					planificationItineraire(X);
				}
			}
			// Decision Action 
			if(!Environement.agent.getObjectifs().isEmpty()) {
				Environement.agent.actualiserObjectif();
			}
			if( !Environement.agent.getObjectifs().isEmpty() && Environement.agent.getMouvementChemin().isEmpty()) { // il existe un objectif et le robot n a rien a faire
				planificationAction();
				
			}

			// Action
			actionAgent();
			
			drawing.render();//mis a jour affichage avec drawing
			try {Thread.sleep(Parametre.DELAI_AGENT);} catch (InterruptedException e) {e.printStackTrace();}
			
		}
	}

	/**Methode utile a la gestion de l agent*/

	public void actionAgent(){
		if(!Environement.agent.getMouvementChemin().isEmpty()){
			switch(Environement.agent.getMouvementChemin().get(0)){
			case Agent.HAUT:
				Environement.agent.goUp();
				break;
			case Agent.BAS:
				Environement.agent.goDown();
				break;
			case Agent.DROITE: 
				Environement.agent.goRight();
				break;
			case Agent.GAUCHE:
				Environement.agent.goLeft();
				break;
			case Agent.ASPIRER:
				Environement.agent.aspirer();
				break;
			case Agent.RAMASSER:
				Environement.agent.ramasser();
				break;
			default:
				break;
			}
			Environement.agent.getMouvementChemin().remove(0);
		}
	}

	public void testArbre() {
		for (int i = 0; i < 3; i++) {
			int x = (int)(Math.random()*10);
			int y = (int)(Math.random()*10);
			model.Environement.agent.getListElementObs().add(new Poussiere(x, y));
		}
		/*ArbreNonInforme A = new ArbreNonInforme(Environement.agent.getListElementObs(),10, Environement.agent.getX(), Environement.agent.getY());
		Environement.agent.setObjectifs(A.getItineraireOptimal());
		System.out.println("Taille final "+Environement.agent.getObjectifs().size());*/
	}

	public void planificationItineraire(int X) {
		ArbreNonInforme A = new ArbreNonInforme(Environement.agent.getListElementObs(),X, Environement.agent.getX(), Environement.agent.getY());
		Environement.agent.setObjectifs(A.getItineraireOptimal());
	}

	public void planificationAction() {
		Element e = Environement.agent.getObjectifs().get(0);
		Environement.agent.cheminVers(e.getX(), e.getY());
		if(e.isPoussiere()) {
			Environement.agent.getMouvementChemin().add(Agent.ASPIRER);
		}else {
			Environement.agent.getMouvementChemin().add(Agent.RAMASSER);
		}

	}

	public void afficherAction() {
		String s = "action : [ ";
		for (int i = 0; i < Environement.agent.getMouvementChemin().size(); i++) {
			s += Environement.agent.getMouvementChemin().get(i)+", ";
		}
		s += "]";
		System.out.println(s);
	}

}
