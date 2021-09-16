package org.dashnet.photofinder1

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import org.dashnet.photofinder1.databinding.ActivityMainBinding
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var adapter: ImageAdapter? = null

    val list = arrayListOf<images>()
    val downloadnumberlist = arrayListOf<Int>()

    val totalnumber: ArrayList<Any> = ArrayList()


    private lateinit var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        permissonCheck()


        storage = Firebase.storage
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerview.layoutManager = GridLayoutManager(this, 2)
        adapter = ImageAdapter(list, this)
        binding.recyclerview.adapter = adapter
        listAllPaginated()

    }

    private fun permissonCheck() {

    }

    fun listAllPaginated() {
        val storage = FirebaseStorage.getInstance()
        val listPageTask = storage.reference.listAll()

        listPageTask
            .addOnSuccessListener { listResult ->
                val items = listResult.items
                for (i in items) {
                    totalnumber.add(i.toString())
                }
                createListForDownload(totalnumber.size)
                createListForView()
            }.addOnFailureListener {
            }
    }

    private fun createListForView() {
        var index = 0
        val loadList = listOf<Int>(10, 100, 400, 400, (totalnumber.size - 400 - 400 - 100 - 10))
        var loadListIndex = 0
        val reference = storage.reference


        for (element in downloadnumberlist) {
            val imageName = "$element.jpg"
            reference.child(imageName).downloadUrl.addOnSuccessListener {
                val downloadurl = it.toString()
                val addelement = images(downloadurl)
                list.add(addelement)
                index++
                //  println(index)
                if (index == loadList[loadListIndex]) {
                    adapter!!.notifyDataSetChanged()
                    index = 0
                    loadListIndex++

                }
            }


        }
    }

    private fun createListForDownload(resim: Int) {
        for (i in 0..resim) {
            downloadnumberlist.add(i)
        }
        downloadnumberlist.shuffle()
    }
}
