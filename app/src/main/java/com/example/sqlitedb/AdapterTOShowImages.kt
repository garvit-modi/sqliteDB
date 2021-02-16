package com.example.sqlitedb


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView


class AdapterTOShowImages(private val context: Context, var list: ArrayList<DataShowImages>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder1(
            LayoutInflater.from(context).inflate(R.layout.image_complaints, parent, false)
        )


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as ViewHolder1).select?.visibility = View.GONE

        (holder as ViewHolder1).image?.setImageURI(list[position].Images.toUri())
        (holder as ViewHolder1).image?.isVisible = true
        (holder as ViewHolder1).cancel?.isVisible = true
        holder.image?.setOnClickListener {
            var flag = list[position].flag
            if (!flag) {
                (holder as ViewHolder1).tick?.isVisible = true
                list[position].flag = true
            } else {
                (holder as ViewHolder1).tick?.isVisible = false
                list[position].flag = false
            }

        }

        holder.cancel?.setOnClickListener {
            var x = list[position].ID
            delted(position, x)
            holder.image?.visibility = View.GONE
            holder.tick?.visibility = View.GONE
            holder.cancel?.visibility = View.GONE
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


    private inner class ViewHolder1 constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {


        var image: ImageView? = itemView.findViewById(R.id.img12)
        var select: ImageView? = itemView.findViewById(R.id.imgSelectimg)
        var relative: RelativeLayout? = itemView.findViewById(R.id.main_layout)
        var tick: ImageView? = itemView.findViewById(R.id.imgTick)
        var cancel: ImageView? = itemView.findViewById(R.id.imgCancel)
        var context = itemView.context


    }


    fun delted(y: Int, x: Int) {



        for (item in list) {
            if (item.ID == x) {
                list.remove(item)
                val deleteId = x
                //creating the instance of DatabaseHandler class
                val databaseHandler: DatabaseHandler = DatabaseHandler(context)

                //calling the deleteEmployee method of DatabaseHandler class to delete record
                val status = databaseHandler.deleteIamges(
                    DataShowImages(
                        x,
                        y,
                        "",
                        false
                    )
                )
                notifyDataSetChanged()
                if (status > -1) {
                    Toast.makeText(context, "record deleted", Toast.LENGTH_LONG).show()
                }
                break
            }
        }


    }

}