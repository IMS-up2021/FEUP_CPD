import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
public class Game{

    static final int gameID;



    public static List<Player> wordle(List<Player> number_players, String word)  {

        final String BG_GREEN = "\u001b[42m";
        final String BG_YELLOW = "\u001b[43m";
        final String RESET = "\u001b[0m";

        System.out.println("WORDLE!");

        int attempts = 0;
        boolean winner = false;

        //6 guesses per player
        while(attempts < number_players.size()*6 && !winner) {
            for(Player player: number_players) {
                try {
                    Socket socket = player.getSocket();
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String guess;
                    do {
                        out.println("Make your guess: ");
                        guess = in.readLine().toUpperCase();
                        if (guess.length() != 5) { out.println("Please write a 5 letter word!");}

                    } while (guess.length() != 5);
                    attempts++;

                    StringBuilder result = new StringBuilder();

                    for (int j = 0; j < 5; j++) {
                        //letter matches
                        if (guess.charAt(j) == word.charAt(j)) {
                            result.append(BG_GREEN).append(guess.charAt(j)).append(RESET);
                        }
                        //letter on the wrong place
                        else if (word.contains(Character.toString(guess.charAt(j)))) {
                            result.append(BG_YELLOW).append(guess.charAt(j)).append(RESET);
                        }
                        //letter doesn't exist
                        else {
                            result.append(guess.charAt(j));
                        }
                    }
                    out.println(result);
                    if (guess.equals(word)) {
                        player.incrementScore();
                        out.println("YOU WON!");
                        winner = true;
                        break;
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        for (Player player: number_players){
            try{
                Socket socket = player.getSocket();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Game over. Your score: " + player.getScore());
                socket.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    return number_players;
    }
}




