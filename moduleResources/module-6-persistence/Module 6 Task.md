# Persistence Task

To get a good handle on Room and how to use it, let's take our NewsStory data model and our API and add in a persistence layer.

There are a few predefined methods and classes here to use:

- `NewsListFragment` has a method called `fetchStories()`; this is a way for us to do work off the main thread without necessarily using Callbacks. It uses a library called **RxJava** to work off the main thread then come back to the main thread when done.
- `NewsStory` is our data model that we'll adapt for Room
- `NewsRepository` is a class that will hold both our API and our DAO so that we can grab data from the network if we don't have it in our database

`NewsRepository` is part of a fairly common pattern called the *Repository* pattern.  The Repository handles getting our data and we don't know exactly _where_ the data is coming from.  We'll give it both our API and our DAO so that it can try to pull from the database and, if it doesn't get anything (or, more often, if the data is stale and needs to be refreshed), we can use the API to get the data.

If you want to try it yourself, you'll need to:
- Add the dependencies (look [here](https://developer.android.com/jetpack/androidx/releases/room#declaring_dependencies))
- Update the NewsStory to be an Entity (this will involve `@Entity` and `@PrimaryKey` at a minimum)
- Add a DAO (involves the `@Dao` annotation and adding the `@Query`/`@Insert`/`@Delete` methods you want)


### Adding the dependencies

The first thing we need to do is to add the Room dependencies.  Look for the `TODO: Add Room dependencies` bit in `build.gradle`. You can go [here](https://developer.android.com/jetpack/androidx/releases/room#declaring_dependencies) to find the dependencies yourself, or just copy this block:

```groovy
    def room_version = "2.2.1"

    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
```

These two libraries will let us build out our database, DAO, and Entity and let us use all those annotations.


### Updating NewsStory as an Entity

To update our NewsStory as an Entity, we'll first have to annotate the class with `@Entity`

We also need a primary key defined for the class; since there really isn't a good fit, we'll just use the `title` field for now, so annotate that with `@PrimaryKey`

Lastly, to align more with SQL, we'll use snake case fields (fields with underlines between words like `example_variable`) rather than camel case (`exampleVariable`).  To do this, we need to annotate the fields we want to change with `@ColumnInfo`.  The ones we want to change are `publishedDate` and `articleAbstract`. Make sure to specify `@ColumnInfo(name = "<snake case name>")` for each field so we specify what name each column has.

The end result looks like this:

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

    ...getters/setters omitted
}
```

### Adding NewsStoryDao

Now we need to create our DAO. Going forward we'll call this the `NewsStoryDao`. Make sure you create it as an interface (or abstract class).

Our NewsStoryDao needs to have the `@Dao` annotation, so give it that.

We'll just go with some simple methods for now:
- A method to retrieve all stories that returns a list of NewsStory
- A method to retrieve a NewsStory based on the title we pass in
- A method to insert a list of stories into the database
- A method to delete a single story

For our first two methods, we'll need to annotate with `@Query` and fill in the queries.

To get all the stories, we can just use `"SELECT * FROM news_stories"` - this query will fetch everything from the `news_stories` table.

To find by title, make sure that we're passing in a `String title` parameter in the method signature.  With that, we can use `"SELECT * FROM news_stories WHERE title LIKE :title LIMIT 1"` for our query - this will fetch one story (because of the `LIMIT 1`) from our table where the title matches the title we passed in

Our insert and delete are simple - just annotate them with `@Insert` and `@Delete`, respectively.

The end result is a DAO that looks like this:

```java
@Dao
public interface NewsStoryDao {

    @Query("SELECT * FROM news_stories")
    List<NewsStory> getAllStories();

    @Query("SELECT * FROM news_stories WHERE title LIKE :title LIMIT 1")
    NewsStory findStoryByTitle(String title);

    @Insert
    void insertAllStories(List<NewsStory> stories);

    @Delete
    void deleteStory(NewsStory story);
}
```

### Adding NewsStoryDatabase

To round out our Room classes, let's implement our database

This is pretty simple; we just need to:
- Create the class
- Annotate the class with `@Database`
- Include the `entities` and `version` in our `@Database` annotation
- Make the class `abstract`
- Make sure the class `extends RoomDatabase`
- Make sure the class contains a method to get our `NewsStoryDao`

Doing all of these, your database will look like this:

```java
@Database(entities = {NewsStory.class}, version = 1)
public abstract class NewsStoryDatabase extends RoomDatabase {
    public NewsStoryDao newsStoryDao;
}
```

Let's also include a couple more things just to make things easier on us:
- A static variable of type `NewsStoryDatabase`
- A method to get the database, creating it if our static variable is null. This method needs to have a `Context` as a parameter so we can pass it in to our Room builder.

This follows what's called the Singleton pattern (where there's only one instance of a thing).  Adding this in, our class now looks like this:

```java
@Database(entities = {NewsStory.class}, version = 1)
public abstract class NewsStoryDatabase extends RoomDatabase {
    private static volatile NewsStoryDatabase INSTANCE;

    public NewsStoryDao newsStoryDao;

    public static NewsStoryDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context,
                    NewsStoryDatabase.class,
                    "news_story_database")
                    .build();
        }
        return INSTANCE;
    }
}
```

### Updating our NewsRepository

Now that all our Room stuff is in place, we can update the repository.  There's actually not much we need to do here.

We need to:
- Add a field for our NewsStoryDao
- Add the NewsStoryDao as a parameter in the constructor
- Add logic to our `fetchNewsStories()` function to check the database before making our API call (returning the list from the database if it exists)

Our repository looks like this after we've done all these updates:
```java
public class NewsRepository {
    private ApiMapper apiMapper;
    private TimesApiService newsApi;
    private NewsStoryDao newsStoryDao;

    public NewsRepository(TimesApiService newsApi, NewsStoryDao newsStoryDao) {
        this.newsApi = newsApi;
        this.newsStoryDao = newsStoryDao;
        apiMapper = new ApiMapper();
    }

    @Nullable
    @WorkerThread
    public List<NewsStory> fetchNewsStories() {
        List<NewsStory> stories = newsStoryDao.getAllStories();

        if (stories != null) {
            return stories;
        }

        try {
            Response<SearchResponse> response = newsApi.search(TimesApiService.API_KEY).execute();
            if (response.isSuccessful() && response.body() != null) {
                return apiMapper.mapStories(response.body());
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

### Updating NewsListFragment

Finally, we need to update our Fragment to make use of our repository.

We really only need to do two things here:
- Get a reference to our NewsStoryDao
- Update our NewsRepository constructor, passing in the NewsStoryDao

We can get our DAO with this line:

```java
NewsStoryDao dao = NewsStoryDatabase.getDatabase(getContext().getApplicationContext()).newsStoryDao;
```

and then we just update our NewsRepository constructor from this:
```java
newsRepository = new NewsRepository(apiService);
```
to this
```java
newsRepository = new NewsRepository(apiService, dao);
```