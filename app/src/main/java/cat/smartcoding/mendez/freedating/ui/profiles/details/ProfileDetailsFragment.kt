package cat.smartcoding.mendez.freedating.ui.profiles.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.smartcoding.mendez.freedating.R
import cat.smartcoding.mendez.freedating.Utils
import cat.smartcoding.mendez.freedating.databinding.ProfileDetailsFragmentBinding

class ProfileDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileDetailsFragment()
    }

    private lateinit var viewModel: ProfileDetailsViewModel
    lateinit var binding : ProfileDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.profile_details_fragment,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(ProfileDetailsViewModel::class.java)



        getUserdata()

        return binding.root
    }

    private fun getUserdata() {

    }
}