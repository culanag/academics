import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author Culanag, Ian
 *
 */
public class Teacher {
	Socket socket;
	
	private String[] questions = {	"Is the sun black?",
									"Do cats meow?",
									"Do dogs bark?",
									"9 11 was an inside job.",
									"The Holocaust didn't happen.",
									"The earth is flat.",
									"Pineapple goes on pizza.",
									"'Apir' means 'up here'.",
									"'Sirit' is derived from the english phrase 'share it'",
									"Pres. Marcos is still alive."};
	private String[] answers = { "False", "True", "True", "False", "False", "False", "False", "True", "True", "False" };
	
	class QuestionWriter extends Thread {
		public void run() {
			try (
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				Scanner kbd = new Scanner(System.in);
			) {
				String question;
				
				while (true) {
					for (int index = 0; index < questions.length; index++) {
						question = questions[index];
						pw.println((index+1) + ". " + question);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class AnswerChecker extends Thread {
		public void run() {
			try (
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			) {
				String studentAnswer;
				int correctAnswers = 0;
				
				while (true) {
					for (int index = 0; index <= questions.length; index++) {
						studentAnswer = br.readLine();
						System.out.print(studentAnswer);
						if (studentAnswer.equalsIgnoreCase(answers[index])) {
							correctAnswers++;
						} else if (!studentAnswer.equalsIgnoreCase("True") || !studentAnswer.equalsIgnoreCase("False")) {
							pw.println("Answers must only be \"True\" or \"False\"");
						} else if (studentAnswer.equals("finish")) {
							System.exit(0);
						}
					}
					pw.println("You answered " + correctAnswers + " items correctly");
					System.out.println("The student correctly answered " + correctAnswers + " items");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Teacher(Socket socket) {
		this.socket = socket;
		
		new QuestionWriter().start();
		new AnswerChecker().start();
	}
	
	public static void main(String[] args) {
		int port = 4000;
		
		try {
			ServerSocket server = new ServerSocket(port);
			Socket socket = server.accept();
			
			new Teacher(socket);
		} catch(Exception e) {
			//TODO code
		}
	}
}
