import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javax.smartcardio.CommandAPDU;
// Application client
public class Client {
	private static Socket socket;
	public static void main(String[] args) throws Exception {
		// Adresse et port du serveur
		String serverAddress = "127.0.0.2";
		int port = 5000;
		
		
		try {
			BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
			
			do {
				System.out.println("Please enter a valid IP address");
				serverAddress = d.readLine(); 
			}while(!isValidIPAddress(serverAddress) );
			// TODO :|| !serverAddress.substring(0,3).equals("127")
			
			
			do {
				System.out.println("Please enter a port between 5000 and 5050");
				port = Integer.parseInt(d.readLine());
			}while(port > 5050 || port < 5000);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		// Création d'une nouvelle connexion aves le serveur
		socket = new Socket(serverAddress, port);
		System.out.format("Serveur lancé sur [%s:%d]", serverAddress, port);
		// Céatien d'un canal entrant pour recevoir les messages envoyés, par le serveur
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream()); 
		// Attente de la réception d'un message envoyé par le, server sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		String command = "";
		Scanner input = new Scanner(System.in);
		do {
			System.out.println("Please enter a valid command");

			command = input.nextLine();
			out.writeUTF(command);
			String receivedData = in.readUTF();
			
			do {
				if (!receivedData.equals("DONE")) {
					System.out.print(receivedData);
				}	
				receivedData = in.readUTF();
			} while(!receivedData.equals("DONE"));
			
		} while(!command.equals("exit"));
		// fermeture de La connexion avec le serveur
		socket.close();
	}
		
		
		 // Function to validate the IPs address.
	    public static boolean isValidIPAddress(String ip)
	    {
	 
	        // Regex for digit from 0 to 255.
	        String zeroTo255
	            = "(\\d{1,2}|(0|1)\\"
	              + "d{2}|2[0-4]\\d|25[0-5])";
	 
	        // Regex for a digit from 0 to 255 and
	        // followed by a dot, repeat 4 times.
	        // this is the regex to validate an IP address.
	        String regex
	            = zeroTo255 + "\\."
	              + zeroTo255 + "\\."
	              + zeroTo255 + "\\."
	              + zeroTo255;
	 
	        // Compile the ReGex
	        Pattern p = Pattern.compile(regex);
	 
	        // If the IP address is empty
	        // return false
	        if (ip == null) {
	            return false;
	        }
	 
	        // Pattern class contains matcher() method
	        // to find matching between given IP address
	        // and regular expression.
	        Matcher m = p.matcher(ip);
	 
	        // Return if the IP address
	        // matched the ReGex
	        return m.matches();
	    }
}
