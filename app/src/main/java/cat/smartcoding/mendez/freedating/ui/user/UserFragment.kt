package cat.smartcoding.mendez.freedating.ui.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import cat.smartcoding.mendez.freedating.R
import cat.smartcoding.mendez.freedating.databinding.UserFragmentBinding

class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel
    private lateinit var bindingNormal: UserFragmentBinding
    private lateinit var bindingEdit: ViewDataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingNormal = DataBindingUtil.inflate(
            inflater,
            R.layout.user_fragment,
            container,
            false
        )
        bindingEdit = DataBindingUtil.inflate(
            inflater,
            R.layout.user_edit_fragment,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)





        bindingNormal


        return inflater.inflate(R.layout.user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel



    }

}