package cat.smartcoding.mendez.freedating.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth;


    public fun onCreateView(){
        auth = FirebaseAuth.getInstance();
    }

    fun onStart(){

    }



    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    init {
        Log.i("GameViewModel", "GameViewModel created!")
    }
}