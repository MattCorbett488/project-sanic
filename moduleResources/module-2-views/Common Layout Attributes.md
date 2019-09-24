# Common Layout Attributes

## Views

#### Layout Width/Height

These attributes will set the width and height of the view

`android:layout_width="wrap_content|match_parent|<int value>dp"`
`android:layout_height="wrap_content|match_parent|<int value>dp"`

#### Padding and Margins

These attributes will set padding or margin on their respective side. `8dp` is used here, but can also be a dimension value e.x. `@dimen/example_dimen_name`

`android:paddingStart="8dp"`
`android:paddingEnd="8dp"`
`android:paddingTop="8dp"`
`android:paddingBottom="8dp"`

`android:layout_marginStart="8dp"`
`android:layout_marginTop="8dp"`
`android:layout_marginEnd="8dp"`
`android:layout_marginBottom="8dp"`

#### Visibility

`android:visibility="visible|invisible|gone"`

#### ID

`android:id="@+id/example_id"`


## ConstraintLayout

These attributes will constrain one side of a View to the specified side of another view (or the parent)
e.x. `app:layout_constraintStart_toStartOf="@id/view_id"` will constrain the start of the View to the start of the View
with the ID `@id/view_id`

`app:layout_constraintStart_toStartOf="@id/view_id|parent"`
`app:layout_constraintStart_toEndOf="@id/view_id|parent"`

`app:layout_constraintEnd_toEndOf="@id/view_id|parent"`
`app:layout_constraintEnd_toStartOf="@id/view_id|parent"`

`app:layout_constraintTop_toTopOf="@id/view_id|parent"`
`app:layout_constraintTop_toBottomOf="@id/view_id|parent"`

`app:layout_constraintBottom_toBottomOf="@id/view_id|parent"`
`app:layout_constraintBottom_toTopOf="@id/view_id|parent"`

## RelativeLayout

`android:layout_centerHorizontal="true|false"` will center that View horizontally in its parent

`android:layout_centerVertical="true|false"` will center that View vertically in its parent

`android:layout_centerInParent="true|false"` will center that View both horizontally and vertically in the parent

`android:layout_alignTop="@id/other_view_id"` aligns the top of the View to the top of the View with the ID specified

`android:layout_alignBottom="@id/other_view_id"` aligns the bottom of the View to the bottom of the View with the ID specified

`android:layout_alignEnd="@id/other_view_id"` aligns the end of the View to the end of the View with the ID specified

`android:layout_alignStart="@id/other_view_id"` aligns the start of the View to the start of the View with the ID specified

`android:layout_alignParentStart="true|false"` will align the start of the View with the start of the parent

`android:layout_alignParentEnd="true|false"` will align the end of the View with the end of the parent

`android:layout_alignParentBottom="true|false"` will align the bottom of the View with the bottom of the parent

`android:layout_alignParentTop="true|false"` will align the top of the View with the top of the parent

`android:layout_below="@id/other_view_id"` places the top of this View below the View with the specified ID

`android:layout_above="@id/other_view_id"` places the bottom of this view above the View with the specified ID

`android:layout_toEndOf="@id/other_view_id"` places the start of this View at the end of the View with the specified ID

`android:layout_toStartOf="@id/other_view_id"` places the end of this View at the start of the View with the specified ID