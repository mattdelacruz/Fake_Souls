package server;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.ClientInfoStatus;
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
                    System.out.println("Join request received from - " + clientID.toString());
                    sendJoinedMessage(clientID, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // format: create, localid, (x,y,z)
            if (msgTokens[0].compareTo("create") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String[] pos = { msgTokens[2], msgTokens[3], msgTokens[4] };
                sendCreateMessages(clientID, pos);
                sendWantsDetailsMessage(clientID);
            }

            if (msgTokens[0].compareTo("bye") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                System.out.println("Exit request received from - " + clientID.toString());
                sendByeMessages(clientID);
                removeClient(clientID);
            }
            // case where server receives a DETAILS-FOR message
            if (msgTokens[0].compareTo("dsfr") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                UUID remoteID = UUID.fromString(msgTokens[2]);
                String[] pos = { msgTokens[3], msgTokens[4], msgTokens[5] };
                sendDetailsForMessage(clientID, remoteID, pos);
            }
            // case where server receives a MOVE message
            if (msgTokens[0].compareTo("move") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String[] pos = { msgTokens[2], msgTokens[3], msgTokens[4] };
                sendMoveMessages(clientID, pos);
            }

            // case where server receives an ANIMATION message
            if (msgTokens[0].compareTo("animation") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String animation = msgTokens[2];
                sendAnimationMessage(clientID, animation);
            }

            // case where server receives an YAW message
            if (msgTokens[0].compareTo("yaw") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String rotation = msgTokens[2];
                sendYawMessage(clientID, rotation);
            }

            if (msgTokens[0].compareTo("attack") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String isAttack = msgTokens[2];
                sendAttackMessage(clientID, isAttack);
            }

            if (msgTokens[0].compareTo("damage") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String damage = msgTokens[2];
                sendDamageMessage(clientID, damage);
            }

            if (msgTokens[0].compareTo("health") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String health = msgTokens[2];
                sendHealthMessage(clientID, health);
            }

            if (msgTokens[0].compareTo("owner") == 0) {
                UUID clientID = UUID.fromString(msgTokens[1]);
                sendOwnerMessage(clientID);
            }
        }
    }

    private void sendDamageMessage(UUID clientID, String damage) {
        try {
            String message = new String("damage," + clientID.toString());
            message += "," + damage;
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendHealthMessage(UUID clientID, String health) {
        try {
            String message = new String("health," + clientID.toString());
            message += "," + health;
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDetailsForMessage(UUID clientID, UUID remoteID, String[] pos) {
        try {
            String message = new String("dsfr," + remoteID.toString());
            message += "," + pos[0];
            message += "," + pos[1];
            message += "," + pos[2];
            sendPacket(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMoveMessages(UUID clientID, String[] pos) {
        try {
            String message = new String("move," + clientID.toString());
            message += "," + pos[0];
            message += "," + pos[1];
            message += "," + pos[2];
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAnimationMessage(UUID clientID, String animation) {
        try {
            String message = new String("animation," + clientID.toString());
            message += "," + animation;
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendYawMessage(UUID clientID, String rotation) {
        try {
            String message = new String("yaw," + clientID.toString());
            message += "," + rotation;
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendByeMessages(UUID clientID) {
        try {
            String message = new String("bye," + clientID.toString());

            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendWantsDetailsMessage(UUID clientID) {
        try {
            String message = new String("wsds," + clientID.toString());
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendOwnerMessage(UUID clientID) {
        try {
            String message = new String("owner," + clientID.toString());
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCreateMessages(UUID clientID, String[] pos) {
        try {
            String message = new String("create," + clientID.toString());
            message += "," + pos[0];
            message += "," + pos[1];
            message += "," + pos[2];
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendJoinedMessage(UUID clientID, boolean success) {
        try {
            String message = new String("join,");
            if (success) {
                message += "success";
            } else {
                message += "failure";
            }
            sendPacket(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAttackMessage(UUID clientID, String isAttack) {
        try {
            String message = new String("attack," + clientID.toString());
            message += "," + isAttack;
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
