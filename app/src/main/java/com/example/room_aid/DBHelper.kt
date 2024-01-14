package com.example.room_aid

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, "RoomAidDB", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create user table
//        val createUserTable = "CREATE TABLE users (id INTEGER PRIMARY KEY, username TEXT, password TEXT)"
//        db.execSQL(createUserTable)

        // Grocery table
        val createGroceryTable = """
            CREATE TABLE groceries (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userId INTEGER,
                name TEXT,
                amount TEXT,
                price TEXT,
                completed INTEGER DEFAULT 0,
                FOREIGN KEY (userId) REFERENCES users(id)
            )
        """.trimIndent()
        db.execSQL(createGroceryTable)

        val createTaskTable = """
            CREATE TABLE tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userId INTEGER,
                name TEXT,
                description TEXT,
                category TEXT,
                dueDate TEXT,
                completed INTEGER DEFAULT 0,
                FOREIGN KEY (userId) REFERENCES users(id)
            )
        """.trimIndent()
        db.execSQL(createTaskTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 4) {
//            db.execSQL("DROP TABLE IF EXISTS users")
            db.execSQL("DROP TABLE IF EXISTS groceries")
            db.execSQL("DROP TABLE IF EXISTS tasks")
            onCreate(db)
        }
    }

    fun getCurrentVersion(): Int {
        val db = this.readableDatabase
        val version = db.version
        db.close()
        return version
    }

    //USER
    fun addUser(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("username", username)
            put("password", password)
        }
        val result = db.insert("users", null, contentValues)
        db.close()
        return result != -1L
    }

    fun getUserIdByUsername(username: String): Int? {
        val db = this.readableDatabase
        var userId: Int? = null

        val cursor = db.query(
            "users",                 // The table to query
            arrayOf("id"),           // The columns to return (just the ID)
            "username = ?",          // The columns for the WHERE clause
            arrayOf(username),       // The values for the WHERE clause
            null,           // group rows
            null,            // filter by row groups
            null             // sort order
        )

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }

        cursor.close()
        db.close()
        return userId
    }

    fun validateUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM users WHERE username = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        if (cursor.moveToFirst()) {
            db.close()
            cursor.close()
            return true
        }
        db.close()
        cursor.close()
        return false
    }

    // Grocery
    fun addGrocery(userId: Int, name: String, amount: String, price: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("userId", userId)
            put("name", name)
            put("amount", amount)
            put("price", price)
        }
        val result = db.insert("groceries", null, contentValues)
        db.close()
        return result != -1L
    }

    fun deleteGrocery(taskName: String) {
        val db = this.writableDatabase
        db.delete("groceries", "name = ?", arrayOf(taskName))
        db.close()
    }

    fun getAllGroceries(): List<String> {
        val groceryList = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM groceries", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(with(cursor) { getColumnIndex("name") })
                groceryList.add(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return groceryList
    }

//    fun getAllGroceriesById(userId: Int): List<String> {
//        val groceryList = mutableListOf<String>()
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT * FROM groceries WHERE userId = ?", arrayOf(userId.toString()))
//        if (cursor.moveToFirst()) {
//            do {
//                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
//                groceryList.add(name)
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//        db.close()
//        return groceryList
//    }

    fun updateGroceriesCompletion(taskId: Int, completed: Boolean) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("completed", if (completed) 1 else 0)  // SQLite uses 1 for true and 0 for false
        }

        // Update the task where the id matches
        db.update("groceries", contentValues, "id = ?", arrayOf(taskId.toString()))
        db.close()
    }

    fun getAllGroceriesById(userId: Int): List<Grocery> {
        val groceryList = mutableListOf<Grocery>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, name, amount, price, completed FROM groceries WHERE userId = ?", arrayOf(userId.toString()) )
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"))
                val price = cursor.getString(cursor.getColumnIndexOrThrow("price"))
                val completed = cursor.getInt(cursor.getColumnIndexOrThrow("completed")) != 0
                groceryList.add(Grocery(id, name, amount, price, completed))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return groceryList
    }

    // TASK
    fun addTask(userId: Int, name: String, description: String, category: String, dueDate: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("userId", userId) // Add the userId to the ContentValues
            put("name", name)
            put("description", description)
            put("category", category)
            put("dueDate", dueDate)
        }
        val result = db.insert("tasks", null, contentValues)
        db.close()
        return result != -1L
    }

    fun deleteTask(taskName: String) {
        val db = this.writableDatabase
        db.delete("tasks", "name = ?", arrayOf(taskName))
        db.close()
    }

    fun updateTaskCompletion(taskId: Int, completed: Boolean) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("completed", if (completed) 1 else 0)  // SQLite uses 1 for true and 0 for false
        }

        // Update the task where the id matches
        db.update("tasks", contentValues, "id = ?", arrayOf(taskId.toString()))
        db.close()
    }

    fun getAllTasks(): List<String> {
        val taskList = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(with(cursor) { getColumnIndex("name") })
                taskList.add(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return taskList
    }

    fun getAllTasksById(userId: Int): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, name, completed FROM tasks WHERE userId = ?", arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val completed = cursor.getInt(cursor.getColumnIndexOrThrow("completed")) != 0
                taskList.add(Task(id, name, completed))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return taskList
    }


//    fun getAllTasksById(userId: Int): List<String> {
//        val taskList = mutableListOf<String>()
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT * FROM tasks WHERE userId = ?", arrayOf(userId.toString()))
//        if (cursor.moveToFirst()) {
//            do {
//                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
//                taskList.add(name)
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//        db.close()
//        return taskList
//    }
}
