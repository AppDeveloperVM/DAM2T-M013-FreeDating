package cat.smartcoding.mendez.freedating

import android.content.ContentValues
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

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
                    val value = snapshot.getValue<newUser>()
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



        data class newUser(
            var name: String = "",
            var email: String = "",
            var gender: String = "",
            var age: Int = -1,
            var location: String? = "",
        )

    }




}