package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Astar {
	private ArrayList<Element> listeElemObs;
	private ArrayList<Noeud> graphe;
	private ArrayList<Element> itineraireOptimal;
	private int xDepart;
	private int yDepart;
	private Element elemDestination;
	private Noeud noeudDestination;
	

	public Astar(int xDepart, int yDepart, ArrayList<Element> listeElemObs, Element elemDestination){
		this.xDepart = xDepart;
		this.yDepart = yDepart;
		this.listeElemObs = listeElemObs;
		this.elemDestination = elemDestination;
		this.graphe = new ArrayList<Noeud>();
		this.itineraireOptimal = new ArrayList<Element>();
	}

	public void creationGraph(){
		
		Noeud noeudArrivee = new Noeud(elemDestination, 0);
		this.noeudDestination = noeudArrivee;
		Element elemDepart = new Poussiere(this.xDepart, this.yDepart);
		Noeud noeudDepart = new Noeud(elemDepart, coutInterNoeud(elemDepart, noeudArrivee));
		this.graphe.add(noeudDepart);
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
		//System.out.println("graphe créé :"+graphe.toString());
		
	}
	public void explorationGraph(){
		ArrayList<Noeud> listeFermee = new ArrayList<Noeud>();
		PriorityQueue<Noeud> listeOuverte = new PriorityQueue<Noeud>(
				new Comparator<Noeud>(){
					public int compare(Noeud a, Noeud b){
						return ((a.getF()>b.getF())?1:-1);
					}
				});
		
		//ajout du premier noeud
		this.graphe.get(0).setG(0);
		listeOuverte.add(this.graphe.get(0));
		boolean trouve = false;
		while((!listeOuverte.isEmpty())&&(!trouve)){
			Noeud noeudTeste = listeOuverte.poll();
			listeFermee.add(noeudTeste);
	
			//System.out.println("liste ouverte"+listeOuverte.toString());
			//System.out.println("liste fermee"+listeFermee.toString());
			
			if(noeudTeste.equals(this.noeudDestination)){
				trouve = true;
			}
			
			for(Arrete a : noeudTeste.getListeArrete()){
				Noeud noeudEnfant = a.getNoeudCible();
				int gEnfant = noeudTeste.getG() + a.getCout();
				int fEnfant = gEnfant + noeudEnfant.getH();
				
				if(listeFermee.contains(noeudEnfant) && fEnfant >= noeudEnfant.getF()){
					continue;
				}
				
				else if(!listeOuverte.contains(noeudEnfant) || fEnfant < noeudEnfant.getF()){
				//	System.out.println(noeudTeste.toString() + noeudEnfant.toString());
					noeudEnfant.setParent(noeudTeste);
					noeudEnfant.setG(gEnfant);
					noeudEnfant.setF(fEnfant);
					
					
					if(listeOuverte.contains(noeudEnfant)){
						listeOuverte.remove(noeudEnfant);
					}
					
					listeOuverte.add(noeudEnfant);
				}
			}
		}
		for(Noeud noeud = this.noeudDestination; noeud != null; noeud = noeud.getParent()){
			this.itineraireOptimal.add(0,noeud.getE());
		}
		System.out.println("iti opti"+this.itineraireOptimal);
		this.itineraireOptimal.remove(0);
		
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
	
	public ArrayList<Element> getItineraireOptimal(){
		return this.itineraireOptimal;
	}

}