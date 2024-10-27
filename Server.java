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
        // Infinite loop to accept clients in new threads
        //* MULTIPLE CLIENTS!
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

// Threaded class to handle the client connection
// This is the real Server
class ServerHandler extends Thread{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean echoMode, tictackMode, RockPaperMode, fourInRowMode, hangmanMode; // All playable modes
    private Hangman hangman;
    private TicTacToe tictack;
    private RockPaperScissors RockPaper;
    private FourInRow fourInRow;

    // Contructor to initialize the client socket passed by the infinite loop
    public ServerHandler(Socket client){
        this.client = client;
    }

    // Close the client connection, closes every stream and the socket
    private void close(){
        try{
            in.close();
            out.close();
            client.close();
        }catch(IOException e){
            System.out.println("Can't close client");
        }
    }

    // Protocol to send messages to the client defined by:
    // 1. The server sends a message to the client, this message is composed by two parts separated by ';;'
    // 2. The first part is the name of the game runningn in the server, and the second part is the message to be displayed
    private void msgProtocol(String hostname, String msg){
        String[] parts = msg.split("\n");
        if(parts.length > 1){
            for (String part : parts) {
                out.println(hostname + ";;" + part);
            }
            return;
        }
        out.println(hostname + ";;" + msg);
    }

    // Search for the mode to activate
    // The client can activate this modes by sending a message with the name of the mode
    // initializes the respective class depending on the mode.
    // and sends the welcome message to the client
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
                msgProtocol("HANGMAN", hangman.welcomeMessage());
                break;
            case "TICTAC_MODE":
                System.out.println("TicTacToe mode activated by >> " + client.getInetAddress().getHostAddress());
                tictackMode = true;
                tictack = new TicTacToe();
                msgProtocol("TICTAC", tictack.welcomeMessage());
                msgProtocol("TICTAC", tictack.printTableIndex());
                break;
            case "ROCKPAPER_MODE":
                System.out.println("RockPaperScissors mode activated by >> " + client.getInetAddress().getHostAddress());
                RockPaperMode = true;
                RockPaper = new RockPaperScissors();
                msgProtocol("ROCK_PAPER", RockPaper.welcomeMessage());
                break;
            case "FOURINROW_MODE":
                System.out.println("FourInRow mode activated by >> " + client.getInetAddress().getHostAddress());
                fourInRowMode = true;
                fourInRow = new FourInRow();
                msgProtocol("4INROW", fourInRow.welcomeMessage());
                break;
            case "EXIT":
                out.println("Closing connection...");
                close();
                break;
            default:
                break;
        }
    }

    // Thread method to run the logic of the server
    // depending on the mode activated, the server will behave differently, in an infinite loop
    // Also, the firts time ever, displays the avaliable commands and modes to play.
    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            msgProtocol("SERVER", "Welcome to 'THE SERVER' -> write this [COMMANDS]: \nTOGGLE_ECHO \nHANGMAN_MODE \nTICTAC_MODE \nROCKPAPER_MODE \nFOURINROW_MODE \nEXIT");
            String message;
            while ((message = in.readLine()) != null) {
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

                //* Just Echo
                if(echoMode){
                    msgProtocol("ECHO", message);
                    continue;
                }

                //? Game Modes

                //* ROCK PAPER SCISSORS
                else if (RockPaperMode){
                    if (message.equals("exit")){
                        RockPaperMode = false;
                        msgProtocol("SERVER", "RockPaperScissors mode deactivated");
                        continue;
                    }
                    msgProtocol("ROCK_PAPER", RockPaper.play(message));
                }

                //* TIC TAC TOE
                else if (tictackMode){
                    msgProtocol("TICTAC", tictack.playerMove(message));
                    String Winner = tictack.checkWinner();
                    if(!Winner.equals("NONE")){
                        msgProtocol("TICTAC", Winner);
                        msgProtocol("SERVER", "GAME ENDED");
                        tictackMode = false;
                        continue;
                    }
                }

                //* HANGMAN
                else if (hangmanMode){
                    if (message.length() == 1){
                        msgProtocol("HANGMAN", hangman.guessChar(message.charAt(0)));
                    }else{
                        if(hangman.guessWord(message)){
                            msgProtocol("HANGMAN", "You Won!");
                            msgProtocol("SERVER", "GAME ENDED");
                            hangmanMode = false;
                        }else{
                            msgProtocol("HANGMAN", "Wrong Word!");
                        }
                    }
                    if(hangman.endGame()){
                        msgProtocol("HANGMAN", "You lost!");
                        msgProtocol("SERVER", "GAME ENDED");
                        hangmanMode = false;
                        continue;
                    }
                }
                
                //* FOUR IN ROW
                else if (fourInRowMode){
                    if (message.equals("exit")){
                        fourInRowMode = false;
                        msgProtocol("SERVER", "Fun ruined by user :/");
                        continue;
                    }
                    else if(message.equals("80")){
                        msgProtocol("4INROW", "*Imagine yourself having the most fun moment of your life*");
                    } 
                    else{
                        msgProtocol("4INROW", "No '80', no fun, RULES!");
                    }
                }
            }
            System.out.println("Connection closed by " + client.getInetAddress().getHostAddress());

        }catch(IOException e){
            System.out.println("Can't read or write to client");
        }
    }
}

