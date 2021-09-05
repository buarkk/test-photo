package org.dashnet.photofinder1

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.dashnet.photofinder1.databinding.RecyclerRowBinding
import java.net.URI

class ImageAdapter(val Imagelist: ArrayList<images>) :
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    class ImageHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        Picasso.get().load(Imagelist[position].downloadlink).into(holder.binding.imageViewid)

    }

    override fun getItemCount(): Int {
        return Imagelist.size
    }
}