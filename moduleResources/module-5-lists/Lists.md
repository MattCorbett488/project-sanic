# Lists

Lists are a very important concept/tool to grasp in mobile development; almost every app has some form of a list - a list of news articles, songs, playlists...some even have lists of lists.

First we’re going to (very) briefly talk about the original list type and then spend most of the time talking about its replacement.

## ListView

The `ListView` was the original way we’d create a list.  This would be a vertically scrolling list and we’d hook up something called an Adapter to it.  The ListView by itself can’t do much - as engineers we have to create the adapters that tell the ListView how to work.  Basically, the adapter would create the view for each item in the list and we’d create that adapter with all the data it needed to create those views.

ListViews initially weren’t very efficient - it was on the engineer to make reusing views a priority.  Eventually, this came to be a list pattern called the ViewHolder pattern which quickly became a standard as it reused views and let us optimize for performance and prevent a lot of unnecessary view inflation.

That’s really all you need to know about ListViews (you’re free to look them up on your own) and they can technically be fine for very simple lists (like where each item in the list has only one line of text and isn’t a very complex layout). Mainly, however, we’re going to use the evolution of the ListView, known as `RecyclerView`.

## RecyclerView

The Android docs refer to a RecyclerView as:
> a more flexible and efficient ListView.

A RecyclerView has three parts:
- The RecyclerView itself
- A RecyclerView Adapter
- One or more ViewHolders

Both Adapter and ViewHolder are inner classes of RecyclerView, so you’d refer to them by `RecyclerView.Adapter<VH extends ViewHolder>` and `RecyclerView.ViewHolder`

### The RecyclerView
The RecyclerView is just the container for the list.  We usually don’t have to mess with it very much once we define it in our XML.  We can give it something called a LayoutManager that can help manage how our elements are laid out.  For instance, if we wanted to make our RecyclerView a linear list of vertically-scrolling items, we’d set the layout manager like this:

```java
recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
```
And if we wanted it to be Horizontal instead, we’d use `LinearLayoutManager.HORIZONTAL` instead of `LinearLayoutManager.VERTICAL`.

### The ViewHolder

The ViewHolder is responsible for one item in a RecyclerView - it’s one cell/row/whatever you want to call it; it’s basically the view for a list item.

You’ll most likely have a custom ViewHolder and that class will have to extend from RecyclerView.ViewHolder.  The only thing you really have to do is to create a constructor matching the RecyclerView.ViewHolder constructor (and the compiler will warn you to do so).

The simplest ViewHolder would look something like this:

```java
class TestViewHolder extends RecyclerView.ViewHolder {

   public TestViewHolder(@NonNull View itemView) {
       super(itemView);
   }
  
   public void testCall() {
       itemView.findViewById(R.id.example_id);
   }
}
```

Where our constructor takes in a View, and we pass that view to our super class.  Once we pass our view to our super class, we have access to a field called `itemView` which is the view of that particular item in the list.  We can use it to find other views (like a TextView that we need to set some text on or a Button that we need to set a click listener on). In the example above, we're using the itemView with findViewById

### The Adapter

So we have our list (the RecyclerView) and we have the view for each item in the list (the ViewHolder) and we have to link them together by using an Adapter.

We’ll usually create our own adapters, and these adapters have to extend from `RecyclerView.Adapter<VH>` (where VH is the ViewHolder class type that we’ve defined, like TestViewHolder above).

When we make an adapter, we have to override certain methods - `onCreateViewHolder`, `onBindViewHolder`, and `getItemCount()`.  A barebones adapter might look like this:

```java
class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {

   @NonNull
   @Override
   public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //Here we would create our TestViewHolder and return it like this:
      
       //Inflate our View first
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_layout, parent, false);
      
       //Create a TestViewHolder with the view we created
       return new TestViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
       //Here we have a TestViewHolder to set data on and we have the position this item
       //will be at in the list.  If we have some backing data, we can get the data from
       //that list using the position and set the data on the TestViewHolder
   }

   @Override
   public int getItemCount() {
       //Here we tell our adapter how big our list is - commonly this is the size of
       //whatever list/array/structure you're using as your backing data
       return 0;
   }
}
```


`onCreateViewHolder` handles creating the actual view and ViewHolder
`onBindViewHolder` is where we bind data to the ViewHolder (basically where we turn our data into whatever shows up on the screen)
`getItemCount` is where we tell our adapter how big our list is

It’s worth mentioning two other Adapter methods that can come in handy:

`public int getItemViewType(int position)` will let us tell the adapter what **TYPE** of view this is - this can be useful if you maybe want the first item in the list to be a header, so you can tell the adapter using this method that if the position is the first item in the list, use the Header type instead of the normal type.

`notifyDataSetChanged()` is useful if the underlying data has changed - maybe you removed some elements, maybe they need to be reordered, or just something has happened to make the data’s contents different from the list’s.  There are a lot of different varieties of this method (notifyItemRangeInserted, notifyItemInserted, notifyItemRemoved, etc) but notifyDataSetChanged is kind of like the nuclear option when you’re not sure what to use.