package cat.smartcoding.mendez.freedating.ui.login

import android.content.ContentValues.TAG
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cat.smartcoding.mendez.freedating.R
import cat.smartcoding.mendez.freedating.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
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
        binding.lifecycleOwner = viewLifecycleOwner;


        return binding.root;
    }


    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        val auth = viewModel.getAuth();
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                //btAutentifica.isEnabled = true
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                   // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                   // Toast.makeText(baseContext, "Authentication failed.",
                    //    Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }




}