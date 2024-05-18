import java.io.*;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.Paths;
import java.util.*;
import java.nio.file.Files;

import static java.lang.Integer.parseInt;

public class Server {

    private final int port;
    private ServerSocket serverSocket;
    private final Scanner console;
    private final Random random;
    public static ArrayList<String> words;
    private final List<User> users;
    private final List<User> authenticatedUsers;
    final String BG_GREEN = "\u001b[42m";
    final String BG_YELLOW = "\u001b[43m";
    final String RESET = "\u001b[0m";

    public Server(int port) throws Exception {
        this.port = port;
        console = new Scanner(System.in);
        random = new Random();
        users = new ArrayList<>();
        authenticatedUsers = new ArrayList<>();

        listWords("resources/words.txt");
        getUsersFromFile("resources/users.txt");
        start();
    }
    public void start() throws Exception {
        this.serverSocket = ServerSocketChannel.open().socket();
        serverSocket.bind(new InetSocketAddress(this.port));
        System.out.println("Server is on port " + this.port);


        // Infinite loop to start a new game after one ends
        while (true) {

            System.out.println(BG_GREEN + "Creating New Lobby" + RESET);

            // Getting lobby size from user
            System.out.println("\"Enter\" for Lobby Size (2 or 3) or \"q\" to close server");
            String lobbySizeInput = console.nextLine();

            // Checking if code is a quit event
            if (lobbySizeInput.equals("q")) {
                break;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Lobby Size: ");
            String lobbySize = scanner.nextLine();

            try{
                if(parseInt(lobbySize) < 2 || parseInt(lobbySize) > 3){
                    System.out.println(BG_YELLOW + "Invalid Lobby Size" + RESET);
                    continue;
                }
            }
            catch(NumberFormatException e){
                System.out.println(BG_YELLOW + "Invalid Input" + RESET);
                continue;
            }

            System.out.println("Waiting for client...");

            startLobby(parseInt(lobbySize));

        }

        // Closing server after receiving quit event from user
        System.out.println(BG_YELLOW + "Closing Server" + RESET);
        console.close();
        serverSocket.close();

    }

    private void startLobby(int lobbySize) throws Exception {
        List<Player> players = new ArrayList<>();

        for(int i = 1; i <= lobbySize; i++){
            Player player = authenticateUser();
            if(player != null){
                players.add(player);
                player = getPlayer();
                System.out.println(player.getName() + " Joined");
            }
            else{
                System.out.println(BG_YELLOW + "Failed to authenticate user " + i + RESET);
                return;
            }
        }
        startGame(players);
    }

    // Method that waits from a socket to make a connection then creates a Player using socket
    public Player getPlayer() throws Exception {

        // Infinite loop until successful connection with a socket
        while (true) {
            // Waiting for a socket to connect
            Socket socket = serverSocket.accept();

            // Setting timeout for connection reads
            socket.setSoTimeout(2000);
            System.out.println("Client connected: " + socket.getInetAddress());


            //BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //PrintWriter writer = new PrintWriter(s.getOutputStream(), true);

            /*try {
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
            }*/
        }
    }

    // Method to get a random word out of the listWords
    public String getWord() {
        return words.get(random.nextInt(words.size())).toUpperCase();
    }

    // Starting the game with the given number of players
    public void startGame(List<Player> numPlayers) {
        Game.wordle(numPlayers, getWord());
    }


    //lists the words that are on the words.txt in an Array List
    public static void listWords(String s) {
        try {
            words = new ArrayList<>(Files.readAllLines(Paths.get(s)));
            System.out.println("Words loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // if user exists and password is correct, return true
    // otherwise call registerUser and add it to users

    public boolean validateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.validatePassword(password)) {
                    if (!authenticatedUsers.contains(user)) {
                        authenticatedUsers.add(user);
                    }
                    return true;
                }
                else {
                    System.out.println("Incorrect password");
                    return false;
                }
            }
        }
        return true; //fixed it
    }

    /*public boolean validateUser(String username, String password) {
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
*/
    //registers a new user
    public void registerUser(Socket socket) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        writer.println("User Registration");
        writer.println(" ");
        writer.println("Username:");

        //input username
        String username = scanner.nextLine();

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
               writer.println("Username already exists. Please enter a new username:");
                username = scanner.nextLine();
            }
        }

        writer.println("Enter Password: ");
        //input password
        String password = scanner.nextLine();
        //check if password has length between 4 and 16
        while (password.length() < 4 || password.length() > 16) {
            writer.println("Password must be between 4 and 16 characters. Please enter a new password:");
            password = scanner.nextLine();
        }
        User user = new User(username, password);
        authenticatedUsers.add(user);
        users.add(user);
        //write user to file
        try {
            FileWriter fileWriter = new FileWriter("assign2/resources/users.txt", true);
            fileWriter.write("\n"+ username + "," + password);
            writer.println("User registered and authenticated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUsersFromFile(String path) {
        try {
            //ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(Paths.get(path));
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while (line != null) {
                String[] parts = line.split(",");
                users.add(new User(parts[0], parts[1]));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player authenticateUser() throws IOException {
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        // This is now waiting for input from the client
        writer.println("Enter username: ");
        String username = in.readLine();
        if(checkUserExists(username)){
            writer.println("Enter password: ");
            String password = in.readLine();
            if(validateUser(username, password)){
                writer.println("User authenticated successfully!");
                return new Player(socket,username);
            }
            else{
                writer.println("Incorrect password. Authentication failed.");
                socket.close();
                return null;
            }
        }
        else{
            writer.println("User does not exist. Would you like to register? (y/n)");
            String response = in.readLine();
            if(response.equalsIgnoreCase("y")){
                registerUser(socket);
                return new Player(socket, username);
            }
            else{
                writer.println("Authentication failed!");
                socket.close();
                return null;
            }
        }
    }

    public boolean checkUserExists(String username) {
        //users.clear();
        //getUsersFromFile(path);
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        Server server = null;
        try {
            server = new Server(6666);
            //listWords("assign2/resources/words.txt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        server.start();
    }
}
