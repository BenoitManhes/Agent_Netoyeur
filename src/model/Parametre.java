package model;

/*Ici sont stocke tout les differents parametre
 * -> gestion au meme endroit, plus facilement gerable*/
public class Parametre {
	
	//Gestion des points
	public static final int POINT_POUSSIERE = 8;
	public static final int POINT_BIJOU = 5;
	public static final int MALUS_BIJOU = -20;
	public static final int COUT_ENERGIE = 1;
	
	//Proba
	public static double PROBA_POUSSIERE = 0.02;
	public static double PROBA_BIJOU = 0.002;
	
	//Frequence d observation
	public static final int FREQUENCE_MAX = 5;	// nb d objectif atteind avant une nouvelle observation
	public static final int NB_PARCOUR_MIN = 5; // nb de parcour min par frequence pour initialiser le tableau des frequences
	
	//autre...
	public static final int TAILLE_GRILLE = 10;
	public static final int PROFONDEUR_ARBRE_MAX = 8;
	public static int DELAI_AGENT = 200; 	// pas de temps en ms de la simulaion de l agent
	public static final int DELAI_ENV = 10;		// idem environnement
	public static final String TITRE_AGENT = "Agent";
	public static final String TITRE_ENVIRONNEMENT = "Environnement";
	
	
}
