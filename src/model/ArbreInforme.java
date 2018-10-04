package model;

import java.util.ArrayList;

public class ArbreInforme {
	private ArrayList<Element> listeElemObs;
	private ArrayList<Element> itineraireOptimal;
	private int xDepart;
	private int yDepart;
	private Element elemDestination;

	public ArbreInforme(int xDepart, int yDepart, ArrayList<Element> listeElemObs, Element elemDestination){
		this.xDepart = xDepart;
		this.yDepart = yDepart;
		this.listeElemObs = listeElemObs;
		this.elemDestination = elemDestination;
		this.itineraireOptimal = new ArrayList<Element>();
	}

	public void greedySearch(){
		ArrayList<Element> listeOuverte = new ArrayList<Element>(this.listeElemObs);
		ArrayList<Element> listeFermee = new ArrayList<Element>();
		Element positionInitiale = new Poussiere(this.xDepart, this.yDepart);
		Element elementActuel = positionInitiale;
		Element prochainElement = null;
		boolean trouve = false;
		while(!trouve){ // tant que l'on a pas atteint le noeud objectif
			int score = -1000;
			for(Element e : listeOuverte){	//on calcul les heuristiques pour les prochains noeuds
				if(heuristique(elementActuel, e) > score){
					score = heuristique(elementActuel, e);
					prochainElement = e;
				}
			}
			elementActuel = prochainElement; //on choisis l'élement avec la meilleur heuristique comme prochain noeud
			listeFermee.add(elementActuel);
			listeOuverte.remove(elementActuel);
			if(elementActuel.equals(this.elemDestination)){
				trouve = true;
			}

		}
		this.itineraireOptimal.addAll(listeFermee);
	}

	public int heuristique(Element a, Element b){
		//Points raportés par le traitement de l'element - cout de déplacement - cout de traitement de la poussiere
		int score = b.getPts() - distanceManhattan(a,b)*Parametres.COUT_ENERGIE - Parametres.COUT_ENERGIE;
		return score;
	}

	public static int distanceManhattan(Element a, Element b) {
		int x = Math.abs(a.getX() - b.getX());
		int y = Math.abs(a.getY() - b.getY());
		return x + y ;
	}

	public ArrayList<Element> getItineraireOptimal(){
		return this.itineraireOptimal;
	}
	
}