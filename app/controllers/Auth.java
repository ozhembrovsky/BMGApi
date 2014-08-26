package controllers;

import play.*;
import play.data.Form;
import play.mvc.*;


import views.html.*;

import static controllers.Parameters.*;
import models.profiles.User;

import java.util.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Auth extends Controller {
    private static final Long LOGIN_TIMEOUT = 100000l;
    
    private static final TokenGenerator generator = new TokenGenerator();
    
    private static Map<String, LoginInfo> authenticated = new HashMap(); // token login_info

    /*public static Result login() {
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
        * /

        if (authorized()) {
            return redirect("/index");
        }
        
        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        if (true) {
        
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
        } else {
            String[] loginParams = Utils.parameters(values, LOGIN_PARAM, PASSWORD_PARAM);

            if (loginParams == null || loginParams[0].isEmpty() || loginParams[1].isEmpty()) {
                response().setContentType("text/html; charset=utf-8");

                return ok("No login information");
            } /*else if () {

            }* /
 
            return ok("TODO");
        }
    }*/

    public static Result login() {
        String token = authorized(0);

        if (token != null) {
            return ok(TOKEN_PARAM + "=" + token);
        }

        Form<User> userForm = Form.form(User.class).bindFromRequest();
        
        if (userForm.hasErrors()) {

            return badRequest("Wrong data");
            //return badRequest(login.render(userForm));
        }
        
        User user = userForm.get();
        List<User> res = 
            User.find
            .where()
            .eq("login", user.login)
            .eq("password", user.password)
            .findList();
        
        if (res.size() == 1) {
            LoginInfo li = new LoginInfo(user.login, System.currentTimeMillis(), 0);

            token = generator.generateToken();

            authenticated.put(token, li);
            
            return ok(TOKEN_PARAM + "=" + token);
        }
        
        return ok("Wrong data");
    }
    
    public static Result loginForm() {
        Form<User> userForm = Form.form(User.class).bindFromRequest();
        return ok(login.render(userForm));
    }

    public static String authorized(int role) { //TODO role access managing
        String tokenValue = Utils.parameters(request().body().asFormUrlEncoded(), TOKEN_PARAM)[0];

        Logger.info("Token from request = " + tokenValue);

        if (tokenValue == null)
            return null;

        LoginInfo loginInfo = authenticated.get(tokenValue);

        if (loginInfo == null)
            return null;

        if (checkTimeout(loginInfo))
            return tokenValue;

        return null;
    }

    private static boolean checkTimeout(LoginInfo loginInfo) {
        if (loginInfo == null)
            return false;

        Long currentTime = System.currentTimeMillis();

        if (currentTime - loginInfo.getLoginTime() > LOGIN_TIMEOUT)
            return false;

        return true;
    }
    
    private static class LoginInfo {
        private String login;
        private Long loginTime;
        private Integer role;

        public LoginInfo(String login, Long loginTime, Integer role) {
            this.login = login;
            this.loginTime = loginTime;
            this.role = role;
        }

        public String getLogin() {
            return login;
        }

        public Long getLoginTime() {
            return loginTime;
        }

        public Integer getRole() {
            return role;
        }
    }
    
    private static class TokenGenerator {
        static final SecureRandom random = new SecureRandom();
    
        static String generateToken() {
            return new BigInteger(130, random).toString(32);
        }
    }
}
