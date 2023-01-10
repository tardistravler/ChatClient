package ChatTest2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.text.DateFormatter;

import org.apache.*;



public class FTPConnect{

	private String server;
	private int port;
	private String user;
	private String pass;
	private Boolean connected;
	FTPClient ftpClient = new FTPClient();
	
	/*
	 * show files aarray and dataformatter
	 * 
	 * */
	
	
	private FTPFile[] files;
	
	DateFormat dateFormater;
	
	
    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
	
    
	
	FTPConnect() {
	
		server = "";
		port = 21;
		user = "ftpuser";
		pass = "kiyo";
		connected = false;
		dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// TODO Auto-generated constructor stub
	}
	
	public Boolean areConnected() {
		return connected;
	}
	
	public void connectToFTP() {
		
		try {
			ftpClient.connect(server, port);
			showServerReply(ftpClient);
			
			int replyCode = ftpClient.getReplyCode();
			
			if(!FTPReply.isPositiveCompletion(replyCode)) {
				
				System.out.println("Operation failed " + replyCode);
				return;
			}
			connected = ftpClient.login(user, pass);
			showServerReply(ftpClient);
			if(!connected) {
				System.out.println("Could not connect to server");
			}
			else {
				files = ftpClient.listFiles();
				System.out.println("Logged in!");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void UploadFile() {
		
		
	}
	
	
	
	
	
	
	public void changeDirectoryToStories() {
		
		try {
	            // Changes working directory
	          boolean  success = ftpClient.changeWorkingDirectory("/story");
	            showServerReply(ftpClient);
	            
	            if (success) {
	                System.out.println("Successfully changed working directory.");
	                
	                files = ftpClient.listFiles();
	                ShowFiles();
	            } else {
	                System.out.println("Failed to change working directory. See server's reply.");
	            }
	 
	            // logs out
	         
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		
	}
	
	public void DownloadFile(String file) throws IOException {
		System.out.println(" : -> : ftp system : -> : ");
		System.out.println("enter file name to download : ");
		
		
		/*URL localFileLocation = FTPConnect.class.getProtectionDomain().getCodeSource().getLocation();*/
		
		final File localFileLocation = new File(".");
		
		System.out.println("localFileLocation " + localFileLocation.getAbsoluteFile().getParent());
		
		
		
		  //String remoteFile1 = "/test/video.mp4";
          File downloadFile1 = new File(localFileLocation + "\\" + file);
          OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
          boolean success = ftpClient.retrieveFile(file, outputStream1);
          outputStream1.close();

          if (success) {
              System.out.println("File "+ file + "has been downloaded successfully.");
              
              if (ftpClient.isConnected()){
            	  ftpClient.logout();
            	  ftpClient.disconnect();
            	  
              }
          }
          else {
        	  System.out.println("Error!");
          }
          
		
		
	}
	
	public void ShowFiles() throws IOException {
		
		boolean succ = ftpClient.changeToParentDirectory();
		files = ftpClient.listFiles();		
		
		for (FTPFile ftpFile : files) {
			if(ftpFile.isDirectory()) {
				System.out.println("directory : " + ftpFile);
			}else {
				System.out.println("file : " + ftpFile); 
			}

		}
		
	}
	
	
	
	
	
	
	
}
