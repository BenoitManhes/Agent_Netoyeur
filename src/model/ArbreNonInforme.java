package model;

import java.util.ArrayList;

public class ArbreNonInforme {
	
	private ArrayList<Element> itineraireOptimale;
	private int topScore;
	private int profondeur;
	private ArrayList<Element> groupElement;
	private int X;
	private int Y;
	
	public ArbreNonInforme(ArrayList<Element> groupE , int x, int y ) {
		itineraireOptimale = new ArrayList<Element>();
		topScore = 0;
		groupElement = new ArrayList<Element>();
		cloneList(groupElement, groupE);
		profondeur = Math.min(Parametres.PROFONDEUR_ARBRE_MAX,Environnement.agent.getListElementObs().size());
		X = x;
		Y = y;
		this.depthLimitedSearch();
	}
	
	public void depthLimitedSearch(){
		
		ArrayList<Element> itineraire = new ArrayList<Element>();
		itineraire.add(new Poussiere(X, Y)); 	// ajout de la positoin initiale
		int score = 0;
		int deep = 0;	
		recursiveDLS(itineraire, score, deep, groupElement);
		if(!itineraireOptimale.isEmpty())
			itineraireOptimale.remove(0); 		 // suppression position initiale -> on obtient une liste d objectif
	}
	
	public void recursiveDLS(ArrayList<Element> itineraire, int score, int deep, ArrayList<Element> eDispo) {
		if(deep < profondeur && !eDispo.isEmpty()) {
			for (int i = 0; i < eDispo.size(); i++) {
				Element e = eDispo.get(i);
				
				ArrayList<Element> newEDispo = new ArrayList<Element>();
				cloneList(newEDispo, eDispo);
				newEDispo.remove(i);
				
				ArrayList<Element> newIteneraire = new ArrayList<Element>();
				cloneList(newIteneraire, itineraire);
				newIteneraire.add(e);
												
				int s = calculerScore(e, newEDispo, score, itineraire);
								
				recursiveDLS(newIteneraire, s, deep+1, newEDispo);
			}
			testCombinaison(itineraire, score); 	// Test agent choisie de rien faire		
		}
		else {
			testCombinaison(itineraire, score);
		}
	}
	
	private int calculerScore(Element e, ArrayList<Element> newEDispo, int score, ArrayList<Element> itineraire) {
		int s = score - distanceManhattan(e, itineraire.get(itineraire.size()-1)) + e.getPts() + Parametres.COUT_ENERGIE;
		if(e.isPoussiere() && verifierPresenceBijou(e, newEDispo)) {
			s += Parametres.MALUS_BIJOU;
		}
		return s;
		
	}
	
	private boolean verifierPresenceBijou(Element e, ArrayList<Element> newEDispo) {
		for(int i = 0 ; i<newEDispo.size() ; i++) {
			if(newEDispo.get(i).getX()==e.getX() && newEDispo.get(i).getY()==e.getY()) {
				if(newEDispo.get(i).isPoussiere()==false) {
					return true;
				}
			}
		}
		return false;
	}

	public void testCombinaison(ArrayList<Element> itineraire,int score) {
		if(score>topScore) {
			cloneList(itineraireOptimale, itineraire);
			topScore=score;
		}
	}
	
	public static int distanceManhattan(Element a, Element b) {
		int x = Math.abs(a.getX() - b.getX());
		int y = Math.abs(a.getY() - b.getY());
		return x + y ;
	}
	
	public static void cloneList (ArrayList<Element> A, ArrayList<Element> B) {
		A.clear();
		A.addAll(B);
	}
	
	public int getTopScore() {
		return topScore;
	}
	
	public ArrayList<Element> getItineraireOptimal(){
		return itineraireOptimale;
	}

}
