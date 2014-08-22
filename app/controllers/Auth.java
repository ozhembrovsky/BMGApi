package controllers;

import play.*;
import play.data.Form;
import play.mvc.*;


import views.html.*;

import models.profiles.User;

import java.util.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Auth extends Controller {
    private static final Long LOGIN_TIME = 100000;
    
    private static final TokenGenerator generator = new TokenGenerator();
    
    private static Map<String, LoginInfo> authenticated = new HashMap(); // token login_info

    public static final String TOKEN_PARAM = "";

    public static Result login() {
        /*Form<User> userForm = Form.form(User.class).bindFromRequest();
        
        if (userForm.hasErrors()) {
            return badRequest(login.render(userForm));
        }
        
        User user = userForm.get();
        List<User> res = 
            User.find
            .where()
            .eq("login", user.login)
            .eq("password", user.password)
            .findList();
        
        if (res.size() == 1) {
            timestamp.put(user.login, System.currentTimeMillis());
            
            return ok("logged in");
        }
        
        return ok("Wrong data");
        */
        
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        
        StringBuilder sb = new StringBuilder();
        
        if (values == null)
            return badRequest("Null value");
        
        for(Map.Entry<String, String[]> e : values.entrySet()) {
            sb.append(e.getKey());
            sb.append(':');
            String[] sv = e.getValue();

            for(int i = 0; i < sv.length; i++) {
                sb.append(sv[i]);
                sb.append(',');
            }

            sb.append('\n');
        }
        
        response().setContentType("text/html; charset=utf-8");
        
        return ok(sb.toString());
    }
    
    public static Result loginForm() {
        Form<User> userForm = Form.form(User.class).bindFromRequest();
        return ok(login.render(userForm));
    }

    public static boolean authorized() {
        String tokenValue = Utils.parameters(request().body().asFormUrlEncoded(), TOKEN_PARAM)[0];

        if (tokenValue == null)
            return false;

        LoginInfo loginInfo = authenticated.get(tokenValue);

        if (loginInfo == null)
            return null;
    }
    
    private static class LoginInfo {
        String login;
        Long loginTime;
    }
    
    private static class TokenGenerator {
        static final SecureRandom random = new SecureRandom();
    
        static String generateToken() {
            return new BigInteger(130, random).toString(32);
        }
    }
}
