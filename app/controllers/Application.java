package controllers;

import play.*;
import play.mvc.*;
import play.data.Form;

import views.html.*;

import models.Category;
import models.profiles.User;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready. Version " + play.core.PlayVersion.current()));
    }
	
	public static Result testindex() {
		return ok(testindex.render(Category.all(), Form.form(Category.class)));
	}
    
    public static Result users() {
        return ok(users.render(User.all()));
    }
}
