package com.example.sqlitedb

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlitedb.MyAdapter.Companion.adapter1
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.update_dialog.*
import java.lang.Double.parseDouble

class MainActivity : AppCompatActivity() {

    companion object {
//        var messagesList: ArrayList<DataPIckImages> = ArrayList()
var Images: ArrayList<String> = ArrayList()
    }
    lateinit var adapter:MyAdapter
//    var Images: ArrayList<String> = ArrayList()
    var ImagesList: ArrayList<DataPIckImages> = ArrayList()
    lateinit var x: RecyclerView
    val list = ArrayList<EmpModelClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        x = findViewById<RecyclerView>(R.id.listView)

        onClicked()

        saveRecord.setOnClickListener {
            saveRecord(u_id.text.toString(), u_name.text.toString(),u_email.text.toString(),false)
        }

    }

    fun saveRecord(id:String,name:String,email:String,isUpdate:Boolean) {
        val id = id
        val name =name
        val email = email
        val string = id
        var numeric = true

       if(!isUpdate)
       {
           try {
               val num = parseDouble(string)
           } catch (e: NumberFormatException) {
               numeric = false
           }

           if (numeric)
           {
               var mess: ArrayList<String> = ArrayList()
               mess = Images


               val databaseHandler: DatabaseHandler = DatabaseHandler(this)
               if (id.trim() != "" && name.trim() != "" && email.trim() != "") {
                   val status = databaseHandler.addEmployee(EmpModelClass(Integer.parseInt(id), name, email))

                   if (mess.size != 0) {
                       ImagesList.clear()
                       for (i in mess) {

                           ImagesList.add(DataPIckImages(id.toInt(), i, false))
                           databaseHandler.addImages(DataPIckImages(Integer.parseInt(id), i, false))
                       }
                   }
//            val Images_status = databaseHandler.addImages(DataPIckImages(Integer.parseInt(id), name,false))
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
           else
               Toast.makeText(this, "$string is not a number" , Toast.LENGTH_LONG).show()

       }else
       {
           var mess: ArrayList<String> = ArrayList()
           mess = Images
           val databaseHandler: DatabaseHandler = DatabaseHandler(this)
           //calling the updateEmployee method of DatabaseHandler class to update record
           val status = databaseHandler.updateEmployee(EmpModelClass(Integer.parseInt(id), name, email))
           if (mess.size != 0) {
               ImagesList.clear()
               for (i in mess) {
                   ImagesList.add(DataPIckImages(id.toInt(), i, false))
               }
               databaseHandler.updateImages(ImagesList, id.toInt())
               onClicked()
           }
           if (status > -1) {
               Toast.makeText(this, "record update", Toast.LENGTH_LONG).show()
           }
       }

    }


    fun viewRecord(view: View) {
        onClicked()
    }

    fun onClicked() {
        val databaseHandler = DatabaseHandler(this)
        val emp: List<EmpModelClass> = databaseHandler.viewEmployee()
        list.clear()
        for (e in emp) {
            list.add(EmpModelClass(e.userId, e.userName, e.userEmail))
        }
        adapter=MyAdapter(list, this)
        x.adapter = adapter
        x.layoutManager = LinearLayoutManager(this)
    }


    //IMAGES..........................................


    fun AddImages(view: View) {
        openGalleryForImages()
    }


    fun openGalleryForImages() {

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
        var message: ArrayList<String> = ArrayList()
        when (requestCode) {
            2 ->
                if (resultCode == RESULT_OK) {


                    if (resultCode == Activity.RESULT_OK && requestCode == 2) {

                        // if multiple images are selected
                        if (data?.getClipData() != null) {
                            var count = data.clipData!!.itemCount
                            message.clear()
                            for (i in 0..count - 1) {
                                var imageUri: Uri = data.clipData?.getItemAt(i)!!.uri
                                if (imageUri != null) {
                                    message.add(imageUri.toString())
                                }
                            }
                            clicked((message))

                        } else if (data?.getData() != null) {
                            // if single image is selected
                            if (data.data != null) {

                                if (resultCode != RESULT_CANCELED) {
                                    val selectedImage = data.data
                                    if (selectedImage != null) {
                                        message.clear()
                                        message.add(selectedImage.toString())
                                        clicked((message))
                                    }
                                }
                            }
                        }
                    }
                }
        }

    }


    fun clicked(message: ArrayList<String>) {

        Images.clear()
        Images = message
    }


}