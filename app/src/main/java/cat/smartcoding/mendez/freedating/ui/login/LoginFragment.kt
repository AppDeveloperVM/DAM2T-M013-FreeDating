package cat.smartcoding.mendez.freedating.ui.login

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import cat.smartcoding.mendez.freedating.R
import cat.smartcoding.mendez.freedating.databinding.FragmentLoginBinding


class LoginFragment : FragmentActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java];
        binding.viewModel = viewModel;
        //binding.lifecycleOwner = viewLife


        return binding.root;
    }
}