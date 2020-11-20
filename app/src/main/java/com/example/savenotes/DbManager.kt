package com.example.savenotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager{
    val Dbname="MyNotes"
    val Dbtable="Notes"
    val ColId="Id"
    val ColTitle="Title"
    val ColDes="Description"
    val DbVersion=2
    val sqlCreatetable = ("CREATE TABLE IF NOT EXISTS "+ Dbtable +"(" + ColId + " INTEGER PRIMARY KEY AUTOINCREMENT ,"+ ColTitle +" TEXT,"+ ColDes +" TEXT"+")")
    var sqldb:SQLiteDatabase?=null
    constructor(context: Context){
        var Db=DbHealpernotes(context)
        sqldb=Db.writableDatabase

    }
   inner class DbHealpernotes:SQLiteOpenHelper{
       var context:Context?=null
       constructor(context: Context):super(context,Dbname,null,DbVersion){
           this.context=context
       }

        override fun onCreate(db: SQLiteDatabase?) {
           db!!.execSQL(sqlCreatetable)
            Toast.makeText(this.context,"database is created",Toast.LENGTH_LONG).show()


        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
           db!!.execSQL("Drop table IF EXISTS" + Dbtable)
        }

    }
    fun Insert(values: ContentValues):Long{
        val id=sqldb!!.insert(Dbtable,"",values)
        return id
    }
    fun Query(projection:Array<String>,selection:String,selectionargs:Array<String>,sororder:String):Cursor{
        val qb=SQLiteQueryBuilder()
        qb.tables=Dbtable
        val cursor=qb.query(sqldb,projection,selection,selectionargs,null,null,sororder)
        return cursor
    }
    fun Delete(selection:String,selectionargs:Array<String>): Int? {
        val count= sqldb?.delete(Dbtable,selection,selectionargs)
        return count
    }
    fun Update(values: ContentValues,selection: String,selectionargs: Array<String>):Int{
        val count=sqldb!!.update(Dbtable,values,selection,selectionargs)
        return count
    }
}