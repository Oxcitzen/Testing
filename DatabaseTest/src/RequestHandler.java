import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class handles the requests received from the client
 */

public class RequestHandler implements Runnable {

    private final Socket clientSocket;
    private static Database database;

    public RequestHandler(Socket clientSocket) {
        Objects.requireNonNull(clientSocket, "the specified client is null");

        this.clientSocket = clientSocket;
    }

    /* saves the user we got from the client */
    public String saveUser(User user) {
        String add;
        boolean added;

        Objects.requireNonNull(user, "The given user is null");
        added = database.addObject(user);

        if (added) {
            add = "success";
        } else {
            add = "fail";
        }

        return add;
    }

    /* removes the user we got from the client */
    public String removeUser(String userIdentifier) {
        String removed;

        Objects.requireNonNull(userIdentifier, "The given user identifier is null");
        User user = database.removeObject(userIdentifier);

        if (user == null) {
            removed = "failed"; //nothing's removed
        } else {
            removed = "success"; //user was removed
        }

        return removed;
    }

    /* find the user in the database */
    public User getUser(String userIdentifier) {

        Objects.requireNonNull(userIdentifier, "The given user identifier is null");
        User user = database.findObject(userIdentifier);

        return user; //returns null if the user isn't found
    }

    /* saves the profile we got from the client */
    public String saveProfile (Profile profile) {
        String add;
        boolean added;

        Objects.requireNonNull(profile, "The given profile is null");
        added = database.addProfile(profile);

        if (added) {
            add = "success";
        } else {
            add = "fail";
        }

        return add;
    }

    /* removes the profile we got from the client*/
    public String removeProfile (String profileId) {
        String remove;
        boolean removed;

        Objects.requireNonNull(profileId, "The given profileId is null");
        removed = database.removeProfile(profileId);

        if (removed) {
            remove = "success";
        } else {
            remove = "fail";
        }

        return remove;
    }

    /* Reads the request from the client, gets the response, and writes/sends it back to the client */
    @Override
    public void run() {
        String request;

        try (var inputStream = this.clientSocket.getInputStream(); //reads in the input stream
             var reader = new BufferedReader(new InputStreamReader(inputStream)); //reads the input stream
             var outputStream = this.clientSocket.getOutputStream(); //creates an output stream
             var writer = new BufferedWriter(new OutputStreamWriter(outputStream))) { //writes the output stream

            /* reads in the database info */
            try (var inputStreamDatabase = new ObjectInputStream(new FileInputStream("objects.ser"))) {
                List<User> objects;

                objects = (List<User>) inputStreamDatabase.readObject();

                database = new Database(objects);
            } catch (IOException | ClassNotFoundException e) {
                database = new Database();
            } //end try-catch
            /* end of database reading */

            ObjectInputStream ois = new ObjectInputStream(this.clientSocket.getInputStream());
            ArrayList<Object> input;

            input = (ArrayList<Object>) ois.readObject(); //reads the email sent
            Objects.requireNonNull(input, "Input was invalid");

            String firstInput = (String) input.get(0);

            if (firstInput.equalsIgnoreCase("login")) { //if user wants to log in

                User user = this.getUser((String) input.get(1)); //user is null if the user isn't in the database

                ObjectOutputStream oos = new ObjectOutputStream(this.clientSocket.getOutputStream());
                oos.flush();

                oos.writeObject(user); //sends the object user to the client
                oos.flush();
                System.out.printf("Sent %s to the client\n", user.toString());

                oos.close();

            } else if (firstInput.equalsIgnoreCase("signup")) { //if user wants to sign up
                String signUpSuccessful;

                User userToAdd = (User) input.get(1); //reads the user sent

                signUpSuccessful = this.saveUser(userToAdd);

                writer.write(signUpSuccessful); //says "success" if added successfully; "failed" if not
                writer.newLine();
                writer.flush();

            } else if (firstInput.equalsIgnoreCase("deleteAccount")) {  //if user wants to delete account
                String userId = reader.readLine(); //reads the email given
                Objects.requireNonNull(userId, "Input was invalid");

                String successOrFail = this.removeUser(userId);

                writer.write(successOrFail); //says "success" if deleted successfully; "failed" if not
                writer.newLine();
                writer.flush();

            } else if (firstInput.equalsIgnoreCase("createProfile")) { //if user wants to create a profile
                String profileCreateSuccess;

                ois = new ObjectInputStream(this.clientSocket.getInputStream());
                Objects.requireNonNull(ois, "Input was invalid");
                Profile profileToAdd = (Profile) ois.readObject(); //reads the profile sent

                profileCreateSuccess = this.saveProfile(profileToAdd);

                writer.write(profileCreateSuccess); //says "success" if added successfully; "failed" if not
                writer.newLine();
                writer.flush();

            } else if (firstInput.equalsIgnoreCase("deleteProfile")) { //if user wants to delete a profile
                String profileId = reader.readLine();
                Objects.requireNonNull(profileId, "Input was invalid");

                String successOrFail = this.removeProfile(profileId);

                writer.write(successOrFail); //says "success" if deleted successfully; "failed" if not
                writer.newLine();
                writer.flush();
            }

            /* writes to the database the updated info */
            try (var outputStreamDatabase = new ObjectOutputStream(new FileOutputStream("objects.ser"))) {
                List<User> objects;

                objects = database.getObjects();

                outputStreamDatabase.writeObject(objects);
            } catch (IOException e) {
                e.printStackTrace();
            } //end try-catch
            /* end of database writing */

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "RequestHandler{" +
                "clientSocket=" + clientSocket +
                '}';
    }
}
