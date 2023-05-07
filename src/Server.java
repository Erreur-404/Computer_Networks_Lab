import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.regex.*;


public class Server {
	private static ServerSocket Listener; // Application Serveur
	public static void main(String[] args) throws Exception {
		
		// Compteur incrémenté à chaque connexion d'un client au serveur
		int clientNumber = 0;
		String serverAddress = "127.0.0.5";
		int serverPort = 5020;
		// Adresse et port du serveur
		try {
			BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
			
			do {
				System.out.println("Please enter a valid IP address");
				serverAddress = d.readLine(); 
			}while(!isValidIPAddress(serverAddress) );
			//TODO : || !serverAddress.substring(0,3).equals("127")
			
			
			do {
				System.out.println("Please enter a port between 5000 and 5050");
				serverPort = Integer.parseInt(d.readLine());
			}while(serverPort > 5050 || serverPort < 5000);
				
			
			
			// Création de la connexion pour communiquer avec les clients
		 
			Listener = new ServerSocket();
			Listener.setReuseAddress(true);
			
			InetAddress serverIP = InetAddress.getByName(serverAddress);
			// Association de l'adresse et du port à la connexion
			Listener.bind(new InetSocketAddress(serverIP, serverPort));
			System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

		
		try {
		// À chaque fois qu'un nouveau client se connecte, on exécute la fonction
		// run() de l'objet ClientHandler
			while (true) {
			// Important : la fonction accept() est bloquante: attend qu'un prochain client se connecte
			// Une nouvetle connection : on incémente le compteur clientNumber 
				new ClientHandler(Listener.accept(), clientNumber++).start();
			}
		
		} finally {
		// Fermeture de la connexion
		Listener.close();
		
		}
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
