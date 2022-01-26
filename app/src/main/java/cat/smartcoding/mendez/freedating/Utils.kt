package cat.smartcoding.mendez.freedating

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cat.smartcoding.mendez.freedating.ui.gallery.GalleryItem
import cat.smartcoding.mendez.freedating.ui.gallery.PhotoAdapter
import cat.smartcoding.mendez.freedating.ui.profiles.ProfileItem
import cat.smartcoding.mendez.freedating.ui.profiles.ProfilesFragment
import cat.smartcoding.mendez.freedating.ui.profiles.ProfilesRecyclerViewAdapter
import cat.smartcoding.mendez.freedating.ui.user.UserFragment
import cat.smartcoding.mendez.freedating.ui.user.edit.UserEditFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException


import java.util.*
import kotlin.collections.HashMap

import java.time.LocalDate
import kotlin.collections.ArrayList


class Utils {

    companion object{
        private var database = FirebaseDatabase.getInstance("https://freedatingapp-66476-default-rtdb.europe-west1.firebasedatabase.app/")
        private var storageRef : StorageReference = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference
        private lateinit var dbref : DatabaseReference
        private var profilesArrayList : ArrayList<ProfileItem> = arrayListOf<ProfileItem>()
        private var imagesProfile : ArrayList<Uri> = arrayListOf<Uri>()
        private lateinit var context : Context

        private lateinit var arrayImagenesGallery: ArrayList<GalleryItem>;
        public var cargaImagenes = true;


        fun onCreate() {

            database.setPersistenceEnabled(true);

        }

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

        fun obtenirProfiles(fragment: ProfilesFragment, r: RecyclerView): Unit?{

            //context = fragment.requireContext()
            profilesArrayList.clear()

            var arrayList : ArrayList<ProfileItem>? = null
            //var storageRef = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference

            dbref = database.getReference("users")
            dbref.keepSynced(true)
            dbref.addListenerForSingleValueEvent(object: ValueEventListener{

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot){
                    if(snapshot.exists()){
                        var contador = 0;
                        val pref_age =  (fragment.activity as MainActivity).pref_age
                        val pref_gender = (fragment.activity as MainActivity).pref_gender;
                        val today = LocalDate.now();

                        for (userSnapshot in snapshot.children) {
                            //var info = userSnapshot.getValue(ProfileItem::class.java)
                            var info = userSnapshot.getValue<ProfileItem>()
                            val id =  userSnapshot.key

                            var bd = info?.birthdate;
                            val bda = bd?.split("/")?.toTypedArray()
                            /*val actual_birthdate =
                                bda?.get(2)?.toInt()?.let {
                                    LocalDate.of(bda?.get(2)?.toInt(), bda?.get(1)?.toInt(),
                                        it
                                    )
                                }*/

                            if(info?.gender!! != pref_gender) {
                                if(pref_gender != "All"){
                                contador++;
                                Log.i("AYUDA", "${info?.gender!!}__$pref_gender");
                                continue
                                }
                            }

                            var profilePic: StorageReference? = null
                            var user : ProfileItem? = null




                            profilePic = storageRef.child( "/users/$id/profile_pic.jpg")

                            val pPim = profilePic.getBytes(5000000)



                            pPim.addOnSuccessListener {


                                var bitmap = BitmapFactory.decodeByteArray( it,0,it.size)
                                bitmap = Bitmap.createScaledBitmap(
                                    bitmap, 250, 250, false
                                )
                                profilesArrayList.add(ProfileItem(bitmap, info?.name,  info?.gender,info?.birthdate));


                                info?.image = bitmap;

                            }.addOnFailureListener {

                                Log.d("Exception","Couldnt get Profile Pic!");
                                profilesArrayList.add(ProfileItem(null, info?.name, info?.gender,info?.birthdate));


                            }.addOnCompleteListener {
                                contador = contador+ 1;

                                if(snapshot.childrenCount <= contador){

                                    r.adapter = ProfilesRecyclerViewAdapter(profilesArrayList,null);
                                }
                            }
                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("The read failed: " , "Error: "+ "code " + error.code + ", " + error.details );
                }

            });

            return null;

        }


        fun obtenirFotos(fragment: Fragment, r: RecyclerView): Unit? {


            var uid : String? = FirebaseAuth.getInstance().currentUser?.uid ?: return null
            if( uid == null ) return null


            if(cargaImagenes == true) {

                arrayImagenesGallery = ArrayList<GalleryItem>();
                storageRef.child("/users/$uid/images/").listAll().addOnSuccessListener { listado ->



                    listado.items.forEach { item ->

                        val bytes = item.getBytes(5000000);

                        bytes.addOnSuccessListener {
                            arrayImagenesGallery.add(
                                GalleryItem(
                                    Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeByteArray(
                                            it,
                                            0,
                                            it.size
                                        ), 250, 250, false
                                    )

                                )
                            );
                            if (item == listado.items.last()) {
                                cargaImagenes = false;
                                r.adapter = PhotoAdapter(arrayImagenesGallery);
                            }


                        }.addOnFailureListener {

                        }
                    }

                }.addOnFailureListener {
                    Log.i("AYUDA", "FALLOOOOOOOOOOOO");
                }

            }else{
                r.adapter = PhotoAdapter(arrayImagenesGallery);
            }
            return null;
        }


        fun obtenirMainUserProfile(fragment: Fragment, type: Int = 0): Unit?{
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            if( uid == null ) return null
            val myRef = database.getReference("/users/$uid")

            //images
            //var storageRef = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference
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
            //var image : String? = "" //assetsToBitmap("ic_launcher_background")
        )
        data class UpdateUser(
            var location: String? = "",
            var otherThings: String? = "",
            var description: String? = ""
        )


       /* fun getBitmapFromURL(src: String?): Bitmap? {
            return try {
                val url = URL(src)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                val input: InputStream = connection.getInputStream()
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                // Log exception
                null
            }
        }*/
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