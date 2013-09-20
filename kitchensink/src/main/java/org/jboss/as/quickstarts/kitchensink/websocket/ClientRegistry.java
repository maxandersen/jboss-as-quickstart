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
import javax.inject.Singleton;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import org.jboss.as.quickstarts.kitchensink.model.Member;

/**
 *
 * @author petrjiricka
 */
@Singleton
public class ClientRegistry {

    /*
     * List of remote clients (Peers)
     */
    private final List<Session> arcList = new ArrayList<>();

    private synchronized void addArc(Session arc) {
        arcList.add(arc);
    }

    synchronized void removeArc(Session arc) {
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
