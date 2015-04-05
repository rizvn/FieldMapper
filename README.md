# Field Mapper
A library to convert list of maps `List<Map<String,Object>>` to a list of objects. It can be used with database access libraries such as spring jdbc, and JDBI which can return a list of maps from queries.

##Maven repository

      <repositories>
            <repository>
                <id>FieldMapper Repo</id>
                <url>https://raw.github.com/rizvn/FieldMapper/mvn-repo/</url>
            </repository>
      </repositories>

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

    public class User {
       @Column
       String firstName;

       @Column("lastName")
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
By default FieldMapper will look for the key that matches the instance variable name in the map. You can override this behavior by specifying the database column name manually `@Column('FIRST_NAME')`.

Once the key is found, instance variable value will be set to the key's value. It will transparently handle most java types. However you may provide your own handlers through the `typeHandler` attribute on @Column `@Column(typeHandler = TimestampToJodaDateTime.class)`

##TypeHandler
Below is the type handler interface

    public interface TypeHandler {
      public <TargetType> TargetType transform(Object src);
    }

It takes the key's the value, transforms it, and returns a new value to be set as instance variable value.

Below is the code for `TimestampToJodaDateTime` type handle used in the User Class example above. `java.sql.Timestamp` is converted  to a DateTime object. The returned value will be set as instance variable value.

    public class TimestampToJodaDateTime implements TypeHandler{
      @Override
      public DateTime transform(Object src) {
        Timestamp timestamp = (Timestamp) src;
        return new DateTime(timestamp);
      }
    }
