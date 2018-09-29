package model;

public class Bijou extends model.Element{
	
	private final int POINT = Parametre.POINT_BIJOU;	// constante plus court et visible dans la classe

	public Bijou(int x, int y) {
		X = x;
		Y = y;
		Pts = POINT;
	}
	
	public boolean isPoussiere() {
		return false;
	}

}
