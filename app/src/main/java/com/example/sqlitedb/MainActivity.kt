package com.example.sqlitedb

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    companion object {
        var messagesList: ArrayList<DataPIckImages> = ArrayList()
    }

    lateinit var x: RecyclerView
    val list = ArrayList<EmpModelClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        x = findViewById<RecyclerView>(R.id.listView)

        onClicked()


    }

    fun saveRecord(view: View) {
        val id = u_id.text.toString()
        val name = u_name.text.toString()
        val email = u_email.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (id.trim() != "" && name.trim() != "" && email.trim() != "") {
            val status =
                databaseHandler.addEmployee(EmpModelClass(Integer.parseInt(id), name, email))
            if (status > -1) {
                Toast.makeText(applicationContext, "record save", Toast.LENGTH_LONG).show()
                onClicked()
                u_id.text.clear()
                u_name.text.clear()
                u_email.text.clear()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "id or name or email cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    fun viewRecord(view: View) {
        onClicked()
    }


    fun onClicked() {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val emp: List<EmpModelClass> = databaseHandler.viewEmployee()
        list.clear()
        for (e in emp) {
            list.add(EmpModelClass(e.userId, e.userName, e.userEmail))
        }
        x.adapter = MyAdapter(list, this)
        x.layoutManager = LinearLayoutManager(this)
    }


    fun BookComplaint(view: View) {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                //                capturePhoto()

                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.CAMERA
                    )
                    == PackageManager.PERMISSION_DENIED
                )
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        1888
                    )


                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1888)


            } else
                if (options[item] == "Choose from Gallery") {
                    //

                    /*  val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                startActivityForResult(intent, 2)*/
               */

                    openGalleryForImages()


                } else
                    if (options[item] == "Cancel") {
                        dialog.dismiss()
                    }
        })
        builder.show()

    }

    private fun openGalleryForImages() {

        if (Build.VERSION.SDK_INT < 19) {

            if (!checkSelfPermission()) {
                requestPermission()
            } else {
                var intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Choose Pictures"), 2
                )
            }
        } else {
            if (!checkSelfPermission()) {
                requestPermission()
            } else {
                var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, 2);
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            6036
        )
    }


    private fun checkSelfPermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//

        when (requestCode) {
//gallery
            2 ->
                if (resultCode == RESULT_OK) {


                    if (resultCode == Activity.RESULT_OK && requestCode == 2) {

                        // if multiple images are selected
                        if (data?.getClipData() != null) {
                            var count = data.clipData!!.itemCount
                            for (i in 0..count - 1) {
                                var imageUri: Uri = data.clipData?.getItemAt(i)!!.uri
                                if (imageUri != null) {
                                    messagesList.add(DataPIckImages(imageUri.toString(), 3, false))
//                                    val editor = shared.edit()
//                                    val json: String = gson.toJson(messagesList)
//                                    editor.putString("TAG", json)
//                                    editor.commit()
//                                    clicked()
                                }

                            }

                            clicked((messagesList))

                        } else if (data?.getData() != null) {
                            // if single image is selected
                            if (data.data != null) {

                                if (resultCode != RESULT_CANCELED) {
                                    val selectedImage = data.data
                                    if (selectedImage != null) {
                                        messagesList.add(
                                            DataPIckImages(
                                                selectedImage.toString(),
                                                3, false
                                            )
                                        )
//
                                        clicked(messagesList)
                                    }

                                }
                            }

                        }
                    }

                }
//camera
            1888 -> {
                if(data != null)
                {
                    val photo: Bitmap = data?.extras?.get("data") as Bitmap
//            mImageView?.setImageBitmap(photo)
                    var x = photo?.let { encodeTobase64(it) }
                    var path = data?.data
                    if (x != null) {
                        messagesList.add(DataPIckImages(x, 6, false))

                        clicked(messagesList)
                    }
                }
                clicked(messagesList)


            }

        }

    }

    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
        Log.d("Image Log:", imageEncoded)
        return imageEncoded
    }

    fun clicked(message: ArrayList<DataPIckImages>) {
//        message.add(DataPIckImages("",1))

//        adapter_Images = AdapterTOShowImages(this, message)
//        recyclerView?.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recyclerView?.adapter = adapter_Images
//        recyclerView?.isNestedScrollingEnabled = false
//        message.clear()

//        val position = adapter_Images.itemCount.minus(1)
//        recyclerView.scrollToPosition(position)
    }


}