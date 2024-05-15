import java.io.*;
import java.util.Scanner;
public class Game{
    private static final String database = "players.txt";
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
    }
    public static void wordle(int number_players, String word)  {

        final String BG_GREEN = "\u001b[42m";
        final String BG_YELLOW = "\u001b[43m";
        final String RESET = "\u001b[0m";

        System.out.println("WORDLE!");

        Scanner reader = new Scanner(System.in);
        String guess;
        int attemps = 0;
        int player_number=1;

        //6 guesses
        while(attemps < number_players*6) {
            boolean flag = false;
            player_number=1;
            while(player_number <= number_players){
                System.out.print("Player " + player_number + " make your guess: ");
                guess = reader.nextLine().toUpperCase();

                if (guess.length() != 5) {
                    System.out.println("Please write a 5 letter word!");
                    continue;
                } else {
                    attemps++;
                    player_number++;
                }


                for (int j = 0; j < 5; j++) {
                    //letter matches
                    if (guess.substring(j, j + 1).equals(word.substring(j, j + 1))) {
                        System.out.print(BG_GREEN + guess.charAt(j) + RESET);
                    }
                    //leter on the wrong place
                    else if (word.contains(guess.substring(j, j + 1))) {
                        System.out.print(BG_YELLOW + guess.charAt(j) + RESET);
                    }
                    //letter doesn't exist
                    else {
                        System.out.print(guess.charAt(j));
                    }
                }
                System.out.println("");
                if (guess.equals(word)) {
                    player_number--;
                    rank++;
                    System.out.println("PLAYER " + player_number + " WON: " + rank + " points");
                    flag=true;
                    break;
                }
            }
            if(flag==true){
                break;
            }
        }
    }
}




