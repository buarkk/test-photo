package org.dashnet.photofinder1

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import org.dashnet.photofinder1.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var adapter: ImageAdapter? = null

    val list = arrayListOf<images>()
    val downloadnumberlist = arrayListOf<Int>()

    val resimAdedi = 100


    private lateinit var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storage = Firebase.storage



        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)



        createListForDownload()
        createListForView()


        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = ImageAdapter(list)
        binding.recyclerview.adapter = adapter

    }

    private fun createListForView() {
        var index = 0
        val reference = storage.reference


        for (element in downloadnumberlist) {
            index++
            val imageName = "$element.jpg"
            reference.child(imageName).downloadUrl.addOnSuccessListener {
                val downloadurl = it.toString()
                val addelement = images(index, downloadurl)
                list.add(addelement)
                adapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun createListForDownload() {
        for (i in 0..resimAdedi) {
            downloadnumberlist.add(i)
        }
        downloadnumberlist.shuffle()
    }

}
