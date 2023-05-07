import java.io.*;
import java.lang.String;
import java.net.Socket;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
public class ClientHandler extends Thread { 
	
	// pour traiter la demande de chaque client sur un socket particulier
	private Socket socket;
	private int clientNumber;
	
	public ClientHandler(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber; 
		System.out.println("New connection with client#" + clientNumber + " at" + socket);
	}
	
	public void run() { // Création de thread qui envoi un message à un client
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream()); 
			// création de canal d’envoi 
			out.writeUTF("Hello from server - you are client#" + clientNumber + "\n"); 
			
			DataInputStream in = new DataInputStream(socket.getInputStream());
			String inputString = "";
			String command = "";
			String argument = "";
			String endOfTransmission = "DONE";
			File cwd = new File(".");
			boolean isDone = false;
			while (!isDone) {
				inputString = in.readUTF();
				command = getCommand(inputString);
				argument = getArgument(inputString);
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");  
				LocalDateTime now = LocalDateTime.now();
				   
				System.out.println("[" 
				+ socket.getRemoteSocketAddress().toString().substring(1) 
				+ " - "
				+ dtf.format(now)
				+ "]:" 
				+ command);
				
				// TODO : Complete 
				switch(command) {
					case "ls":
						File[] files = cwd.listFiles();
						for (File file : files) {
							if (file.isFile()) {
								out.writeUTF("[File]    ");
							}
							else {
								out.writeUTF("[Folder]  ");
							}
							out.writeUTF(file.getName() + "\n");
						}
						out.writeUTF(endOfTransmission);
						break;
					case "mkdir":
						File newDir = new File(cwd.getCanonicalPath() + "/" + argument);
						if (newDir.mkdir()) {
							out.writeUTF("Le dossier " + newDir.getName() + " a bien été créé.");
						}
						else {
							out.writeUTF("Erreur lors de la création du dossier " + newDir.getName() + ". Opération avortée.");
						}
						out.writeUTF(endOfTransmission);
						break;
					case "cd":
						File newWd = new File(cwd.getCanonicalPath() + "/" + argument);
						if (newWd.isDirectory()) {
							cwd = newWd;
							out.writeUTF("Vous êtes dans le dossier " + cwd.getName() + ".");
							out.writeUTF(endOfTransmission);
						}
						break;
					case "upload":
						break;
					case "download":
						break;
					case "exit":
						out.writeUTF("DONE");
						isDone = true;
						break;
					default:
				}
			}
			
		} catch (IOException e) {
			System.out.println("Error handling client# " + clientNumber + ": " + e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Couldn't close a socket, what's going on?");
			}
			System.out.println("Connection with client# " + clientNumber+ " closed");
		}
	}
	
	public String getCommand(String input) {
		int endIndex = input.indexOf(" ");
		if (endIndex == -1) {
			return input;
		}
		return input.substring(0, endIndex);
	}
	
	public String getArgument(String input) {
		int startIndex = input.indexOf(" ");
		return input.substring(startIndex + 1, input.length());		
	}
}
