package cat.smartcoding.mendez.freedating.ui.login


import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import cat.smartcoding.mendez.freedating.MainActivity
import cat.smartcoding.mendez.freedating.R
import cat.smartcoding.mendez.freedating.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {



    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: RegisterFragmentBinding
    private lateinit var auth: FirebaseAuth;
    private lateinit var database: FirebaseDatabase;





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.register_fragment,
            container,
            false
        )
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java];
        database = FirebaseDatabase.getInstance("https://freedatingapp-66476-default-rtdb.europe-west1.firebasedatabase.app/")

        binding.viewModel = viewModel;
        binding.lifecycleOwner = viewLifecycleOwner;
        auth = viewModel.getAuth();
        auth.signOut();

        viewModel.onRegister.observe(viewLifecycleOwner,{
            if(viewModel.onRegister.value == true){

                createAccount(
                    binding.etRegisterEmail.text.toString(),
                    binding.etRegisterPassword.text.toString(),
                    binding.etRegisterConfirmPassword.text.toString(),
                    binding.etRegisterName.text.toString()
                    );

                viewModel.onRegisterButtonComplete();
            }
        })

        return binding.root;
    }


    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setDrawer_Locked();
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }

    }



    private fun reload() {
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(user: FirebaseUser?) {
        if( user != null ) {

            (activity as MainActivity).setDrawer_Unlocked();
            NavHostFragment.findNavController(this).navigate(RegisterFragmentDirections.actionNavRegisterToNavGallery());

        }else{
            //COSAS QUE HACER PARA CUANDO NO ESTE LOGEADO
        }
    }

    private fun createAccount(email: String, password: String, passwordConfirm: String, name: String) {
        // [START create_user_with_email]
        binding.btnRegisterRegister.isEnabled = false;

        if(password == passwordConfirm) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    binding.btnRegisterRegister.isEnabled = true;
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                        saveUser(name, email);
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context, "Creation failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        }else{
            binding.btnRegisterRegister.isEnabled = true;
            Toast.makeText(
                context, "Passwords do not match.",
                Toast.LENGTH_SHORT
            ).show()
        }
        // [END create_user_with_email]
    }

    fun saveUser(name: String, email: String){
        val uid = FirebaseAuth.getInstance().currentUser?.uid;
        if( uid == null ) return

        val user =  newUser( name, email);

        val myRef = database.getReference("/users/$uid")
        myRef.setValue(user)
    }



    data class newUser(
        var name: String? = "",
        var email: String? = "",
    )

}