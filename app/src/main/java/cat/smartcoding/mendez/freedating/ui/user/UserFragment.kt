package cat.smartcoding.mendez.freedating.ui.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.smartcoding.mendez.freedating.R
import cat.smartcoding.mendez.freedating.Utils
import cat.smartcoding.mendez.freedating.Utils.Companion.obtenirFotos
import cat.smartcoding.mendez.freedating.databinding.UserFragmentBinding
import cat.smartcoding.mendez.freedating.ui.gallery.GalleryItem
import cat.smartcoding.mendez.freedating.ui.gallery.PhotoAdapter

class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel
    lateinit var binding: UserFragmentBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.user_fragment,
            container,
            false
        )

        //setRecyclerView
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        recyclerView = binding.rvUsergallery;
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context,3)
        recyclerView.setHasFixedSize(true)


        Utils.obtenirMainUserProfile(this, 0);
        getUserdata();

        //return inflater.inflate(R.layout.user_fragment, container, false)
        return binding.root;
    }


    private fun getUserdata() {
        Utils.obtenirFotos(this, recyclerView);
        //recyclerView.adapter = UserPhotoAdapter(newArrayList)
    }



}