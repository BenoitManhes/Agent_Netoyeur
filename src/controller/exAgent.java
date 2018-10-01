package controller;
import org.omg.CORBA.SystemException;
import org.w3c.dom.css.ElementCSSInlineStyle;

import model.*;
import view.*;

public class exAgent implements Runnable{

	public void run() {

		/**Initialisation de l affichage de l agent*/
		Draw drawing = new Draw(Parametre.TITRE_AGENT, Environement.agent.getListElementObs());

		/**Gestion de l agent*/
		while(true) {	// gestion de l agent en boucle infini

			// Observer environement
			observation();

			// Mise a jour Etat
			miseAJourEtat();

			// Decision Action 
			decision();

			// Action
			actionAgent();

			//mis a jour affichage et delai
			drawing.render();
			try {Thread.sleep(Parametre.DELAI_AGENT);} catch (InterruptedException e) {e.printStackTrace();}

		}
	}

	/** ======================================= Methode utile a la gestion de l agent ===========================================================*/

	// -------------------------------------------------Observation-------------------------------------------------------------------------------
	public void observation() {
		if(Environement.agent.getMouvementChemin().isEmpty()) { // si aucun objectif est en attente
			Environement.agent.observerEnvironnement();
		}
	}

	// -----------------------------------------------Mise a jour Etat----------------------------------------------------------------------------
	public void miseAJourEtat() {
		//Mise a jour des donnes sur la frequence
		if(Environement.agent.getNbrCasesParcourues()>0) {	// si le robot a deja effectuer un cycle
			Environement.agent.ajoutPerformance();
		}
		// Mise a jour du prochain element a atteindre
		if(!Environement.agent.getObjectifs().isEmpty()) {	
			Environement.agent.actualiserObjectif();
		}
	}

	// --------------------------------------------------Decision---------------------------------------------------------------------------------
	public void decision() {
		// Choix de la frequence d observation
		if(Environement.agent.getObjectifs().isEmpty()) {
			Environement.agent.choixFrequence();
			System.out.println("Frequence choisi : "+Environement.agent.getFrequenceObs());
		}
		// Choix du chemin a parcourir
		if(!Environement.agent.getListElementObs().isEmpty() && Environement.agent.getObjectifs().isEmpty()) {	
			int nbElementObs = Environement.agent.getListElementObs().size();
			if(nbElementObs>0) {
				planificationItineraire();
			}
		}
		// Planification des actions a faire 
		if( !Environement.agent.getObjectifs().isEmpty() && Environement.agent.getMouvementChemin().isEmpty()) { // objectif existant et aucune action en attente
			planificationAction();
		}
	}

	public void planificationItineraire() {
		ArbreNonInforme A = new ArbreNonInforme(Environement.agent.getListElementObs(),Environement.agent.getFrequenceObs(), Environement.agent.getX(), Environement.agent.getY());
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

	// ---------------------------------------------------Action----------------------------------------------------------------------------------
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







	/** =============================================== Methode Test ===========================================================================*/
	public void testArbre() {
		for (int i = 0; i < 3; i++) {
			int x = (int)(Math.random()*10);
			int y = (int)(Math.random()*10);
			model.Environement.agent.getListElementObs().add(new Poussiere(x, y));
		}
		ArbreNonInforme A = new ArbreNonInforme(Environement.agent.getListElementObs(),10, Environement.agent.getX(), Environement.agent.getY());
		Environement.agent.setObjectifs(A.getItineraireOptimal());
		System.out.println("Taille final "+Environement.agent.getObjectifs().size());
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
