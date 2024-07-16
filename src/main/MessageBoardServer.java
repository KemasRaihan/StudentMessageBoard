package main;

import org.json.simple.*;

import javax.swing.text.View;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MessageBoardServer {

    static Scanner input = new Scanner(System.in);
    static final String channelsPath = "channels.bin";
    static final int portNumber = 12345;

    public static void main(String[] args) {
        MessageBoardServer server = new MessageBoardServer();
        server.startServer();
    }

    // start server

    public void startServer(){
        Scanner input = new Scanner(System.in);


        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            System.out.println("Server started!");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("Exception listening for connection on port " +
                    portNumber);
            System.out.println(e.getMessage());
        }

    }







    static class ClientHandler extends Thread {
        private static ArrayList<Channel> channels = new ArrayList<>();

        private static ArrayList<User> users = new ArrayList<>();


        ModifiedScanner input = new ModifiedScanner();


        private Socket client;
        private PrintWriter toClient;
        private BufferedReader fromClient;

        public ClientHandler(Socket socket) throws IOException {
            client = socket;
            toClient = new PrintWriter(client.getOutputStream(), true);
            fromClient = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
        }

        public void run() {
            try {
                String clientRequest;
                Request req;
                Object json;

                retrieveChannelsFromFile();

                while((clientRequest = fromClient.readLine()) != null){

                    // parse request, then try to deserialize JSON
                    json = JSONValue.parse(clientRequest);

                    System.out.println(json);

                    // menu request after the user successfully signed-in
                    if((req = EntryRequest.fromJSON(json))!=null){
                        toClient.println(new EntryResponse());
                        continue;
                    }

                    if((req = CreateUserRequest.fromJSON(json))!=null){
                        User userToBeAdded = ((CreateUserRequest) req).getUser();
                        if(userToBeAdded.getName().equals("")){
                            toClient.println(new CreateUserResponse());
                        }else{
                            boolean userAlreadyExists = false;
                            System.out.println("User to be added: " + userToBeAdded.getName());
                            for(User user : users){
                                if(userToBeAdded.getName().equals(user.getName())){
                                    System.out.println("Existing user: " + user.getName());
                                    userAlreadyExists=true;
                                    break;
                                }
                            }
                            if(!userAlreadyExists) {
                                users.add(userToBeAdded);
                                toClient.println(new SuccessResponse("You have successfully created a new user!"));
                                toClient.println(new MenuResponse(userToBeAdded));
                            }
                            else
                                toClient.println(new ErrorResponse("User already exists"));
                        }
                        continue;
                    }

                    if((req = LoginRequest.fromJSON(json))!=null){
                        String name = ((LoginRequest) req).getName();
                        User userToBeSignedIn = new User(name, new ArrayList<>());
                        if(name.equals("")){
                            toClient.println(new LoginResponse(name));
                        }else{
                            boolean userAlreadyExists = false;
                            for(User user : users){
                                if(name==user.getName()){
                                    userAlreadyExists=true;
                                    userToBeSignedIn=user;
                                    break;
                                }
                            }
                            if(!userAlreadyExists) {
                                toClient.println(new SuccessResponse("Welcome " + name));
                                toClient.println(new MenuResponse(userToBeSignedIn));
                            }
                            else
                                toClient.println(new ErrorResponse("User already exists"));
                        }

                        continue;
                    }

                    // menu request after the user successfully signed-in
                    if((req = MenuRequest.fromJSON(json))!=null){
                        User user = ((MenuRequest) req).getUser();
                        toClient.println(new MenuResponse(user));
                        continue;
                    }

                    if((req = CreateChannelRequest.fromJSON(json))!=null){
                        Channel channel = ((CreateChannelRequest) req).getChannel();
                        if(channel.getName().equals("")) { // if user has not created a channel yet
                            toClient.println(new CreateChannelResponse());
                        }else{
                            synchronized (ClientHandler.class) {
                                channels.add(channel);
                                toClient.println(new SuccessResponse("You have successfully created a channel!"));
                            }
                        }
                        continue;
                    }

                    // client requested to view channels that have been created
                    if((req = ViewChannelsRequest.fromJSON(json))!=null){
                        // respond with list of channels that have been created
                        if(channels.size()>0)
                            toClient.println(new ViewChannelsResponse(channels));
                        else
                            toClient.println(new ErrorResponse("There are no channels to view!"));
                        continue;
                    }

                    if((req = LogoutRequest.fromJSON(json))!=null){
                        toClient.println(new LogoutResponse());
                        continue;
                    }

                    if((req = ViewChannelRequest.fromJSON(json))!=null){
                        Channel channel = ((ViewChannelRequest) req).getChannel();
                        toClient.println(new ViewChannelResponse(channel));
                        continue;
                    }

                    if((req = ReadRequest.fromJSON(json))!=null){
                        User user = ((ReadRequest) req).getUser();
                        toClient.println(new ReadResponse(user));
                        continue;

                    }

                    if((req = WriteRequest.fromJSON(json))!=null){
                        Channel channel = ((WriteRequest) req).getChannel();
                        Message message = ((WriteRequest) req).getMessage();
                        synchronized (ClientHandler.class) {
                            channel.addToBoard(message);
                        }
                        toClient.println(new SuccessResponse("Message added to " + channel.getName()));
                        continue;
                    }


                    if((req = SubscribeRequest.fromJSON(json))!=null){
                        User user = ((SubscribeRequest) req).getUser();
                        Channel channel = ((SubscribeRequest) req).getChannel();
                        synchronized (ClientHandler.class) {
                            channel.addToSubscribers(user);
                            user.addToChannels(channel);
                        }
                        toClient.println(new SuccessResponse("You have subscribed to " + channel.getName()));
                        continue;
                    }

                    // quit request
                    if (QuitRequest.fromJSON(json) != null) {
                        toClient.println(new QuitResponse());
                        synchronized (ClientHandler.class){
                            persistChannelsToFile();
                        }
                        toClient.close();
                        fromClient.close();
                        return;
                    }

                    // error response acknowledging illegal request
                    toClient.println(new ErrorResponse("ILLEGAL REQUEST"));

                };

            }catch (IOException e) {
                System.out.println("Exception while connected");
                System.out.println(e.getMessage());
            }
        }

        void retrieveChannelsFromFile(){
            File file = new File(channelsPath);

            try {
                FileInputStream in = new FileInputStream(file);
                ObjectInputStream io = new ObjectInputStream(in);

                while(in.available() > 0) {
                    Channel channel = (Channel)io.readObject();
                    channels.add(channel);
                }

                in.close();
                io.close();
            } catch (FileNotFoundException var6) {
                System.out.println("File not found.");
            } catch (ClassNotFoundException var7) {
                System.out.println("Class not found.");
            } catch (IOException var8) {
                System.out.println("Something went wrong");
            }

        }


        void persistChannelsToFile(){

            File file = new File(channelsPath);

            try {
                FileOutputStream fot = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fot);
                Iterator channelsItr = channels.iterator();

                while(channelsItr.hasNext()) {
                    Channel channel = (Channel)channelsItr.next();
                    out.writeObject(channel);
                }

                System.out.println("Objects successfully written to disk!");
                fot.close();
                out.close();
            } catch (FileNotFoundException var7) {
                System.out.println("File cannot be found");
            } catch (IOException var8) {
                System.out.println("Something went wrong");
            }

        }
    }




}
