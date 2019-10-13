# Networking

We’ve looked at ways to structure our app, views to include, and screens to build.  Until now, we’ve only been thinking of how to do things on our local device, but most apps are going to have some sort of online feature.  Your app might be focused on serving up online content (like Reddit) or just using a connection to sync with some backend (think Google Photos), but connectivity is important.

First we’re going to talk about how to connect to the network using standard Android components.  We’re not going to talk about this very much because, honestly, it’s not used very much.  It’s important to know because they’re fundamentals of Android development and can give you some insight into implementation, but we’re going to focus on the real way that we usually implement a networking stack on Android - libraries.

A quick note on networking data: we primarily see data in two forms: XML and JSON.  Android has a built-in `XmlPullParser` class for parsing XML and `JSONObject`/`JSONArray` for parsing JSON.  I personally see a LOT more JSON than XML, but both are worth exploring.

## The Main Thread

One thing we have to talk about briefly before we jump in is how Android handles threading.  The main thread is also known as the UI thread because it’s where all the updates to our UI get made.  If you block this thread for too long (around 4 seconds), the system will display an alert about how your app isn’t responsive and allows the user to force quit it.

Because of how this thread works, we can never do networking activity on the main thread; in fact, our app will crash if we try, throwing a NetworkOnMainThreadException.

We’ll talk about a few different ways that we can do networking outside of the main thread.

## Declaring Permissions

First, we have to declare permissions. We’ll need the `android.permission.INTERNET permission` and the `android.permission.ACCESS_NETWORK_STATE` permission.  We can add permissions to our app by opening up the `AndroidManifest`
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Native Connection

We’ll briefly touch on how to connect to the network using built-in Android components. The documentation is a good resource for networking, but we’ll lay it all out here quickly.

The `AsyncTask` is often used for networking, as it happens in a background thread rather than the main thread.  Typically you’ll subclass an AsyncTask.  

The first thing you have to provide here are three parameter types (if you look at the class definition of AsyncTask, you’ll see something like `AsyncTask<Params, Progress, Result>`).  The first parameter type is the type of parameter you send to the AsyncTask (so a String or URL if you’re grabbing something from a URL), the second parameter type is Progress (the type of unit published to indicate progress - could be a number that you use to fill up a progress bar), and the last parameter is Result (which is what you get back from all your background work; maybe a Bitmap if you’re fetching an image from a URL).

AsyncTask requires you to overwrite three methods:

`onPreExecute()` - happens on the main/UI thread and is used for any setup you need to do before the background work starts

`doInBackground(Params params...)` - this happens on your background thread and is where all your networking would go; you also get the parameters passed in (like the URL) so you can grab them and use them in your networking

`onPostExecute(Result result)` - this happens on the main/UI thread after your background work is done and lets you update your UI; you also get the Result type as an argument so you can pass it on if you like.

So how do we actually **PERFORM** a network call? There’s a lot of Java code involved here, but here’s a typical request:
```java
InputStream stream = null;
HttpsURLConnection connection = null;
String result = null;
try {
    connection = (HttpsURLConnection) url.openConnection();
    // Timeout for reading InputStream arbitrarily set to 3000ms.
    connection.setReadTimeout(3000);
    // Timeout for connection.connect() arbitrarily set to 3000ms.
    connection.setConnectTimeout(3000);
    // For this use case, set HTTP method to GET.
    connection.setRequestMethod("GET");
    // Already true by default but setting just in case; needs to be true since this request
    // is carrying an input (response) body.
    connection.setDoInput(true);
    // Open communications link (network traffic occurs here).
    connection.connect();
    publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
    int responseCode = connection.getResponseCode();
    if (responseCode != HttpsURLConnection.HTTP_OK) {
        throw new IOException("HTTP error code: " + responseCode);
    }
    // Retrieve the response body as an InputStream.
    stream = connection.getInputStream();
        
    if (stream != null) {
        // Converts Stream to String with max length of 500.
        result = readStream(stream, 500);
    }
} finally {
    // Close Stream and disconnect HTTPS connection.
    if (stream != null) {
        stream.close();
    }
    if (connection != null) {
        connection.disconnect();
    }
}
return result;
```

The `readStream()` method:
```java
/**
 * Converts the contents of an InputStream to a String.
 */
public String readStream(InputStream stream, int maxReadSize)
        throws IOException, UnsupportedEncodingException {
    Reader reader = null;
    reader = new InputStreamReader(stream, "UTF-8");
    char[] rawBuffer = new char[maxReadSize];
    int readSize;
    StringBuffer buffer = new StringBuffer();
    while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
        if (readSize > maxReadSize) {
            readSize = maxReadSize;
        }
        buffer.append(rawBuffer, 0, readSize);
        maxReadSize -= readSize;
    }
    return buffer.toString();
}
```

And here’s how we might tie that all together; in this example we’ll use an AsyncTask subclass with String for Params (we’ll pass in a URL), Void for the Progress (since we’re not going to use it), and String for the Result.

```java
public class SampleAsyncTask extends AsyncTask<String, Void, String> {

    public SampleAsyncTask() {
        //Might pass a listener here to use in onPostExecute
    }

    @Override
    protected void onPreExecute() {
        //Set up our UI for loading
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        //Do all our network code we posted earlier
        return "Sample string";
    }

    @Override
    protected void onPostExecute(String result) {
        //Do something with our result
    }
}
```

So that, in a nutshell, is how we perform networking natively on Android.  There are a couple other steps we didn’t really touch on, like usually you’ll create an interface and you’d use that interface to get the Result in onPostExecute, but this is the quick and dirty understanding.


