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
            msgProtocol("SERVER", "Bienvenido al servidor -> usa estos [COMANDOS]: \nTOGGLE_ECHO \nHANGMAN_MODE \nTICTAC_MODE \nROCKPAPER_MODE \nFOURINROW_MODE \nEXIT");
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
                    if(hangman.endGame()){
                        msgProtocol("HANGMAN", "You lost!");
                        msgProtocol("SERVER", "GAME ENDED");
                        hangmanMode = false;
                        continue;
                    }
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
                }
            }
            System.out.println("Connection closed by " + client.getInetAddress().getHostAddress());

        }catch(IOException e){
            System.out.println("Can't read or write to client");
        }
    }
}

