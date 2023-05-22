package com.LiveChat.LiveChat.Controller;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.LiveChat.LiveChat.VO.Client;

@Controller
@ServerEndpoint("/websocket")
public class MessageController extends Socket {
    private static final List<Session> session = new ArrayList<Session>();
    private static final List<Client> ClientList = new ArrayList<>();

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @OnOpen
    public void open(Session newClient) {
        System.out.println("connected");
        session.add(newClient);
        System.out.println(newClient.getId());
    }
    
    @OnClose
    public void close(Session Client){
        System.out.println("disconnected");
        session.remove(Client);
        System.out.println(Client.getId());
    }

    @OnMessage
    public void getMsg(Session recieveSession, String msg) {
        for (int i = 0; i < session.size(); i++) {
            if (!recieveSession.getId().equals(session.get(i).getId())) {
                try {
                    session.get(i).getBasicRemote().sendText("상대 : "+msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    session.get(i).getBasicRemote().sendText("나 : "+msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // @OnMessage
    // public void processMessage(Session ClientSession, String message) {
    //     // Find the corresponding Client
    //     Client currentClient = findClientBySession(ClientSession);

    //     if (currentClient == null) {
    //         // Create a new Client with the received message as the Clientname
    //         Client newClient = new Client(message);
    //         newClient.setSession(ClientSession);

    //         // Add the Client to the Client list
    //         ClientList.add(newClient);
    //     } else {
    //         // Process the message
    //         String formattedMessage = currentClient.getName() + ": " + message;

    //         // Send the message to other Clients
    //         sendToOtherClients(ClientSession, formattedMessage);
    //     }
    // }

    // private Client findClientBySession(Session ClientSession) {
    //     for (Client Client : ClientList) {
    //         if (Client.getSession().equals(ClientSession)) {
    //             return Client;
    //         }
    //     }
    //     return null;
    // }

    // private void sendToOtherClients(Session senderSession, String message) {
    //     for (Session session : session) {
    //         if (!session.equals(senderSession)) {
    //             try {
    //                 session.getBasicRemote().sendText(message);
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }
    // }
}