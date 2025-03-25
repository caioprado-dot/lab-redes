import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change for EC2 isnatce server address
        int serverPort = 12345; // Server port
        
        try (Socket socket = new Socket(serverAddress, serverPort);
             InputStream serverInputStream = socket.getInputStream()) {

            long startTime = System.currentTimeMillis(); // Start time for transfer
            
            DataInputStream dis = new DataInputStream(serverInputStream);
            long fileSize = dis.readLong(); // Read the file size
            
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
            int bytesRead;
            
            // Receive file data (it won't be persisted)
            while ((bytesRead = serverInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            
            long endTime = System.currentTimeMillis(); // End time for transfer
            System.out.println("Time to receive file: " + (endTime - startTime) + "ms");
            System.out.println("File received, size: " + fileSize + " bytes.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

