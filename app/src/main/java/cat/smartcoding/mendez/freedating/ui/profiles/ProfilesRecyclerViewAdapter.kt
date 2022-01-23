package cat.smartcoding.mendez.freedating.ui.profiles

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64.decode
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import cat.smartcoding.mendez.freedating.Utils
import cat.smartcoding.mendez.freedating.databinding.ProfilesFragmentItemBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import java.security.AccessController.getContext
import java.util.*
import kotlin.collections.ArrayList

class ProfilesRecyclerViewAdapter(
    private val values: ArrayList<ProfileItem>,
    private val onClickListener: OnClickListener?

) : RecyclerView.Adapter<ProfilesRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder(ProfilesFragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.setIsRecyclable(false);


        val currentItem = values?.get(position)
        //val imageBytes = android.util.Base64.decode(currentItem.image, android.util.Base64.DEFAULT);
        //val imageBytes = android.util.Base64.decode(currentItem.image, android.util.Base64.DEFAULT);
        //val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        //currentItem.image?.let { holder.image.setImageResource(it) }  //currentItem.image
        //currentItem.image?.let { holder.image.setImageBitmap(it) }  //currentItem.image
        if (currentItem != null) {
            Log.i("AYUDA", currentItem.image.toString() + " PARA: " + currentItem.name)
        };
        if (currentItem != null) {
            if(currentItem.image != null) {
                holder.image.setImageBitmap(currentItem.image);
            }
        }
        if (currentItem != null) {
            holder.name.text = currentItem.name
        }

        holder.itemView.setOnClickListener {
           //navController.navigate(R.id.nav_user_edit)
            var name : String? = currentItem.name
            var gender : String? = currentItem.gender
            var birthdate: String? = currentItem.birthdate
            Toast.makeText(it.context,
                "Nombre: " + name +"\n"+
                        "Género: "+ gender +"\n"+
                        "BirthDate: "+ birthdate
                ,Toast.LENGTH_SHORT).show()
            Log.d("click!", it.toString())
        }
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

    class OnClickListener(val clickListener: (user: Utils.Companion.User) -> Unit) {
        fun onClick(user: Utils.Companion.User) = clickListener(user)
    }

}

