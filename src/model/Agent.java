package model;

import java.util.ArrayList;

public class Agent {
	
	private ArrayList<Element> ListElementObs = new ArrayList<Element>();
	private ArrayList<Element> Objectifs = new ArrayList<Element>();
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
			this.lastAction = Parametre.HAUT;
			this.energieDepense++;
			System.out.println("Agent : Je me suis deplace vers le haut");
		}
		
		
	}
	
	public void goDown(){
		if(this.Y<Parametre.TAILLE_GRILLE-1){
			this.Y++;
			this.lastAction = Parametre.BAS;
			this.energieDepense++;
			System.out.println("Agent : Je me suis deplace vers le bas");
		}
	}
	
	public void goRight(){
		if(this.X<Parametre.TAILLE_GRILLE-1){
			this.X++;
			this.lastAction = Parametre.DROITE;
			this.energieDepense++;
			System.out.println("Agent : Je me suis deplace vers la droite");
		}
	}
	
	public void goLeft(){
		if(this.X>0){
			this.X--;
			this.lastAction = Parametre.GAUCHE;
			this.energieDepense++;
			System.out.println("Agent : Je me suis deplace vers la gauche");
		}
	}
	
	public void ramasser(){ 
		this.lastAction = Parametre.RAMASSER;
		this.energieDepense++;
		System.out.println("Agent : J'ai ramasse le contenu de la case");
	}
	
	public void aspirer(){
		this.lastAction = Parametre.ASPIRER;
		this.energieDepense++;
		System.out.println("Agent : J'ai aspire le contenu de la case");
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
	
	
}