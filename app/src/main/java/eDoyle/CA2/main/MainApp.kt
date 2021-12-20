package eDoyle.CA2.main

import SignInActivity
import android.app.Application
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import eDoyle.CA2.models.CharacterJSONStore
import eDoyle.CA2.models.CharacterStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var characters: CharacterStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        characters = CharacterJSONStore(applicationContext)
        i("Character started")

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        binding.tv_user_id.text = "user ID:: $userId"
        binding.tv_email_id.text = "email ID:: $emailId"

        binding.btnLogout.setOnClicklistener{
            FirebaseAuth.getInstance.signOut()

            startActivity(Intent(this@MainApp, SignInActivity::class.java))
            finish()
        }


    }
}