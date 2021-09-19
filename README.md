# Simple Todo

This is a refactoring of this app as it exists on `main`. The changes include:
 - Migrating from a multi-activity pattern to a single activity pattern with fragments
 - Using the Android Navigation Component to define and perform navigation in the app
 - Use ViewBinding rather than manually calling `findViewById`
 - Use Dagger/Hilt for dependency injection in the app
 - Use Timber for logging rather than the default Android.Log

The functionality of the app overall hasn't changed, but this is now a more accurate
example of what I would consider best practices when developing modern Android apps.

## Original README contents are below

Written as an example for Code Louisville's September 2021 Android course

## Summary

This is a simple todo app, intended to serve as an example of a simple Android application written
in Kotlin. It manages a single list of simple to do items and allows users to create, edit, mark
complete, as well as delete items from that list. It stores its data in a SQLite database using
Room to manage its data and models.

## App Overview

This app has the following features: Ability to create, edit, and delete todo items in a single todo
list. Items have a title, description, and isComplete value. These items are persisted to a SQLite
database locally on the device so that data is saved and reloaded when the app is relaunched.

Other notes:
 * This app uses a multi-activity, MVVM Pattern
 * There are three screens: TodoList (the default/home screen), CreateTodo, and EditTodo
 * Database interaction is done using Room

## Overview of classes

The Data Layer:

 * `SimpleTodoDatabase`: Our abstract Room database that will build our Dao (below)
 * `TodoItemDao`: interface describing a Data Access Object that Room will create.
   - Functions to observe, get, insert, update, and delete records
   - Uses Flows and suspending functions to not block the main thread
 * `TodoItem`: Data class representing the data model our app is built around

The UI Layer:
 * `TodoListActivity`/`TodoListViewModel`: View & ViewModel for our main screen
 * `CreateTodoActivity`/`CreateTodoViewModel`: View & ViewModel for adding a todo
 * `EditTodoActivity`/`EditTodoViewModel`: View & ViewModel for editing a todo

Classes for our ListAdapter:
 * `TodoListAdapter`: The main adapter that TodoListActivity uses to map the list of our TodoItems
   into its RecyclerView
 * `TodoListViewHolder`: Holds one instance of a UI element in our list, and provides a method to
   bind data to it.
 * `TodoItemDiffCallback`: Class for comparing TodoItems in order to animate transitions to the list
   - Contains methods to determine if elements represent the same record (they have the same ID), or
     if items have the exact same content (title, isComplete and description).
   - This is used by the RecyclerView in order to determine when items in the list need to be
     updated as the underlying data changes.

## Building and running

This app requires a minimum of Android 6.0 to run, as well as the SDK version 31 to build.

After downloading this project's source, opening it in Android Studio and building should be
sufficient to compile the app. In order to run, create an AVD if you don't have one (or if you don't
plan on testing on a physical Android device). During development, I used a