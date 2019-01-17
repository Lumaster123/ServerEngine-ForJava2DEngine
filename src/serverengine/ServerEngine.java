package serverengine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverengine.basicHandler.ClientHub;
import serverengine.client.Client;

public class ServerEngine {

    private static ArrayList<Client> clients;
    private static ArrayList<ClientHub> hubs;
    private static int id = 1;
    
    private static ServerSocket server;
    private static int port = 33333;
    
    public static void main(String[] args) {
        
        
        
        clients = new ArrayList<>();
        hubs = new ArrayList<>();
        
        
        try {
            server = new ServerSocket(port);
            
            Window window = new Window(server);
            window.setVisible(true);
            
            while(true){
                
                Socket socket = server.accept();
                Client client = new Client(socket);
                clients.add(client);
                window.clientItemNameList.addElement(client.toString());
                
                if(clients.size() % 2 == 0 && clients.size() > 0){
                    ClientHub hub = new ClientHub();
                    hub.addClient(clients.get(clients.size()-2));
                    hub.addClient(clients.get(clients.size()-1));
                }
                
            }
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
    }
    
}
