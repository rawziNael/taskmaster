package com.amplifyframework.datastore.generated.model;


import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks")
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField TITLE = field("Task", "title");
  public static final QueryField BODY = field("Task", "body");
  public static final QueryField STATUS = field("Task", "status");
  public static final QueryField IMAGE = field("Task", "image");
  public static final QueryField LOCATION_LATITUDE = field("Task", "locationLatitude");
  public static final QueryField LOCATION_LONGITUDE = field("Task", "locationLongitude");
  public static final QueryField TEAM_TASKS_ID = field("Task", "teamTasksId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String", isRequired = true) String body;
  private final @ModelField(targetType="String", isRequired = true) String status;
  private final @ModelField(targetType="String") String image;
  private final @ModelField(targetType="Float") Double locationLatitude;
  private final @ModelField(targetType="Float") Double locationLongitude;
  private final @ModelField(targetType="ID") String teamTasksId;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getBody() {
      return body;
  }
  
  public String getStatus() {
      return status;
  }
  
  public String getImage() {
      return image;
  }
  
  public Double getLocationLatitude() {
      return locationLatitude;
  }
  
  public Double getLocationLongitude() {
      return locationLongitude;
  }
  
  public String getTeamTasksId() {
      return teamTasksId;
  }
  
  private Task(String id, String title, String body, String status, String image, Double locationLatitude, Double locationLongitude, String teamTasksId) {
    this.id = id;
    this.title = title;
    this.body = body;
    this.status = status;
    this.image = image;
    this.locationLatitude = locationLatitude;
    this.locationLongitude = locationLongitude;
    this.teamTasksId = teamTasksId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getBody(), task.getBody()) &&
              ObjectsCompat.equals(getStatus(), task.getStatus()) &&
              ObjectsCompat.equals(getImage(), task.getImage()) &&
              ObjectsCompat.equals(getLocationLatitude(), task.getLocationLatitude()) &&
              ObjectsCompat.equals(getLocationLongitude(), task.getLocationLongitude()) &&
              ObjectsCompat.equals(getTeamTasksId(), task.getTeamTasksId());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getBody())
      .append(getStatus())
      .append(getImage())
      .append(getLocationLatitude())
      .append(getLocationLongitude())
      .append(getTeamTasksId())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("body=" + String.valueOf(getBody()) + ", ")
      .append("status=" + String.valueOf(getStatus()) + ", ")
      .append("image=" + String.valueOf(getImage()) + ", ")
      .append("locationLatitude=" + String.valueOf(getLocationLatitude()) + ", ")
      .append("locationLongitude=" + String.valueOf(getLocationLongitude()) + ", ")
      .append("teamTasksId=" + String.valueOf(getTeamTasksId()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Task justId(String id) {
    return new Task(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      body,
      status,
      image,
      locationLatitude,
      locationLongitude,
      teamTasksId);
  }
  public interface TitleStep {
    BodyStep title(String title);
  }
  

  public interface BodyStep {
    StatusStep body(String body);
  }
  

  public interface StatusStep {
    BuildStep status(String status);
  }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id);
    BuildStep image(String image);
    BuildStep locationLatitude(Double locationLatitude);
    BuildStep locationLongitude(Double locationLongitude);
    BuildStep teamTasksId(String teamTasksId);
  }
  

  public static class Builder implements TitleStep, BodyStep, StatusStep, BuildStep {
    private String id;
    private String title;
    private String body;
    private String status;
    private String image;
    private Double locationLatitude;
    private Double locationLongitude;
    private String teamTasksId;
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          title,
          body,
          status,
          image,
          locationLatitude,
          locationLongitude,
          teamTasksId);
    }
    
    @Override
     public BodyStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public StatusStep body(String body) {
        Objects.requireNonNull(body);
        this.body = body;
        return this;
    }
    
    @Override
     public BuildStep status(String status) {
        Objects.requireNonNull(status);
        this.status = status;
        return this;
    }
    
    @Override
     public BuildStep image(String image) {
        this.image = image;
        return this;
    }
    
    @Override
     public BuildStep locationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
        return this;
    }
    
    @Override
     public BuildStep locationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
        return this;
    }
    
    @Override
     public BuildStep teamTasksId(String teamTasksId) {
        this.teamTasksId = teamTasksId;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String body, String status, String image, Double locationLatitude, Double locationLongitude, String teamTasksId) {
      super.id(id);
      super.title(title)
        .body(body)
        .status(status)
        .image(image)
        .locationLatitude(locationLatitude)
        .locationLongitude(locationLongitude)
        .teamTasksId(teamTasksId);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder body(String body) {
      return (CopyOfBuilder) super.body(body);
    }
    
    @Override
     public CopyOfBuilder status(String status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder image(String image) {
      return (CopyOfBuilder) super.image(image);
    }
    
    @Override
     public CopyOfBuilder locationLatitude(Double locationLatitude) {
      return (CopyOfBuilder) super.locationLatitude(locationLatitude);
    }
    
    @Override
     public CopyOfBuilder locationLongitude(Double locationLongitude) {
      return (CopyOfBuilder) super.locationLongitude(locationLongitude);
    }
    
    @Override
     public CopyOfBuilder teamTasksId(String teamTasksId) {
      return (CopyOfBuilder) super.teamTasksId(teamTasksId);
    }
  }
  
}
