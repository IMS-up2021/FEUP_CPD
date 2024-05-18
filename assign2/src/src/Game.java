import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
public class Game{
    /*private static final String database = "players.txt";
    public static int rank = 0;

    public static void updateRanking(String playerName, int newRanking) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(database));
            String line;
            StringBuilder content = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                String username = parts[0].split(": ")[1];
                if (username.equals(playerName)) {
                    parts[2] = "Rank: " + newRanking;
                    line = String.join(", ", parts);
                }
                content.append(line).append("\n");
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(database));
            writer.write(content.toString());
            writer.close();

            System.out.println("Ranking updated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public static void wordle(List<Player> number_players, String word)  {

        final String BG_GREEN = "\u001b[42m";
        final String BG_YELLOW = "\u001b[43m";
        final String RESET = "\u001b[0m";

        System.out.println("WORDLE!");

        int attemps = 0;
        boolean winner = false;

        //6 guesses
        while(attemps < number_players.size()*6 && !winner) {
            for(Player player: number_players) {
                try {
                    Socket socket = player.getSocket();
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    out.println("Make your guess: ");
                    String guess = in.readLine().toUpperCase();
                    if (guess.length() != 5) {
                        System.out.println("Please write a 5 letter word!");
                        continue;
                    } else {
                        attemps++;
                    }

                    StringBuilder result = new StringBuilder();

                    for (int j = 0; j < 5; j++) {
                        //letter matches
                        if (guess.substring(j, j + 1).equals(word.substring(j, j + 1))) {
                            result.append(BG_GREEN).append(guess.charAt(j)).append(RESET);
                        }
                        //leter on the wrong place
                        else if (word.contains(guess.substring(j, j + 1))) {
                            result.append(BG_YELLOW).append(guess.charAt(j)).append(RESET);
                        }
                        //letter doesn't exist
                        else {
                            result.append(guess.charAt(j));
                        }
                        out.println(result);
                        if (guess.equals(word)) {
                            player.incrementScore();
                            out.println("YOU WON!");
                            winner = true;
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
    }
}




