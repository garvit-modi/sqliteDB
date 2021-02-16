package com.example.sqlitedb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.media.Image
import android.provider.MediaStore

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_CONTACTS = "EmployeeTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"

        private val TABLE_IMAGES = "Images"
        private val KEY_IMAGES_ID = "Images_Id"
        private val IMAGES_NAME = "Images"
        private val ID = "ID"
    }
    override fun onCreate(db: SQLiteDatabase?) {


        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        val CREATE_IMAGES_TABLE = ("CREATE TABLE " + TABLE_IMAGES + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_IMAGES_ID + " INTEGER," + IMAGES_NAME + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
        db?.execSQL(CREATE_IMAGES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)

        onCreate(db)
    }


    //method to insert data
    fun addEmployee(emp: EmpModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,emp.userEmail ) // EmpModelClass Phone
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to read data
    fun viewEmployee():List<EmpModelClass>{
        val empList:ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var userName: String
        var userEmail: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                val emp= EmpModelClass(userId = userId, userName = userName, userEmail = userEmail)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }
    //method to update data
    fun updateEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,emp.userEmail ) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_IMAGES_ID, emp.userId) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }



    //IMAGES...........................................................................................................



    fun addImages(images: DataPIckImages):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_IMAGES_ID, images.Images_ID)
        contentValues.put(IMAGES_NAME, images.Images)

        val success = db.insert(TABLE_IMAGES, null, contentValues)

        db.close()
        return success
    }

    fun viewImages():ArrayList<DataShowImages>{
        val imagesList:ArrayList<DataShowImages> = ArrayList<DataShowImages>()
        val selectQuery = "SELECT  * FROM $TABLE_IMAGES"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var Id: Int
        var userName: String

        if (cursor.moveToFirst()) {
            do {
                Id = cursor.getInt(cursor.getColumnIndex(ID))
                userId = cursor.getInt(cursor.getColumnIndex(KEY_IMAGES_ID))
                userName = cursor.getString(cursor.getColumnIndex(IMAGES_NAME))

                val emp= DataShowImages(ID = Id, Images_ID = userId, Images = userName,false)
                imagesList.add(emp)
            } while (cursor.moveToNext())
        }
        return imagesList
    }

    fun updateImages( ImagesList: ArrayList<DataPIckImages> , id :Int){
        val db = this.writableDatabase
        // EmpModelClass Name

        db.delete(TABLE_IMAGES,KEY_IMAGES_ID + "="+id,null)

        for (i in ImagesList)
        { val contentValues = ContentValues()
            contentValues.put(KEY_IMAGES_ID, i.Images_ID)
            contentValues.put(IMAGES_NAME, i.Images)
            db.insert(TABLE_IMAGES, null, contentValues)

        }
        // Updating Row
//        val success = db.update(TABLE_IMAGES, contentValues,KEY_IMAGES_ID+"="+images.Images_ID+ "AND" +IMAGES_NAME +"=" +images.Images,null)

        db.close()

    }

    fun deleteIamges(images: DataShowImages):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, images.ID)

        val success = db.delete(TABLE_IMAGES,"ID ="+images.ID,null)

        db.close()
        return success
    }
}