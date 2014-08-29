package controllers;

import play.*;
import play.mvc.*;
import play.libs.ws.*;
import play.libs.F.Function;
import play.libs.F.Promise;

import java.util.*;
import org.w3c.dom.*;

import com.fasterxml.jackson.databind.JsonNode;

public class OAuth extends Controller {
	//private static String URL = /*"https://www.facebook.com/dialog/oauth";//*/"https://graph.facebook.com/oauth/authorize";
    //private static String URL = "https://www.facebook.com/v2.1/dialog/oauth";
    private static String URL = "https://www.facebook.com/dialog/oauth";
    //private static String URL = "https://www.facebook.com/login.php";
	//https://www.facebook.com/dialog/oauth?client_id=1524941771068708&secret=b4310b646a5e589261a283f268dbbf3e&redirect_uri=https://localhost:9443t&display=popup
	private static String APP_ID = "1524941771068708";
	private static String SECRET = "b4310b646a5e589261a283f268dbbf3e"; 

    private static Map<String, String> cookiesJar = null;
    private static HashSet<WSCookie> jar = new HashSet<>();


	public static Result sendRequest() {
		WSRequestHolder holder = prepareHolder(URL, true);
		holder.setQueryParameter("client_id", APP_ID)
				  .setQueryParameter("secret", SECRET)
                  .setQueryParameter("response_type", "token")
				  .setQueryParameter("redirect_uri", "https://localhost:9443")
                  .setQueryParameter("noscript", "1");
        
        WSResponse response = handleResponse(holder.get().get(10000));

        //if (true) return ok(response.getBody());

        String body = response.getBody();

        String form = retreiveNode("<form", ">", body);

        String action = retreiveValue("action", form);

        String lgnrnd = retreiveValue("value", retreiveNode("lgnrnd", "/>", body));

        String lsd = retreiveValue("value", retreiveNode("lsd", "/>", body));

        holder = prepareHolder("https://facebook.com" + action, true);

        Logger.info("ACTION " + action);

        holder.setHeader("Content-Type", "application/x-www-form-urlencoded");
        holder.setHeader("Connection", "keep-alive");
        holder.setHeader("Cache-Control", "max-age=0");
        holder.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,* /*;q=0.8");
        holder.setHeader("Origin", "null");
        holder.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36");
        holder.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        holder.setHeader("Accept-Language", "en-US,en;q=0.8,uk;q=0.6");
        //holder.setHeader("Referer", "https://www.facebook.com/login.php?skip_api_login=1&api_key=1524941771068708&signed_next=1&next=https%3A%2F%2Fwww.facebook.com%2Fv2.1%2Fdialog%2Foauth%3Fredirect_uri%3Dhttps%253A%252F%252Flocalhost%253A9443%252Foauth%26response_type%3Dtoken%26client_id%3D1524941771068708%26ret%3Dlogin&cancel_uri=https%3A%2F%2Flocalhost%3A9443%2Foauth%3Ferror%3Daccess_denied%26error_code%3D200%26error_description%3DPermissions%2Berror%26error_reason%3Duser_denied%23_%3D_&display=page");

        StringBuilder paramStr = new StringBuilder();
        paramStr.append("lsd=");
        paramStr.append(lsd);
        paramStr.append("&api_key=");
        paramStr.append(APP_ID);
        paramStr.append("&display=page");
        paramStr.append("&enable_profile_selector=");
        paramStr.append("&legacy_return=1");
        paramStr.append("&profile_selector_ids=");
        paramStr.append("&skip_api_login=1");
        paramStr.append("&signed_next=1");
        paramStr.append("&trynum=1");
        paramStr.append("&timezone=");
        //paramStr.append("u_0_0");
        paramStr.append("&lgnrnd=");
        paramStr.append(lgnrnd);
        paramStr.append("&lgnjs=n");
        paramStr.append("&email=");
        paramStr.append("oleg.zhembrovsky%40fastforwardpro.com");
        paramStr.append("&pass=");
        paramStr.append("potato0face");
        //paramStr.append("&persistent=1");
        paramStr.append("&default_persistent=0");

        Logger.info("FORM");

        response = handleResponse(holder.post(paramStr.toString()).get(10000));

        return ok(response.getBody());

/*
        ///////////////////

        Logger.info("REDIRECT1 " + response.getAllHeaders().toString());

        if (true) return ok(response.getBody());

        ///////////////////

        int status = response.getStatus();

        int count = 1;

        while(status == 301 || status == 302) {
            String location = response.getHeader("Location");

            holder = WS.url(location).setFollowRedirects(false);
            response = holder.get().get(10000);

            String cs = cookieString(response.getCookies());

            Logger.info(count++ + cs);

            status = response.getStatus();
        }

        List<WSCookie> cookies = response.getCookies();

        //Logger.info("HEADERS " + response.getAllHeaders().toString());

        String cs = cookieString(cookies);

        String location = response.getHeader("Location");

        Logger.info("LOCATION1 " + location);

        Logger.info("COOKIES1 " + cs.toString());
        Logger.info("\n\n\n");

        holder = WS.url(location).setFollowRedirects(false);
        //Logger.info("HEADERS2 " + response.getAllHeaders().toString());

        String body = holder.get().map(resp -> (String) resp.getBody()).get(10000);

        if (true) return ok("" + response.getStatus());

        String form = retreiveNode("<form", ">", body);

        String action = retreiveValue("action", form);

        String lgnrnd = retreiveValue("value", retreiveNode("lgnrnd", "/>", body));

        String lsd = retreiveValue("value", retreiveNode("lsd", "/>", body));

        //Logger.info(lgnrnd);
        //Logger.info(form);

        holder = WS.url("https://facebook.com" + action).setFollowRedirects(false);

        response = holder.get().get(10000);

        String headers = maplistToString(response.getAllHeaders());

        Logger.info("ALLHEADERS " + headers);

        location = response.getHeader("Location");

        Logger.info("LOCATION2 " + location);

        if (response.getStatus() == 302 || response.getStatus() == 301) {
            holder = WS.url(location).setFollowRedirects(false);

            holder.setHeader("Content-Type", "application/x-www-form-urlencoded");
            holder.setHeader("Connection", "keep-alive");
            holder.setHeader("Cache-Control", "max-age=0");
            holder.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,* /*;q=0.8");
            holder.setHeader("Origin", "null");
            holder.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36");
            //holder.setHeader("Accept-Encoding", "gzip,deflate,sdch");
            holder.setHeader("Accept-Language", "en-US,en;q=0.8,uk;q=0.6");
            holder.setHeader("Cookie", cs.toString());

            StringBuilder paramStr = new StringBuilder();
            paramStr.append("lsd=");
            paramStr.append(lsd);
            paramStr.append("&api_key=");
            paramStr.append(APP_ID);
            paramStr.append("&display=page");
            paramStr.append("&enable_profile_selector=");
            paramStr.append("&legacy_return=1");
            paramStr.append("&profile_selector_ids=");
            paramStr.append("&skip_api_login=1");
            paramStr.append("&signed_next=1");
            paramStr.append("&trynum=1");
            paramStr.append("&timezone=");
            //paramStr.append("u_0_0");
            paramStr.append("&lgnrnd=");
            paramStr.append(lgnrnd);
            paramStr.append("&lgnjs=n");
            paramStr.append("&email=");
            paramStr.append("oleg.zhembrovsky@fastforwardpro.com");
            paramStr.append("&pass=");
            paramStr.append("potato0face");
            //paramStr.append("&persistent=1");
            paramStr.append("&default_persistent=0");

            response = holder.post(paramStr.toString()).get(10000);

            Logger.info("STATUS " + response.getStatus());

            body = holder
                .post(paramStr.toString())
                .map(resp -> (String) resp.getBody()).get(10000);

            headers = maplistToString(response.getAllHeaders());

            if (true) return ok(body);
        }

        //holder.setHeader("Cookie", setCookie);
        holder.setHeader("Cookie", cs.toString());

        /*String rawCookies = "datr=gkv8U5Z-tt0qiqIPcpEZk3X3; "
                        + "fr=0YMX5kCtNz4iLch3i.AWW5_AEQms9nqVThJdVTb-v7bBA.BT_EwN.5s.AAA.0.AWUXmASb; "
                        + "lu=SA40OAXCE6dPVbnRbea7h1Bw; "
                        + "noscript=1; "
                        + "reg_fb_gate=https://www.facebook.com/?stype=lo&jlou=AfdV3-sxPOpfA3ot-jt4cH3gaxdkdUjpcSvOQQ8VGB6dNomufPOfiIsVEysPdZVr7eew-YJGwz18auNfXW5rsUAd6IKWvkq4xncxhDXU3RxBmQ&smuh=9088&lh=Ac9tW1TgyR32Qg7N; "
                        + "reg_fb_ref=https://www.facebook.com/login.php?skip_api_login=1&api_key=1524941771068708&signed_next=1&next=https%3A%2F%2Fwww.facebook.com%2Fv2.1%2Fdialog%2Foauth%3Fredirect_uri%3Dhttps%253A%252F%252Flocalhost%253A9443%252Foauth%26response_type%3Dtoken%26client_id%3D1524941771068708%26ret%3Dlogin&cancel_uri=https%3A%2F%2Flocalhost%3A9443%2Foauth%3Ferror%3Daccess_denied%26error_code%3D200%26error_description%3DPermissions%2Berror%26error_reason%3Duser_denied%23_%3D_&display=page; "
                        + "x-src=/docs/facebook-login/manually-build-a-login-flow/v2.1|documentation_body_pagelet;";* /


        //holder.setHeader("Cookie", rawCookies);

        holder.setHeader("Content-Type", "application/x-www-form-urlencoded");
        holder.setHeader("Connection", "keep-alive");
        holder.setHeader("Cache-Control", "max-age=0");
        holder.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*\/*;q=0.8");
        holder.setHeader("Origin", "null");
        holder.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36");
        //holder.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        holder.setHeader("Accept-Language", "en-US,en;q=0.8,uk;q=0.6");

        StringBuilder paramStr = new StringBuilder();
        paramStr.append("lsd=");
        paramStr.append(lsd);
        paramStr.append("&api_key=");
        paramStr.append(APP_ID);
        paramStr.append("&display=page");
        paramStr.append("&enable_profile_selector=");
        paramStr.append("&legacy_return=1");
        paramStr.append("&profile_selector_ids=");
        paramStr.append("&skip_api_login=1");
        paramStr.append("&signed_next=1");
        paramStr.append("&trynum=1");
        paramStr.append("&timezone=");
        //paramStr.append("u_0_0");
        paramStr.append("&lgnrnd=");
        paramStr.append(lgnrnd);
        paramStr.append("&lgnjs=n");
        paramStr.append("&email=");
        paramStr.append("oleg.zhembrovsky%40fastforwardpro.com");
        paramStr.append("&pass=");
        paramStr.append("potato0face");
        //paramStr.append("&persistent=1");
        paramStr.append("&default_persistent=0");

        Logger.info("\n");
        Logger.info(paramStr.toString());

        /*complexHolder = 
            holder.setQueryParameter("lgnrnd", lgnrnd);*/

        /*String raw = "lsd=AVpdp0Ii&api_key=1524941771068708&display=page&enable_profile_selector=&legacy_return=1&profile_selector_ids=&skip_api_login=1&signed_next=1"
                    + "&trynum=1&timezone=&lgnjs=n&email=oleg.zhembrovsky%40fastforwardpro.com&pass=potato0face&default_persistent=0"
                    + "&login=%26%231059%3B%26%231074%3B%26%231110%3B%26%231081%3B%26%231090%3B%26%231080%3B&lgnrnd=" + lgnrnd;* /

        Map<String, Collection<String>> headmap = holder.getHeaders();

        Set<Map.Entry<String, Collection<String>>> entrySet = headmap.entrySet();

        StringBuilder entryStr = new StringBuilder();

        for(Map.Entry<String, Collection<String>> e : entrySet) {
            entryStr.append(e.getKey());
            entryStr.append(": ");

            Collection<String> coll = e.getValue();

            int i = 0;
            for(String s : coll) {
                entryStr.append(s);

                if (i < coll.size() - 1)
                    entryStr.append(", ");
            }

            entryStr.append(';');
        }

        Logger.info("REQUEST HEADERS " + entryStr.toString());
        Logger.info("--- REQUEST HEADERS ");

        response = holder.post(paramStr.toString()).get(10000);

        cookies = response.getCookies();

        cs = cookieString(cookies);

        Logger.info("COOKIES2 " + cs);
        Logger.info("\n\n\n");



        body = holder
                .post(paramStr.toString())
                .map(resp -> (String) resp.getBody()).get(10000);

        return ok(body);
        //return complexHolder.get().map(resp -> (Result) ok(resp.getBody())); */
	}

