package eDoyle.CA2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import eDoyle.CA2.R
import eDoyle.CA2.adapters.CharacterAdapter
import eDoyle.CA2.adapters.CharacterListener
import eDoyle.CA2.databinding.ActivityCharacterListBinding
import eDoyle.CA2.main.MainApp
import eDoyle.CA2.models.CharacterModel

class CharacterListActivity : AppCompatActivity(), CharacterListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityCharacterListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCharacterListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadCharacters()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, CharacterActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCharacterClick(character: CharacterModel) {
        val launcherIntent = Intent(this, CharacterActivity::class.java)
        launcherIntent.putExtra("character_edit", character)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadCharacters() }
    }

    private fun loadCharacters() {
        showCharacters(app.characters.findAll())
    }

    fun showCharacters (characters: List<CharacterModel>) {
        binding.recyclerView.adapter = CharacterAdapter(characters, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }


}