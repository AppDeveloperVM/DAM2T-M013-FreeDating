package cat.smartcoding.mendez.freedating

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import cat.smartcoding.mendez.freedating.ui.user.UserFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
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

        fun obtenirMainUserProfile(fragment: Fragment): Unit?{
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if( uid == null ) return null
            val myRef = database.getReference("/users/$uid")



            myRef.addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot){

                    val value = snapshot.getValue<User>()

                    (fragment as UserFragment)
                    fragment.binding.tvUserName.text = value?.name;
                    fragment.binding.tvUserAge.text = value?.birthdate;
                    fragment.binding.tvUserGender.text = value?.gender;
                    fragment.binding.tvUserLocation.text = if (value?.location != "") value?.location else "Not specify";
                    fragment.binding.tvUserOther.text = if (value?.otherThings != "") value?.otherThings else "Nothing more";
                    fragment.binding.tvUserDescription.text = if (value?.description != "") value?.description else "This person has no description yet";
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