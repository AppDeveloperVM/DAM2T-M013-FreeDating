package cat.smartcoding.mendez.freedating.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth;



    private val _onRegister = MutableLiveData<Boolean>(false);
    var onRegister: LiveData<Boolean> = _onRegister;

    /*public fun onCreateView(){

    }*/




    fun getAuth(): FirebaseAuth {
        return auth;
    }
    fun onRegisterButton(){

        _onRegister.value = true;
    }
    fun onRegisterButtonComplete(){
        _onRegister.value = false;
    }



    init {
        auth = FirebaseAuth.getInstance();
    }
}