package model;

import java.util.ArrayList;

public class ArbreNonInforme {
	
	private ArrayList<Element> itineraireOptimale;
	private int topScore;
	private int profondeur;
	private ArrayList<Element> groupElement;
	private int X;
	private int Y;
	
	public ArbreNonInforme(ArrayList<Element> groupE, int p , int x, int y ) {
		itineraireOptimale = new ArrayList<Element>();
		topScore = 0;
		groupElement = new ArrayList<Element>();
		cloneList(groupElement, groupE);
		profondeur = p;
		X = x;
		Y = y;
		this.cheminNonInforme();
	}
	
	public void cheminNonInforme(){
		
		ArrayList<Element> itineraire = new ArrayList<Element>();
		itineraire.add(new Poussiere(X, Y)); 	// ajout de la positoin initiale
		int score = 0;
		int deep = 0;
		
		parcourChemin(itineraire, score, deep, groupElement);
		itineraireOptimale.remove(0); 		 // suppression position initiale -> on obtient une liste d objectif
	}
	
	public void parcourChemin(ArrayList<Element> itineraire, int score, int deep, ArrayList<Element> EDispo) {
		if(deep < profondeur && !EDispo.isEmpty()) {
			for (int i = 0; i < EDispo.size(); i++) {
				//System.out.println(itineraire.size());
				Element e = EDispo.get(i);
				int s= score - distanceManhattan(e, itineraire.get(itineraire.size()-1) );
				s+= Parametre.COUT_ENERGIE + e.getPts();
				ArrayList<Element> newIteneraire = new ArrayList<Element>();
				cloneList(newIteneraire, itineraire);
				newIteneraire.add(e);
				ArrayList<Element> newEDispo = new ArrayList<Element>();
				cloneList(newEDispo, EDispo);
				newEDispo.remove(i);
				parcourChemin(newIteneraire, s, deep+1, newEDispo);
			}
			testCombinaison(itineraire, score); 	// Test agent choisie de rien faire		
		}
		else {
			testCombinaison(itineraire, score);
		}
	}
	
	public void testCombinaison(ArrayList<Element> itineraire,int score) {
		if(score>topScore) {
			cloneList(itineraireOptimale, itineraire);
			topScore=score;
			//System.out.println("Max score = "+score+" nb = "+itineraireOptimale.size());
		}
	}
	
	public static int distanceManhattan(Element a, Element b) {
		int x = Math.abs(a.getX() - b.getX());
		int y = Math.abs(a.getY() - b.getY());
		return x + y ;
	}
	
	public static void cloneList (ArrayList<Element> A, ArrayList<Element> B) {
		A.clear();
		for (int i = 0; i < B.size(); i++) {
			A.add(B.get(i));
		}
	}
	
	public int getTopScore() {
		return topScore;
	}
	
	public ArrayList<Element> getItineraireOptimal(){
		return itineraireOptimale;
	}

}
