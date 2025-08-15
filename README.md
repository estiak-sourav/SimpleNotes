# Notes App (Kotlin + MVVM + Room)

A simple native Android app to add, list, edit, delete, and search notes. Data is stored locally using Room database. Built with MVVM and Kotlin Coroutines.

## Features
- Add, edit, delete notes
- Delete confirmation dialog for safety
- Swipe or long press to delete with undo
- Search notes by title or description

## Tech Stack
- Kotlin, Coroutines
- Room database
- MVVM architecture + Repository pattern

## How to Use
- Tap **+** to add a note.
- Tap a note to edit.
- Swipe left/right or longpress to delete (confirmation dialog appears).
- Tap **Delete** in the dialog to remove the note or **Cancel** to keep it.
- Tap search icon to filter notes by title or description.
