/*
 * Class ClientThread.
 */
package jhelp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides a network connection between end client of
 * {@link jhelp.Client} type and {@link jhelp.Server} object. Every object of
 * this class may work in separate thread.
 *
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 * @version 1.0
 * @see jhelp.Client
 * @see jhelp.Server
 */
public class ClientThread implements JHelp, Runnable {

    private Server server;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Data data;

    /**
     * Creates a new instance of Client
     *
     * @param server reference to {@link Server} object.
     * @param socket reference to {@link java.net.Socket} object for connection
     * with client application.
     */
    public ClientThread(Server server, Socket socket) {
        System.out.println("MClient: constructor");
        this.server = server;
        this.clientSocket = socket;
    }

    /**
     * The method defines main job cycle for the object.
     */
    public void run() {
        System.out.println("MClient: run");
        if (connect() == OK) {
            try {
                while (true) {
                    if (clientSocket.isClosed()) {
                        break;
                    }
                    data = (Data) input.readObject();
                    if (data.getOperation() == DISCONNECT) {
                        break;
                    }
                    data = server.getData(data);
                    output.writeObject(data);
                }
            } catch (IOException | ClassNotFoundException ex) {
                showMessage("run(): " + ex.getMessage());
            }
            disconnect();
        }

    }

    /**
     * Opens input and output streams for data interchanging with client
     * application. The method uses default parameters.
     *
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    public int connect() {
        System.out.println("MClient: connect");
        int flag = OK;
        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            flag = ERROR;
            showMessage("connect(): " + ex.getMessage());
        } finally {
            return flag;
        }
    }

    /**
     * Opens input and output streams for data interchanging with client
     * application. This method uses parameters specified by parameter
     * <code>args</code>.
     *
     * @param args defines properties for input and output streams.
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    public int connect(String[] args) {
        System.out.println("MClient: connect");
        return JHelp.OK;
    }

    /**
     * Transports {@link Data} object from client application to {@link Server}
     * and returns modified {@link Data} object to client application.
     *
     * @param data {@link Data} object which was obtained from client
     * application.
     * @return modified {@link Data} object
     */
    public Data getData(Data data) {
        System.out.println("Client: getData");
        return null;
    }

    /**
     * The method closes connection with client application.
     *
     * @return error code. The method returns {@link JHelp#OK} if input/output
     * streams and connection with client application was closed successfully,
     * otherwise the method returns {@link JHelp#ERROR}.
     */
    public int disconnect() {
        System.out.println("MClient: disconnect");
        try {
            clientSocket.close();
        } catch (IOException ex) {
            showMessage("disconnect(): " + ex.getMessage());
        }
        return JHelp.OK;
    }

    private void showMessage(String msg) {
        System.out.println("MClientError: " + msg);
    }
}
