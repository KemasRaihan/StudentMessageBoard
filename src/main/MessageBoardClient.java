package main;// Solution to Week 8 Homework Exercise 4

// compile: javac -cp json-simple-1.1.1.jar;. MessageBoardClientV4.java
// run:     java  -cp json-simple-1.1.1.jar;. MessageBoardClientV4 <host name> <port number>

import org.json.simple.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class MessageBoardClient {
    private User user;
    private Channel channel;
    private Socket socket; // client socket
    private PrintWriter toServer; // write to server
    private BufferedReader fromServer; // read from server

    private String serverResponse;
    private Response res;
    private Request req;
    private Object json;

    private BufferedReader fromConsole;

    static final int portNumber = 12345;

    public static void main(String[] args) {
        MessageBoardClient msgclient = new MessageBoardClient();
    }

    public MessageBoardClient() {
        connect();
    }

    public void connect() {
        String hostName = "localhost";

        try {
            socket = new Socket(hostName, portNumber);
            toServer = new PrintWriter(socket.getOutputStream(), true);
            fromServer = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            fromConsole = new BufferedReader(
                    new InputStreamReader(System.in));
            String userInput;

            System.out.println("Connected to server...");

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }

        toServer.println(new EntryRequest());
        entry();

        System.out.println("Disconnected from server");

    }


    Scanner input = new Scanner(System.in);

    public void entry() {

        try {

            String serverResponse;
            Response res;
            Request req;
            Object json;

            // get server response
            while ((serverResponse = fromServer.readLine()) != null) {

                // Parse user and build request
                json = JSONValue.parse(serverResponse);

                if ((res = EntryResponse.fromJSON(json)) != null) {
                    // ask client to create an account or sign-in
                    ((EntryResponse) res).displayOptions();

                    String userInput = input.next();
                    Scanner sc = new Scanner(userInput);
                    switch (sc.next()) {
                        case "create":
                            req = new CreateUserRequest(new User("", new ArrayList<>()));
                            break;
                        case "login":
                            req = new LoginRequest("");
                            break;
                        case "quit":
                            req = new QuitRequest();
                            break;
                        default:
                            System.out.println("ILLEGAL COMMAND");

                            // request server to open entry options again
                            toServer.println(new EntryRequest());
                            continue;
                    }
                    // write entry option to server
                    toServer.println(req);
                    continue;
                }

                if((res = CreateUserResponse.fromJSON(json))!=null){
                    this.user=((CreateUserResponse) res).createNewUser();
                    req = new CreateUserRequest(user);
                    toServer.println(req);
                    continue;
                }

                if((res = LoginResponse.fromJSON(json))!=null){
                    String name = ((LoginResponse) res).enterDetails();
                    req = new LoginRequest(name);
                    toServer.println(req);
                    continue;
                }


                if ((res = SuccessResponse.fromJSON(json)) != null) {
                    System.out.println(((SuccessResponse) res).getMessage());

                    // go to menu
                    menu();
                    continue;
                }

                if ((res = ErrorResponse.fromJSON(json)) != null) {
                    System.out.println(((ErrorResponse) res).getError());

                    // return to menu
                    toServer.println(new EntryRequest());
                    continue;
                }

                if ((res = QuitResponse.fromJSON(json)) != null) {
                    System.out.println(((QuitResponse) res).getMessage());
                    toServer.close();
                    fromServer.close();
                    return;
                }

                System.out.println("Unable to get response from server.");
                System.exit(1);
            }
        } catch (NoSuchElementException e) {
            System.out.println("ILLEGAL COMMAND");
        } catch (IOException e) {
            e.getMessage();
        }


    }
    public void menu() {

        try {

            String serverResponse;
            Response res;
            Request req;
            Object json;

            // get server response
            while ((serverResponse = fromServer.readLine()) != null) {

                // Parse user and build request
                json = JSONValue.parse(serverResponse);

                if ((res = MenuResponse.fromJSON(json)) != null) {
                    // ask client to create an account or sign-in
                    ((MenuResponse) res).displayOptions();

                    String userInput = input.next();
                    Scanner sc = new Scanner(userInput);
                    switch (sc.next()) {
                        case "create":
                            req = new CreateChannelRequest(new Channel("", new HashMap<>(), new ArrayList<>()));
                            break;
                        case "view":
                            req = new ViewChannelsRequest();
                            break;
                        case "logout":
                            req = new LogoutRequest();
                            break;
                        default:
                            System.out.println("ILLEGAL COMMAND");

                            // request server to open entry options again
                            toServer.println(new MenuRequest(user));
                            continue;
                    }
                    // write entry option to server
                    toServer.println(req);
                    continue;
                }

                System.out.println(json);

                // Parse user and build request
                json = JSONValue.parse(serverResponse);


                // user is able to create a channel
                if ((res = CreateChannelResponse.fromJSON(json)) != null) {
                    this.channel = ((CreateChannelResponse) res).createNewChannel();
                    req = new CreateChannelRequest(channel);
                    // write new request to server
                    toServer.println(req);
                    continue;
                }

                if ((res = ViewChannelsResponse.fromJSON(json)) != null) {
                    // retrieve list of available channels
                    ((ViewChannelsResponse) res).view();
                    System.out.println();
                    channel = ((ViewChannelsResponse) res).chooseChannel();
                    if(channel==null)
                        toServer.println(new MenuRequest(user));
                    else
                        toServer.println(new ViewChannelRequest(channel));
                    continue;
                }

                if((res = ViewChannelResponse.fromJSON(json))!=null){
                    System.out.println("Viewing " + channel.getName());
                    toServer.println(new ViewChannelRequest(channel));
                    viewChannel();
                    continue;
                }

                if((res = LogoutResponse.fromJSON(json))!=null){
                    System.out.println("\nYou have logged out\n");
                    toServer.println(new EntryRequest());
                    break;
                }


                if ((res = SuccessResponse.fromJSON(json)) != null) {
                    System.out.println(((SuccessResponse) res).getMessage());

                    // return to menu
                    toServer.println(new MenuRequest(user));
                    continue;
                }

                if ((res = ErrorResponse.fromJSON(json)) != null) {
                    System.out.println(((ErrorResponse) res).getError());

                    // return to menu
                    toServer.println(new MenuRequest(user));
                    continue;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("ILLEGAL COMMAND");
        } catch (IOException e) {
            e.getMessage();
        }


    }

    public void viewChannel() {

        try {

            String serverResponse;
            Response res;
            Request req;
            Object json;


            // get server response
            while ((serverResponse = fromServer.readLine()) != null) {

                // Parse user and build request
                json = JSONValue.parse(serverResponse);


                // entry to channel response
                if ((res = ViewChannelResponse.fromJSON(json)) != null) {
                    ((ViewChannelResponse) res).displayOptions();
                    Channel channel = ((ViewChannelResponse) res).getChannel();

                    String userInput = input.next();
                    Scanner sc = new Scanner(userInput);
                    switch (sc.next()) {
                        case "write":
                            req = new WriteRequest(channel,user.getName(), null);
                            ((WriteRequest) req).writeMessage();
                            break;
                        case "subscribe":
                            req = new SubscribeRequest(channel, user);
                            break;
//                        case "unsubscribe":
//                            req = new SubscribeRequest(channel, new User("",  new ArrayList<>()));
//                            break;
                        case "return":
                            req = new MenuRequest(user);
                            break;
                        default:
                            System.out.println("ILLEGAL COMMAND");

                            // request server to open entry options again
                            toServer.println(new ViewChannelRequest(channel));
                            continue;
                    }
                    // write entry option to server
                    toServer.println(req);
                    continue;
                }


                if((res = MenuResponse.fromJSON(json))!=null){
                    toServer.println(new MenuRequest(user));
                    break;
                }


                if ((res = SuccessResponse.fromJSON(json)) != null) {
                    System.out.println(((SuccessResponse) res).getMessage());

                    // return to menu
                    toServer.println(new MenuRequest(user));
                    continue;
                }

                if ((res = ErrorResponse.fromJSON(json)) != null) {
                    System.out.println(((ErrorResponse) res).getError());

                    // return to menu
                    toServer.println(new MenuRequest(user));
                    continue;
                }


                System.out.println("Unable to get response from server.");
                System.exit(1);
            }
        } catch (NoSuchElementException e) {
            System.out.println("ILLEGAL COMMAND");
        } catch (IOException e) {
            e.getMessage();
        }


    }
}
