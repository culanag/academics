import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author Culanag, Ian
 *
 */
public class Student {
	Socket socket;
	
	class AnswerWriter extends Thread {
		public void run() {
			try (
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				Scanner kbd = new Scanner(System.in);
			) {
				String answers;
				System.out.println("True or False. Type \"True\" if the answer is true and \"False\" otherwise.");
				while (true) {
					answers = kbd.nextLine();
					pw.println(answers);
					
					if (answers.equals("finish")) {
						System.exit(0);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class QuestionReader extends Thread {
		public void run() {
			try (
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			) {
				String question;
				int c = 0;
				while (c != 10) {
					question = br.readLine();
					System.out.println(question);
					c++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Student(Socket socket) {
		this.socket = socket;
		
		new AnswerWriter().start();
		new QuestionReader().start();
	}
	
	public static void main(String[] args) {
		String ipaddress = "192.168.5.151";
		int port = 4000;
		
		try {
			Socket socket = new Socket(ipaddress, port);
			
			new Student(socket);
		} catch (Exception e) {
			//TODO
		}
	}
}
