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
import android.view.View
import android.widget.Toast
//import com.lenhunt.catanalyzer.R.string
import com.lenhunt.catanalyzer.R.string.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    //private var output_text = "@string/output_text"

    var outputText = ""



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
                    requestPermissions(permission, PERMISSION_CODE)
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
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
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
            picture_container.setImageURI(image_uri)

            // set the text here
            //outputText = setOutputText()

            //picture_container_text.text = "+"

            //val values = ContentValues()

            //var string = "@string/output_text_1"
            //var values1 = "@string/output_text_1"

            //val output_text_array = arrayOf(String())

            //var output_text_text = String()

            //output_text_array.plusElement(R.string.output_text_1.toString())

            //var output_text_text = R.string.output_text_1.toString().plus(R.string.location_text.toString()).plus(R.string.output_text_2.toString()).plus(R.string.personality_text.toString()).plus(R.string.output_text_3.toString()).plus(R.string.history.toString())

            //var outputTextText:String = R.string.output_text_1.toString()
/*
            output_text_text +=  R.string.output_text_1.toString()
            //output_text_array.push(R.string.output_text_1.toString())
            output_text_text +=  R.string.location_text.toString()
            output_text_text +=  R.string.output_text_2.toString()
            output_text_text +=  R.string.personality_text.toString()
            output_text_text +=  R.string.output_text_3.toString()
            output_text_text +=  R.string.history.toString()
            */


            //picture_container_text.text = output_text_1.toString()

            //val string: String = getString(R.string.your_string_id)

            picture_container_text.text = getString(output_text_1).plus(" ").plus(getString(location_text)).plus(getString(output_text_2)).plus(" ").plus(getString(personality_text)).plus(getString(output_text_3)).plus(" ").plus(getString(history))

            button_take_a_picture.visibility = View.INVISIBLE

            //picture_container_text.setText("@string/output_text")


        }
    }

    //private fun setOutputText(): String = ("@string/)



}