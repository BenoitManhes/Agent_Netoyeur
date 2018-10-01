package model;

import java.util.ArrayList;

public class Astar {
	private ArrayList<Element> listeElemObs;
	private ArrayList<Noeud> graphe;
	private ArrayList<Element> itineraireOptimal;
	private int xDepart;
	private int yDepart;
	private Element elemDestination;
	

	public Astar(int xDepart, int yDepart, ArrayList<Element> listeElemObs, Element elemDestination){
		this.xDepart = xDepart;
		this.yDepart = yDepart;
		this.listeElemObs = listeElemObs;
		this.elemDestination = elemDestination;
		this.graphe = new ArrayList<Noeud>();
		this.itineraireOptimal = new ArrayList<Element>();
	}

	public void creationGraph(){
		listeElemObs.remove(elemDestination);
		Noeud noeudArrivee = new Noeud(elemDestination, 0);
		Element elemDepart = new Poussiere(this.xDepart, this.yDepart);
		Noeud noeudDepart = new Noeud(elemDepart, coutInterNoeud(elemDepart, noeudArrivee));
		this.graphe.add(noeudDepart);
		this.graphe.add(noeudArrivee);
		for(Element elem : listeElemObs){
			this.graphe.add(new Noeud(elem, coutInterNoeud(elem,noeudArrivee)));
		}
		for(int i=0; i<this.graphe.size(); i++){
			for(int j=0; j<this.graphe.size(); j++){
				if(i != j){
					graphe.get(i).getListeArrete().add(new Arrete(this.graphe.get(j), coutInterNoeud(this.graphe.get(i), this.graphe.get(j))));
				}
			}
			
		}
		
	}

	public void explorationGraph(){

	}
	
	public static int coutInterNoeud(Noeud noeudA, Noeud noeudB){
		int cout = distanceManhattan(noeudA.getE(), noeudB.getE()); 
		//a modifier pour prendre en compte le gain des elements
		return cout;
	}
	
	//surcharge pour la position initiale
	public static int coutInterNoeud(Element elemDepart, Noeud noeudB){
		int cout = distanceManhattan(elemDepart, noeudB.getE()); 
		//a modifier pour prendre en compte le gain des elements
		return cout;
	}
	
	public static int distanceManhattan(Element a, Element b) {
		int x = Math.abs(a.getX() - b.getX());
		int y = Math.abs(a.getY() - b.getY());
		return x + y ;
	}

}