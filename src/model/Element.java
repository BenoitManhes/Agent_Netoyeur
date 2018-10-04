package model;

public abstract class Element {
	
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
	
	public abstract boolean isPoussiere();

 	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}

	public int getPts() {
		return Pts;
	}
	
	//debuggage 
	public String toString(){
		return "("+this.X+","+this.Y+")";
	}
}
