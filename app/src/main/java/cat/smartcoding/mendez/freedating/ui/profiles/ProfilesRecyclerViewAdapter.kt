package cat.smartcoding.mendez.freedating.ui.profiles

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cat.smartcoding.mendez.freedating.databinding.ProfilesFragmentItemBinding

class ProfilesRecyclerViewAdapter(
    private val values: ArrayList<ProfileItem>
) : RecyclerView.Adapter<ProfilesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {



        return ViewHolder(ProfilesFragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = values[position]
        val uri : Uri = Uri.parse(currentItem.image.toString())
        holder.image.setImageURI(uri)  //currentItem.image
        holder.name.text = currentItem.name
    }

    override fun getItemCount(): Int = values.size

    //itemView: View
    inner class ViewHolder(binding: ProfilesFragmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.image
        val name: TextView = binding.name

        override fun toString(): String {
            return super.toString() + " '" + name.text + "'"
        }

    }

}