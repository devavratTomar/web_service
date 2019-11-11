package webservice;

import webservice.Webservice;;

public class Main {
	public static void main(String[] args) {
		System.out.println("Web Service Example by Devavrat Tomar");
		Webservice ws = new Webservice(3);
		ws.startService(8080);
	}
}
