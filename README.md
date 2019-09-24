# Android Workshop Series

Hi! This repo serves as our home base for our Android Workshop series in which we go from having zero Android knowledge to building a basic app (including networking, persisting data, and following best practices).

Our goal here is for everything to be taught by someone that knows the platform but also allow for self-teaching so that folks can go at their own pace.

We also know life gets hectic - that's pretty much how school **always** feels.  We don't want someone to fall off and skip our fourth module onward because life got unexpectedly busy when module three was being taught.  To that end, we're providing several "checkpoints" that people can use to start from wherever they missed (in addition to checking the content of that module here).  We'll do that by creating several branches noting the start/end states of each module.

## How to use this repo

Each module is represented by two different branches:

`module-<number>-<description of module>-start`

and

`module-<number>-<description of module>-end`

so to check out the start of our first module, `Introduction to Android`, we'd just go into our terminal and type

`git checkout module-1-introduction-to-android-start`

Additionally, if you want to access a more documented part of what each workshop consists of, there's a `moduleResources` directory that has folders for each module.  Inside each folder is a Markdown file containing the subject matter for each module as well as screenshots.

You'll also find resources for further exploration, like documentation around certain subjects we don't get a chance to dive deeper on.

Since just reading or talking at you gets boring *pretty fast*, we're also including some ideas for tasks that will give you a more hands-on approach to figuring out how all this stuff works.

## How do I get started?

You'll need Git and Android Studio installed to make the most out of this.

You can [install Android Studio here](https://developer.android.com/studio/install) and can see [how to install Git here](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

Once you've got both of those set up, use `git checkout` to checkout whichever module you want and look for the corresponding module folder in `moduleResources`