# Persistence

We live in an always-online world, but that world isn’t always online.  The offline experience can be as, if not more, important sometimes.  We also don’t want to pull from the network **every time** if it’s data that might not change very often (like a list of employees - we don’t need to update that every time we check the app but maybe more like every two weeks).  We’ll talk about two common ways to persist your data: `SharedPreferences` and `Room`

## SharedPreferences

SharedPreferences is a way of persisting small bits of data that you might read often (whether the user wants to use WiFi only, whether we need to show a promotion, etc) or that you might need to check every time the user starts your app.

### What is SharedPreferences?

Essentially, it's a set of key/value pairs (similar to a Map/Dictionary). Each piece of data we put into our preferences needs some sort of key to reference the associated value.

### Creating SharedPreferences

We won't instantiate a `SharedPreferences` object in the way that we normally create other objects (so `new SharedPreferences()` shouldn't be a thing you really think about).

To create an instance of SharedPreferences, we'll have some file name - usually something like `app_name_preferences` (and let's say we're storing that file in a string resource of the same name) and use that file name to create our preferences. The normal statement looks like this:

```java
SharedPreferences sharedPref = context.getSharedPreferences(
        getString(R.string.app_name_preferences), Context.MODE_PRIVATE);
```

We're using a `Context` object method, `getSharedPreferences`, to create our instance.  The first parameter is a String that represents the file name where our preference file lives, and the second parameter (`Context.MODE_PRIVATE`) just says that we should only allow that file to be accessed by our application

### Accessing and storing values

To access or store a value in our preferences, we need to know what general type it is.  The following types are supported:
```java
Boolean
Float
Int
Long
String
Set<String>
```

Accessing data looks like this:
```java
//Assume a SharedPreferences object named preferences

preferences.getBoolean(EXAMPLE_BOOLEAN_KEY, false);
preferences.getFloat(EXAMPLE_FLOAT_KEY, 0.0);
preferences.getInt(EXAMPLE_INT_KEY, 0);
preferences.getLong(EXAMPLE_LONG_KEY, 0);
preferences.getString(EXAMPLE_STRING_KEY, "");
preferences.getStringSet(EXAMPLE_STRING_SET_KEY, null);
```

In the above example, our EXAMPLE_\<TYPE>_KEYs are just unique strings referencing a preference. The second argument for each method is a default value - this default value will be supplied in case a value associated with the key can't be found.

Storing data looks very similar except we have to operate on an object called an `Editor` - we can get this Editor from our SharedPreferences instance and need to call `apply()` to make the change take effect. Here are a few examples:

```java
//Assume a SharedPreferences object named preferences

SharedPreferences.Editor editor = preferences.edit();

editor.putBoolean(EXAMPLE_BOOLEAN_KEY, booleanValue);

editor.apply();
```

We first fetch our Editor with `preferences.edit()`. Next, we use `putBoolean` and specify a key (`EXAMPLE_BOOLEAN_KEY`) and a value to associate with that key (`booleanValue`).  Lastly, to save this, we call `editor.apply()`.  This example used the `putBoolean` method, but all the same types are supported for `put<Type>` as with get. Calling apply will apply changes in-memory immediately but writes to disk asynchronously.

Do note that Set\<String> has some odd behavior compared to other types, so just be careful when using it.


## Room

Room is Android's solution for larger data storage - think if you needed to store a larger list of users, each user with a bunch of their own fields.

Android uses SQLite for handling larger amounts of data; SQLite is a lighter implementation of the common database language SQL (Structured Query Language), and Room is a way for us to use SQL in a much cleaner way.

We're going to use some terms here that you might not be familiar with, so let's define them here first:

- `Database`: The main access point for our app's persisted data
- `Entity`: A table in the database (basically a unit of data, like a table called `users` that holds a list of `User`)
- `DAO`: Stands for *Data Access Object* and is basically our middleman between Entities and the Database (it accesses the database and gets us our data)


Before we dive in to Room, we'll talk really briefly about the original way we handled storage, SQLiteOpenHelper.


### SQLiteOpenHelper

Before Room, we had to use SQLiteOpenHelper and basically did our own implementation.  You would have to do all these steps:

