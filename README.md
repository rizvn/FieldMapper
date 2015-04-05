# Field Mapper
A library to convert list of maps `List<Map<String,Object>>` to a list of objects. It can be used with database access libraries such as spring jdbc, and JDBI which can return a list of maps from queries.

## Usage

Example table schema:

      create table users(
         id serial, 
         firstName varchar(255), 
         lastName varchar(255), 
         email varchar(255), 
         lastUpdated TIMESTAMP)

#### 1. Create class, and annotate fields with @Column
We want run a query to select all users and map, the results to a User class. So we first defined a user class

    /**
    * Annotate the field
    */
    public class User {
       
       @Column
       String firstName;

       @Column
       String lastName;

       @Column
       String email;
      
       @Column(typeHandler = TimestampToJodaDateTime.class)
       DateTime lastUpdated;
       
       /** Getters and setters */
    }

#### 2. Run query
Now use spring's jdbcTemplate to run the query and return a list of maps.
    
    List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT * FROM USERS");

#### 3. Convert maps to objects
Now we call `FieldMapper.mapListToObjectList(..)` to map the list of maps to list of objects

    List<User> users = FieldMapper.mapListToObjectList(results, User.class);


## @Column
By default FieldMapper will look for the key that matches the instance variable name. You can override this behavior by specifying the database column name like this: `@Column('FIRST_NAME')`. 

Once the key is found, it will set the value on the field. It will transparently handle most java types. However you may provide your own handler through the `typeHandler` attribute on @Column, like: `@Column(typeHandler = TimestampToJodaDateTime.class)` 

##TypeHandler
Below is the type handler interface

    public interface TypeHandler {
      public <TargetType> TargetType transform(Object src);
    }

It takes the map takes the value returned from the db, and converts it into the type of the instance variable. 

Below is the code for `TimestampToJodaDateTime` type handle used in the User Class above. `java.sql.Timestamp` is returned from the db. we convert it to a DateTime object and return the new object. The returned value will be set on the object. 

    public class TimestampToJodaDateTime implements TypeHandler{
      @Override
      public DateTime transform(Object src) {
        Timestamp timestamp = (Timestamp) src;
        return new DateTime(timestamp);
      }
    }
