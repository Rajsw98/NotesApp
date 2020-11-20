package com.example.savenotes

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {
    private val TAG:String = "MainActivity"
    var listNotes=ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

LoadQuery("%")
    }

    override fun onResume() {
        LoadQuery("%")
        super.onResume()
    }

    fun LoadQuery(title:String){

        var dbManager=DbManager(this)
        val projections= arrayOf("Id","Title","Description")
        val selectionargs= arrayOf(title)
        val cursor=dbManager.Query(projections,"Title like ?",selectionargs,"Title")
       listNotes.clear()
        if (cursor.moveToFirst()){
            do {
                val ID=cursor.getInt(cursor.getColumnIndex("Id"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val description=cursor.getString(cursor.getColumnIndex("Description"))
                Log.v(TAG,"Id= $ID")
                Log.d(TAG,"Title= $Title")
                Log.e(TAG,"Description= $description")
                listNotes.add(Note(ID,Title,description))
            }while (cursor.moveToNext())
        }
        var mynotesAdapter=MYNotesAdapter( this,listNotes)
        list_viewnote.adapter=mynotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menue,menu)
        val sv= menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        val sm=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query,Toast.LENGTH_LONG).show()
                LoadQuery("%"+ query +"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!==null){
            when(item.itemId){
                R.id.addNote->{
                    //go to add page
                    var intent=Intent(this,AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    inner class MYNotesAdapter:BaseAdapter{
        var listnotesadapter=ArrayList<Note>()
        var context: Context?=null
        constructor( context: Context, listnotesadapter:ArrayList<Note>):super(){
            this.listnotesadapter=listnotesadapter
            this.context=context
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       var myview=layoutInflater.inflate(R.layout.ticket,null)
            var myNode=listnotesadapter[position]
            myview.tv_title.text=myNode.noteName
            myview.tv_description.text=myNode.noteDes
            myview.iv_delete.setOnClickListener(View.OnClickListener {
                var dbManager=DbManager(this.context!!)
                val selectionargs= arrayOf(myNode.noteId.toString())
                dbManager.Delete("Id=?",selectionargs)
                LoadQuery("%")
            })
            myview.iv_edit.setOnClickListener(View.OnClickListener {
                Gotoupdate(myNode)
            })
            return myview
        }
        override fun getCount(): Int {
          return  listnotesadapter.size
        }

        override fun getItem(position: Int): Any {
          return listnotesadapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }


    }
    fun Gotoupdate(note:Note){
        var intent=Intent(this,AddNotes::class.java)
        intent.putExtra("id",note.noteId)
        intent.putExtra("name",note.noteName)
        intent.putExtra("des",note.noteDes)
        startActivity(intent)
    }
}