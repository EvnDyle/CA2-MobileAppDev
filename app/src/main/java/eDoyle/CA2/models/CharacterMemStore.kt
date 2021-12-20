package eDoyle.CA2.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class CharacterMemStore : CharacterStore {

    private val characters = ArrayList<CharacterModel>()

    override fun findAll(): List<CharacterModel> {
        return characters
    }

    override fun create(character: CharacterModel) {
        characters.add(character)
        logAll()
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

    override fun delete(character: CharacterModel){
        characters.remove(character)
    }

    fun logAll() {
        characters.forEach{ i("${it}") }
    }

}