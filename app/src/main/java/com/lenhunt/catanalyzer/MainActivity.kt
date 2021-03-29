package com.lenhunt.catanalyzer

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
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

            val res: Resources = resources
            val locationStringArray = res.getStringArray(R.array.locations)
            val personalityStringArray = res.getStringArray(R.array.personality_text)
            val historyStringArray = res.getStringArray(R.array.history)
            val selectedLocation = locationStringArray[(locationStringArray.indices).random()]
            val selectedPersonality = personalityStringArray[(personalityStringArray.indices).random()]
            val selectedHistory = historyStringArray[(historyStringArray.indices).random()]

            val textToOutput = "${res.getString(R.string.output_text_1)}${selectedLocation}. ${res.getString(R.string.output_text_2)}${selectedPersonality}. ${res.getString(R.string.output_text_3)}${selectedHistory}."

            if (BuildConfig.DEBUG) {
                //textToOutput = textToOutput.plus(_imageURI.toString())
                //textToOutput = textToOutput.plus("\nSize of Array arr is: ${locationStringArray.size}\n").plus(picture_container.height.toString())
            }
            picture_container_text.text = textToOutput
        }
    }
}





