import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    public BufferedReader in;
    public PrintWriter out;

    // Contructor og the client, connects to the desired port in localhost (Changa as needed) and starts the input and output streams acording to the socket
    public Client(int port) throws IOException {
        socket = new Socket("localhost", port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    // Debugg mode to Echo from the Alpha version
    //! Deprecated
    public void echoMessages(){
        try{
            out.println("ECHO_MODE");
            String response = in.readLine();
            System.out.println(response);
        }catch(IOException e){
            System.out.println("Can't start Echo mode");
        }
    }

    // Debugg mode to Hangman from the Alpha version
    //! Deprecated
    public void hangmanMessages(){
        try{
            out.println("HANGMAN_MODE");
            String response = in.readLine();
            System.out.println(response);
        }catch(IOException e){
            System.out.println("Can't start Hangman mode");
        }
    }

    // Main method to comunicate with the server, only establishes a communication chanel between the client and the server
    // The server need to follow the protocol to communicate with the client
    // this protocol is defined as follows:
    // 1. The server sends a message to the client, this message is composed by two parts separated by ';;'
    // 2. The first part is the name of the game runningn in the server, and the second part is the message to be displayed
    // The client starts a new thread to read the messages from the server to prevent blocking the main thread leading to malfunction
    public void communicate(){
        new Thread(() -> {
            String response;
            try {
                while ((response = in.readLine()) != null) {
                    String[] parts = response.split(";;");
                    if(parts.length != 2){
                        System.out.println("protocol response error, expected 2 parts, got " + parts.length);
                        continue;
                    }
                    System.out.println("[" + parts[0] + "] >>> " + parts[1]);
                }
            } catch (IOException e) {
                System.out.println("Error leyendo del servidor");
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while(true){
            String message = scanner.nextLine();
            if(message.equals("EXIT")){
                scanner.close();
                break;
            }
            out.println(message);
        }
    }

    // Debugg mode to Party from the Alpha version
    //! Deprecated
    public void partyMode(){
        try{
            out.println("PARTY_MODE");
            String response = in.readLine();
            System.out.println(response);
        }catch(IOException e){
            System.out.println("Can't start Party mode");
        }
    }

    public static void main(String[] args) {
        if (args.length < 1){
            System.out.println("Usage: java Client [PORT]");
            System.exit(1);
        }
        
        try{
            int port = Integer.parseInt(args[0]);
            try(Scanner scanner = new Scanner(System.in)){
                Client client = new Client(port);
                client.communicate();
            }catch(IOException e){
                System.out.println("Can't connect to server");
            }
        }catch(NumberFormatException e){
            System.out.println("Invalid port");
            System.exit(1);
        }
    }
}