    public static Result red() {
        String url = "https://www.facebook.com/v2.1/dialog/oauth?client_id=1524941771068708&secret=b4310b646a5e589261a283f268dbbf3e&redirect_uri=https://localhost:9443/oauth&response_type=token";

        WSRequestHolder holder = WS.url(url).setFollowRedirects(false);
        WSResponse response = holder.get().get(10000);

        Map<String, List<String>> headmap = response.getAllHeaders();

        Set<Map.Entry<String, List<String>>> entrySet = headmap.entrySet();

        StringBuilder entryStr = new StringBuilder();

        for(Map.Entry<String, List<String>> e : entrySet) {
            entryStr.append(e.getKey());
            entryStr.append(": ");

            List<String> list = e.getValue();

            int i = 0;
            for(String s : list) {
                entryStr.append(s);

                if (i < list.size() - 1)
                    entryStr.append(", ");
            }

            entryStr.append(';');
        }

        return ok(entryStr.toString());
    }

	public static Result showResponse() {
		return ok(request().toString());
	}

    private static String retreiveValue(String name, String source) {
        int start = source.indexOf(name);
        start += name.length();

        boolean open = false;

        StringBuilder result = new StringBuilder();

        for(int i = start; i < source.length(); i++) {
            if (source.charAt(i) == '"') {
                if (open)
                    break;

                open = true;
                continue;
            }

            if (open)
                result.append(source.charAt(i));
        }

        return result.toString();
    }

