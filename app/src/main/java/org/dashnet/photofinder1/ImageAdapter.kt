package org.dashnet.photofinder1

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.dashnet.photofinder1.databinding.RecyclerRowBinding
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class ImageAdapter(val Imagelist: ArrayList<images>, private val context: Context) :
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    class ImageHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        Picasso.get().load(Imagelist[position].downloadlink).into(holder.binding.imageViewid)
        holder.binding.imageViewid.setOnClickListener {
            setupDialog(Imagelist[position])
        }

    }

    override fun getItemCount(): Int {
        return Imagelist.size
    }

    private fun setupDialog(images: images) {


        val dialog = Dialog(context, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_wallpaper)
        val dialogImageView = dialog.findViewById<ImageView>(R.id.dialogImageView)
       // val testdialogImageView = dialog.findViewById<ImageView>(R.id.testdialogImage)
        val setDownloadBTN = dialog.findViewById<Button>(R.id.setDownloadBtn)
        val dialogProgressBar = dialog.findViewById<ProgressBar>(R.id.dialogProgressBar)


        dialogProgressBar.visibility = View.VISIBLE
        setDownloadBTN.visibility = View.GONE

        //load image into Picasso
        Picasso.get().load(images.downloadlink)
            .into(dialogImageView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    setDownloadBTN.visibility = View.VISIBLE
                    dialogProgressBar.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    Log.d("errorLoad", e.toString())
                }

            })

        setDownloadBTN.setOnClickListener {

            //Generating a file name
            val time = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault()).format(
                System.currentTimeMillis()
            )
            val filename = "${time}.jpg"

            //Output stream
            var fos: OutputStream? = null

            //For devices running android >= Q
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //getting the contentResolver
                context?.contentResolver?.also { resolver ->

                    //Content resolver will process the contentvalues
                    val contentValues = ContentValues().apply {

                        //putting file information in content values
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }

                    //Inserting the contentValues to contentResolver and getting the Uri
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                    //Opening an outputstream with the Uri that we got
                    fos = imageUri?.let { resolver.openOutputStream(it) }
                }
            } else {
                //These for devices running on android < Q
                //So I don't think an explanation is needed here
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                fos = FileOutputStream(image)
            }

            fos?.use {
                val bitmap = dialogImageView.drawable.toBitmap()
                //Finally writing the bitmap to the output stream that we opened
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(context, "DOWNLOADED", Toast.LENGTH_SHORT).show()
            }


            /*

            val wrapper = ContextWrapper(it.context)
            var file = wrapper.getDir("TESTDOWNLOADS", Context.MODE_PRIVATE)
            file = File(file, "${UUID.randomUUID()}.jpg")

            try {
                val bitmap = dialogImageView.drawable.toBitmap()
                val stream:OutputStream=FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
                Toast.makeText(context, "DOWNLOADED", Toast.LENGTH_SHORT).show()

                stream.flush()
                stream.close()


                /*
                                val stream = FileOutputStream(file)
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                stream.flush()
                stream.close()
                Toast.makeText(context, "DONE", Toast.LENGTH_SHORT).show()

                 val bitmap = dialogImageView.drawable.toBitmap()

                var outputStream: FileOutputStream? = null
                val file = Environment.getExternalStorageDirectory()
                val dir = File(file.absolutePath.toString() + "/MyPics")
                dir.mkdirs()

                val filename = String.format("%d.png", System.currentTimeMillis())
                val outFile = File(dir, filename)
                try {
                    outputStream = FileOutputStream(outFile)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                try {
                    outputStream!!.flush()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                try {
                    outputStream!!.close()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }


                val dir = File(Environment.getExternalStorageDirectory(), "SaveImage")

                if (!dir.exists()) {
                    dir.mkdir()
                }

                val bitmap = dialogImageView.drawable.toBitmap()

                val file = File(dir, System.currentTimeMillis().toString() + ".jpg")
                try {
                    outputStream = FileOutputStream(file)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                Toast.makeText(context, "Successfuly Saved", Toast.LENGTH_SHORT).show()

                try {
                    outputStream?.flush()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    outputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                //to get the image from the ImageView (say iv)
                //to get the image from the ImageView (say iv)
                val bitmap: Bitmap = dialogImageView.drawable.toBitmap()



                var outStream: FileOutputStream? = null
                val sdCard = Environment.getExternalStorageDirectory()
                val dir = File(sdCard.absolutePath + "/YourFolderName")
                dir.mkdirs()
                val fileName = String.format("%d.jpg", System.currentTimeMillis())
                val outFile = File(dir, fileName)
                outStream = FileOutputStream(outFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                outStream.flush()
                outStream.close()

                Toast.makeText(context, "downladed", Toast.LENGTH_SHORT).show()


                var request=DownloadManager.Request(Uri.parse(url))
                    .setTitle("Image")
                    .setDescription("Image-describtion")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setAllowedOverMetered(true)

                var dm: DownloadManager? = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                mydownloadid=dm.equals(request)

                val bitmap: Bitmap = dialogImageView.drawable.toBitmap()
                val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                    System.currentTimeMillis()
                )
                val path = Environment.getExternalStorageDirectory()
                val dir = File("$path/DCIM")
                dir.mkdirs()
                val imagename = "$time.PNG"
                val file = File(dir, imagename)
                var out: OutputStream? = null

                 */


            } catch (e: Exception) {
                Toast.makeText(context, "DOWNLOADING FAIL", Toast.LENGTH_SHORT).show()
            }

             */


        }

        dialog.show()

    }


}
