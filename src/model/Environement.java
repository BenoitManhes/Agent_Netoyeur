package model;

import java.util.ArrayList;

public class Environement {
	
	public static ArrayList<Element> ListEnvironement = new ArrayList<Element>();
	public static Agent agent = new Agent();
	public static int performance;
	public static int scoreEnvironnement;
	
	// verifie que la case x,y ne contient pas deja un element similaire
	public static boolean caseDisponible(int x, int y, boolean poussiere) {
		boolean dispo = true;
		for (int i = 0; i < ListEnvironement.size(); i++) {
			int a = ListEnvironement.get(i).getX();
			int b = ListEnvironement.get(i).getY();
			if(x==a && y==b && ListEnvironement.get(i).isPoussiere()==poussiere) {
				dispo = false;
			}
		}
		return dispo;
	}
	
	public static int indiceElement(int x, int y, boolean poussiere) {
		int id = -1;
		for (int i = 0; i < ListEnvironement.size(); i++) {
			int a = ListEnvironement.get(i).getX();
			int b = ListEnvironement.get(i).getY();
			if(x==a && y==b && ListEnvironement.get(i).isPoussiere()==poussiere) {
				id = i;
			}
		}
		return id;
	}
}
