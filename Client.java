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

    public void communicate(){
        try(Scanner scanner = new Scanner(System.in)){
            String response;
            while(true){
                System.out.print(">>>");
                String message = scanner.nextLine();
                out.println(message);
                response = in.readLine();
                System.out.println("S>>> " + response);
            }
        }catch(IOException e){
            System.out.println("Can't communicate with server");
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
        try(Scanner scanner = new Scanner(System.in)){

            Client client = new Client(8080);
            client.echoMessages();
            client.communicate();
        }catch(IOException e){
            System.out.println("Can't connect to server");
        }
    }
}
