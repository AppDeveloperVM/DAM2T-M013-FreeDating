package cat.smartcoding.mendez.freedating.ui.user.edit

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import cat.smartcoding.mendez.freedating.MainActivity
import cat.smartcoding.mendez.freedating.R
import cat.smartcoding.mendez.freedating.Utils
import cat.smartcoding.mendez.freedating.Utils.Companion.updateDadesUsuari
import cat.smartcoding.mendez.freedating.databinding.UserEditFragmentBinding
import com.google.android.gms.common.wrappers.Wrappers
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UserEditFragment : Fragment() {

    companion object {
        fun newInstance() = UserEditFragment()
    }

    private lateinit var viewModel: UserEditViewModel
    lateinit var binding: UserEditFragmentBinding;

    private val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String
    lateinit var currentPhotoName: String
    lateinit var currentPhotoURI: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.user_edit_fragment,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(UserEditViewModel::class.java)
        Utils.obtenirMainUserProfile(this, 1);


        binding.btnUserEditSave.setOnClickListener {
            updateDadesUsuari(activity as MainActivity, this);
        }







        //
        //  OPEN CAMERA
        //
        //

        @Throws(IOException::class)
        fun createImageFile(): File {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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


        fun openGallery() {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            Log.i("AYUDAME", "antes");
            startActivityForResult(gallery, 3)
        }

         fun openCamera() {

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent

                takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
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
                            requireActivity().applicationContext,
                            "cat.smartcoding.mendez.freedating",
                            it
                        )
                        currentPhotoURI = photoURI;
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, 3)

                    }
                }
            }

        }


        val builder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(context)
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

        binding.ivUserEditButtonProfile.setOnClickListener {
            builder?.show();
        }


        //
        //  OPEN CAMERA
        //
        //



       // return inflater.inflate(R.layout.user_edit_fragment, container, false)
        return binding.root;
    }


}