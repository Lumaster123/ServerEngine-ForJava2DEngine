package serverengine.client;

import engine.connection.Packet;

public interface ClientListener {
    
    public void receivedInput(Client client, Packet packet);
    
}
