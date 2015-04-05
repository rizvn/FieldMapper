# Field Mapper
A library to convert list of maps `List<Map<String,Object>>` to a list of objects. It can be used with database access libraries such as spring jdbc, and JDBI which can return a list of maps from queries.

##Maven
The jars are hosted in github repo:

      <repositories>
            <repository>
                <id>FieldMapper Repo</id>
                <url>https://raw.github.com/rizvn/FieldMapper/mvn-repo/</url>
            </repository>
      </repositories>

The maven dependency is:

    <dependency>
       <groupId>com.rizvn</groupId>
       <artifactId>fieldMapper</artifactId>
       <version>0.1</version>
    </dependency>

## Usage
Example table schema:

      create table users(
         id serial, 
         firstName varchar(255), 
         lastName varchar(255), 
         email varchar(255), 
         lastUpdated TIMESTAMP)

#### 1. Create class, and annotate fields with @Column (com.rizvn.fieldmapper.annotation.Column)
Define a class to which we want to map results to.

    public class User {
       @Column
       String firstName;

       @Column("lastName") //names are optional
       String lastName;

       @Column
       String email;
      
       @Column(typeHandler = TimestampToJodaDateTime.class)
       DateTime lastUpdated;
       
       /** Getters and setters */
    }

#### 2. Run query
Run the query and return a list of maps. Below is a Spring JDBC Template example.
    
    List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT * FROM USERS");

#### 3. Convert maps to objects
Now we call `FieldMapper.mapListToObjectList(..)` to map the list of maps to list of objects

    List<User> users = FieldMapper.mapListToObjectList(results, User.class);


## @Column
By default FieldMapper will look for the key that matches the instance variable name in the map. You can override this behavior by specifying the database column name manually i.e `@Column('FIRST_NAME')`.

Once the key is found, instance variable value will be set to the key's value. It will transparently handle most java types. However you may provide your own handlers through the `typeHandler` attribute on @Column `@Column(typeHandler = TimestampToJodaDateTime.class)`

##TypeHandler
Below is the TypeHandler interface

    public interface TypeHandler {
      public <TargetType> TargetType transform(Object src);
    }

It takes value from the map and returns the transformed value, which will be set as the instance variable value.

Below is the code for `TimestampToJodaDateTime` Type handler used in the User Class example above. `java.sql.Timestamp` is converted  to a DateTime object. The returned value will be set as instance variable value.

    public class TimestampToJodaDateTime implements TypeHandler{
      @Override
      public DateTime transform(Object src) {
        Timestamp timestamp = (Timestamp) src;
        return new DateTime(timestamp);
      }
    }
