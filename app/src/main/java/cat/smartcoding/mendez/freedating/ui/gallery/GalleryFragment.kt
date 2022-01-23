package cat.smartcoding.mendez.freedating.ui.gallery

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.smartcoding.mendez.freedating.*
import cat.smartcoding.mendez.freedating.databinding.FragmentGalleryBinding
import cat.smartcoding.mendez.freedating.ui.profiles.ProfileItem
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerView: RecyclerView

    private var columnCount = 3
    private lateinit var storageRef: StorageReference;
    lateinit var newArrayList : ArrayList<GalleryItem>
    lateinit var imageId : Array<Int>
    lateinit var name : Array<String>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        newArrayList = arrayListOf<GalleryItem>()


        Utils.obtenirFotos(this);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        galleryViewModel =
            ViewModelProvider(this)[GalleryViewModel::class.java]

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        val view: View = binding.root

        //Firebase
        storageRef = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference

        val pathReference = storageRef.child( "/users/.../images/")
        val getImage = pathReference.child("images/")
        val localfile = File.createTempFile("tempImage","jpg")
        val im = pathReference.getBytes(50000)

        /*im.addOnSuccessListener {
            //llegim la imatge que estarà en "it"
            var bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            //ivBaixada.setImageBitmap( bitmap ) //posa el bitmap a la imatge
        }
        */

        //fill recyclerView gallery - PLACEHOLDER
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

        //recyclerView.layoutManager = GridLayoutManager(recyclerView.context,3)
        //recyclerView.setHasFixedSize(true)

        //getUserdata()


        //Utils.obtenirDadesUsuari(activity as MainActivity);

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                newArrayList = arrayListOf<GalleryItem>()
                //getUserdata()
                //adapter = ProfilesRecyclerViewAdapter(newArrayList)
            }
        }
        return view

    }

    fun getUserdata(imagesArrayList: java.util.ArrayList<Uri>? = null) {
        //ArrayList<Utils.Companion.GalleryItem> = null

        if (imagesArrayList != null) {
            for(imageItem in imagesArrayList){
                val img = GalleryItem(imageItem)
                newArrayList.add(img)
            }

        }

        recyclerView.adapter = PhotoAdapter(newArrayList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}