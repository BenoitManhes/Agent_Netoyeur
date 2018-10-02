package model;

import java.util.ArrayList;

public class Glouton {
	private ArrayList<Element> listeElemObs;
	private ArrayList<Element> itineraireOptimal;
	private int xDepart;
	private int yDepart;
	private Element elemDestination;


public Glouton(int xDepart, int yDepart, ArrayList<Element> listeElemObs, Element elemDestination){
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
	while(!trouve){
		int score = -1000;
		for(Element e : listeOuverte){
			if(elementScore(elementActuel, e) > score){
				score = elementScore(elementActuel, e);
				prochainElement = e;
			}
		}
		elementActuel = prochainElement;
		listeFermee.add(elementActuel);
		listeOuverte.remove(elementActuel);
		if(elementActuel.equals(this.elemDestination)){
			trouve = true;
		}
			
	}
	this.itineraireOptimal.addAll(listeFermee);
}

public int elementScore(Element a, Element b){
	//Points raportés par le traitement de l'element - cout de déplacement - cout de traitement de la poussiere
	int score = b.getPts() - distanceManhattan(a,b)*Parametre.COUT_ENERGIE - Parametre.COUT_ENERGIE;
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