    private static String retreiveNode(String name, String closing, String source) {
        int middle = source.indexOf(name);
        int end = source.indexOf(closing, middle);
        int start = -1;

        if (!name.startsWith("<")) {
            for(int i = middle; i >= 0; i--) {
                if (source.charAt(i) == '<') {
                    start = i;

                    break;
                }
            }
        } else {
            start = middle + name.length();
        }

        return source.substring(start + 1, end);
    }

    private static String retreiveHeader(String name, String source) {
        int start = source.indexOf(name);
        int end = source.indexOf("\"", start);

        return source.substring(start + name.length() + 1, end);
    }

    private static String maplistToString(Map<String, List<String>> map) {
        Set<Map.Entry<String, List<String>>> entryS = map.entrySet();

        StringBuilder entryB = new StringBuilder();

        for(Map.Entry<String, List<String>> e : entryS) {
            entryB.append(e.getKey());
            entryB.append(": ");

            List<String> list = e.getValue();

            int i = 0;
            for(String s : list) {
                entryB.append(s);

                if (i < list.size() - 1)
                    entryB.append(", ");
            }

            entryB.append(';');
        }

        return entryB.toString();
    }

    private static String cookieString(List<WSCookie> cookies) {
        StringBuilder cs = new StringBuilder();

        for(int i = 0; i < cookies.size(); i++) {
            WSCookie cook = cookies.get(i);
            cs.append(cook.getName());
            cs.append('=');
            cs.append(cook.getValue());
            cs.append(';');

            if (i < cookies.size() - 1)
                cs.append(' ');
        }

        return cs.toString();
    }

