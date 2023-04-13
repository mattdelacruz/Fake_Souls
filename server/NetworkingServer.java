package server;

import java.io.IOException;
import java.net.BindException;

public class NetworkingServer {

    private GameServerUDP thisUDPServer;

    public NetworkingServer(int serverPort, String protocol) throws BindException {
        try {
            if (protocol.toUpperCase().compareTo("UDP") == 0) {
                thisUDPServer = new GameServerUDP(serverPort);
            } else {
                System.err.println("Unsupported protocol: " + protocol);
                System.exit(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws BindException, NumberFormatException {
        if (args.length > 1) {
            NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]), args[1]);
        }
    }

}
