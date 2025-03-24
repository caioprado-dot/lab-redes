import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345; // Port number for server to listen
        ExecutorService executor = Executors.newCachedThreadPool(); // Handle multiple clients in threads
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                executor.submit(new ClientHandler(clientSocket)); // Handle each client in a new thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (InputStream fileInputStream = new FileInputStream("Appearance.png"); 
             OutputStream clientOutputStream = clientSocket.getOutputStream()) {
            
            // Get the file size and send it first
            File file = new File("Appearance.png");
            long fileSize = file.length();
            DataOutputStream dos = new DataOutputStream(clientOutputStream);
            dos.writeLong(fileSize);
            
            // Send the file data
            byte[] buffer = new byte[1024];
            int bytesRead;
            long startTime = System.currentTimeMillis(); // Start time for transfer

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                clientOutputStream.write(buffer, 0, bytesRead);
            }
            
            long endTime = System.currentTimeMillis(); // End time for transfer
            System.out.println("Time to send file: " + (endTime - startTime) + "ms");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

