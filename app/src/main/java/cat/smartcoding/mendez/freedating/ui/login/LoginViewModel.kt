package cat.smartcoding.mendez.freedating.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    init {
        Log.i("GameViewModel", "GameViewModel created!")
    }
}