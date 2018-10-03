package controller;
import javax.annotation.processing.SupportedSourceVersion;

import org.omg.CORBA.SystemException;
import org.w3c.dom.css.ElementCSSInlineStyle;

import model.*;
import view.*;

public class exAgent implements Runnable{

	public void run() {

		/**Initialisation de l affichage de l agent*/
		Draw drawing = new Draw(Parametre.TITRE_AGENT, Environement.agent.getListElementObs());
		
	   //testAInformee();
	   //testArbre();
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
				
				Environement.enregistrerPerf();
				Environement.reinitialiserPerf();
			

			//mis a jour affichage et delai
			drawing.render();
			try {Thread.sleep(Parametre.DELAI_AGENT);} catch (InterruptedException e) {e.printStackTrace();}

		}
	}

	/** ======================================= Methode utile a la gestion de l agent ===========================================================*/

	// -------------------------------------------------Observation-------------------------------------------------------------------------------
	public void observation() {
		// Actualisation objectif si cible atteinte
		if(!Environement.agent.getObjectifs().isEmpty()) {	
			Environement.agent.actualiserObjectif();
		}
		// Observation si un cycle est finie ou liste d element observe vide
		if(Environement.agent.getFrequenceObs() == Environement.agent.getNbElementCycle() || Environement.agent.getObjectifs().isEmpty()) { 
			Environement.agent.setDebutCycle(true);
			
			if(Environement.agent.getFrequenceObs() == Environement.agent.getNbElementCycle())
			{
				System.out.println("--Demarage d'un nouveau cycle--");
			}
			System.out.println("Agent : J'observe l'environnement a l'aide de mes capteurs");
			Environement.agent.observerEnvironnement();
			
			//mode safe : espace d'etat trop grand pour le non informe -> passage en informee
			if(Environement.agent.getListElementObs().size()>Parametre.LIMITE_NON_INFORME && !Draw.isChoixInformee()) {
				Draw.setChoixInformee(true);
				System.out.println("Systeme : Safe Mode Activated : mode informe");
			}
		}
	}

	// -----------------------------------------------Mise a jour Etat----------------------------------------------------------------------------
	public void miseAJourEtat() {
		//Mise a jour des donnes sur la frequence
		if(Environement.agent.getFrequenceObs() == Environement.agent.getNbElementCycle() || Environement.agent.getObjectifs().isEmpty()) {	// si le robot a terminer son cycle
			System.out.println("Agent : Je met a jour mon etat interne");
			Environement.agent.ajoutPerformance();
			Environement.agent.resetNbElementCycle(); 	// Nouveau cycle
			Environement.agent.getObjectifs().clear();
			Environement.agent.getMouvementChemin().clear();
			/*System.out.println("Performance enregistr�");
			affichageFrequence();*/
		}

	}

	// --------------------------------------------------Decision---------------------------------------------------------------------------------
	public void decision() {
		// Choix de la frequence d observation
		if(Environement.agent.getNbElementCycle() == 0 && Environement.agent.getObjectifs().isEmpty()) { // cycle termine
			Environement.agent.choixFrequence();
			System.out.println("Agent : Je planifie mes prochaines actions");
			//System.out.println("Frequence choisi : "+Environement.agent.getFrequenceObs());
			
		}
		// Choix du chemin a parcourir
		if(!Environement.agent.getListElementObs().isEmpty() && (Environement.agent.getNbElementCycle() == 0 || Environement.agent.getObjectifs().isEmpty())) {	
			int nbElementObs = Environement.agent.getListElementObs().size();

			if(nbElementObs>0) {
				if(!Draw.isChoixInformee()) {
					planificationItineraire();
				}
				else if(Draw.isChoixInformee()) {
					planificationInformee();
				}
				
			}

		}
		// Planification des actions a faire 
		if( !Environement.agent.getObjectifs().isEmpty() && Environement.agent.getMouvementChemin().isEmpty()) { // objectif existant et aucune action en attente
			planificationAction();
		}
		
	}

	public void planificationItineraire() {
		ArbreNonInforme A = new ArbreNonInforme(Environement.agent.getListElementObs(), Environement.agent.getX(), Environement.agent.getY());
		
		if(!A.getItineraireOptimal().isEmpty()) {
			Environement.agent.setObjectifs(A.getItineraireOptimal());
		}
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
		if(Environement.agent.isDebutCycle()){
			System.out.println("Agent : J'effectue la sequence d'actions planifiees");
		}
		
		if(!Environement.agent.getMouvementChemin().isEmpty()){
			Environement.agent.setDebutCycle(false);
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
		ArbreNonInforme A = new ArbreNonInforme(Environement.agent.getListElementObs(), Environement.agent.getX(), Environement.agent.getY());
		Environement.agent.setObjectifs(A.getItineraireOptimal());
		System.out.println("Taille final "+Environement.agent.getObjectifs().size());
	}
	
	public void testAInformee() {
		for (int i = 0; i < 8; i++) {
			int x = (int)(Math.random()*10);
			int y = (int)(Math.random()*10);
			model.Environement.agent.getListElementObs().add(new Poussiere(x, y));
		}
		
		Glouton arbreinf = new Glouton(Environement.agent.getX(), Environement.agent.getY(), Environement.agent.getListElementObs(), Environement.agent.getListElementObs().get(0));
		arbreinf.greedySearch();
		Environement.agent.setObjectifs(arbreinf.getItineraireOptimal());
	}
	

	public void planificationItineraire(int X) {
		ArbreNonInforme A = new ArbreNonInforme(Environement.agent.getListElementObs(), Environement.agent.getX(), Environement.agent.getY());
		Environement.agent.setObjectifs(A.getItineraireOptimal());
		Environement.enregistrerPerf();
		Environement.reinitialiserPerf();
	}
	
	public void planificationInformee() {
		Glouton arbreinf = new Glouton(Environement.agent.getX(), Environement.agent.getY(), Environement.agent.getListElementObs(), Environement.agent.getListElementObs().get(0));
		arbreinf.greedySearch();
		Environement.agent.setObjectifs(arbreinf.getItineraireOptimal());
		Environement.enregistrerPerf();
		Environement.reinitialiserPerf();
	}


	public void afficherAction() {
		String s = "action : [ ";
		for (int i = 0; i < Environement.agent.getMouvementChemin().size(); i++) {
			s += Environement.agent.getMouvementChemin().get(i)+", ";
		}
		s += "]";
		System.out.println(s);
	}

	public void affichageFrequence() {
		double[][] tab = Environement.agent.getTabFrequence();
		for (int i = 0; i < tab.length; i++) {
			
			double taux = 0;
			if( tab[i][1] != 0 ) { // pas de division par 0
				taux = tab[i][0] / ( tab[i][1] );
			}
			System.out.print("F="+(i+1)+" : "+taux+"\t");
		}
		System.out.println();
	}

	public void afficherItineraire() {
		for (int i = 0; i < Environement.agent.getObjectifs().size(); i++) {
			Element e = Environement.agent.getObjectifs().get(i);
			System.out.println("("+e.getX()+","+e.getY()+")  ");
		}
	}
	
}
