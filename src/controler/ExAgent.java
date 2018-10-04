package controler;
import model.*;
import view.*;

public class ExAgent implements Runnable{

	public void run() {

		/**Initialisation de l affichage de l agent*/
		Draw drawing = new Draw(Parametres.TITRE_AGENT, Environnement.agent.getListElementObs());

		//testArbreNonInforme();
		//testArbreInforme();

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

			drawing.render();
			try {Thread.sleep(Parametres.DELAI_AGENT);} catch (InterruptedException e) {e.printStackTrace();}
		}

	}

	/** ======================================= Methode utile a la gestion de l agent ===========================================================*/

	// -------------------------------------------------Observation-------------------------------------------------------------------------------
	public void observation() {
		// Actualisation objectif si cible atteinte
		if(!Environnement.agent.getObjectifs().isEmpty()) {	
			Environnement.agent.actualiserObjectif();
		}
		// Observation si un cycle est fini ou liste d element observe vide
		if(Environnement.agent.getFrequenceObs() == Environnement.agent.getNbElementCycle() || Environnement.agent.getObjectifs().isEmpty()) { 
			Environnement.agent.setDebutCycle(true);

			System.out.println("--Demarage d'un nouveau cycle--");
			System.out.println("Agent : J'observe l'environnement a l'aide de mes capteurs");

			Environnement.agent.observerEnvironnement();

			//mode safe : espace d'etat trop grand pour le non informe -> passage en informee
			if(Environnement.agent.getListElementObs().size()>Parametres.LIMITE_NON_INFORME && !Draw.isChoixInformee()) {
				Draw.setChoixInformee(true);
				System.out.println("Systeme : Safe Mode Activated : mode informe");
			}
		}else {
			Environnement.agent.setDebutCycle(false);
		}
	}

	// -----------------------------------------------Mise a jour Etat----------------------------------------------------------------------------
	public void miseAJourEtat() {
		//Mise a jour des donnes sur la frequence
		if(Environnement.agent.isDebutCycle() && !Environnement.ListEnvironement.isEmpty()) {	// si le robot a terminer son cycle
			System.out.println("Agent : Je met a jour mon etat interne");
			Environnement.agent.ajoutPerformance();
			Environnement.agent.resetNbElementCycle(); 	// Nouveau cycle
			Environnement.agent.getObjectifs().clear();
			Environnement.agent.getMouvementChemin().clear();
		}

	}

	// --------------------------------------------------Decision---------------------------------------------------------------------------------
	public void decision() {
		// Choix de la frequence d observation
		if(Environnement.agent.isDebutCycle()) { // cycle termine
			Environnement.agent.choixFrequence();
			System.out.println("Agent : Je planifie mes prochaines actions");
		}
		// Choix du chemin a parcourir
		if(!Environnement.agent.getListElementObs().isEmpty() && Environnement.agent.isDebutCycle()) {	
			int nbElementObs = Environnement.agent.getListElementObs().size();

			if(nbElementObs>0) {
				if(!Draw.isChoixInformee()) {
					planificationNonInforme();
				}
				else{
					planificationInformee();
				}

			}

		}
		// Planification des actions a faire 
		if( !Environnement.agent.getObjectifs().isEmpty() && Environnement.agent.getMouvementChemin().isEmpty()) { // objectif existant et aucune action en attente
			planificationAction();
		}

	}

	public void planificationNonInforme() {
		ArbreNonInforme A = new ArbreNonInforme(Environnement.agent.getListElementObs(), Environnement.agent.getX(), Environnement.agent.getY());
		if(!A.getItineraireOptimal().isEmpty()) {
			Environnement.agent.setObjectifs(A.getItineraireOptimal());
		}
	}


	public void planificationInformee() {
		ArbreInforme arbreinf = new ArbreInforme(Environnement.agent.getX(), Environnement.agent.getY(), Environnement.agent.getListElementObs(), Environnement.agent.getListElementObs().get(0));
		arbreinf.greedySearch();
		Environnement.agent.setObjectifs(arbreinf.getItineraireOptimal());
	}

	public void planificationAction() {
		Element e = Environnement.agent.getObjectifs().get(0);
		Environnement.agent.cheminVers(e.getX(), e.getY());
		if(e.isPoussiere()) {
			Environnement.agent.getMouvementChemin().add(Agent.ASPIRER);
		}else {
			Environnement.agent.getMouvementChemin().add(Agent.RAMASSER);
		}

	}

	// ---------------------------------------------------Action----------------------------------------------------------------------------------
	public void actionAgent(){
		if(Environnement.agent.isDebutCycle()){
			System.out.println("Agent : J'effectue la sequence d'actions planifiees");
		}

		if(!Environnement.agent.getMouvementChemin().isEmpty()){
			switch(Environnement.agent.getMouvementChemin().get(0)){
			case Agent.HAUT:
				Environnement.agent.goUp();
				break;
			case Agent.BAS:
				Environnement.agent.goDown();
				break;
			case Agent.DROITE: 
				Environnement.agent.goRight();
				break;
			case Agent.GAUCHE:
				Environnement.agent.goLeft();
				break;
			case Agent.ASPIRER:
				Environnement.agent.aspirer();
				break;
			case Agent.RAMASSER:
				Environnement.agent.ramasser();
				break;
			default:
				break;
			}
			Environnement.agent.getMouvementChemin().remove(0);
		}
	}

	/** =============================================== Methode Test ===========================================================================*/
	public void testArbreNonInforme() {
		for (int i = 0; i < 5; i++) {
			int x = (int)(Math.random()*10);
			int y = (int)(Math.random()*10);
			Environnement.agent.getListElementObs().add(new Poussiere(x, y));
		}
		ArbreNonInforme A = new ArbreNonInforme(Environnement.agent.getListElementObs(), Environnement.agent.getX(), Environnement.agent.getY());
		Environnement.agent.setObjectifs(A.getItineraireOptimal());
	}

	public void testArbreInforme() {
		for (int i = 0; i < 5; i++) {
			int x = (int)(Math.random()*10);
			int y = (int)(Math.random()*10);
			Environnement.agent.getListElementObs().add(new Poussiere(x, y));
		}

		ArbreInforme B = new ArbreInforme(Environnement.agent.getX(), Environnement.agent.getY(), Environnement.agent.getListElementObs(), Environnement.agent.getListElementObs().get(0));
		B.greedySearch();
		Environnement.agent.setObjectifs(B.getItineraireOptimal());
	}

	public void afficherAction() {
		String s = "action : [ ";
		for (int i = 0; i < Environnement.agent.getMouvementChemin().size(); i++) {
			s += Environnement.agent.getMouvementChemin().get(i)+", ";
		}
		s += "]";
		System.out.println(s);
	}

	public void affichageFrequence() {
		double[][] tab = Environnement.agent.getTabFrequence();
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
		for (int i = 0; i < Environnement.agent.getObjectifs().size(); i++) {
			Element e = Environnement.agent.getObjectifs().get(i);
			System.out.println("("+e.getX()+","+e.getY()+")  ");
		}
	}

}
