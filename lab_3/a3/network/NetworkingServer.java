package a3.network;

import java.io.IOException;

public class NetworkingServer {

    private GameServerUDP thisUDPServer;

    public NetworkingServer(int serverPort, String protocol) {
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

    public static void main(String[] args) {
        if (args.length > 1) {
            NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]), args[1]);
        }
    }
    
}
