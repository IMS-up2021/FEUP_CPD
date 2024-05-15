import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.*;
import java.nio.file.Files;

public class Server {
    private ServerSocket serverSocket;
    private Scanner console;
    private String code;
    private String lobbySize;
    private Player playerOne;
    private Player playerTwo;
    private Player playerThree;
    private Random random;

    private List<User> users;
    private List<User> authenticatedUsers;
    public Scanner scanner;
    private int i;
    public HashMap<Integer, String> words;
    final String BG_GREEN = "\u001b[42m";
    final String BG_YELLOW = "\u001b[43m";
    final String RESET = "\u001b[0m";

    public Server() throws Exception {
        console = new Scanner(System.in);
        serverSocket = new ServerSocket(6666);
        random = new Random();
        users = new ArrayList<User>();
        getUsersFromFile();
        authenticatedUsers = new ArrayList<User>();
        authenticateUser();
        i = 0;

        // Infinite loop to start a new game after one ends
        while (true) {
            System.out.println(BG_GREEN + "Creating New Lobby" + RESET);

            // Getting lobby code from user
            System.out.println("\"Enter\" for Lobby Cregode or \"q\" to close server");
            code = console.nextLine();

            // Checking if code is a quit event
            if (code.equals("q")) {
                break;
            }

            // Getting lobby size from user (either 1 or 2)
            System.out.println("Enter Lobby Size");
            lobbySize = console.nextLine();

            if (lobbySize.equals("2")) {
                System.out.println("Lobby Size: 2");

                // Getting first player
                playerOne = getPlayer();
                System.out.println(playerOne.getName() + " Joined");

                // Getting second player
                playerTwo = getPlayer();
                System.out.println(playerTwo.getName() + " Joined");

                // Create and start a game with two players
                startGame(2);

            } else if (lobbySize.equals("3")) {
                System.out.println("Lobby Size: 3");

                // Getting first player
                playerOne = getPlayer();
                System.out.println(playerOne.getName() + " Joined");

                // Getting second player
                playerTwo = getPlayer();
                System.out.println(playerTwo.getName() + " Joined");

                // Getting third player
                playerThree = getPlayer();
                System.out.println(playerThree.getName() + " Joined");

                // Create and start a game with three players
                startGame(3);

            } else {
                System.out.println(BG_YELLOW + "Invalid Lobby Size" + RESET);
            }
        }

        // Closing server after receiving quit event from user
        System.out.println(BG_YELLOW + "Closing Server" + RESET);
        console.close();
        serverSocket.close();


    }


    // Method that waits from a socket to make a connection then creates a Player using socket
    public Player getPlayer() throws Exception {
        Socket s;
        BufferedReader reader;
        PrintWriter writer;
        String input;

        // Infinite loop until successful connection with a socket
        while (true) {
            // Waiting for a socket to connect
            s = serverSocket.accept();

            // Setting timeout for connection reads
            s.setSoTimeout(2000);
            System.out.println("Accepted socket");
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new PrintWriter(s.getOutputStream(), true);

            try {
                // Waiting 2 seconds for socket to send lobby code
                input = reader.readLine();

                // Making sure lobby code is correct
                if (code.equals(input)) {
                    writer.println(lobbySize);
                    return new Player(s);

                    // If lobby code is incorrect, closing connection and telling socket they sent incorrect code
                } else {
                    writer.println("F");
                    s.close();
                }

                // Catching exception if didn't send lobby code within timeout time or if socket closed its connection
            } catch (Exception e) {
                writer.println("F");
                s.close();
                reader.close();
                writer.close();
            }
        }
    }

    // Method to get random word
    public String getWord() {
        return words.get(random.nextInt(i)).toUpperCase();
    }

    public void startGame(int numPlayers) {
        // Creating a new instance of Game
        Game game = new Game();

        // Starting the game with the given number of players
        game.wordle(numPlayers);
    }

    public void getWords(String s) {
        try {
            words = (HashMap<Integer, String>) Files.readAllLines(Paths.get(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // if user exists and password is correct, return true; otherwise call registerUser and add it to users
    public boolean validateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.validatePassword(password)) {
                    authenticatedUsers.add(user);
                    return true;
                }
                else {
                    System.out.println("Incorrect password");
                    return false;
                }
            }
        }

        return false;
    }

    public void registerUser() {
        System.out.println("User Registration");
        System.out.println("Username:");
        //input username
        String username = console.nextLine();

        while (true) {
            boolean unique = true;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                break;
            } else {
                System.out.println("Username already exists. Please enter a new username:");
                username = console.nextLine();
            }
        }

        System.out.println("Password:");
        //input password
        String password = console.nextLine();
        //check if password has length between 4 and 16
        while (password.length() < 4 || password.length() > 16) {
            System.out.println("Password must be between 4 and 16 characters. Please enter a new password:");
            password = console.nextLine();
        }
        User user = new User(username, password);
        authenticatedUsers.add(user);
        users.add(user);
        //write user to file
        try {
            FileWriter writer = new FileWriter("assign2/resources/users.txt", true);
            writer.write("\n"+username + "," + password);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("User registered and authenticated successfully!");

        }
    }

    public void getUsersFromFile() {
        try {
            ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(Paths.get("assign2/resources/users.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                users.add(new User(parts[0], parts[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean authenticateUser() {
        System.out.println("Enter username:");
        String username = console.nextLine();
        boolean userExists = checkUserExists(username);
        if(userExists){
            System.out.println("Enter password:");
            String password = console.nextLine();
            boolean authenticated = validateUser(username, password);
            while(!authenticated) {
                System.out.println("password:");
                password = console.nextLine();
                authenticated = validateUser(username, password);
            }
            System.out.println("User authenticated successfully.");
            return true;

        }
        else {
            System.out.println("User does not exist. Would you like to register?");
            System.out.println("Enter 'y' for yes or 'n' for no.");
            String response = console.nextLine();
            if (response.equals("y")) {
                registerUser();
                return true;
            }
            else {
                return false;
            }
        }
    }

    public boolean checkUserExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }





}


