package com.uit.party.ui.main.add_new_dish

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uit.party.R
import com.uit.party.model.Thumbnail


class SpinnerAdapter internal constructor(context: Context, resource: Int, list: Array<Thumbnail>) :
    ArrayAdapter<Thumbnail>(context, resource, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return rowView(convertView, position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return rowView(convertView, position)
    }

    @SuppressLint("InflateParams")
    private fun rowView(convertView: View?, position: Int): View {
        val holder: ViewHolder
        var rowView = convertView
        if (rowView == null) {

            holder = ViewHolder()
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.item_spinner_dish, null, false)

            holder.tile = rowView!!.findViewById(R.id.item_spinner_name)
            holder.image = rowView.findViewById(R.id.item_spinner_thumbnail)
            rowView.tag = holder
        } else {
            holder = rowView.tag as ViewHolder
        }
        //        holder.image.setImageResource(getItem(position).getImg());
        Glide.with(context).load(getItem(position)!!.image)
            .apply(RequestOptions.centerCropTransform()).into(holder.image!!)
        holder.tile!!.text = getItem(position)!!.dishName

        return rowView
    }

    private inner class ViewHolder {
        var tile: TextView? = null
        var image: ImageView? = null
    }
}
