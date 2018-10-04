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
	private double[][] tabFrequence = new double[Parametres.FREQUENCE_MAX][2];		// tab[i][j] : i= les differentes frequences X=i+1, j=1 : score total pour X, j=2 : nb total de parcour effectuer avec X
	private int lastAction;
	private int X;
	private int Y;
	private int frequenceObs = Parametres.FREQUENCE_MAX;
	private int nbElementCycle = 0;	// nb element atteint sans avoir effectuer d observation
	private boolean debutCycle = true;
	private int energieDepense = 0;
	private int energieDepenseTotal = 0;
	private int nbrCasesParcourues = 0;
	private int nbrObjetsAspires = 0;
	private int nbrBijouxRamasses = 0;

	
	public Agent() {
		X = (int) (Math.random()*Parametres.TAILLE_GRILLE);
		Y = (int) (Math.random()*Parametres.TAILLE_GRILLE);
	}

	
	/** ============================================== Observation =============================================================================*/
	public void actualiserObjectif() {
		Element e = this.Objectifs.get(0);
		if( this.X==e.getX() && this.Y==e.getY() && mouvementChemin.isEmpty()) {
			this.Objectifs.remove(0);	//objectif atteint, il est supprime des objectif
			int i = indiceElement(this.X, this.Y, e.isPoussiere());
			this.ListElementObs.remove(i);	//objectif atteint, il est supprime des element observe 
		}
	}

	
	public void observerEnvironnement(){
		this.ListElementObs.clear();
		this.ListElementObs.addAll(Environnement.ListEnvironement);
	}

	/** ============================================ Mise ajour Etat ===========================================================================*/
	public void ajoutPerformance() {
		int score = Environnement.getScoreEnvironnement() - energieDepense;	// calcul des points
		tabFrequence[frequenceObs-1][0] += score; 
		tabFrequence[frequenceObs-1][1] += nbElementCycle;
		Environnement.scoresObtenus.add(score);
		//remise de spoints a zero
		energieDepense=0;
		Environnement.setScoreEnvironnement(0);
	}
	
	public void resetNbElementCycle() {
		nbElementCycle=0;
	}
	
	/** =============================================== Decision ===============================================================================*/
	public void choixFrequence() {
		frequenceObs = 1;
		double tauxMax = 0;
		//calcul du X optimal par rapport au score
		for (int i = 0; i < tabFrequence.length; i++) {
			//calcul du nb moyen de points apporte pour chaque element avec une frequence d observation X = i+1
			double taux = 0;
			if( tabFrequence[i][1] != 0 ) { // pas de division par 0
				taux = tabFrequence[i][0] / ( tabFrequence[i][1] );
			}
			if(taux>tauxMax) {
				tauxMax = taux;
				frequenceObs = i+1;
			}
		}
		//si un X n est pas assez echantillone , on le choisit
		for (int i = 0; i < tabFrequence.length; i++) {
			if(tabFrequence[i][1] < Parametres.ECHANTILLON_MIN) {
				frequenceObs = i+1;
			}
		}
	}
	
	/** ================================================ Action ================================================================================*/
	public void goUp(){
		if(this.Y>0){
			this.Y--;
			this.lastAction = HAUT;
			this.incrementeEnergieDepense();
			this.nbrCasesParcourues++;
		}
	}

	public void goDown(){
		if(this.Y<Parametres.TAILLE_GRILLE-1){
			this.Y++;
			this.lastAction = BAS;
			this.incrementeEnergieDepense();
			this.nbrCasesParcourues++;
		}
	}

	public void goRight(){
		if(this.X<Parametres.TAILLE_GRILLE-1){
			this.X++;
			this.lastAction = DROITE;
			this.incrementeEnergieDepense();
			this.nbrCasesParcourues++;
		}
	}

	public void goLeft(){
		if(this.X>0){
			this.X--;
			this.lastAction = GAUCHE;
			this.incrementeEnergieDepense();
			this.nbrCasesParcourues++;
		}
	}

	public void ramasser(){ 
		this.lastAction = RAMASSER;
		this.incrementeEnergieDepense();
		this.nbrBijouxRamasses++;
		this.nbElementCycle++;			//objectif atteint : on incremente le nb d objectif atteint du cycle
	}

	public void aspirer(){
		this.lastAction = ASPIRER;
		this.incrementeEnergieDepense();
		this.nbrObjetsAspires++;
		this.nbElementCycle++;			//objectif atteint : on incremente le nb d objectif atteint du cycle
	}

	public void incrementeEnergieDepense() {
		this.energieDepense += Parametres.COUT_ENERGIE;
		this.energieDepenseTotal += Parametres.COUT_ENERGIE;
	}

	/** ============================================= methode de calcul ========================================================================*/

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

	/**============================================= Accesseurs et mutateurs ===========================================================================*/
	public ArrayList<Element> getListElementObs() {
		return ListElementObs;
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
	
	public int getLastAction() {
		return lastAction;
	}

	public void setLastAction(int l) {
		this.lastAction = l;
	}

	public int getEnergieDepense() {
		return energieDepense;
	}

	public void setEnergieDepense(int e) {
		this.energieDepense = e;
	}

	public ArrayList<Integer> getMouvementChemin() {
		return mouvementChemin;
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
	
	public int getFrequenceObs() {
		return frequenceObs;
	}
	
	public double[][] getTabFrequence(){
		return tabFrequence;
	}

	public int getNbElementCycle() {
		return nbElementCycle;
	}

	public boolean isDebutCycle() {
		return debutCycle;
	}

	public void setDebutCycle(boolean debutCycle) {
		this.debutCycle = debutCycle;
	}

	public int getEnergieDepenseTotal() {
		return energieDepenseTotal;
	}

}