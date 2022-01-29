package cat.smartcoding.mendez.freedating.ui.profiles.details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cat.smartcoding.mendez.freedating.R
import cat.smartcoding.mendez.freedating.ui.gallery.GalleryItem

class ProfileDetailsPhotoAdapter (private val gItems: ArrayList<GalleryItem>? = null) :
    RecyclerView.Adapter<ProfileDetailsPhotoAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView: View

        itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.profile_details_gallery_view,
            parent,false)

        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Log.i("AYUDA", "A ENTRAO EN EL ADAPTER")

        val currentItem = gItems?.get(position)
        currentItem?.imageUrl?.let { holder.titleImage.setImageBitmap(it) }

    }

    override fun getItemCount(): Int {
        if (gItems != null) {
            return gItems.size
        }
        return 0
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleImage: ImageView = itemView.findViewById(R.id.img)
    }

}

