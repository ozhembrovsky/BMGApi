package models.profiles;

import play.data.validation.Constraints;
import play.db.ebean.Model;


import java.util.List;
import javax.persistence.*;

@Entity
public class User extends Model {
    @Id
    public Integer id;
    @Constraints.Required
    public String login;
    @Constraints.Required
    public String password;

    public static Finder<Integer, User> find = new Finder<Integer, User>("profiles", Integer.class, User.class);
    
    public static List<User> all() {
        return find.all();
    }
}