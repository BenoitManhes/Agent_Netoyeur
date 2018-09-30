package model;

import java.util.ArrayList;

public class Agent {
	
	//Mouvement de l'agent
	public static final int NE_RIEN_FAIRE = 0;
	public static final int HAUT = 1;
	public static final int BAS = 2;
	public static final int DROITE = 3;
	public static final int GAUCHE = 4;
	public static final int ASPIRER = 5;
	public static final int RAMASSER = 6;
	
	private ArrayList<Element> ListElementObs = new ArrayList<Element>();
	private ArrayList<Element> Objectifs = new ArrayList<Element>();
	private ArrayList<Integer> mouvementChemin = new ArrayList<Integer>();
	private int lastAction;
	private int X;
	private int Y;
	private int energieDepense;
	
	public Agent() {
		X = (int) (Math.random()*Parametre.TAILLE_GRILLE);
		Y = (int) (Math.random()*Parametre.TAILLE_GRILLE);
	}

	public ArrayList<Element> getListElementObs() {
		return ListElementObs;
	}

	public void setListElementObs(ArrayList<Element> listElementObs) {
		ListElementObs = listElementObs;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public ArrayList<Element> getObjectifs() {
		return Objectifs;
	}

	public void setObjectifs(ArrayList<Element> objectifs) {
		Objectifs = objectifs;
	}
	
	public void goUp(){
		if(this.Y>0){
			this.Y--;
			this.lastAction = HAUT;
			this.energieDepense++;
			System.out.println("Agent : Je me suis deplace vers le haut");
		}
		
		
	}
	
	public void goDown(){
		if(this.Y<Parametre.TAILLE_GRILLE-1){
			this.Y++;
			this.lastAction = BAS;
			this.energieDepense++;
			System.out.println("Agent : Je me suis deplace vers le bas");
		}
	}
	
	public void goRight(){
		if(this.X<Parametre.TAILLE_GRILLE-1){
			this.X++;
			this.lastAction = DROITE;
			this.energieDepense++;
			System.out.println("Agent : Je me suis deplace vers la droite");
		}
	}
	
	public void goLeft(){
		if(this.X>0){
			this.X--;
			this.lastAction = GAUCHE;
			this.energieDepense++;
			System.out.println("Agent : Je me suis deplace vers la gauche");
		}
	}
	
	public void ramasser(){ 
		this.lastAction = RAMASSER;
		this.energieDepense++;
		System.out.println("Agent : J'ai ramasse le contenu de la case");
	}
	
	public void aspirer(){
		this.lastAction = ASPIRER;
		this.energieDepense++;
		System.out.println("Agent : J'ai aspire le contenu de la case");
	}
	
	public void observerEnvironnement(){
		this.ListElementObs.addAll(Environement.ListEnvironement);
		System.out.println("Agent : J'observe l'environnement à l'aide de mes capteurs");
	}
	
	public void cheminVers(int x, int y){
		this.mouvementChemin.clear();
		int diffX = this.X - x;
		int diffY = this.Y - y;
		while(diffX != 0){
			if(diffX < 0)
			{
				this.mouvementChemin.add(DROITE);
				diffX++;
			}
			else{
				this.mouvementChemin.add(GAUCHE);
				diffX--;
			}
		}
		while(diffY != 0){
			if(diffY < 0){
				this.mouvementChemin.add(BAS);
				diffY++;
			}
			else{
				this.mouvementChemin.add(HAUT);
				diffY--;
			}
		}
		
		
	}

	public int getLastAction() {
		return lastAction;
	}

	public void setLastAction(int lastAction) {
		this.lastAction = lastAction;
	}

	public int getEnergieDepense() {
		return energieDepense;
	}

	public void setEnergieDepense(int energieDepense) {
		this.energieDepense = energieDepense;
	}

	public ArrayList<Integer> getMouvementChemin() {
		return mouvementChemin;
	}

	public void setMouvementChemin(ArrayList<Integer> mouvementChemin) {
		this.mouvementChemin = mouvementChemin;
	}
	
	
}