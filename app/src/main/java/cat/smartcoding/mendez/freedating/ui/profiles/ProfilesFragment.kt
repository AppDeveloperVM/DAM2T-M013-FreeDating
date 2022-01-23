package cat.smartcoding.mendez.freedating.ui.profiles

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.smartcoding.mendez.freedating.R
import cat.smartcoding.mendez.freedating.Utils
import cat.smartcoding.mendez.freedating.databinding.ProfilesFragmentItemListBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.storage.StorageReference

/**
 * A fragment representing a list of Items.
 */
class ProfilesFragment : Fragment() {

    private lateinit var dbref: FirebaseDatabase
    //.getInstance("https://freedatingapp-66476-default-rtdb.europe-west1.firebasedatabase.app/")

    private var _binding: ProfilesFragmentItemListBinding? = null
    private val binding get() = _binding!!

    private var columnCount = 3

     lateinit var recyclerView: RecyclerView
    private lateinit var storageRef: StorageReference;
    private lateinit var newArrayList : ArrayList<ProfileItem>
    lateinit var imageId : Array<Int>
    lateinit var name : Array<String>

    private lateinit var userArrayList : ArrayList<ProfileItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userArrayList = arrayListOf<ProfileItem>()


//        arguments?.let {
//            columnCount = it.getInt(ARG_COLUMN_COUNT)
//        }

        Utils.obtenirProfiles(this)

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
                newArrayList = arrayListOf<ProfileItem>()
                //getUserdata()
            //adapter = ProfilesRecyclerViewAdapter(newArrayList)
            }
        }
        return view
    }

    fun getUserdata(profilesArrayList : ArrayList<ProfileItem>? = null) {


        for(i in imageId.indices){
            val images = ProfileItem(imageId[i],name[i])
            newArrayList.add(images)
        }

        //newArrayList
        recyclerView.adapter = profilesArrayList?.let { ProfilesRecyclerViewAdapter(it) }
    }

}