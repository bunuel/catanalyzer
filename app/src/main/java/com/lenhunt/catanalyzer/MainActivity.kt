package com.lenhunt.catanalyzer


import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.lenhunt.catanalyzer.R.string.*
import com.lenhunt.catanalyzer.R.array.locations
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val _permissionCode = 1000
    private val _imageCaptureCode = 1001
    private var _imageURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //button click
        button_take_a_picture.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, _permissionCode)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        _imageURI = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, _imageURI)
        startActivityForResult(cameraIntent, _imageCaptureCode)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            _permissionCode -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            picture_container.setImageURI(_imageURI)

            //val values = ContentValues()

            //val randomNumbers: MutableList<Int> = getRandomNumber(3);

            val randomNumber = (0 until 10).random()
            val locationText: String = getString(locations[(randomNumber)])

            //randomNumber = (0 until 10).random()

           //var location_text: String = getString("location_00".plus(firstRandomNumber))

            //arrayOf(values.[values.[]])
            var textToOutput: String = getString(output_text_1).plus("\n").plus(locationText).plus("\n").plus(getString(output_text_2)).plus(" ").plus(getString(personality_text)).plus("\n").plus(getString(output_text_3)).plus(" ").plus(getString(history)).plus("\n")
            //button_take_a_picture.visibility = View.INVISIBLE
            if (BuildConfig.DEBUG) {
                textToOutput = textToOutput.plus(_imageURI.toString())
                textToOutput = textToOutput.plus("    ").plus(picture_container.height.toString())
            }
            picture_container_text.text = textToOutput
        }
    }

    //
    //private fun getRandomNumber(numberOfTimes: Int) : MutableList<Int> {
        //val list: MutableList<Int> = ArrayList(numberOfTimes)

        //list = (0 until 10).random()
        //for (i in 1..numberOfTimes) {
            // for() {
            //add(0 until 10).random().ArrayList()
            //list[i] = (0 until 10).random()
            //list += (0 until 10).random()
       //}

        //return list
        //}
    //}

}

private operator fun Int.get(i: Int): Int {
    return i
}

