package model;

public class Arrete {
	private Noeud noeudCible;
	private int cout;

	public Arrete(Noeud noeudCible, int cout){
		this.setNoeudCible(noeudCible);
		this.setCout(cout);
	}

	public Noeud getNoeudCible() {
		return noeudCible;
	}

	public void setNoeudCible(Noeud noeudCible) {
		this.noeudCible = noeudCible;
	}

	public int getCout() {
		return cout;
	}

	public void setCout(int cout) {
		this.cout = cout;
	}
	
	public String toString(){
		return "("+noeudCible.getE()+","+cout+")";
	}
}