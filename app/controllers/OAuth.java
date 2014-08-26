package controllers;

import play.*;
import play.mvc.*;
import play.libs.ws.*;
import play.libs.F.Function;
import play.libs.F.Promise;

import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;

public class OAuth extends Controller {
	private static String URL = "https://graph.facebook.com/oauth/authorize";
	//https://www.facebook.com/dialog/oauth?client_id=1524941771068708&secret=b4310b646a5e589261a283f268dbbf3e&redirect_uri=https://localhost:9443t&display=popup
	private static String APP_ID = "1524941771068708";
	private static String SECRET = "b4310b646a5e589261a283f268dbbf3e"; 

	public static Promise<Result> sendRequest() {
		WSRequestHolder holder = WS.url(URL);
		WSRequestHolder complexHolder = 
			holder.setQueryParameter("client_id", APP_ID)
				  .setQueryParameter("secret", SECRET)
				  .setQueryParameter("redirect_uri", "https://localhost:9443"); 

	    //Promise<WSResponse> responsePromise = complexHolder.get();

		/*Promise<JsonNode> jsonPromise = complexHolder.get().map(
    		new Function<WSResponse, JsonNode>() {
        		public JsonNode apply(WSResponse response) {
            		JsonNode json = response.asJson();
            		return json;
        		}
    		}
		);*/

        
        Promise<JsonNode> json = complexHolder.get().map(resp -> ok(resp.asJson()));

        Iterator<JsonNode> elements = json.elements();

        return complexHolder.get().map(resp -> (Result) ok(resp.getBody()));
	}

	public static Result showResponse() {
		return ok(request().toString());
	}
}