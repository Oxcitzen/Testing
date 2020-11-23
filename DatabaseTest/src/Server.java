import javax.swing.*;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * This is the server of the program
 */

public final class Server {
    /**
     * The server socket of this server.
     */
    ServerSocket serverSocket;
    Socket socket;

    /**
     * Constructs a newly allocated {@code PowerServer} object with the specified port.
     *
     * @param port the port to be used in construction
     * @throws IllegalArgumentException if the specified port is negative
     * @throws IOException if an I/O error occurs
     */
    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    } //PowerServer

    public Server(Socket socket) {
        this.socket = socket;

    }

    /**
     * Serves the clients that connect to this server.
     */
    public void serveClients() {
        InetAddress address;
        String hostName;
        int port;
        Socket clientSocket;
        RequestHandler handler;
        Thread handlerThread;
        int clientCount = 0;

        try { //gets the address of the machine this server is running on
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();

            return;
        } //end try catch

        hostName = address.getCanonicalHostName(); //gets the hostName of the machine the server is running on

        port = this.serverSocket.getLocalPort(); //gets the port number of the machine the server is running on

        //OPEN FOR BUSINESS
        System.out.printf("<Host Name: %s, Port: %d>%n", hostName, port);

        System.out.println("<Now serving clients...>");

        /* Start getting clients */
        while (true) {
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                break;
            } //end try catch

            handler = new RequestHandler(clientSocket);
            //gets the request of client and response of server (check RequestHandler.java)

            handlerThread = new Thread(handler); //this is allows multiple clients

            handlerThread.start();

            System.out.printf("<Client #%d connected...>%n", clientCount);

            clientCount++;
        } //end while
    } //serveClients

    /**
     * Returns the hash code of this server.
     *
     * @return the hash code of this server
     */
    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;

        result = result * prime + Objects.hashCode(this.serverSocket);

        return result;
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this server. {@code true} is returned if and only if
     * the specified object is an instance of {@code PowerServer} and its server socket is equal to this server's.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this server and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof Server) {
            return Objects.equals(this.serverSocket, ((Server) object).serverSocket);
        } else {
            return false;
        } //end if
    } //equals

    /**
     * Returns the {@code String} representation of this server. The returned {@code String} consists of this server's
     * server socket surrounded by this class' name and square brackets ("[]").
     *
     * @return the {@code String} representation of this server
     */
    @Override
    public String toString() {
        String format = "PowerServer[serverSocket=%s]";

        return String.format(format, this.serverSocket);
    } //toString

    /**
     * Creates and starts an instance of {@code PowerServer}.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server server;

        try {
            server = new Server(0); //server port 0 means the port is going to have a random port number
        } catch (IOException e) {
            e.printStackTrace();

            return;
        } //end try catch

        server.serveClients();
    } //main


}