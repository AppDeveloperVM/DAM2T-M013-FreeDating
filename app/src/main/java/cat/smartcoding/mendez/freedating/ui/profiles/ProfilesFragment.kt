package cat.smartcoding.mendez.freedating.ui.profiles

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.smartcoding.mendez.freedating.GalleryItem
import cat.smartcoding.mendez.freedating.PhotoAdapter
import cat.smartcoding.mendez.freedating.R
import cat.smartcoding.mendez.freedating.databinding.FragmentGalleryBinding
import cat.smartcoding.mendez.freedating.databinding.ProfilesFragmentItemListBinding
import com.google.firebase.storage.StorageReference

/**
 * A fragment representing a list of Items.
 */
class ProfilesFragment : Fragment() {

    private var _binding: ProfilesFragmentItemListBinding? = null
    private val binding get() = _binding!!

    private var columnCount = 3

    private lateinit var recyclerView: RecyclerView
    private lateinit var storageRef: StorageReference;
    private lateinit var newArrayList : ArrayList<GalleryItem>
    lateinit var imageId : Array<Int>
    lateinit var name : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let {
//            columnCount = it.getInt(ARG_COLUMN_COUNT)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.profiles_fragment_item_list, container, false)

        _binding = ProfilesFragmentItemListBinding.inflate(inflater, container, false)
        val view: View = binding.root

        recyclerView = binding.list

        //fill recyclerView gallery
        imageId = arrayOf(
            R.drawable.ic_launcher_round,
            R.drawable.ic_launcher_round,
            R.drawable.ic_launcher_round,
            R.drawable.ic_launcher_round,
            R.drawable.ic_launcher_round
        )
        name = arrayOf(
            "Jonny",
            "Manuel",
            "Rocío",
            "Jenny",
            "Cristina"
        )


        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                newArrayList = arrayListOf<GalleryItem>()
                getUserdata()
            //adapter = ProfilesRecyclerViewAdapter(newArrayList)
            }
        }
        return view
    }

    private fun getUserdata() {

        for(i in imageId.indices){
            val images = GalleryItem(imageId[i],name[i])
            newArrayList.add(images)
        }

        recyclerView.adapter = PhotoAdapter(newArrayList)
    }

}