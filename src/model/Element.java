package model;

public class Element {
	
	protected int X;	//coordonne en abscisse 
	protected int Y;	//coordonne en ordonne
	
	protected int Pts;	// pts recompense 

	public Element() {
	}
	
	public Element(int x, int y, int p) {
		X=x;
		Y=y;
		Pts=p;
	}

 	public int getX() {
		return X;
	}

	public void setX(int x) {
		this.X = x;
	}
	
	public int getY() {
		return Y;
	}

	public void setY(int y) {
		this.Y = y;
	}

	public int getPts() {
		return Pts;
	}
}
