package model;

import java.util.ArrayList;

public class Agent {

	private ArrayList<Element> ListElementObs = new ArrayList<Element>();
	private int X;
	private int Y;
	
	public Agent() {
	}

	public ArrayList<Element> getListElementObs() {
		return ListElementObs;
	}

	public void setListElementObs(ArrayList<Element> listElementObs) {
		ListElementObs = listElementObs;
	}
}