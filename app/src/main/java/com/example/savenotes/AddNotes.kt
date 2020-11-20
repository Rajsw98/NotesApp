package com.example.savenotes

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotes : AppCompatActivity() {
    val dbTable="Notes"
var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try {
            var bundle: Bundle? =intent.extras
            if (bundle != null) {
                id=bundle.getInt("id",0)
            }
            if (id!=0){
                if (bundle != null) {
                    Addtitle.setText(bundle.getString("name"))
                    Addsdescription.setText(bundle.getString("des"))
                }
            }

        }catch (ex:Exception){

        }

    }
    fun buAdd(view:View){
        var dbManager=DbManager(this)
        var values=ContentValues()

        values.put("Title",Addtitle.text.toString())
        values.put("Description",Addsdescription.text.toString())


        if(id==0) {
            val ID=dbManager.Insert(values)
            if (ID > 0) {
                Toast.makeText(this, "note is added", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "cannot  added", Toast.LENGTH_LONG).show()

            }
        }else{
            var selectioargs= arrayOf(id.toString())
            val ID=dbManager.Update(values,"Id=?",selectioargs)
            if (ID > 0) {
                Toast.makeText(this, "note is added", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "cannot  added", Toast.LENGTH_LONG).show()

            }
        }
    }
}