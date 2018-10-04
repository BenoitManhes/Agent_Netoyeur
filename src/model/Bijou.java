package model;

public class Bijou extends model.Element{
	
	private final int POINT = Parametres.POINT_BIJOU;	
	
	public Bijou(int x, int y) {
		X = x;
		Y = y;
		Pts = POINT;
	}
	
	public boolean isPoussiere() {
		return false;
	}

}
