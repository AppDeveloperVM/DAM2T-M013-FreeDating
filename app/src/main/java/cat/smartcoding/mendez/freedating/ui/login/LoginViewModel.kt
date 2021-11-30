package cat.smartcoding.mendez.freedating.ui.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth;


    private val _onLogin = MutableLiveData<Boolean>(false);
    var onLogin: LiveData<Boolean> = _onLogin;

    /*public fun onCreateView(){

    }*/




    fun getAuth(): FirebaseAuth{
        return auth;
    }
    fun onLoginButton(){
        _onLogin.value = true;
    }
    fun onLoginButtonComplete(){
        _onLogin.value = false;
    }



    init {
        auth = FirebaseAuth.getInstance();
    }
}