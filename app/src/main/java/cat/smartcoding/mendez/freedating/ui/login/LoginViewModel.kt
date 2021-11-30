package cat.smartcoding.mendez.freedating.ui.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth;



    public fun onCreateView(){
        auth = FirebaseAuth.getInstance();
    }

    fun onStart(){

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

        }else{

        }
    }


    fun getAuth(): FirebaseAuth{
        return auth;
    }



    init {
        Log.i("GameViewModel", "GameViewModel created!")
    }
}