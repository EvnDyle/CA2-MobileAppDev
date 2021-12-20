package eDoyle.CA2.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import eDoyle.CA2.R
import eDoyle.CA2.databinding.ActivityCharacterBinding
import eDoyle.CA2.databinding.ActivityRegisterBinding
import eDoyle.CA2.main.MainApp

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener{
            when {

                TextUtils.isEmpty(binding.etRegisterEmail.text.toString().trim{ it <= ' ' }) -> {
                    Toast.makeText(this@RegisterActivity, "Please enter an email", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(binding.etRegisterPassword.text.toString().trim{ it <= ' ' }) -> {
                    Toast.makeText(this@RegisterActivity, "Please enter a password", Toast.LENGTH_SHORT).show()
                } else -> {
                        val email: String = binding.etRegisterEmail.text.toString().trim{ it <= ' ' }
                        val password: String = binding.etRegisterPassword.text.toString().trim{ it <= ' ' }

                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                            OnCompleteListener { task ->

                                if(task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(this@RegisterActivity, "You have been registered!", Toast.LENGTH_SHORT).show()

                                    val intent = Intent(this@RegisterActivity, MainApp::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this@RegisterActivity, task.exception.message.toString(), Toast.LENGTH_SHORT).show()
                                }


                            }
                        )
                    }
                    }

                }

            }

    }



    }

}