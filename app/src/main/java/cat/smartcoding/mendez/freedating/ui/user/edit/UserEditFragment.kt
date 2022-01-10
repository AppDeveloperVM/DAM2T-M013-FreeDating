package cat.smartcoding.mendez.freedating.ui.user.edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.smartcoding.mendez.freedating.R

class UserEditFragment : Fragment() {

    companion object {
        fun newInstance() = UserEditFragment()
    }

    private lateinit var viewModel: UserEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}