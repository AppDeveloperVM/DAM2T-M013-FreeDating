package cat.smartcoding.mendez.freedating.ui.gallery

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.smartcoding.mendez.freedating.*
import cat.smartcoding.mendez.freedating.databinding.FragmentGalleryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.database.DatabaseReference




class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var storageRef: StorageReference;
    private lateinit var newArrayList : ArrayList<GalleryItem>
    lateinit var imageId : Array<Int>
    lateinit var name : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this)[GalleryViewModel::class.java]

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //-------Firebase STORAGE - GET IMAGES--------
        storageRef = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        Log.i("AYUDA", FirebaseAuth.getInstance().currentUser?.uid.toString());

        val databaseReference: DatabaseReference = firebaseDatabase.reference
        val pathReference = storageRef.child("/users/.../images/")
        val getImage = databaseReference.child("image")

        /*getImage.addListenerForSingleValueEvent(new ValueEventListener() {
        })*/

        //fill recyclerView gallery
        imageId = arrayOf(
            R.drawable.ic_launcher_round,
            R.drawable.ic_launcher_round,
            R.drawable.ic_launcher_round,
            R.drawable.ic_launcher_round,
            R.drawable.ic_launcher_round
        )
        name = arrayOf(
            "Juan",
            "Manuel",
            "Rocío",
            "Jenny",
            "Cristina"
        )

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context,3)
        recyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<GalleryItem>()
        getUserdata()


        //Utils.obtenirDadesUsuari(activity as MainActivity);

        return root
    }

    private fun getUserdata() {

        for(i in imageId.indices){
            val images = GalleryItem(imageId[i])
            newArrayList.add(images)
        }

        recyclerView.adapter = PhotoAdapter(newArrayList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}