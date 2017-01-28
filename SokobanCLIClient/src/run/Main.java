package run;

import client.CLIClient;

public class Main {

	public static void main(String[] args) {
		String ip=args[0];
		int port = Integer.parseInt(args[1]);
		CLIClient client = new CLIClient();
		client.start(ip, port);
	}

}