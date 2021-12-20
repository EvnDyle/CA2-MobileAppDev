package eDoyle.CA2.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import eDoyle.CA2.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_character_image.toString())
    intentLauncher.launch(chooseFile)
}