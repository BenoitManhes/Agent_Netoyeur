package model;

/*Ici sont stocke tout les differents parametre
 * -> gestion au meme endroit, plus facilement gerable*/
public class Parametre {
	
	//Gestion des points
	public static final int POINT_POUSSIERE = 10;
	public static final int POINT_BIJOU = 5;
	public static final int MALUS_BIJOU = -15;
	public static final int COUT_ENERGIE = 1;
	
	//Proba
	public static final double PROBA_POUSSIERE = 0.1;
	public static final double PROBA_BIJOU = 0.01;
	
	//autre...
	public static final int TAILLE_GRILLE = 10;
	public static final int DELAI_AGENT = 1000; // pas de temps en ms de la simulaion
	public static final int DELAI_ENV = 10;
	
	//Mouvement de l'agent
	public static final int NE_RIEN_FAIRE = 0;
	public static final int HAUT = 1;
	public static final int BAS = 2;
	public static final int DROITE = 3;
	public static final int GAUCHE = 4;
	public static final int ASPIRER = 5;
	public static final int RAMASSER = 6;
	
}
