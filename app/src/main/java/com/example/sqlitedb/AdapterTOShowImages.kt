package com.example.sqlitedb


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView



class AdapterTOShowImages(private val context: Context, var list: ArrayList<DataPIckImages>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return ViewHolder1(
            LayoutInflater.from(context).inflate(R.layout.image_complaints, parent, false)
        )


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



            when (list[position].messageType) {


                3 -> {

                    (holder as ViewHolder1).select?.visibility = View.GONE

                    (holder as ViewHolder1).image?.setImageURI(list[position].message.toUri())
                    (holder as ViewHolder1).image?.isVisible =true
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
                        var x = list[position].message
                        var y = list[position].messageType
                        delted(position)

                        holder.image?.visibility = View.GONE
                        holder.tick?.visibility = View.GONE
                        holder.cancel?.visibility = View.GONE
                    }

                }


                6 -> {

                    (holder as ViewHolder1).select.visibility = View.GONE
                    var x = decodeBase64(list[position].message)
                    (holder as ViewHolder1).image?.isVisible =true
                    (holder as ViewHolder1).image?.setImageBitmap(x)
                    (holder as ViewHolder1).cancel?.isVisible = true
                    holder.image?.setOnClickListener {
                        var flag = list[position].flag
                        if (flag) {
                            (holder as ViewHolder1).tick?.isVisible = true
                            list[position].flag = true
                        } else {
                            (holder as ViewHolder1).tick?.isVisible = false
                            list[position].flag = false
                        }

                    }
                    holder.cancel?.setOnClickListener {
                        var x = list[position].message
                        var y = list[position].messageType
                        delted(position)

                        holder.image?.visibility = View.GONE
                        holder.tick?.visibility = View.GONE
                        holder.cancel?.visibility = View.GONE
                    }


                }



            }



    }


    fun decodeBase64(input: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }


    override fun getItemCount(): Int {
        return list.size
    }


    private inner class ViewHolder1 constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {


        var image: ImageView? = itemView.findViewById(R.id.img12)
        var select: ImageView = itemView.findViewById(R.id.imgSelectimg)
        var relative: RelativeLayout = itemView.findViewById(R.id.main_layout)
        var tick: ImageView? = itemView.findViewById(R.id.imgTick)
        var cancel: ImageView? = itemView.findViewById(R.id.imgCancel)
        var context = itemView.context



    }


    fun delted(y: Int) {
        var i = 0
        list.removeAt(y)
        notifyDataSetChanged()


    }



}