- Create your own class extending `SQLiteOpenHelper`; this class would handle creating your tables, configuring your database, and upgrading your database
- Adding methods to manually perform your common Insert/Update/Delete/Get operations

These operations involved something called a `Cursor` and led to a lot of verbose code.  Here's an sample of making a simple query:

`db.query(GROUPS_TABLE_NAME, projection, selection, selectionArgs, null, null, null);`

This will query the database, looking in the table `GROUPS_TABLE_NAME` and has some other arguments that we don't need to get into.  Note the three `null`s at the end - really verbose for no real reason.

Room helps us solve a lot of these issues and handles the heavy lifting for us.

### Getting Started with Room

The first thing we need to do is add the dependencies to our Gradle build file.  Your `build.gradle` file just needs to look like this:

```groovy
dependencies {
  def room_version = "2.2.1"

  implementation "androidx.room:room-runtime:$room_version"
  annotationProcessor "androidx.room:room-compiler:$room_version"
}
```

This will let us use the basic Room implementation.

It will also expose three different annotations for us:
- `@Database`
- `@Dao`
- `@Entity`

Each of these corresponds to a large part of our database (all these are as defined earlier).  Let's dive into each to see how they work.

### Room - Database

A database in Room is the main entry point for your app’s persisted data - it's the highest-level object we work with.

Generally this is where your DAO will be fetched from, usually with some sort of call like 

```java
public abstract ExampleDao getExampleDao();
```

Databases in Room have four conditions that should be met:
- The class should be declared abstract and extend `RoomDatabase`
- The class is annotated with the `@Database` annotation
- Includes a list of Entities in the `@Database` annotation
- Contains abstract methods (0 arguments) for each DAO class annotated with `@Dao`

A simple Database would look like this:

```java
@Database(entities = {NewsStory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
}
```

Our `AppDatabase` here will have `NewsStory` entities (so entries in its table) and gives us access to the `NewsDao` via the one defined abstract method.

To get an instance of our database, we'd do the following:

```java
AppDatabase db = Room.databaseBuilder(getApplicationContext(),
        AppDatabase.class, "database-name").build();
```

We have to call `Room.databaseBuilder()` and pass it a Context (`getApplicationContext()` here), the Database class (`AppDatabase.class`), and a name for the database (`"database-name"`).  We can define this as a singleton (only one object so we can never instantiate more than one) by defining a static method in our Database class.

If we add that method, our Database class would look like this:

```java
@Database(entities = {NewsStory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract NewsDao newsDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(getApplicationContext(),
        AppDatabase.class, "database-name").build();
        }
        return INSTANCE;
    }
}
```

Then, elsewhere, we could grab our DAO from this database with:
```java
AppDatabase.getAppDatabase(getApplicationContext()).newsDao()
```

### Room - Data Access Object (DAO)

The DAO is basically how we perform our SQL queries. We write these queries as a way of interacting with the data you want in the database

The DAO **must** be an interface or an abstract class and annotated with the `@Dao` annotation

We can create our queries just by writing function signatures and annotating them with the appropriate action.  Valid actions are:
- `@Insert`: inserts an item (or items) into the database
- `@Delete`: deletes an item from the database (this is only used for **ONE** item - to delete multiple items you need to use `@Query`)
- `@Update`: update an item in the row (this update will be based on the primary key of the item)
- `@Query`: perform a query that can be used for any of the above actions (also for retrieving data)

A sample DAO for our News app might look like this:

```java
@Dao
public interface NewsStoryDao {

    @Query("SELECT * FROM news_stories")
    List<NewsStory> getAllStories();

    @Query("SELECT * FROM news_stories WHERE title LIKE :title LIMIT 1")
    NewsStory findStoryByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllStories(List<NewsStory> stories);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertStory(NewsStory story);

    @Delete
    void deleteStory(NewsStory story);

    @Query("DELETE FROM news_stories")
    void deleteAll();
}
```

Our `insertAllStories` and `insertStory` methods will insert all the stories in the list (in the case of `insertAllStories`) and a single NewsStory. The `onConflict = OnConflictStrategy.IGNORE` there just means that Room won't take any action if Room determines that the story already exists in the database.

