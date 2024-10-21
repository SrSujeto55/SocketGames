import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import Games.FourInRow;
import Games.Hangman;
import Games.RockPaperScissors;
import Games.TicTacToe;

public class Server {
    public static void main(String[] args) {
        if (args.length < 1){
            System.out.println("Usage: java Server [PORT]");
            System.exit(1);
        }
        try{
            int port = Integer.parseInt(args[0]);
            try(ServerSocket server = new ServerSocket(port)){
                System.out.println("Server started at port " + port);
                while(true){
                    Socket client = server.accept();
                    new ServerHandler(client).start();
                }
            }catch(IOException e){
                System.out.println("Can't start server");
            }
        }catch(NumberFormatException e){
            System.out.println("Invalid port");
            System.exit(1);
        }
        
    }   
}


class ServerHandler extends Thread{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean echoMode, tictackMode, RockPaperMode, fourInRowMode, hangmanMode;
    private Hangman hangman;
    private TicTacToe tictack;
    private RockPaperScissors RockPaper;
    private FourInRow fourInRow;

    public ServerHandler(Socket client){
        this.client = client;
    }

    private void close(){
        try{
            in.close();
            out.close();
            client.close();
        }catch(IOException e){
            System.out.println("Can't close client");
        }
    }

    private void searchMode(String msg){
        switch (msg) {
            case "ECHO_MODE":
                echoMode = !echoMode;
                out.println("Echo toggled " + (echoMode ? "ON" : "OFF")); 
                break;
            case "HANGMAN_MODE":
                System.out.println("Hangman mode activated by >> " + client.getInetAddress().getHostAddress());
                hangmanMode = true;
                hangman = new Hangman();
                out.println(hangman.welcomeMessage());
                break;
            case "TICTACTOE_MODE":
                System.out.println("TicTacToe mode activated by >> " + client.getInetAddress().getHostAddress());
                tictackMode = true;
                tictack = new TicTacToe();
                out.println("Modo en mantenimiento, disculpa las molestias");
                break;
            case "ROCKPAPERSCISSORS_MODE":
                System.out.println("RockPaperScissors mode activated by >> " + client.getInetAddress().getHostAddress());
                RockPaperMode = true;
                RockPaper = new RockPaperScissors();
                out.println("Modo en mantenimiento, disculpa las molestias");
                break;
            case "FOURINROW_MODE":
                System.out.println("FourInRow mode activated by >> " + client.getInetAddress().getHostAddress());
                fourInRowMode = true;
                fourInRow = new FourInRow();
                out.println("Modo en mantenimiento, disculpa las molestias");
                break;
            case "EXIT":
                out.println("Closing connection...");
                close();
                break;
            default:
                break;
        }
    }

    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            out.println("Bienvenido al servidor -> usa estos [COMANDOS]: \n TOGGLE_ECHO \n HANGMAN_MODE \n TICTACTOE_MODE \n ROCKPAPERSCISSORS_MODE \n FOURINROW_MODE \n EXIT");
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(echoMode);
                System.out.println("recv message from " + client.getInetAddress().getHostAddress() + ": " + message);

                if (message.equals("TOGGLE_ECHO")){
                    echoMode = !echoMode;
                    System.out.println("Echo toggled " + (echoMode ? "ON" : "OFF"));
                    out.println("Echo toggled " + (echoMode ? "ON" : "OFF"));
                    continue; 
                }
                
                if(!hangmanMode && !echoMode && !tictackMode && !RockPaperMode && !fourInRowMode){
                    searchMode(message);
                    continue;
                }

                if(echoMode){
                    out.println("[ECHO] " + message);
                    continue;
                }
                if (hangmanMode){
                    if(hangman.endGame()){
                        out.println("You lost!");
                        hangmanMode = false;
                        continue;
                    }
                    if (message.length() == 1){
                        out.println(hangman.guessChar(message.charAt(0)));
                    }else{
                        if(hangman.guessWord(message)){
                            out.println("You won!");
                            hangmanMode = false;
                        }else{
                            out.println("Wrong word!");
                        }
                    }
                }
            }
            System.out.println("Connection closed by " + client.getInetAddress().getHostAddress());

        }catch(IOException e){
            System.out.println("Can't read or write to client");
        }
    }
}

