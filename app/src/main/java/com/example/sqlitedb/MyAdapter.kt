package com.example.sqlitedb

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(val list: ArrayList<EmpModelClass>, val context: Activity) :
    RecyclerView.Adapter<MyAdapter.MyHolder>() {



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

    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messagesList: ArrayList<DataPIckImages> = ArrayList()
        var id = itemView.findViewById<TextView>(R.id.textViewId)
        var name = itemView.findViewById<TextView>(R.id.textViewName)
        var email = itemView.findViewById<TextView>(R.id.textViewEmail)
        var update = itemView.findViewById<TextView>(R.id.btn1)
        var delete = itemView.findViewById<TextView>(R.id.btn2)
var recycle : RecyclerView = itemView.findViewById(R.id.recycleImg)



        var x = itemView.context

        init {
            val context = itemView.context

            recycle?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            recycle.adapter = AdapterTOShowImages(context, messagesList)
        }
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

        dialogBuilder.setTitle("Update Record")
        dialogBuilder.setMessage("Enter data below")
        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

            val updateId = id.toString()
            val updateName = edtName.text.toString()
            val updateEmail = edtEmail.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(context)
            if (updateName.trim() != "" && updateEmail.trim() != "") {
                //calling the updateEmployee method of DatabaseHandler class to update record
                val status = databaseHandler.updateEmployee(
                    EmpModelClass(
                        Integer.parseInt(updateId),
                        updateName,
                        updateEmail
                    )
                )

                if (status > -1) {
                    Toast.makeText(context, "record update", Toast.LENGTH_LONG).show()
                    list[pos].userEmail = updateEmail
                    list[pos].userName = updateName

                }
            } else {
                Toast.makeText(context, "id or name or email cannot be blank", Toast.LENGTH_LONG)
                    .show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()

        notifyDataSetChanged()

    }


}

