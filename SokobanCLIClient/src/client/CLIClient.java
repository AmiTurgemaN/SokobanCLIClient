package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CLIClient {

	private boolean stop;
	private void readInputAndSend(BufferedReader in, PrintWriter out,String exitStr)
	{
		try{
			String line;
			while(!stop)
			{
				line = in.readLine();
				out.println(line);
				out.flush();
				if(line.equals(exitStr))
					this.stop=true;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private Thread aSyncReadInputAndSend(BufferedReader in, PrintWriter out,String exitStr)
	{
		Thread t=new Thread(new Runnable() {
			@Override
			public void run() {
				readInputAndSend(in, out, exitStr);
			}
		});
		t.start();
		return t;
	}

	public void start(String ip,int port)
	{
		try {
			this.stop=false;
			Socket theServer = new Socket(ip,port);

			System.out.println("Connected to server");

			BufferedReader userInput =new BufferedReader(new InputStreamReader(System.in));
			BufferedReader serverInput =new BufferedReader(new InputStreamReader(theServer.getInputStream()));
			PrintWriter outToServer = new PrintWriter(theServer.getOutputStream());
			PrintWriter outToClient = new PrintWriter(System.out);
			Thread t1 = aSyncReadInputAndSend(userInput, outToServer, "exit");
			Thread t2 = aSyncReadInputAndSend(serverInput, outToClient, "bye");

			t1.join();t2.join();

			userInput.close();
			serverInput.close();
			outToServer.close();
			outToClient.close();
			theServer.close();	
		}
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}