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

Main entry point for your app’s persisted data
Gives you access to your DAOs for data fetching/updating

### Room - Data Access Object (DAO)

You write these as a way of interacting with the data you want in the database
Actions are interpreted using annotations (@Insert, @Query)
Methods can have a return type if you’re getting information
Query-annotated methods have a String parameter inside for writing the corresponding SQL query
Can also pass arguments to these methods for restrictions (like passing a minimum age for an employee range)

### Room - Entity

These are essentially entries into your table (e.x. An Employee in an Employees table)
Usually has a primary key (unique identifier or identifiers of an entry) annotated with @PrimaryKey
You can call DAO methods from your code to perform operations on the underlying database (e.x. Getting your employees from the table, or deleting old entries from a database)

### Putting it all together

Build your database
Getting the appropriate DAO from the database
Getting data from the database