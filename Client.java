package ChatTest2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Client implements Runnable {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private Boolean done;

	private DataOutputStream dataOutputStream = null;
	private DataInputStream dataInputStream = null;
	private int randomWithThreadLocalRandomInARange;
	String message;
	private String username;
	private FTPConnect ftpConnect;

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {

			ftpConnect = new FTPConnect();

			done = false;
			socket = new Socket("127.0.0.1", 5999);

			out = new PrintWriter(socket.getOutputStream(), true); // send to server and everyone
			in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // get from client

			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());

			InputHandler inHandler = new InputHandler();
			Thread t = new Thread(inHandler);
			t.start();

			String inMessage;

			while ((inMessage = in.readLine()) != null) {

				System.out.println(inMessage);
				System.out.print(": ");
			}

		} catch (IOException e) {
			// e.printStackTrace();
		}

	}

	class InputHandler implements Runnable {

		@Override
		public void run() {
			try {
				BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in)); // what client sends

				while (!done) {
					message = inReader.readLine();

					if (message.equals("/quit")) {
						out.println("/quit");
						inReader.close();
						shutdown();

					}
					
					if(message.equals("/readstory")) {
						readStory();
						
					}

					if (message.equals("/sendmessage")) {

						out.println("/sendmessage");

					}

					if (message.equals("/helpftp")) {
						showFTPHelp();
					}

					if (message.equals("/showfile")) {
						showFile();
					}

					if (message.equals("/downloadfile")) {
						downloadFile();
					}

					if (message.equals("/username")) {
						out.print("/username");
					}

					if (message.equals("/connect")) {
						out.println("/connect");
						ftpConnect.connectToFTP();
						showFTPHelp();

					}

					/*
					 * add a handler for direct messaging new string if contains --> and
					 * Nickname(your nick) sysout a message only to the client.
					 */

					else {
						out.println(message);
					}

				}

			} catch (IOException e) {
				// TODO: handle exception
			}

		}
		
		public void readStory() {
			
		try {
			ftpConnect.changeDirectoryToStories();
			
			
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
			
		}

		public void sendMesage(String message) {
			try {
				System.out.println("nickname " + message);
			} catch (Exception e) {
				System.out.println(e);
			}

		}

		public int getRandomInt() {
			return randomWithThreadLocalRandomInARange;
		}

		public void assignRandomNumber() {

			randomWithThreadLocalRandomInARange = ThreadLocalRandom.current().nextInt(1, 9999);
		}

		public void showFTPHelp() {
			System.out.println("to show help type in /helpftp\n");
			System.out.println("\t" + "/showfile" + "\t" + "/sendfile");
			System.out.println("\t" + "/downloadfile" + "\t" + "/exit");
		}

		public void showFile() {
			try {
				ftpConnect.ShowFiles();
			} catch (Exception e) {
				System.out.println("error : " + e);
			}

		}

		public void downloadFile() throws IOException {
			BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in)); // what client sends
			String message = inReader.readLine();
			ftpConnect.DownloadFile(message);

		}

		public void shutdown() {
			done = true;
			try {
				while (done) {
					in.close();
					out.close();
					if (!socket.isClosed()) {
						socket.close();
					}
					System.exit(0);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		public void sendFile(String path) {

			try {

				int bytes = 0;
				// Open the File where he located in your pc
				File file = new File(path);
				FileInputStream fileInputStream = new FileInputStream(file);

				// Here we send the File to Server
				dataOutputStream.writeLong(file.length());
				// Here we break file into chunks
				byte[] buffer = new byte[4 * 1024];
				while ((bytes = fileInputStream.read(buffer)) != -1) {
					// Send the file to Server Socket
					dataOutputStream.write(buffer, 0, bytes);
					dataOutputStream.flush();
				}
				// close the file here
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}
}
