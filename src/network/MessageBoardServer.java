package network;

import org.json.simple.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MessageBoardServer {

    static Scanner input = new Scanner(System.in);

    // start server

    public void start(){
        Scanner input = new Scanner(System.in);

        System.out.println("Please enter a port number: ");
        int portNumber = input.nextInt();

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            System.out.println("Server started!");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("Exception listening for connection on port " +
                    portNumber);
            System.out.println(e.getMessage());
        }
    }


    static class Clock {
        private long t;

        public Clock() { t = 0; }

        // tick the clock and return the current time
        public synchronized long tick() { return ++t; }
    }


    static class ClientHandler extends Thread {
        private static Map<String, User> users = new HashMap<>();
        // shared message board
        private static List<Message> board = new ArrayList<Message>();

        // shared logical clock
        private static Clock clock = new Clock();

        // number of messages that were read by this client already
        private int read;

        // login name; null if not set
        private User login;

        // password; null if not set
        private String password;

        private Socket client;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) throws IOException {
            client = socket;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            read = 0;
            login = null;
        }

        public void run() {
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // tick the clock and record the current time stamp
                    long ts = clock.tick();

                    // parse request, then try to deserialize JSON
                    Object json = JSONValue.parse(inputLine);

                    Request req;

                    // create a new account request
                    if (login == null &&
                            (req = CreateRequest.fromJSON(json)) != null) {

                        out.println(new CreateResponse());

                        users.put();
                        System.out.println(login + " logged in");
                        continue;
                    }

                    // login request? Must not be logged in already
                    if (login == null &&
                            (req = LoginRequest.fromJSON(json)) != null) {
                        // set login name
                        login = ((LoginRequest)req).getLogin();
                        // response acknowledging the login request
                        out.println(new LoginResponse());
                        System.out.println(login + " logged in");
                        continue;
                    }

                    // login already
                    if (login != null &&
                            (req = LoginRequest.fromJSON(json)) != null) {
                        out.println(new ErrorResponse("YOU HAVE ALREADY LOGGED IN"));
                        continue;
                    }

                    // post request? Must be logged in
                    if (login != null &&
                            (req = PostRequest.fromJSON(json)) != null) {
                        String message = ((PostRequest)req).getMessage();
                        // synchronized access to the shared message board
                        synchronized (ClientHandler.class) {
                            // add message with login and timestamp
                            board.add(new Message(message, login, ts));
                        }
                        // response acknowledging the post request
                        out.println(new SuccessResponse());
                        System.out.println(login + " : " + message);
                        continue;
                    }

                    // read request? Must be logged in
                    if (login != null && ReadRequest.fromJSON(json) != null) {
                        List<Message> msgs;
                        // synchronized access to the shared message board
                        synchronized (ClientHandler.class) {
                            msgs = board.subList(read, board.size());
                        }
                        // adjust read counter
                        read = board.size();
                        // response: list of unread messages
                        out.println(new MessageListResponse(msgs));
                        continue;
                    }

                    // quit request? Must be logged in; no response
                    if (login != null && QuitRequest.fromJSON(json) != null) {
                        System.out.println(login + " has quit...");
                        in.close();
                        out.close();
                        return;
                    }

                    // error response acknowledging illegal request
                    out.println(new ErrorResponse("ILLEGAL REQUEST"));
                }
            } catch (IOException e) {
                System.out.println("Exception while connected");
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        MessageBoardServer server = new MessageBoardServer();
        server.start();
    }



}
