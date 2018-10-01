package model;

import java.util.ArrayList;

public class Noeud {
	private Element e;
	private Noeud parent;
	//ici cout est une fonction du cout en energie dépensée par le robot
	//et des point gagné par le traitement de l'element
	private int f; //g+h
	private int g; //cout pour atteindre ce noeud jusqu'à present
	private int h; //heuristique : cout estimé du noeud jusqu'au but
	private ArrayList<Arrete> listeArrete;
	
	public Noeud(Element e, int h){
		this.setE(e);
		this.setH(h);
		this.setListeArrete(new ArrayList<Arrete>());
	}

	public Element getE() {
		return e;
	}

	public void setE(Element e) {
		this.e = e;
	}

	public Noeud getParent() {
		return parent;
	}

	public void setParent(Noeud parent) {
		this.parent = parent;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public ArrayList<Arrete> getListeArrete() {
		return listeArrete;
	}

	public void setListeArrete(ArrayList<Arrete> listeArrete) {
		this.listeArrete = listeArrete;
	}
}
