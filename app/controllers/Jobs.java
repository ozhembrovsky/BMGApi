package controllers;

import java.util.List;

import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;

import models.*;

public class Jobs extends Controller {
    public static Result add(Long categoryId) {
        Form<Job> filledForm = Form.form(Job.class).bindFromRequest();
        
        if (filledForm.hasErrors()) {
            Category category = Category.find.byId(categoryId);
            return badRequest(testshow.render(category, filledForm));
        }
        
        Job job = filledForm.get();
        job.category = Category.find.ref(categoryId);
        job.save();
        return redirect(routes.Categories.show(categoryId));
    }
}