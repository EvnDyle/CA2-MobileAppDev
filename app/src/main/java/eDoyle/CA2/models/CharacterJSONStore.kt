package eDoyle.CA2.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import eDoyle.CA2.helpers.exists
import eDoyle.CA2.helpers.read
import eDoyle.CA2.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*


const val JSON_FILE = "characters.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<CharacterModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class CharacterJSONStore(private val context: Context) : CharacterStore {

    var characters = mutableListOf<CharacterModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<CharacterModel> {
        logAll()
        return characters
    }

    override fun create(character: CharacterModel) {
        character.id = generateRandomId()
        characters.add(character)
        serialize()
    }


    override fun update(character: CharacterModel) {
        var foundCharacter: CharacterModel? = characters.find { p -> p.id == character.id }
        if (foundCharacter != null) {
            foundCharacter.name = character.name
            foundCharacter.description = character.description
            foundCharacter.image = character.image
            logAll()
        }
    }

    override fun delete(character: CharacterModel) {
        characters.remove(character)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(characters, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        characters = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        characters.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}