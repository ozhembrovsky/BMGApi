package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import play.data.validation.*;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Job extends Model {
  
  @Id
  public Long id;
  
  @Constraints.Required
  public String title;
  
  @ManyToOne
  public Category category;
  
  public static Finder<Long,Job> find = new Finder("default", Long.class, Job.class);
}