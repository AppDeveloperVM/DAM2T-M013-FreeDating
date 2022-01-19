package cat.smartcoding.mendez.freedating

import android.R.attr
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cat.smartcoding.mendez.freedating.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.content.res.AssetFileDescriptor
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import androidx.core.app.ActivityCompat.startActivityForResult
import android.R.attr.data
import android.widget.Toast
import android.R.attr.bitmap
import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import cat.smartcoding.mendez.freedating.ui.user.edit.UserEditFragment


/*
*
* Application Features:
Matches
Hot or Not
Profile Guests
Profile Likes
Gifts
Profile Photo/Cover
Profile Information
People Nearby
Search users
Friends System
Upgrades (Ghost Mode | Verified Badge | Off Ads)
In-app purchases
Credits – Virtual currency
Simple Gallery
Blocked List
Direct Messages with images/photos (Real time)
Submitting tickets to support from application
Abuse reports to photos and users
Facebook login|sign up|connect|disconnect
Support Emoji in photos comments, gifts comments and messages.
Profile photo and cover
Push notifications about profile likes, gifts, messages, friend requests and accept friend request
Personalize your notifications
AdMob banner
Rewarded video Ads
And much more …
*
* */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout;
    private lateinit var storageRef: StorageReference;
    private lateinit var auth: FirebaseAuth;
    private val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String
    lateinit var currentPhotoName: String
    lateinit var currentPhotoURI: Uri


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageRef = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference

        setSupportActionBar(binding.appBarMain.toolbar)
        val builder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage("Selecciona el método")?.setPositiveButton("Camara",
            DialogInterface.OnClickListener { dialog, id ->
                openCamera();
            })?.setNegativeButton("Galeria",
            DialogInterface.OnClickListener { dialog, id ->
                openGallery();
            })
        val dialog: AlertDialog? = builder?.create()
        builder?.create();

        binding.appBarMain.fab.setOnClickListener { view ->
            builder?.show()
        }











        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_gallery,
                R.id.nav_search,
                R.id.nav_profiles,
                R.id.nav_mailbox,
                R.id.nav_user,
                //R.id.nav_login