    private static String mapToString(Map<String, String> map) {
        Set<Map.Entry<String, String>> entryS = map.entrySet();

        StringBuilder entryB = new StringBuilder();

        int i = 0;
        for(Map.Entry<String, String> e : entryS) {
            entryB.append(e.getKey());
            entryB.append("=");
            entryB.append(e.getValue());

            if (i < entryS.size() - 1)
                entryB.append("; ");
        }

        return entryB.toString();
    }

    private static void cookiesToJar(List<WSCookie> cookies) {
        if (cookiesJar == null)
            cookiesJar = new HashMap<>();

        for(int i = 0; i < cookies.size(); i++) {
            WSCookie cook = cookies.get(i);

            //Logger.info("RESPONSE COOKIE " + cook.getName() + " ... " + cook.getValue());

            //if (!cookiesJar.containsKey(cook.getName()))
                cookiesJar.put(cook.getName(), cook.getValue());
            //else
                //Logger.info("SKIP");
        }
    }

    private static void wsCookiestToJar(List<WSCookie> list) {
        for(int i = 0; i < list.size(); i++) {
            jar.add(list.get(i));
        }
    }

    private static WSResponse handleResponse(WSResponse response) {
        int status = response.getStatus();

        cookiesToJar(response.getCookies());
        //wsCookiestToJar(response.getCookies());

        //Logger.info("SET_COOKIE " + response.getHeader("Set-Cookie"));

        if (status == 301 || status == 302) {
            String location = response.getHeader("Location");

            Logger.info("REDIRECT " + location);

            WSRequestHolder holder = prepareHolder(location, true);

            response = handleResponse(holder.get().get(10000));
        }

        return response;
    }

