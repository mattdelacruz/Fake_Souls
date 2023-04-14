package a3.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import org.joml.Vector3f;

import a3.MyGame;
import tage.networking.client.GameConnectionClient;

public class ProtocolClient extends GameConnectionClient {
    private MyGame game;
    private UUID id;
    private GhostManager ghostManager;

    public ProtocolClient(InetAddress remoteAddr, int remotePort,
            ProtocolType protocolType, MyGame game) throws IOException {
        super(remoteAddr, remotePort, protocolType);
        this.game = game;
        this.id = UUID.randomUUID();
        ghostManager = game.getGhostManager();
    }

    @Override
    protected void processPacket(Object msg) {
        String strMessage = (String) msg;
        String[] msgTokens = strMessage.split(",");

        if (msgTokens.length > 0) {
            if (msgTokens[0].compareTo("join") == 0) {
                if (msgTokens[1].compareTo("success") == 0) {
                    System.out.println("connection is a success");
                    game.setIsConnected(true);
                    sendCreateMessage(game.getPlayer().getLocalLocation());
                }
                if (msgTokens[1].compareTo("failure") == 0) {
                    System.out.println("connection is a failure");
                    game.setIsConnected(false);
                }
            }

            if (msgTokens[0].compareTo("bye") == 0) {
                UUID ghostID = UUID.fromString(msgTokens[1]);
                ghostManager.removeGhostAvatar(ghostID);
            }

            if ((msgTokens[0].compareTo("dsfr") == 0) || (msgTokens[0].compareTo("create") == 0)) {
                UUID ghostID = UUID.fromString(msgTokens[1]);
                Vector3f ghostPosition = new Vector3f(
                        Float.parseFloat(msgTokens[2]),
                        Float.parseFloat(msgTokens[3]),
                        Float.parseFloat(msgTokens[4]));

                try {
                    ghostManager.createGhostAvatar(ghostID, ghostPosition);
                } catch (IOException e) {
                    System.out.println("error creating ghost avatar");
                }
            }
            if (msgTokens[0].compareTo("wsds") == 0) {
                UUID ghostID = UUID.fromString(msgTokens[1]);
                sendDetailsForMessage(ghostID, game.getPlayer().getLocalLocation());

            }
            if (msgTokens[0].compareTo("move") == 0) {

                UUID ghostID = UUID.fromString(msgTokens[1]);

                // Parse out the position into a Vector3f
                Vector3f ghostPosition = new Vector3f(
                        Float.parseFloat(msgTokens[2]),
                        Float.parseFloat(msgTokens[3]),
                        Float.parseFloat(msgTokens[4]));

                ghostManager.updateGhostAvatar(ghostID, ghostPosition);
            }
        }
    }

    private void sendDetailsForMessage(UUID remoteID, Vector3f pos) {
        try {
            String message = new String("dsfr," + remoteID.toString() + "," + id.toString());
            message += "," + pos.x();
            message += "," + pos.y();
            message += "," + pos.z();

            sendPacket(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendJoinMessage() {
        try {
            sendPacket(new String("join," + id.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCreateMessage(Vector3f pos) {
        // changed Vector3 to Vector3f, Vector3 is on the doc
        //
        try {
            String message = new String("create," + id.toString());
            message += "," + pos.x + "," + pos.y + "," + pos.z;
            sendPacket(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMoveMessage(Vector3f pos) {
        try {
            String message = new String("move," + id.toString());
            message += "," + pos.x();
            message += "," + pos.y();
            message += "," + pos.z();

            sendPacket(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getID() {
        return id;
    }
}
