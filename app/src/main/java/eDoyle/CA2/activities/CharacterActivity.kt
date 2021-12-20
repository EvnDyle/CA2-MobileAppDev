package eDoyle.CA2.activities

import android.content.Intent
import android.net.Uri
import eDoyle.CA2.databinding.ActivityCharacterBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import timber.log.Timber.i
import eDoyle.CA2.models.CharacterModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import eDoyle.CA2.R
import eDoyle.CA2.helpers.showImagePicker
import eDoyle.CA2.main.MainApp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class CharacterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var character = CharacterModel()
    var edit = false

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        binding.chooseImage.setOnClickListener {
            i("Select image")
        }

        app = application as MainApp

        i("Character Activity started...")

        if (intent.hasExtra("character_edit")) {
            edit = true
            character = intent.extras?.getParcelable("character_edit")!!
            binding.characterName.setText(character.name)
            binding.description.setText(character.description)
            binding.btnAdd.setText(R.string.save_character)
            binding.btnDel.setText(R.string.delete_character)
            Picasso.get()
                .load(character.image)
                .into(binding.characterImage)
            if (character.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_character_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            character.name = binding.characterName.text.toString()
            character.description = binding.description.text.toString()
            if (character.name.isEmpty()) {
                Snackbar.make(it,R.string.enter_character_name, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.characters.update(character.copy())
                } else {
                    app.characters.create(character.copy())
                }
            }
            i("add Button Pressed: $character")
            setResult(RESULT_OK)
            finish()
        }

        binding.btnDel.setOnClickListener(){
            i("delete Button Pressed: $character")
            app.characters.delete(character)
            finish()
        }


        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_character, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            character.image = result.data!!.data!!
                            Picasso.get()
                                .load(character.image)
                                .into(binding.characterImage)
                            binding.chooseImage.setText(R.string.change_character_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }


}