    private static WSRequestHolder prepareHolder(String location, boolean useCookie) {
        WSRequestHolder holder = WS.url(location).setFollowRedirects(false);

        if (useCookie && cookiesJar != null && !cookiesJar.isEmpty()) {
            String cookie = mapToString(cookiesJar);

            //Logger.info("COOKIE " + cookie);

            holder.setHeader("Cookie", cookie);

        }

        return holder;
    }

    /*private static class Cookie {
        private String name;
        private String value;
        private int maxAge;
        private String path;
        private String domain;
        private boolean secure;
        private boolean httpOnly;

        public Cookie(String name,
                   String value,
                   int maxAge,
                   String path,
                   String domain,
                   boolean secure,
                   boolean httpOnly) {
            this.name = name;
            this.value = value;
            this.maxAge = maxAge;
            this.path = path;
            this.domain = domain;
            this.secure = secure;
            this.name = name;
            this.httpOnly = httpOnly;
        }
    }*/
}

/*
public static Result sendRequest() {
        WSRequestHolder holder = WS.url(URL);
        /*holder.setQueryParameter("client_id", APP_ID)
                  .setQueryParameter("secret", SECRET)
                  .setQueryParameter("response_type", "token")
                  .setQueryParameter("redirect_uri", "https://localhost:9443")
                  .setQueryParameter("noscript", "1");*/

        //Promise<WSResponse> responsePromise = complexHolder.get();

        /*Promise<JsonNode> jsonPromise = complexHolder.get().map(
            new Function<WSResponse, JsonNode>() {
                public JsonNode apply(WSResponse response) {
                    JsonNode json = response.asJson();
                    return json;
                }
            }
        );* /
        
        
        //Promise<JsonNode> json = complexHolder.get().map(resp -> resp.asXml());

        //JsonNode formNode = json.get(10000).findParent("form");

        //Promise<Document> doc = complexHolder.get().map(resp -> resp.asXml());
        //Document doc = complexHolder.get().map(resp -> resp.asXml()).get(10000);
        //NodeList formNode = doc.getElementsByTagName("form");

        //Logger.info("size " + formNode.getLength());
        
        WSResponse response = holder.get().get(10000);

        List<WSCookie> cookies = response.getCookies();

        Logger.info("HEADERS " + response.getAllHeaders().toString());

        //String setCookie = retreiveHeader("Set-Cookie", response.getAllHeaders().toString());

        StringBuilder cs = new StringBuilder();

        for(int i = 0; i < cookies.size(); i++) {
            WSCookie cook = cookies.get(i);
            cs.append(cook.getName());
            cs.append('=');
            cs.append(cook.getValue());
            cs.append(';');

            if (i < cookies.size() - 1)
                cs.append(' ');
        }

        //cs.append(" noscript=1;");

        Logger.info("COOKIES " + cs.toString());
        Logger.info("\n\n\n");


        String body = holder.get().map(resp -> (String) resp.getBody()).get(10000);

        String form = retreiveNode("<form", ">", body);

        String action = retreiveValue("action", form);

        String lgnrnd = retreiveValue("value", retreiveNode("lgnrnd", "/>", body));

        String lsd = retreiveValue("value", retreiveNode("lsd", "/>", body));

        //Logger.info(lgnrnd);
        //Logger.info(form);

        holder = WS.url("https://facebook.com" + action).setFollowRedirects(true);

        //holder.setHeader("Cookie", setCookie);
        holder.setHeader("Cookie", cs.toString());

        /*String rawCookies = "datr=gkv8U5Z-tt0qiqIPcpEZk3X3; "
                        + "fr=0YMX5kCtNz4iLch3i.AWW5_AEQms9nqVThJdVTb-v7bBA.BT_EwN.5s.AAA.0.AWUXmASb; "
                        + "lu=SA40OAXCE6dPVbnRbea7h1Bw; "
                        + "noscript=1; "
                        + "reg_fb_gate=https://www.facebook.com/?stype=lo&jlou=AfdV3-sxPOpfA3ot-jt4cH3gaxdkdUjpcSvOQQ8VGB6dNomufPOfiIsVEysPdZVr7eew-YJGwz18auNfXW5rsUAd6IKWvkq4xncxhDXU3RxBmQ&smuh=9088&lh=Ac9tW1TgyR32Qg7N; "
                        + "reg_fb_ref=https://www.facebook.com/login.php?skip_api_login=1&api_key=1524941771068708&signed_next=1&next=https%3A%2F%2Fwww.facebook.com%2Fv2.1%2Fdialog%2Foauth%3Fredirect_uri%3Dhttps%253A%252F%252Flocalhost%253A9443%252Foauth%26response_type%3Dtoken%26client_id%3D1524941771068708%26ret%3Dlogin&cancel_uri=https%3A%2F%2Flocalhost%3A9443%2Foauth%3Ferror%3Daccess_denied%26error_code%3D200%26error_description%3DPermissions%2Berror%26error_reason%3Duser_denied%23_%3D_&display=page; "
                        + "x-src=/docs/facebook-login/manually-build-a-login-flow/v2.1|documentation_body_pagelet;";* /


        //holder.setHeader("Cookie", rawCookies);

        holder.setHeader("Content-Type", "application/x-www-form-urlencoded");
        holder.setHeader("Connection", "keep-alive");
        holder.setHeader("Cache-Control", "max-age=0");
        holder.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,* /*;q=0.8");
        holder.setHeader("Origin", "null");
        holder.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36");
        //holder.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        holder.setHeader("Accept-Language", "en-US,en;q=0.8,uk;q=0.6");

        StringBuilder paramStr = new StringBuilder();
        paramStr.append("lsd=");
        paramStr.append(lsd);
        paramStr.append("&api_key=");
        paramStr.append(APP_ID);
        paramStr.append("&display=page");
        paramStr.append("&enable_profile_selector=");
        paramStr.append("&legacy_return=1");
        paramStr.append("&profile_selector_ids=");
        paramStr.append("&skip_api_login=1");
        paramStr.append("&signed_next=1");
        paramStr.append("&trynum=1");
        paramStr.append("&timezone=");
        //paramStr.append("u_0_0");
        paramStr.append("&lgnrnd=");
        paramStr.append(lgnrnd);
        paramStr.append("&lgnjs=n");
        paramStr.append("&email=");
        paramStr.append("oleg.zhembrovsky%40fastforwardpro.com");
        paramStr.append("&pass=");
        paramStr.append("potato0face");
        //paramStr.append("&persistent=1");
        paramStr.append("&default_persistent=0");

        Logger.info("\n");
        Logger.info(paramStr.toString());

        /*complexHolder = 
            holder.setQueryParameter("lgnrnd", lgnrnd);*/

        /*String raw = "lsd=AVpdp0Ii&api_key=1524941771068708&display=page&enable_profile_selector=&legacy_return=1&profile_selector_ids=&skip_api_login=1&signed_next=1"
                    + "&trynum=1&timezone=&lgnjs=n&email=oleg.zhembrovsky%40fastforwardpro.com&pass=potato0face&default_persistent=0"
                    + "&login=%26%231059%3B%26%231074%3B%26%231110%3B%26%231081%3B%26%231090%3B%26%231080%3B&lgnrnd=" + lgnrnd;* /

        Map<String, Collection<String>> headmap = holder.getHeaders();

        Set<Map.Entry<String, Collection<String>>> entrySet = headmap.entrySet();

        StringBuilder entryStr = new StringBuilder();

        for(Map.Entry<String, Collection<String>> e : entrySet) {
            entryStr.append(e.getKey());
            entryStr.append(": ");

            Collection<String> coll = e.getValue();

            int i = 0;
            for(String s : coll) {
                entryStr.append(s);

                if (i < coll.size() - 1)
                    entryStr.append(", ");
            }

            entryStr.append(';');
        }

        Logger.info("REQUEST HEADERS " + entryStr.toString());
        Logger.info("--- REQUEST HEADERS ");

        response = holder.post(paramStr.toString()).get(10000);

        cookies = response.getCookies();

        cs = new StringBuilder();

        for(int i = 0; i < cookies.size(); i++) {
            WSCookie cook = cookies.get(i);
            cs.append(cook.getName());
            cs.append('=');
            cs.append(cook.getValue());
            cs.append(';');

            if (i < cookies.size() - 1)
                cs.append(' ');
        }

        Logger.info("COOKIES2 " + cs.toString());
        Logger.info("\n\n\n");



        body = holder
                .post(paramStr.toString())
                .map(resp -> (String) resp.getBody()).get(10000);

        return ok(body);
        //return complexHolder.get().map(resp -> (Result) ok(resp.getBody()));
    }
*/