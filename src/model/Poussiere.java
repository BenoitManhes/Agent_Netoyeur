package model;

public class Poussiere extends model.Element{
	
	private final int POINT = Parametre.POINT_POUSSIERE;

	public Poussiere(int x,int y) {
		X=x;
		Y=y;
		Pts=POINT;
	}
	
	public boolean isPoussiere() {
		return true;
	}
}
