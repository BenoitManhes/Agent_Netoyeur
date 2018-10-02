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
		profondeur = Math.min(Parametre.PROFONDEUR_ARBRE_MAX,Environement.agent.getListElementObs().size());
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
		if(!itineraireOptimale.isEmpty())
			itineraireOptimale.remove(0); 		 // suppression position initiale -> on obtient une liste d objectif
	}
	
	public void parcourChemin(ArrayList<Element> itineraire, int score, int deep, ArrayList<Element> EDispo) {
		if(deep < profondeur && !EDispo.isEmpty()) {
			for (int i = 0; i < EDispo.size(); i++) {
				Element e = EDispo.get(i);
<<<<<<< HEAD
<<<<<<< Frequence
				int s= score - distanceManhattan(e, itineraire.get(itineraire.size()-1) );
				s+= Parametre.COUT_ENERGIE + e.getPts();
				
				ArrayList<Element> newIteneraire = new ArrayList<Element>();
				cloneList(newIteneraire, itineraire);
				newIteneraire.add(e);
				
				ArrayList<Element> newEDispo = new ArrayList<Element>();
				cloneList(newEDispo, EDispo);
				newEDispo.remove(i);
=======
				ArrayList<Element> newEDispo = new ArrayList<Element>();
				cloneList(newEDispo, EDispo);
				newEDispo.remove(i);
=======
				ArrayList<Element> newEDispo = new ArrayList<Element>();
				cloneList(newEDispo, EDispo);
				newEDispo.remove(i);
>>>>>>> Score_Arbre
				int s = calculerScore(e, newEDispo, score, itineraire);
				ArrayList<Element> newIteneraire = new ArrayList<Element>();
				cloneList(newIteneraire, itineraire);
				newIteneraire.add(e);
<<<<<<< HEAD
>>>>>>> Prise en compte du cas d'un bijou sur de la poussiere
=======
>>>>>>> Score_Arbre
				
				parcourChemin(newIteneraire, s, deep+1, newEDispo);
			}
			testCombinaison(itineraire, score); 	// Test agent choisie de rien faire		
		}
		else {
			testCombinaison(itineraire, score);
		}
	}
	
	private int calculerScore(Element e, ArrayList<Element> newEDispo, int score, ArrayList<Element> itineraire) {
		int s = score - distanceManhattan(e, itineraire.get(itineraire.size()-1)) + e.getPts() + Parametre.COUT_ENERGIE;
		if(e.isPoussiere() && verifierPresenceBijou(e, newEDispo)) {
			s += Parametre.MALUS_BIJOU;
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
		A.addAll(B);
	}
	
	public int getTopScore() {
		return topScore;
	}
	
	public ArrayList<Element> getItineraireOptimal(){
		return itineraireOptimale;
	}

}
