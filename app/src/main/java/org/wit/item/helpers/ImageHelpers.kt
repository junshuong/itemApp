package org.wit.item.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import org.wit.item.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_item_image.toString())
    intentLauncher.launch(chooseFile)
}