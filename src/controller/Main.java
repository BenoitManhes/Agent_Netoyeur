package controller;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 ExEnvironnement exE = new ExEnvironnement();
		 ExAgent exA = new ExAgent();
		 
		 new Thread(exE).start();
		 new Thread(exA).start();
	}

}
