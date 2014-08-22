package controllers;

import play.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;
import models.*;

public class Categories extends Controller {
    public static Result add() {
        Form<Category> filledForm = Form.form(Category.class).bindFromRequest();
        if (filledForm.hasErrors()) {
          return badRequest(testindex.render(Category.find.all(), filledForm));
        }
        Category category = filledForm.get();
        category.save();
        return redirect(routes.Application.testindex());
    }
  
  public static Result show(Long id) {
    Category category = Category.find.byId(id);

    return ok(testshow.render(category, Form.form(Job.class)));
  }
}
