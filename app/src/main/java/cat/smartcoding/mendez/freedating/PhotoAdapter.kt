package cat.smartcoding.mendez.freedating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.smartcoding.mendez.freedating.PhotoAdapter.*
import java.util.ArrayList
import android.widget.ImageView

class PhotoAdapter(private val gItems: ArrayList<GalleryItem>) :
    RecyclerView.Adapter<PhotoAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_custom_view,
        parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = gItems[position]
        holder.titleImage.setImageResource(currentItem.titleimage)
        holder.name.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return gItems.size
    }

    class MyViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){
        val titleImage : ImageView = itemView.findViewById(R.id.image)
        val name : TextView = itemView.findViewById(R.id.name)
    }

}