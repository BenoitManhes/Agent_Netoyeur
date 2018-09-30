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
	private int nbrCasesParcourues;
	private int nbrObjetsAspires;
	private int nbrBijouxRamasses;

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
			this.nbrCasesParcourues++;
			System.out.println("Agent : Je me suis deplace vers le bas");
		}
	}

	public void goRight(){
		if(this.X<Parametre.TAILLE_GRILLE-1){
			this.X++;
			this.lastAction = DROITE;
			this.energieDepense++;
			this.nbrCasesParcourues++;
			System.out.println("Agent : Je me suis deplace vers la droite");
		}
	}

	public void goLeft(){
		if(this.X>0){
			this.X--;
			this.lastAction = GAUCHE;
			this.energieDepense++;
			this.nbrCasesParcourues++;
			System.out.println("Agent : Je me suis deplace vers la gauche");
		}
	}

	public void ramasser(){ 
		this.lastAction = RAMASSER;
		this.energieDepense++;
		this.nbrBijouxRamasses++;
		System.out.println("Agent : J'ai ramasse le contenu de la case");
	}

	public void aspirer(){
		this.lastAction = ASPIRER;
		this.energieDepense++;
		this.nbrObjetsAspires++;
		System.out.println("Agent : J'ai aspire le contenu de la case");
	}

	public void observerEnvironnement(){
		this.ListElementObs.addAll(Environement.ListEnvironement);
		System.out.println("Agent : J'observe l'environnement a l'aide de mes capteurs");
	}

	public void cheminVers(int x, int y){
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

	public void actualiserObjectif() {
		Element e = this.Objectifs.get(0);
		if( this.X==e.getX() && this.Y==e.getY()) {
			this.Objectifs.remove(0);	//objectif atteint, il est supprime des objectif

			int i = indiceElement(this.X, this.Y, e.isPoussiere());
			ListElementObs.remove(i);	//objectif atteint, il est supprime des element observe 
		}
	}


	public int indiceElement(int x, int y, boolean poussiere) {
		int id = -1;
		for (int i = 0; i < ListElementObs.size(); i++) {
			int a = ListElementObs.get(i).getX();
			int b = ListElementObs.get(i).getY();
			if(x==a && y==b && ListElementObs.get(i).isPoussiere()==poussiere) {
				id = i;
			}
		}
		return id;
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

	public int getNbrCasesParcourues() {
		return nbrCasesParcourues;
	}

	public void setNbrCasesParcourues(int nbrCasesParcourues) {
		this.nbrCasesParcourues = nbrCasesParcourues;
	}

	public int getNbrObjetsAspirees() {
		return nbrObjetsAspires;
	}

	public void setNbrObjetsAspirees(int nbrObjetsAspirees) {
		this.nbrObjetsAspires = nbrObjetsAspirees;
	}

	public int getNbrBijouxRamasses() {
		return nbrBijouxRamasses;
	}

	public void setNbrBijouxRamasses(int nbrBijouxRamasses) {
		this.nbrBijouxRamasses = nbrBijouxRamasses;
	}


}