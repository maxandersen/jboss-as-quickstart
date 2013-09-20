/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.as.quickstarts.kitchensink.websocket;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;

/**
 *
 * @author petrjiricka
 */
@javax.websocket.server.ServerEndpoint(
        value = "/registration", 
        encoders = {MemberEncoder.class})
public class RegistrationEndpoint {

    @OnMessage
    public String onMessage(String message, Session s) {
        cr.handleLoginRequest(s);
        return null;
    }

    @OnClose
    public void onClose(Session session) {
        cr.removeArc(session);
    }

    @Inject
    ClientRegistry cr;

}
