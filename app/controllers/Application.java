package controllers;

import play.*;
import play.mvc.*;
import play.data.Form;

import views.html.*;

import models.profiles.*;
import models.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    /*public static Result auth() {
        

        return ok();
    }

    public static Result login() {
        return ok(login.render());
    }*/

    public static Result testindex() {
		return ok(testindex.render(Category.all(), Form.form(Category.class)));
	}
    
    public static Result users() {
        return ok(users.render(User.all()));
    }

}
