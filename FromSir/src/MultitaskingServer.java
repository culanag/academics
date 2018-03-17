import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * This class complements the actions of MultitaskingClient.java (see comments in the other program)
 *
 * @author Laurence F. Balmeo
 * @version 1.00 2018/3/13
 */

public class MultitaskingServer {
	Socket socket;

    class ClientWriter extends Thread{
    	public void run() {
			try (
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				Scanner kb = new Scanner(System.in);
			) {
				String messageToClient;

				System.out.println("Type \"quit\" to terminate.");

				while (true) {
					messageToClient = kb.nextLine();
					pw.println(messageToClient);

					if (messageToClient.equals("quit")) {
						System.exit(0); // exit code = 0 (normal termination)
					}
				}

			} catch(Exception e) {
				// should handle IOException
			}
    	}
    }

    class ClientReader extends Thread{
    	public void run() {
			try (
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			) {
				String messageFromClient;

				while (true) {
					messageFromClient = br.readLine();
					System.out.println(messageFromClient);

					if (messageFromClient == null || messageFromClient.equals("quit")) {
						System.exit(0); // exit code = 0 (normal termination)
					}
				}

			} catch(Exception e) {
				// should handle IOException
			}
    	}
    }

	public MultitaskingServer(Socket socket) {
		this.socket = socket;

		new ClientReader().start();
		new ClientWriter().start();
	}
    public static void main(String[] args) {
    	int portNo = 4000;

    	try {
    		ServerSocket server = new ServerSocket(portNo);
    		Socket socket = server.accept();

    		new MultitaskingServer(socket);

    	} catch(Exception e) {
    		// should handle the exception
    	}

    }
}