//                R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Bottom navigation setup
        val bottomNavigationView = binding.appBarMain.content.bottomNavigationView;
        bottomNavigationView.setupWithNavController(navController)

    }


    override fun onResume() {
        super.onResume()
        val sharedp = getSharedPreferences("cat.smartcoding.mendez.freedating_preferences", MODE_PRIVATE);

        if(sharedp.getBoolean("night_mode", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        navController.navigateUp()
//        navController.navigate()

        val id = item.itemId

        when(id){
            R.id.action_editprofile -> navController.navigate(R.id.nav_user_edit);
            R.id.action_settings -> navController.navigate(R.id.nav_settings);
        }

        /*if(id == R.id.action_editprofile){
            navController.navigate(R.id.nav_user_edit);
        }*/

        return super.onOptionsItemSelected(item)
//        when(item.itemId) {
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun openGallery(code: Int = 2) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

        startActivityForResult(gallery, code)
    }

    private fun openCamera(code: Int = 1) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "cat.smartcoding.mendez.freedating",
                        it
                    )
                    currentPhotoURI = photoURI;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, code)

                }
            }
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val prefix = "JPEG_${timeStamp}_";
        return File.createTempFile(
            prefix, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            currentPhotoName = prefix + ".jpg"
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {


            //if (File(currentPhotoPath).exists()) {
            var bitmaps =
                MediaStore.Images.Media.getBitmap(this.getContentResolver(), currentPhotoURI)
            var outba = ByteArrayOutputStream();
            if (bitmaps != null) {
                bitmaps.compress(
                    Bitmap.CompressFormat.JPEG,
                    70,
                    outba
                );
                val dadesbytes = outba.toByteArray();

                val pathReferenceSubir =
                    storageRef.child("/users/${FirebaseAuth.getInstance().currentUser?.uid}/images/$currentPhotoName")

                pathReferenceSubir.putBytes(dadesbytes);

                Toast.makeText(
                    applicationContext,
                    "Has been uploaded successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(applicationContext, "Operation was canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == 2) {

            if (data != null) {

                var bitmaps =
                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData())
                var outba = ByteArrayOutputStream();

                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val prefix = "JPEG_${timeStamp}_";

                bitmaps.compress(
                    Bitmap.CompressFormat.JPEG,
                    70,
                    outba
                );
                val dadesbytes = outba.toByteArray();

                val pathReferenceSubir =
                    storageRef.child("/users/${FirebaseAuth.getInstance().currentUser?.uid}/images/$prefix.jpg")
                //val pathReferenceSubir = storageRef.child("imagesnova/nova.jpg")
                pathReferenceSubir.putBytes(dadesbytes);
                Toast.makeText(
                    applicationContext,
                    "Has been uploaded successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(applicationContext, "Operation was canceled", Toast.LENGTH_SHORT)
                    .show()
            }

        }else if (requestCode == 3){
            var bitmaps =
                MediaStore.Images.Media.getBitmap(this.getContentResolver(), currentPhotoURI)
            var outba = ByteArrayOutputStream();
            if (bitmaps != null) {
                bitmaps.compress(
                    Bitmap.CompressFormat.JPEG,
                    70,
                    outba
                );
                val dadesbytes = outba.toByteArray();

                val pathReferenceSubir =
                    storageRef.child("/users/${FirebaseAuth.getInstance().currentUser?.uid}/profile_pic.jpg")

                pathReferenceSubir.putBytes(dadesbytes);

                Toast.makeText(
                    applicationContext,
                    "Has been uploaded successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(applicationContext, "Operation was canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        }else if (requestCode == 4) {


            if (data != null) {

                var bitmaps =
                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData())
                var outba = ByteArrayOutputStream();

                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val prefix = "JPEG_${timeStamp}_";

                bitmaps.compress(
                    Bitmap.CompressFormat.JPEG,
                    70,
                    outba
                );
                val dadesbytes = outba.toByteArray();

                val pathReferenceSubir =
                    storageRef.child("/users/${FirebaseAuth.getInstance().currentUser?.uid}/profile_pic.jpg")
                //val pathReferenceSubir = storageRef.child("imagesnova/nova.jpg")
                pathReferenceSubir.putBytes(dadesbytes);
                Toast.makeText(
                    applicationContext,
                    "Has been uploaded successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(applicationContext, "Operation was canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        }else if (requestCode == 5){
            var bitmaps =
                MediaStore.Images.Media.getBitmap(this.getContentResolver(), currentPhotoURI)
            var outba = ByteArrayOutputStream();
            if (bitmaps != null) {
                bitmaps.compress(
                    Bitmap.CompressFormat.JPEG,
                    70,
                    outba
                );
                val dadesbytes = outba.toByteArray();

                val pathReferenceSubir =
                    storageRef.child("/users/${FirebaseAuth.getInstance().currentUser?.uid}/background_pic.jpg")

                pathReferenceSubir.putBytes(dadesbytes);

                Toast.makeText(
                    applicationContext,
                    "Has been uploaded successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(applicationContext, "Operation was canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        }else if (requestCode == 6) {


            if (data != null) {

                var bitmaps =
                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData())
                var outba = ByteArrayOutputStream();

                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val prefix = "JPEG_${timeStamp}_";

                bitmaps.compress(
                    Bitmap.CompressFormat.JPEG,
                    70,
                    outba
                );
                val dadesbytes = outba.toByteArray();

                val pathReferenceSubir =
                    storageRef.child("/users/${FirebaseAuth.getInstance().currentUser?.uid}/background_pic.jpg")
                //val pathReferenceSubir = storageRef.child("imagesnova/nova.jpg")
                pathReferenceSubir.putBytes(dadesbytes);
                Toast.makeText(
                    applicationContext,
                    "Has been uploaded successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(applicationContext, "Operation was canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data.extras.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }*/


    fun setDrawer_Locked() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding.appBarMain.fab.hide();
        binding.appBarMain.content.bottomNavigationView.visibility = View.GONE;
        supportActionBar?.hide();
    }

    fun setDrawer_Unlocked() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        binding.appBarMain.fab.show();
        binding.appBarMain.content.bottomNavigationView.visibility = View.VISIBLE;
        supportActionBar?.show();

    }


    fun changeUserMenuData(name: String, email: String, image: String = "") {
        (binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_mainMenuName)).text = name
        (binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_mainMenuEmail)).text =
            email
        if(image != ""){
            //(binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.iv_mainMenuImage)).
        }
    }


    fun updateUserPicProfile(){
        /*UPDATE PROFILE PIC*/
        val builderProfilePic: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }
        builderProfilePic?.setMessage("Selecciona el método")?.setPositiveButton("Camara",
            DialogInterface.OnClickListener { dialog, id ->
                openCamera(3);
            })?.setNegativeButton("Galeria",
            DialogInterface.OnClickListener { dialog, id ->
                openGallery(4);
            })
        val dialogProfilePic: AlertDialog? = builderProfilePic?.create()
        builderProfilePic?.create();

        builderProfilePic?.show();
    }

    fun updateUserBackgroundPicProfile(){
        /*UPDATE PROFILE PIC*/
        val builderProfilePic: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }
        builderProfilePic?.setMessage("Selecciona el método")?.setPositiveButton("Camara",
            DialogInterface.OnClickListener { dialog, id ->
                openCamera(5);
            })?.setNegativeButton("Galeria",
            DialogInterface.OnClickListener { dialog, id ->
                openGallery(6);
            })
        val dialogProfilePic: AlertDialog? = builderProfilePic?.create()
        builderProfilePic?.create();

        builderProfilePic?.show();
    }


    override fun onStart() {
        super.onStart()


    }


}