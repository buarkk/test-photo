package org.dashnet.photofinder1

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import org.dashnet.photofinder1.databinding.ActivityMainBinding
import java.io.IOException
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    var adapter: ImageAdapter? = null

    val list = arrayListOf<images>()
    val downloadnumberlist = arrayListOf<Int>()

    val totalnumber: ArrayList<Any> = ArrayList()


    private lateinit var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        storage = Firebase.storage
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerview.layoutManager = GridLayoutManager(this, 2)
        adapter = ImageAdapter(list, this)
        binding.recyclerview.adapter = adapter
        listAllPaginated()
        adapter!!.notifyDataSetChanged()


        registerLauncher()
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

                print("*************")
                println(totalnumber.size)
                print("*************")
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
              //  println("url yazdırılıyor   *******  $downloadurl")
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


    fun checkStatus(view: android.view.View) {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            println("henüz izin verilmedi")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("give Permission",View.OnClickListener {
                    //izin istenecek.
                    println("İZİN İSTENİYOR")

                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                }).show()
            }else{
                    //request
                println("İZİN REQUESTED")

                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)


            }


        }else{
            println("izin daha önceden verildi.")
        }
        println(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE))

       // println("status checking")
    }
    fun getPermisson(view: android.view.View) {
        println("izin verildi")
    }


    private fun registerLauncher(){

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted
                println("izin şimdi verildi")
            } else {
                //permission denied
                Toast.makeText(this, "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }

    }
}
