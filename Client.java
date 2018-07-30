package client;

/*
 * Connects to server through port 3940
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

public class Client extends Thread {

	public static Socket client;
	public static BufferedReader in;
	public static PrintWriter out;
	public GUI gui;
	private String username;

	public Client(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void run() {

		try {
			// Server connection
			System.out.println("Connecting to server...");
			client = new Socket("18.191.226.146", 3940);
			System.out.println("Connected");
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);

			// Gets current IP address
			String ip;
			try {
				URL url = new URL("http://bot.whatismyipaddress.com");
				BufferedReader sc = new BufferedReader(new InputStreamReader(url.openStream()));
				ip = sc.readLine().trim();
			} catch (Exception e) {
				ip = "Cannot Execute Properly";
			}

			// Reads and displays data
			String data;
			while ((data = in.readLine()) != null) {
				if (data.equals("GETUSERNAME")) {
					gui.promptUsername();
					username = gui.getUsername();
					out.println(username);
				} else if (data.equals("GETIPADDRESS")) {
					out.println(ip);
				} else if (data.equals("NEWUSER")) {
					gui.allUsers.add(in.readLine());
					gui.updateUsers();
				} else if (data.equals("DELUSER")) {
					gui.allUsers.remove(in.readLine());
					gui.updateUsers();
				} else {
					gui.displayMessage(data);
				}

			}

			in.close();
			out.close();
			client.close();
		} catch (

		Exception e) {
		}

	}

	public void send(String data) {
		if (out != null)
			out.println(data);
	}

}
