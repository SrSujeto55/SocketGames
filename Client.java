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

    public Client(int port) throws IOException {
        socket = new Socket("localhost", port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void echoMessages(){
        try{
            out.println("ECHO_MODE");
            String response = in.readLine();
            System.out.println(response);
        }catch(IOException e){
            System.out.println("Can't start Echo mode");
        }
    }

    public void hangmanMessages(){
        try{
            out.println("HANGMAN_MODE");
            String response = in.readLine();
            System.out.println(response);
        }catch(IOException e){
            System.out.println("Can't start Hangman mode");
        }
    }

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
