package controller;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 exEnvironement exE = new exEnvironement();
		 exAgent exA = new exAgent();
		 
		 new Thread(exE).start();
		 new Thread(exA).start();
	}

}
