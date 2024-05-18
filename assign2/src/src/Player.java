import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class Player {
    private Socket socket;
    private String name;
    private int score;

    public Player(Socket socket, String name, Integer score) {
        this.socket = socket;
        this.name = name;
        this.score = score;
    }

    public Socket getSocket(){return socket;}
    public String getName() {
        return name;
    }
    public int getScore(){return score;}
    public void incrementScore(){this.score++;}


    public static void main(String[] args) {
        try{
            Socket socket = new Socket("localhost", 6666);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            String response;
            while((response = input.readLine()) != null){
                System.out.println(response);
                if(response.contains("Enter username: ")){
                    String username = console.readLine();
                    output.println(username);
                } else if (response.contains("Enter password: ")) {
                    String password = console.readLine();
                    output.println(password);
                } else if(response.contains("Make your guess: ")){
                    String guess = console.readLine();
                    output.println(guess);
                }
                else if(response.contains("User does not exist. Would you like to register? (y/n)")){
                    String guess = console.readLine();
                    output.println(guess);
                }
                else if (response.contains("Password must be between 4 and 16 characters. Please enter a new password:")) {
                    String password = console.readLine();
                    output.println(password);
                }

            }
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}



