package cat.smartcoding.mendez.freedating

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
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageRef =FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference

        setSupportActionBar(binding.appBarMain.toolbar)
        val builder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage("Selecciona el método")?.setPositiveButton("Camara",
            DialogInterface.OnClickListener { dialog, id ->
                openCamera();
            })?.setNegativeButton("Galeria",
                DialogInterface.OnClickListener { dialog, id ->

                })
        val dialog: AlertDialog? = builder?.create()
        builder?.create();

        binding.appBarMain.fab.setOnClickListener { view ->
            builder?.show()
            /*if(!snackOn){
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }else{
                builder?.create();
            }*/

        }



        /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Selecciona un metodo")
                    .setItems(
                        arrayOf("a","b"),
                        DialogInterface.OnClickListener { dialog, which ->
                            // The 'which' argument contains the index position
                            // of the selected item
                        })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }*/




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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        navController.navigateUp()
//        navController.navigate()
        return super.onOptionsItemSelected(item)
//        when(item.itemId) {
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    private fun openCamera() {
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
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 1)

                    //ESPERAR A QUE LA IMAGEN SE GUARDE



                    var bitmaps = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI)
                    var outba = ByteArrayOutputStream();
                    bitmaps.compress(Bitmap.CompressFormat.JPEG,
                        70,
                        outba);
                    val dadesbytes = outba.toByteArray();
                    val pathReferenceSubir = storageRef.child("/users/${FirebaseAuth.getInstance().currentUser?.uid}/images/$currentPhotoName")
                    pathReferenceSubir.putBytes(dadesbytes);
                    //val pathReference = storageRef.child("images/nuevoFondo.jpg")
                    //val fileDescriptor = applicationContext.contentResolver.openAssetFileDescriptor(photoURI, "r")
                    //val fileSize = fileDescriptor!!.length
                    //val im = pathReference.getBytes(fileSize);
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
    }
    fun setDrawer_Unlocked() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        binding.appBarMain.fab.show();
        binding.appBarMain.content.bottomNavigationView.visibility = View.VISIBLE;

    }


    fun changeUserMenuData(name: String, email: String){
        (binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_mainMenuName)).text = name
        (binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_mainMenuEmail)).text = email
    }


    override fun onStart() {
        super.onStart()


    }


}