package com.example.sqlitedb

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(val list: ArrayList<EmpModelClass>, val context: MainActivity) :
    RecyclerView.Adapter<MyAdapter.MyHolder>() {


    companion object
    {
        var img : ImageView? = null
        lateinit var adapter1:AdapterTOShowImages
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.custom_list, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {


//        var myLogo = (songs[position].img as BitmapDrawable).bitmap
//        holder.img.setImageBitmap(myLogo)
        holder.id.text = list[position].userId.toString()
        holder.name.text = list[position].userName.toString()
        holder.email.text = list[position].userEmail.toString()
        holder.update.setOnClickListener {
            update(list[position].userId, position)
        }

        holder.delete.setOnClickListener {
            delted(list[position].userId.toString())
        }

//holder.recycle.adapter = MyAdapter(list , )

        var messagesList: ArrayList<DataShowImages> = ArrayList()
        val databaseHandler = DatabaseHandler(context)
        try {
            val emp: List<DataShowImages> = databaseHandler.viewImages()
            if (emp.size != 0) {
                messagesList.clear()
                for (e in emp) {
                    if(e.Images_ID == list[position].userId)
                    {
                        messagesList.add(DataShowImages(e.ID,e.Images_ID, e.Images, false))
                    }
                }


              holder.recycle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                adapter1 = AdapterTOShowImages(context, messagesList)
                holder.recycle.adapter = adapter1
            }

        } catch (e: Exception) {
            Toast.makeText(context, "$e", Toast.LENGTH_LONG).show()
            Log.e("images", e.toString())
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id = itemView.findViewById<TextView>(R.id.textViewId)
        var name = itemView.findViewById<TextView>(R.id.textViewName)
        var email = itemView.findViewById<TextView>(R.id.textViewEmail)
        var update = itemView.findViewById<TextView>(R.id.btn1)
        var delete = itemView.findViewById<TextView>(R.id.btn2)
        var recycle: RecyclerView = itemView.findViewById(R.id.recycleImg)
        var x = itemView.context


    }


    fun delted(x: String) {
        var i = 0
        for (item in list) {
            if (item.userId == x.toInt()) {
                list.remove(item)

                val deleteId = x
                //creating the instance of DatabaseHandler class
                val databaseHandler: DatabaseHandler = DatabaseHandler(context)
                if (deleteId.trim() != "") {
                    //calling the deleteEmployee method of DatabaseHandler class to delete record
                    val status = databaseHandler.deleteEmployee(
                        EmpModelClass(
                            Integer.parseInt(deleteId),
                            "",
                            ""
                        )
                    )
                    notifyDataSetChanged()
                    if (status > -1) {
                        Toast.makeText(context, "record deleted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "id or name or email cannot be blank",
                        Toast.LENGTH_LONG
                    ).show()
                }

                break
            }
            i++

        }
    }


    fun update(id: Int, pos: Int) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)


        val edtName = dialogView.findViewById(R.id.updateName) as EditText
        val edtEmail = dialogView.findViewById(R.id.updateEmail) as EditText
        img = dialogView.findViewById<ImageView>(R.id.imgSelect)
        img?.setOnClickListener {
            context.openGalleryForImages()
        }
        dialogBuilder.setTitle("Update Record")
        dialogBuilder.setMessage("Enter data below")
        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->
            context.saveRecord(id.toString(),edtName.text.toString(),edtEmail.text.toString(),true)
//
        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()

    }



}

