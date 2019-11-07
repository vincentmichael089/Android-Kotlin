package com.example.landmarkindonesia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar

class AboutActivity : AppCompatActivity() {

    private var title: String = "Tentang Saya"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val actionBar = supportActionBar

        setActionBarTitle(title)

        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun setActionBarTitle(title: String) {
        if(supportActionBar!=null){
            (supportActionBar as ActionBar).title = title
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home->{
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
