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
    public lateinit var binding: UserEditFragmentBinding;

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


        binding.ivUserEditButtonProfile.setOnClickListener {
            (activity as MainActivity).updateUserPicProfile();
            Utils.obtenirMainUserProfile(this, 1)
        }





       // return inflater.inflate(R.layout.user_edit_fragment, container, false)
        return binding.root;
    }


}