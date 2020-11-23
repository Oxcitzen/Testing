import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class is just for testing
 */

public class ClientTest {
    public static void main(String[] args) {
        BufferedReader inputReader;
        String hostName;
        String portString;
        int port;
        String request;
        String response = null;
        String input;
        ArrayList<Object> sendsStuff = new ArrayList<>();

        inputReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter the server's host name: ");

        try {
            hostName = inputReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();

            return;
        } //end try catch

        if (hostName == null) {
            return;
        } //end if

        System.out.print("Enter the server's port: ");

        try {
            portString = inputReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();

            return;
        } //end try catch

        if (portString == null) {
            return;
        } //end if

        try {
            port = Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            System.out.println();

            System.out.println("Error: Invalid port!");

            return;
        } //end try catch

        System.out.print("Enter a request: ");

        try {
            request = inputReader.readLine();
            sendsStuff.add(request);

        } catch (IOException e) {
            e.printStackTrace();

            return;
        } //end try catch

        if (request.equalsIgnoreCase("login")) {
            System.out.println("email?");
            try {
                input = inputReader.readLine();
                sendsStuff.add(input);

            } catch (IOException e) {
                e.printStackTrace();

                return;
            } //end try catch
        } else if (request.equalsIgnoreCase("signup")) {
            System.out.println("Name?");
            try {
                String name = inputReader.readLine();
                String email = inputReader.readLine();
                String birthday = inputReader.readLine();
                String gender = inputReader.readLine();

                User user = new User(name, email, gender, birthday);
                sendsStuff.add(user);
            } catch (IOException e) {
                e.printStackTrace();

                return;
            } //end try catch
        }

        while (request != null) {
            try (var socket = new Socket(hostName, port);
                 var outputStream = socket.getOutputStream();
                 var inputStream = socket.getInputStream();
                 var socketWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                 var socketReader = new BufferedReader(new InputStreamReader(inputStream))) {



                var objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(sendsStuff);

                objectOutputStream.flush();

                ObjectInputStream ois = new ObjectInputStream(inputStream);
                while (ois.available() > 0) {
                    response = (String) ois.readObject();
                }

                System.out.println();

                System.out.printf("Response from the server: %s%n%n", response);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();

                break;
            } //end try catch

            System.out.print("Enter a request: ");

            try {
                request = inputReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();

                break;
            } //end try catch
        } //end while

        /*
         * Host Name: data.cs.purdue.edu
         * Port: 33475
         */
    } //main
}

