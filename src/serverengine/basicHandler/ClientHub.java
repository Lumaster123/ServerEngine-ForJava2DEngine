package serverengine.basicHandler;

import engine.connection.Packet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverengine.client.Client;
import serverengine.client.ClientListener;
import serverengine.engineTools.ThreadHandler;

public class ClientHub implements ClientListener{
    
    ArrayList<Client> clients;
    
    public ClientHub(){
        clients = new ArrayList<>();
        System.out.println("ClientHub created");
    }
    
    public void addClient(Client client){
        client.addListener(this);
        clients.add(client);
        System.out.println(client.toString()+" added to ClientHub");
        
        
        
        if(clients.size() == 2){
            for (int i = 0; i < 2; i++) {
                Packet packet = new Packet("player "+(i+1));
                clients.get(i).preparePacket(packet);
                sendBroadcast(clients.get(i), packet);
            }
        }
    }
    
    private void sendBroadcast(Client client, Packet packet){
        ArrayList<Client> clients = (ArrayList<Client>) this.clients.clone();
        clients.remove(client);
        
        for (Client client1 : clients) {
            Packet p = packet.clone();
            client1.preparePacket(p);
            client1.sendPacket(p);
        }
    }

    @Override
    public void receivedInput(Client client, Packet packet) {
        sendBroadcast(client, packet);
    }
    
}
