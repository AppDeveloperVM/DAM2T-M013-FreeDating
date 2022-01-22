package cat.smartcoding.mendez.freedating

import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import cat.smartcoding.mendez.freedating.ui.profiles.ProfileItem
import cat.smartcoding.mendez.freedating.ui.profiles.ProfilesFragment
import cat.smartcoding.mendez.freedating.ui.profiles.ProfilesRecyclerViewAdapter
import cat.smartcoding.mendez.freedating.ui.user.UserFragment
import cat.smartcoding.mendez.freedating.ui.user.edit.UserEditFragment
import com.google.android.gms.tasks.Task
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.security.AccessController.getContext


import java.util.*
import kotlin.collections.HashMap

class Utils {

    companion object{
        private var database = FirebaseDatabase.getInstance("https://freedatingapp-66476-default-rtdb.europe-west1.firebasedatabase.app/")
        private lateinit var dbref : DatabaseReference
        private var profilesArrayList : ArrayList<ProfileItem> = arrayListOf<ProfileItem>()
        private lateinit var context : Context

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

        fun obtenirProfiles(fragment: ProfilesFragment): Unit?{

            //context = fragment.requireContext()

            var arrayList : ArrayList<ProfileItem>? = null
            var storageRef = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference

            dbref = database.getReference("users")
            dbref.addValueEventListener(object: ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot){
                    if(snapshot.exists()){
                        for(userSnapshot in snapshot.children){

                            var info = userSnapshot.getValue(ProfileItem::class.java)
                            val id =  userSnapshot.key

                            val profilePic = storageRef.child( "/users/$id/profile_pic.jpg")
                            val pPim = profilePic.getBytes(5000000)

                            pPim.addOnSuccessListener {
                                var bitmap = BitmapFactory.decodeByteArray( it, 0, it.size )
                                info?.image = 0

                            }.addOnFailureListener {
                                Log.d("Exception","Couldnt get Profile Pic!");
                            }


                            val user = userSnapshot.getValue(ProfileItem::class.java)
                            if (info != null) {
                                profilesArrayList.add(user!!)
                            }
                        }
                    }

                    //userRecyclerView.adapter = MyAdapter(profilesArrayList)
                    //arrayList = profilesArrayList

                    (fragment as ProfilesFragment)
                    fragment.getUserdata(profilesArrayList)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            });

            return null;

        }


        fun obtenirFotos(fragment: Fragment): Unit? {

            //uid = id ?: currentUser
            var uid : String? = FirebaseAuth.getInstance().currentUser?.uid

            //user Data
            var myRef : DatabaseReference = if(uid == null)
                database.getReference("/users")
            else
                database.getReference("/users/$uid")

            //ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);

            //images
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


        fun obtenirMainUserProfile(fragment: Fragment, type: Int = 0): Unit?{
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            if( uid == null ) return null
            val myRef = database.getReference("/users/$uid")

            //images
            var storageRef = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference
            // sustituir img hardcodeada por img de perfil
            val profilePic = storageRef.child( "/users/$uid/profile_pic.jpg")
            val pPim = profilePic.getBytes(5000000)

            val profileBackgroundPic = storageRef.child( "/users/$uid/background_pic.jpg")
            val pBPim = profileBackgroundPic.getBytes(5000000)

            pPim.addOnSuccessListener {
                var bitmap = BitmapFactory.decodeByteArray( it, 0, it.size )

                if (type == 0) {
                    (fragment as UserFragment)
                    fragment.binding.ivUserProfile.setImageBitmap(bitmap)
                }else if (type == 1){
                    (fragment as UserEditFragment)
                    fragment.binding.ivUserEditProfile.setImageBitmap(bitmap)
                }
            }.addOnFailureListener {
            }

            pBPim.addOnSuccessListener {
                var bitmap = BitmapFactory.decodeByteArray( it, 0, it.size )

                if (type == 0) {
                    (fragment as UserFragment)
                    fragment.binding.iwUserBanner.setImageBitmap(bitmap)
                }else if (type == 1){
                    (fragment as UserEditFragment)
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


        private fun assetsToBitmap(fileName:String): Bitmap?{
            return try{
                val stream = context?.assets?.open(fileName)
                BitmapFactory.decodeStream(stream)
            }catch (e: IOException){
                e.printStackTrace()
                null
            }
        }


        data class User(
            var name: String = "",
            var email: String = "",
            var gender: String = "",
            var birthdate: String = "",
            var location: String? = "",
            var otherThings : String? = "",
            var description : String?= "",
            var image : String? = "" //assetsToBitmap("ic_launcher_background")
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