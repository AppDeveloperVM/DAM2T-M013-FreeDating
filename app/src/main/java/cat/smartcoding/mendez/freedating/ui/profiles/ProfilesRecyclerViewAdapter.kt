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
import androidx.navigation.fragment.NavHostFragment
import cat.smartcoding.mendez.freedating.Utils
import cat.smartcoding.mendez.freedating.databinding.ProfilesFragmentItemBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import java.security.AccessController.getContext
import java.util.*
import kotlin.collections.ArrayList
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.findFragment
import cat.smartcoding.mendez.freedating.ui.profiles.details.ProfileDetailsFragment


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
            var email: String? = currentItem.email
            var description : String? = currentItem.description
            var other : String? = currentItem.otherThings
            /*
            Toast.makeText(it.context,
                "Nombre: " + name +"\n"+
                        "Género: "+ gender +"\n"+
                        "BirthDate: "+ birthdate + "\n" +
                        "Email: " + email+ "\n" +
                        "Description: " + description + "\n"+
                        "Other: " + other
                ,Toast.LENGTH_SHORT).show()
            Log.d("click!", "ProfilesAdapter")
            */



            NavHostFragment.findNavController(it.findFragment()).navigate(
                ProfilesFragmentDirections.actionNavProfilesToProfileDetailsFragment(
                    currentItem
                )

            );

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

    class OnClickListener(val clickListener: (profile: ProfileItem) -> Unit) {
        fun onClick(profile: ProfileItem) = clickListener(profile)
    }

}