`deleteStory()` will delete a single story, and `deleteAll()` will delete ALL our stories.

Our first two methods include some text inside the `@Query` annotation - these are our *queries*.  The first one just selects everything from a table called `news_stories` while the second will match the `title` to the title we pass in (the `WHERE title LIKE :title` bit) and make sure that we only return one story (the `LIMIT 1` bit).

Notice that our `@Delete` and `@Insert` methods are all `void` return types - we don't need anything back from them.  Our `getAllStories()` and `findStoryByTitle()` methods return one or more NewsStory objects because we're querying to find them.  Whenever we expect to get something back, we specify it as a return type like any normal method.

You might've also noticed us passing `String title` to our `findStoryByTitle()` method - this is how we can add variables to our queries.  The `:title` part of our `findStoryByTitle()` query looks for an argument to the method called `title` and uses that in place of `:title`.

### Room - Entity

Entities are essentially entries into your table (e.x. An Employee in an Employees table)

These aren't very different from our normal data models.  Like let's look at a normal NewsStory:

```java
public class NewsStory {
    private String section;
    private String subsection;

    private String title = "";

    private String articleAbstract;

    private String byline;
    private String publishedDate;
}
```

To turn this into an Entity, we first need to annotate with `@Entity`:

```java
@Entity
public class NewsStory {
    private String section;
    private String subsection;

    private String title = "";

    private String articleAbstract;

    private String byline;
    private String publishedDate;
}
```

We also need to make sure it has a **Primary Key**. The primary key is the way to uniquely identify an entry in the row - normally this is something like a `userId` or just an `id` (some numeric value uniquely assigned).  Since we don't really have that here, let's say that each story has to have a unique title, so we'll annotate `title` with `@PrimaryKey` (this is also why title is initialized - primary keys cannot be null):

```java
@Entity
public class NewsStory {
    private String section;
    private String subsection;

    @PrimaryKey
    private String title = "";

    private String articleAbstract;

    private String byline;
    private String publishedDate;
}
```

Lastly, if we want some of our column names to be different from their variable names, we can use an annotation called `@ColumnInfo` to specify the new column name. If we change `articleAbstract` to `article_abstract` and `publishedDate` to `published_date`, our Entity looks like this:

```java
@Entity
public class NewsStory {
    private String section;
    private String subsection;

    @PrimaryKey
    private String title = "";

    @ColumnInfo(name = "article_abstract")
    private String articleAbstract;

    private String byline;

    @ColumnInfo(name = "published_date")
    private String publishedDate;
}
```

### Putting it all together

Stringing these all together, we have the following:

- the database
```java
@Database(entities = {NewsStory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract NewsDao newsDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(getApplicationContext(),
        AppDatabase.class, "database-name").build();
        }
        return INSTANCE;
    }
}
```

- the DAO
```java
@Dao
public interface NewsStoryDao {

    @Query("SELECT * FROM news_stories")
    List<NewsStory> getAllStories();

    @Query("SELECT * FROM news_stories WHERE title LIKE :title LIMIT 1")
    NewsStory findStoryByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllStories(List<NewsStory> stories);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertStory(NewsStory story);

    @Delete
    void deleteStory(NewsStory story);

    @Query("DELETE FROM news_stories")
    void deleteAll();
}
```

- and the Entity
```java
@Entity
public class NewsStory {
    private String section;
    private String subsection;

    @PrimaryKey
    private String title = "";

    @ColumnInfo(name = "article_abstract")
    private String articleAbstract;

    private String byline;

    @ColumnInfo(name = "published_date")
    private String publishedDate;
}
```


So somewhere in our code, we could access the database and pass the DAO into some helper object that will get our info for us:

```java
//Get our database
AppDatabase database = AppDatabase.getAppDatabase(getContext().getApplicationContext());
//Get our DAO
NewsDao newsDao = database.newsDao();

//Pass our DAO into our helper
ExampleRepository repository = new ExampleRepository(newsDao);
```

and inside that `ExampleRepository`, we might have a `getStories()` method:

```java
public List<NewsStory> getStories() {
    return newsDao.getAllStories();
}
```

This example's a very simple way in which we could set up our data, our DAO, and our database.