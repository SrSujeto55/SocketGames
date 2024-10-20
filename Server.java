import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try(ServerSocket server = new ServerSocket(8080)){
            System.out.println("Server started at port 8080");
            while(true){
                Socket client = server.accept();
                new ServerHandler(client).start();
            }
        }catch(IOException e){
            System.out.println("Can't start server");
        }
    }   
}


class ServerHandler extends Thread{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private Boolean echoMode = false;
    public ServerHandler(Socket client){
        this.client = client;
    }


    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("recv message from " + client.getInetAddress().getHostAddress() + ": " + message);
                if (message.equals("ECHO_MODE")) {
                    out.println("Echo mode enabled");
                    echoMode = true;
                    continue;
                }
                if(echoMode){
                    out.println("[ECHO] " + message);
                    continue;
                }else{
                    out.println("Message received! thanks for your information");
                }
            }
            System.out.println("Connection closed by " + client.getInetAddress().getHostAddress());

        }catch(IOException e){
            System.out.println("Can't read or write to client");
        }
    }
}

