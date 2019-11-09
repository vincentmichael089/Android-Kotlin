package com.example.landmarkindonesia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar

class LandmarkDetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_IMG = "extra_img"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detail)
        val actionBar = supportActionBar

        setActionBarTitle(intent.getStringExtra(EXTRA_NAME))

        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val tvLandmarkName: TextView = findViewById(R.id.tv_landmark_name)
        val tvLandmarkDetail: TextView = findViewById(R.id.tv_landmark_detail)
        val ivLandmarkImage: ImageView = findViewById(R.id.iv_landmark_photo)

        tvLandmarkName.text = intent.getStringExtra(EXTRA_NAME)
        tvLandmarkDetail.text = intent.getStringExtra(EXTRA_DETAIL)
        ivLandmarkImage.setImageResource(intent.getIntExtra(EXTRA_IMG,0))

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
