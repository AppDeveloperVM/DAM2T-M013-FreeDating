package cat.smartcoding.mendez.freedating.ui.gallery

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.smartcoding.mendez.freedating.*
import cat.smartcoding.mendez.freedating.databinding.FragmentGalleryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.database.DatabaseReference
import okio.Utf8.size
import java.io.File
import java.nio.file.Files.size


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

        //Firebase
        storageRef = FirebaseStorage.getInstance("gs://freedatingapp-66476.appspot.com").reference

        val pathReference = storageRef.child( "/users/.../images/")
        val getImage = pathReference.child("images/")
        val localfile = File.createTempFile("tempImage","jpg")
        val im = pathReference.getBytes(50000)

        im.addOnSuccessListener {
            //llegim la imatge que estarà en "it"
            var bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            //ivBaixada.setImageBitmap( bitmap ) //posa el bitmap a la imatge
        }


        //Utils.obtenirFotos(this);

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