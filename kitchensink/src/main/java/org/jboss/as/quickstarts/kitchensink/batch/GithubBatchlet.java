package org.jboss.as.quickstarts.kitchensink.batch;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;

@Named
public class GithubBatchlet extends AbstractBatchlet {

	 @Inject
	 private MemberRepository repository;

	 
	@Override
	public String process() throws Exception {
		System.out.println("Running inside a batchlet "+ repository);
		
		List<Member> allMembers = repository.findAllOrderedByName();
		
		for (Member member : allMembers) {
			String avatar = repository.getAvatarURL(member.getId());
			if(avatar==null) {
			
				// lookp github user by name
				URLConnection connection = new URL("https://api.github.com/search/users?q=" + member.getName()).openConnection();
		        // accept header needs preview set for github to respond to queries.
				connection.addRequestProperty("Accept", "application/vnd.github.preview");
				System.out.println("Waiting to not overload github ratelimit...");
		        Thread.sleep(TimeUnit.MINUTES.toMillis(1)); // sleeping to not overload github
				
		        
		        try(InputStream stream = connection.getInputStream()) {
		        
		        	JsonReader reader = Json.createReader(stream);
		        	JsonObject jsonObject=reader.readObject();
		        	
		        	if(jsonObject.containsKey("items")) {
		        		JsonArray items = jsonObject.getJsonArray("items");
		        		if(items.size()>0) {
		        			avatar = items.getJsonObject(0).getString("avatar_url");
		        		}
		        	}
		        	
		        	if (avatar==null) {
		        		System.out.println("No avatar found for " + member.getName());
		        	} else {
		         		System.out.println("Avatar " +avatar + " found for " + member.getName());
		         		repository.addAvatarURL(member.getId(), avatar);
		        	}
		        } catch(Exception e) {
		        	//ignore
		        	System.out.println(e.getMessage());
		        }
			}
		}
		 	
		return null;
	}

}
