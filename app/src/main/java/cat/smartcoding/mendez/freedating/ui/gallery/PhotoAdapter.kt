package cat.smartcoding.mendez.freedating.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.smartcoding.mendez.freedating.ui.gallery.PhotoAdapter.*
import android.widget.ImageView
import cat.smartcoding.mendez.freedating.R
import kotlin.collections.ArrayList

class PhotoAdapter(private val gItems: ArrayList<GalleryItem>? = null) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.gallery_custom_view,
        parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = gItems?.get(position)
        if (currentItem != null) {
            holder.titleImage.setImageURI(currentItem.imageUrl)
        }
    }

    override fun getItemCount(): Int {
        if (gItems != null) {
            return gItems.size
        }
        return 0
    }

    class MyViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){
        val titleImage : ImageView = itemView.findViewById(R.id.image)
    }

}