## How Networking **Usually** Works

I have never written an AsyncTask in a professional setting.  It’s a lot of code and it can be error-prone and there are much better ways to go about it.  This is where libraries come into play.  Libraries can help us cut down on boilerplate code and (at least for the good ones) can provide an incredibly optimized implementation of whatever we want to do.  They are very widely used across the industry, especially for networking.

Enter `OkHttp` - an HTTP library.  It can be used to make HTTP requests, get responses, handle network errors, and asynchronously execute your HTTP calls.  To get started, we need to add the dependency to our Module:app build.gradle.  We need to find the dependencies block and add this line:

`implementation "com.squareup.okhttp3:okhttp:4.2.1"`

After adding that line, we’ll most likely need to Sync Gradle.

There are a few steps to get up and running with OkHttp; we’ll need to:
- Build the OkHttp Client
- Build an HTTP Request
- Use our client to execute that request
- Handle the result

A very minimal implementation of those steps looks like this:

```java
//Create our client
OkHttpClient client = new OkHttpClient();

//We’re passing in our URL to this method
String run(String url) throws IOException {
    //Build our request with OkHttp’s Request.Builder()
    Request request = new Request.Builder()
    //Pass the URL to the OkHttp Builder
    .url(url)
    .build();
    
    //client.newCall(request) will build out our HTTP Call
    //while execute() will (synchronously) execute that call
    try (Response response = client.newCall(request).execute()) {
        //Return our Http Response body as a string
        return response.body().string();
    }
}
```

The above example shows how we’d synchronously call that code (the execute() method is synchronous) so we couldn’t use that code on the main thread.  OkHttp has a built-in asynchronous method called `enqueue()` that takes a Callback. That looks something like this:
```java
client.newCall(request).enqueue(new Callback() {
   @Override
   public void onFailure(@NotNull Call call, @NotNull IOException e) {
      //If our call fails, this gets called and we can see what exception was thrown
   }

   @Override
   public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
       //We get our response here
       //Note that the response might not be successful (the HTTP status code might indicate a 404 or 500 error) and can check with response.isSuccessful()
       response.isSuccessful()
       //We can get our response body with response.body() and make it a String with .string()
       response.body().string()
   }
});
```

Typically we don’t see OkHttp on its own - it’s often paired with another library called Retrofit.  `Retrofit` is a way to turn our HTTP API into a Java/Kotlin interface.  Rather than have to build out URLs ourselves and pass them into an OkHttp Request builder, we can lean on Retrofit to do a lot of that for us.

To build out Retrofit, we’ll need a few more steps:

- Add the Retrofit dependency to our app module build.gradle (and sync gradle after)
`implementation 'com.squareup.retrofit2:retrofit:2.6.2'`

- Create an interface class for your API (in this example we’ll use GitHub):
```java
public interface GitHubService {
  @GET("users/{user}/repos")
  Call<List<Repo>> listRepos(@Path("user") String user);
}
```
There’s a lot going on in just a few lines of code here, so let’s take it apart:

Our interface is called `GitHubService`; this is our service for getting data from GitHub

The `@GET` annotation relates to our normal HTTP verbs (GET, PATCH, POST, DELETE).  Using a @GET annotation over a method tells Retrofit that the method will be used to make an HTTP GET request. The string passed to @GET is the relative endpoint (so the URL will end with `“users/{user}/repos`”

The return type is shown as a `Call` (similar to OkHttp) with a type of List<Repo>. Call can be used to make a synchronous or asynchronous HTTP call

The `@Path(“user”)` annotation lets us pass in arguments to be used for placeholder values in a URL.  We can see the “{user}” part in our @GET annotation - the value of String user will be used in place of “{user}” since we annotated that argument with @Path(“user”).

To build our GitHubService, we’ll need to do the following:
```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .build();
GitHubService service = retrofit.create(GitHubService.class);
```
The above code uses Retrofit’s built-in Builder class to construct our Retrofit instance. We have to provide it a baseUrl; if we look at our last bit of code where we created the GitHubService interface, we see the string inside the @GET parentheses.  This is used with the baseUrl to build out a full url (`“https://api.github.com/users/{user}/repos”` in this case).

Using that Retrofit object, we can create our GitHubService class by calling retrofit.create() and passing the class as an argument.

We can perform API calls with Retrofit in two ways: synchronously or asynchronously.

Synchronously would look like this:
```java
Call<List<Repo>> repos = service.listRepos("octocat").execute();
```

While asynchronously looks like this:
```java
service.listRepos("octocat").enqueue(new Callback<List<String>>() {
   @Override
   public void onResponse(Call<List<String>> call, Response<List<String>> response) {
      
   }

   @Override
   public void onFailure(Call<List<String>> call, Throwable t) {

   }
   });
);
```
The response object in onResponse has a lot of data in it, including whether it succeeded, the body itself, and a lot of metadata (headers, etc) about the call.

Lastly, one thing to look at is something called serialization.  Typically we want to turn our API response into some object model that we’ve created to represent the data.  Retrofit has support for two libraries that make this really easy - GSON and Moshi.  We’ll talk about GSON for now, but Moshi is a fantastic library that I’d recommend over GSON.

We can add the GSON dependency with these line in our build.gradle:
```
implementation 'com.google.code.gson:gson:2.8.6'
implementation “com.squareup.retrofit2:converter-gson:2.6.2”
```

And then, when we’re building our Retrofit instance, we can specify the converter factory in the Builder like this:

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build();
```