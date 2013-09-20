/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.as.quickstarts.kitchensink.websocket;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import org.jboss.as.quickstarts.kitchensink.model.Member;

/**
 *
 * @author petrjiricka
 */
class MemberEncoder implements Encoder.TextStream<Member>  {

    // Create JSON writer factory with pretty printing enabled
    private static final Map<String, Boolean> config;
    private static final JsonWriterFactory factory;
    static {
        config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, Boolean.TRUE);
        factory = Json.createWriterFactory(config);
    }

    @Override
    public void encode(Member object, Writer writer) throws EncodeException, IOException {
        final JsonObjectBuilder jobj = Json.createObjectBuilder();
        object.writeTo(jobj);
        try (JsonWriter jw = factory.createWriter(writer)) {
            jw.writeObject(jobj.build());
        }
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
    
}
