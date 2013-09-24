/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.as.quickstarts.kitchensink.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jboss.as.quickstarts.kitchensink.model.Member;

/**
 *
 * @author petrjiricka
 */
@ServerEndpoint(
        value = "/registration", 
        encoders = {MemberEncoder.class}) 
public class RegistrationEndpoint {

	@OnOpen
	public void onOpen(Session peer) {
        //peers.add(peer);
		addArc(peer);
    }

    @OnMessage
    public String onMessage(String message, Session s) {
    	System.out.println("received: " + message);
        handleLoginRequest(s);
       
        return "received!";
    }

    @OnClose
    public void onClose(Session session) {
        removeArc(session);
    }

    /*
     * List of remote clients (Peers)
     */
    static private final List<Session> arcList = new ArrayList<>();

    public synchronized void addArc(Session arc) {
    	System.out.println(arc + " added");
        arcList.add(arc);
    }

    synchronized void removeArc(Session arc) {
    	System.out.println(arc + " removed");
        arcList.remove(arc);
    }

    /*
     * New user logs into the auction.
     */
    void handleLoginRequest(Session arc) {
    	
        //arc.getUserProperties().put("name", messsage.getData());
        if (!getRemoteClients().contains(arc)) {
            this.addArc(arc);
        }                
    }
    
    public void sendRegisteredMessage(Member m) {
        for (Session arc : getRemoteClients()) {
            try {
                arc.getBasicRemote().sendObject(m);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
                Logger.getLogger(RegistrationEndpoint.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    private List<Session> getRemoteClients() {
        return Collections.unmodifiableList(arcList);
    }

}
