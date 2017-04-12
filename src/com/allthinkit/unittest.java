package com.allthinkit;

import java.awt.Event;
import java.util.Scanner;

import com.allthinkit.ClientAsync.HasReceiveListener;

public class unittest {

	private static ClientAsync client = null;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String stroption = " ";
		client = new ClientAsync("127.0.0.1", 9000);
		// Add Event Handler invoke when receive the message
		client.SetHasReceiveListener(new HasReceiveListener() {
			@Override
			public void doHandleReceive() {
				// TODO Auto-generated method stub
				System.out.print(new String(client.GetByteReceive()));
			}
		});
		try {
			do {
				printManu();
				stroption = sc.next();
				switch (stroption) {
				case "1":
					System.out.print("UnderConstructor!!");
					break;
				case "2":
					if (client.doSend("Test".getBytes())) {
						System.out.println("Send Data...");
					}
					break;
				case "3":

					if (client.doConnect()) {
						System.out.println("Start Communicate.");
					}
					break;
				case "4":
					if (client.doDisconnect()) {
						System.out.println("Closed Connection.");
					}
					break;
				default:
					System.out.print("Invalid Options!! \n");
					break;
				}
			} while (!stroption.equals("4"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sc.close();
		}
		System.exit(0);
	}
	private static void printManu() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("1. Reserve Menu \n"));
		sb.append(String.format("2. Send Message '%s' \n","Test"));
		sb.append("3. Start Communicate \n");
		sb.append("4. Stop Communicate \n");
		sb.append("Please select options :");
		System.out.print(sb.toString());
	}
}
