package serverengine.client;

import engine.connection.Packet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverengine.basicHandler.ClientHub;
import serverengine.engineTools.ThreadHandler;

public class Client {
    
    private Socket socket;
    
    private static int ID_COUNT = 0;
    private final int id;
    
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    
    private ArrayList<ClientListener> listenerList;
    
    public Client(Socket socket){
        listenerList = new ArrayList<>();
        
        id = ID_COUNT;
        ID_COUNT++;
        
        this.socket = socket;
        initializeStreams();
        
        startInputListener();
    }
    
    private void initializeStreams(){
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addListener(ClientListener listener){
        listenerList.add(listener);
    }
    
    private void startInputListener(){
        Client client = this;
        ThreadHandler.invoke(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try {
                            Object input = inputStream.readObject();
                            if(input instanceof Packet){
                                System.out.println("Server received packet from "+this.toString());
                                for (ClientListener clientListener : listenerList) {
                                    clientListener.receivedInput(client, (Packet) input);
                                }
                            }else{
                                System.err.println("Received an object, which is not a Packet!");
                            }
                        } catch (IOException | ClassNotFoundException ex) {
                            Logger.getLogger(ClientHub.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
    }
    
    public void sendPacket(Packet packet){
        try {
            outputStream.writeObject(packet);
            System.out.println("Server sent Packet to "+this.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void preparePacket(Packet packet){
        packet.preparePacket(socket.getInetAddress(), socket.getLocalAddress());
    }
 
    @Override
    public String toString(){
        return "Client-"+id;
    }
    
}
