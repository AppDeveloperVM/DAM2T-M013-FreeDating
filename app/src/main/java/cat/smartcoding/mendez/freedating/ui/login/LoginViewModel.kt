package cat.smartcoding.mendez.freedating.ui.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth;



    /*public fun onCreateView(){

    }*/




    fun getAuth(): FirebaseAuth{
        return auth;
    }



    init {
        auth = FirebaseAuth.getInstance();
    }
}