package cat.smartcoding.mendez.freedating

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cat.smartcoding.mendez.freedating.ui.gallery.GalleryFragment
import cat.smartcoding.mendez.freedating.ui.user.UserFragment
import cat.smartcoding.mendez.freedating.ui.user.edit.UserEditFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class Utils {

    companion object{
        private var database = FirebaseDatabase.getInstance("https://freedatingapp-66476-default-rtdb.europe-west1.firebasedatabase.app/")

        fun obtenirDadesUsuari(activity: MainActivity){
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if( uid == null ) return



            val myRef = database.getReference("/users/$uid")
            myRef.addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val value = snapshot.getValue<User>()
                    Log.d(ContentValues.TAG, "Value is: $value")
                    //val mostra = "${value?.?:""} ${value?.cognoms?:""} ${value?.cont?: ""}"

                    activity.changeUserMenuData(value?.name ?: "",value?.email ?: "")

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("AYUDA", "Failed to read value.", error.toException())
                    activity.changeUserMenuData("error","error")
                    //btModifica.isEnabled = true
                }
            })
        }
        fun updateDadesUsuari(activity: MainActivity, fragment: UserEditFragment){
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if( uid == null ) return



            val myRef = database.getReference("/users/$uid")

            var hashupdate: HashMap<String, Any> = HashMap();
            if(fragment.binding.etUserEditLocation.text.toString() != ""){
                hashupdate.set("location", fragment.binding.etUserEditLocation.text.toString());
            }
            if(fragment.binding.etUserEditOtherthings.text.toString() != ""){
                hashupdate.set("otherThings", fragment.binding.etUserEditOtherthings.text.toString());
            }
            if(fragment.binding.etUserEditDescription.text.toString() != ""){
                hashupdate.set("description", fragment.binding.etUserEditDescription.text.toString());
            }



            myRef.updateChildren(hashupdate);
        }




        fun obtenirFotos(fragment: Fragment): Unit? {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if( uid == null ) return null

            //images
            val myRef = database.getReference("/users/$uid")
            var storageRef = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference
            val pathReference = storageRef.child( "/users/$uid/images/")
            val im = pathReference.getBytes(50000)

            im.addOnSuccessListener {
                var bitmap = BitmapFactory.decodeByteArray( it, 0, it.size )

                (fragment as UserFragment)
                fragment.binding.ivUserProfile.setImageBitmap(bitmap)

                //setImageBitmap(bitmap)

            }.addOnFailureListener {
            }

            return null;
        }


        fun obtenirMainUserProfile(fragment: Fragment, type: Int = 0): Unit?{
            val uid = FirebaseAuth.getInstance().currentUser?.uid



            if( uid == null ) return null
            val myRef = database.getReference("/users/$uid")

            //images
            var storageRef = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference
            // sustituir img hardcodeada por img de perfil
            val pathReference = storageRef.child( "/users/$uid/images/JPEG_20211213_204507_.jpg")
            val im = pathReference.getBytes(50000)

            im.addOnSuccessListener {
                var bitmap = BitmapFactory.decodeByteArray( it, 0, it.size )

                if (type == 0) {
                    (fragment as UserFragment)
                    fragment.binding.ivUserProfile.setImageBitmap(bitmap)
                    fragment.binding.iwUserBanner.setImageBitmap(bitmap)
                }else if (type == 1){
                    (fragment as UserEditFragment)
                    fragment.binding.ivUserEditProfile.setImageBitmap(bitmap)
                    fragment.binding.iwUserEditBanner.setImageBitmap(bitmap)
                }
            }.addOnFailureListener {
            }

            myRef.addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot){

                    val value = snapshot.getValue<User>()

                    if(type == 0){
                        (fragment as UserFragment)
                        fragment.binding.tvUserName.text = value?.name;
                        fragment.binding.tvUserAge.text = value?.birthdate;
                        fragment.binding.tvUserGender.text = value?.gender;
                        fragment.binding.tvUserLocation.text = if (value?.location != "") value?.location else "Not specify";
                        fragment.binding.tvUserOther.text = if (value?.otherThings != "") value?.otherThings else "Nothing more";
                        fragment.binding.tvUserDescription.text = if (value?.description != "") value?.description else "This person has no description yet";
                    }else if (type == 1){
                        (fragment as UserEditFragment)
                        fragment.binding.tvUserEditName.text = value?.name;
                        fragment.binding.tvUserEditAge.text = value?.birthdate;
                        fragment.binding.tvUserEditGender.text = value?.gender;
                        fragment.binding.etUserEditLocation.hint = if (value?.location != "") value?.location else "Not specify";
                        fragment.binding.etUserEditOtherthings.hint = if (value?.otherThings != "") value?.otherThings else "Nothing more";
                        fragment.binding.etUserEditDescription.hint = if (value?.description != "") value?.description else "This person has no description yet";
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("AYUDA", "Failed to read value.", error.toException())
                }
            })
            return null;
        }





        data class User(
            var name: String = "",
            var email: String = "",
            var gender: String = "",
            var birthdate: String = "",
            var location: String? = "",
            var otherThings : String? = "",
            var description : String?= ""
        )
        data class UpdateUser(
            var location: String? = "",
            var otherThings: String? = "",
            var description: String? = ""
        )

    }






    class DatePickerFragment : DialogFragment() {
        private var listener: DatePickerDialog.OnDateSetListener? = null

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(activity as MainActivity, listener, year, month, day)
        }


        companion object {
            fun newInstance(listener: DatePickerDialog.OnDateSetListener): DatePickerFragment {
                val fragment = DatePickerFragment()
                fragment.listener = listener
                return fragment
            }
        }
    }
}