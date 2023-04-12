package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import tage.networking.server.GameConnectionServer;
import tage.networking.server.IClientInfo;

public class GameServerUDP extends GameConnectionServer<UUID> {

    public GameServerUDP(int localPort) throws IOException {
        super(localPort, ProtocolType.UDP);
    }

    @Override
    public void processPacket(Object o, InetAddress senderIP, int senderPort) {
        String message = (String) o;
        String[] msgTokens = message.split(",");

        if (msgTokens.length > 0) {
            if (msgTokens[0].compareTo("join") == 0) {
                try {
                    IClientInfo ci;
                    ci = getServerSocket().createClientInfo(senderIP, senderPort);
                    UUID clientID = UUID.fromString(msgTokens[1]);
                    addClient(ci, clientID);
                    sendJoinedMessage(clientID, true);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //format: create, localid, (x,y,z)
            if (msgTokens[0].compareTo("create") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String[] pos = {msgTokens[2], msgTokens[3], msgTokens[4]};
                sendCreateMessages(clientID, pos);
                sendWantsDetailsMessage(clientID);
                }
            
            if (msgTokens[0].compareTo("bye") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                sendByeMessages(clientID);
                removeClient(clientID);
            }
            //case where server receives a DETAILS-FOR message
            if (msgTokens[0].compareTo("dsfr") == 0) {

            }
            //case where server receives a MOVE message
            if (msgTokens[0].compareTo("move") == 0) {

            }
            
        }
    }

    private void sendByeMessages(UUID clientID) {
        try {
            String message = new String("Goodbye!" + clientID.toString());

            sendPacket(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendWantsDetailsMessage(UUID clientID) {
    }

    private void sendCreateMessages(UUID clientID, String[] pos) {
        try
        { 
            String message = new String("create," + clientID.toString());
            message += "," + pos[0];
            message += "," + pos[1];
            message += "," + pos[2];
            forwardPacketToAll(message, clientID);
        }
        catch (IOException e) { 
            e.printStackTrace();
        }
    }

    private void sendJoinedMessage(UUID clientID, boolean success) {
        try { 
            String message = new String("join,");
            if (success) {
                 message += "success"; 
            }
            else {
                message += "failure";
            }
            sendPacket(message, clientID);
        }
        catch (IOException e) {
             e.printStackTrace();
        }
    }

}
