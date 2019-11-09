package com.example.landmarkindonesia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import androidx.recyclerview.widget.RecyclerView

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {
    private lateinit var rvLandmark : RecyclerView
    private var list : ArrayList<LandmarkModel> = arrayListOf()
    private var title: String = "Landmark Indonesia"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setActionBarTitle(title)

        rvLandmark = findViewById(R.id.rv_landmarks)
        rvLandmark.setHasFixedSize(true)

        list.addAll(LandmarkData.listData)
        showRecyclerList()
    }

    private fun setActionBarTitle(title: String) {
        if(supportActionBar!=null){
            (supportActionBar as ActionBar).title = title
        }
    }

    private fun showRecyclerList() {
        rvLandmark.layoutManager = LinearLayoutManager(this)
        val cardViewLandmarkAdapter = CardViewLandmarkAdapter(list)
        rvLandmark.adapter = cardViewLandmarkAdapter

        cardViewLandmarkAdapter.setOnItemClickCallback(
            object : CardViewLandmarkAdapter.OnItemClickCallback{
                override fun onItemClicked(data: LandmarkModel) {
                    landmarkClicked(data)
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        clickAbout(item!!.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun clickAbout(itemId: Int){
        when(itemId){
            R.id.action_about ->{
                val intent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun landmarkClicked(landmark:LandmarkModel){
        //Toast.makeText(this, landmark.name, Toast.LENGTH_LONG).show()
        val intent = Intent(this@MainActivity,LandmarkDetailActivity::class.java)
        intent.putExtra(LandmarkDetailActivity.EXTRA_NAME,landmark.name)
        intent.putExtra(LandmarkDetailActivity.EXTRA_DETAIL,landmark.detail)
        intent.putExtra(LandmarkDetailActivity.EXTRA_IMG,landmark.photo)
        startActivity(intent)
    }
}
