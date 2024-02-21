package com.example.codeclauseinternship.pdfreader.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.codeclauseinternship.pdfreader.Model.FavModel
import com.example.codeclauseinternship.pdfreader.Model.HistoryModel


@Suppress("CAST_NEVER_SUCCEEDS", "UNREACHABLE_CODE")
class DBHelper(context: Context) : SQLiteOpenHelper(context, "DocumentDataBase", null, 1) {

    private val TABLE_HISTORY = "HistoryTable"
    private val TABLE_FAV = "FavTable"
    private val KEY_ID = "id"
    private val KEY_PATH = "path"
    private val KEY_DATE = "date"
    private val KEY_FILRENAME = "filename"
    private val KEY_SIZE = "size"


    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_HISTORY_TABLE =
            ("CREATE TABLE " + TABLE_HISTORY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PATH + " TEXT," + KEY_FILRENAME + " TEXT," + KEY_SIZE + " TEXT," + KEY_DATE + " TEXT" + ")")
        db?.execSQL(CREATE_HISTORY_TABLE)

        val CREATE_FAV_TABLE1 =
            ("CREATE TABLE " + TABLE_FAV + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PATH + " TEXT," + KEY_FILRENAME + " TEXT," + KEY_SIZE + " TEXT," + KEY_DATE + " TEXT" + ")")
        db?.execSQL(CREATE_FAV_TABLE1)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    fun insertHistory(historyModel: HistoryModel) {
        val writableDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_PATH, historyModel.path)
        contentValues.put(KEY_FILRENAME, historyModel.filename)
        contentValues.put(KEY_SIZE, historyModel.size)
        contentValues.put(KEY_DATE, historyModel.date)
        writableDatabase.insert(TABLE_HISTORY, null, contentValues)
    }

    fun insertFav(favModel: FavModel) {
        val writableDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_PATH, favModel.path)
        contentValues.put(KEY_FILRENAME, favModel.filename)
        contentValues.put(KEY_SIZE, favModel.size)
        contentValues.put(KEY_DATE, favModel.date)
        writableDatabase.insert(TABLE_FAV, null, contentValues)
    }

    fun getHistoryData(): ArrayList<HistoryModel> {
        val writableDatabase = writableDatabase
        val arrayList = ArrayList<HistoryModel>()
        val rawQuery =
            writableDatabase.rawQuery("select * from HistoryTable ORDER BY id DESC", null)
        rawQuery.moveToFirst()
        while (!rawQuery.isAfterLast) {
            val historyModel = HistoryModel(
                rawQuery.getString(1),
                rawQuery.getString(4),
                rawQuery.getString(2),
                false,
                rawQuery.getString(3)
            )
            arrayList.add(historyModel)
            rawQuery.moveToNext()
        }
        rawQuery.close()
        return arrayList
    }

    fun getFavData(): ArrayList<FavModel> {
        val writableDatabase = writableDatabase
        val arrayList = ArrayList<FavModel>()
        val rawQuery = writableDatabase.rawQuery("select * from FavTable ORDER BY id DESC", null)
        rawQuery.moveToFirst()
        while (!rawQuery.isAfterLast) {
            val historyModel = FavModel(
                rawQuery.getString(1),
                rawQuery.getString(4),
                rawQuery.getString(2),
                false,
                rawQuery.getString(3)
            )
            arrayList.add(historyModel)
            rawQuery.moveToNext()
        }
        rawQuery.close()
        return arrayList
    }

    fun isAlreadyAvailableHistory(str: String): Boolean {
        val writableDatabase = writableDatabase
        val arrayList: ArrayList<String> = ArrayList<String>()
        val rawQuery = writableDatabase.rawQuery(
            "select * from HistoryTable where path='$str'", null
        )
        rawQuery.moveToFirst()
        while (!rawQuery.isAfterLast) {
            arrayList.add(rawQuery.getString(1))
            rawQuery.moveToNext()
        }
        rawQuery.close()
        return arrayList.size > 0
    }

    fun isAlreadyAvailableFavourite(str: String): Boolean {
        val writableDatabase = writableDatabase
        val arrayList: ArrayList<String> = ArrayList<String>()
        val rawQuery = writableDatabase.rawQuery(
            "select * from FavTable where path='$str'", null
        )
        rawQuery.moveToFirst()
        while (!rawQuery.isAfterLast) {
            arrayList.add(rawQuery.getString(1))
            rawQuery.moveToNext()
        }
        rawQuery.close()
        return arrayList.size > 0
    }


    fun removeOldHistory(str: String): Int? {
        return Integer.valueOf(
            writableDatabase.delete(
                TABLE_HISTORY, "path = ? ", arrayOf(str)
            )
        )
    }


    fun removeOldFavDocument(str: String): Int? {
        return Integer.valueOf(
            writableDatabase.delete(
                TABLE_FAV, "path = ? ", arrayOf(str)
            )
        )
    }
}