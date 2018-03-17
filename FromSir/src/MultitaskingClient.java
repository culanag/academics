import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * This client can "talk" and "listen" asynchronously
 *
 * The original code using synchronous communication would contain the code in the main() method:
 *
 *     public static void main(String[] args) {
 *         while (true) {
 * 	           // STEP 1. Client "TALKS" to the server, i.e. get input from the keyboard and send it to the server
 *             messageToServer = kb.nextLine();
 *             pw.println(messageToServer);
 *
 *             if (messageToServer.equals("quit") {
 *                 System.exit(0);
 *             }
 *
 *             // STEP 2. Client "LISTENS" to the server, i.e. get input from the server and print it on the console
 *             messageFromServer = br.readLine();
 *             System.out.println(messageFromServer);
 *
 *             if (messageFromServer == null || messageFromServer.equals("quit")) {
 *                 System.exit(0);
 *             }
 *         }
 *     } // end of main() method
 *
 *
 * This code was modified so that "TALKING" and "LISTENING" are done on separate threads of execution
 *
 * The SERVER WRITER class now contains this code in its run() method:
 *
 *     public void run() {
 *         while (true) {
 * 	           // Client "TALKS" to the server
 *             messageToServer = kb.nextLine();
 *             pw.println(messageToServer);
 *
 *             if (messageToServer.equals("quit") {
 *                 System.exit(0);
 *             }
 *         }
 *     } // end of run() method
 *
 *
 * The SERVER READER class now contains this code in it run() method:
 *
 *     public void run() {
 *         while (true) {
 *             // Client "LISTENS" to the server
 *             messageFromServer = br.readLine();
 *             System.out.println(messageFromServer);
 *
 *             if (messageFromServer == null || messageFromServer.equals("quit")) {
 *                 System.exit(0);
 *             }
 *         }
 *     } // end of run() method
 *
 *
 * @author Laurence F. Balmeo
 * @version 1.00 2018/3/13
 */

public class MultitaskingClient {
	Socket socket;
	JOptionPane pane;

    class ServerWriter extends Thread{
    	public void run() {
			try (
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				Scanner kb = new Scanner(System.in);
			) {
				String messageToServer;

				System.out.println("Type \"quit\" to terminate.");

				while (true) {
					messageToServer = kb.nextLine();
					pw.println(messageToServer);

					if (messageToServer.equals("quit")) {
						System.exit(0); // exit code = 0 (normal termination)
					}
				}

			} catch(Exception e) {
				// should handle IOException
			}
    	}
    }

    class ServerReader extends Thread{
    	public void run() {
			try (
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			) {
				String messageFromServer;

				while (true) {
					messageFromServer = br.readLine();
					System.out.println(messageFromServer);

					if (messageFromServer == null || messageFromServer.equals("quit")) {
						System.exit(0); // exit code = 0 (normal termination)
					}
				}

			} catch(Exception e) {
				// should handle IOException
			}
    	}
    }

	public MultitaskingClient(Socket socket) {
		this.socket = socket;

		new ServerReader().start();
		new ServerWriter().start();
	}

    public static void main(String[] args) {
    	String ipaddress = "127.0.0.1";
    	int portNo = 4000;

    	try {
    		Socket socket = new Socket(ipaddress, portNo);

    		new MultitaskingClient(socket);

    	} catch(Exception e) {
    		// should handle the exception
    	}

    }
}

