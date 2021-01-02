package com.uit.party.util

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import com.uit.party.R


/*@Suppress("UNCHECKED_CAST")
@BindingAdapter("app:gridRecyclerData")
fun <T> setupGridRecyclerView(recyclerView: RecyclerView, items: ArrayList<T>) {
    recyclerView.setHasFixedSize(false)
//    val layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
    val layoutManager = GridLayoutManager(recyclerView.context, 2)
    recyclerView.layoutManager = layoutManager

    if (recyclerView.adapter is BindableAdapter<*>) {
        (recyclerView.adapter as BindableAdapter<T>).setData(items)
    }
}*/

/*@Suppress("UNCHECKED_CAST")
@BindingAdapter("app:linearRecyclerData")
fun <T> setupLinearRecyclerView(recyclerView: RecyclerView, items: ArrayList<T>) {
    recyclerView.setHasFixedSize(false)
    val layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.layoutManager = layoutManager

    if (recyclerView.adapter is BindableAdapter<*>) {
        (recyclerView.adapter as BindableAdapter<T>).setData(items)
    }
}*/

@BindingAdapter("app:avatarUrl")
fun setAvatarUrl(imageView: AppCompatImageView, url: String?) {
    if (!url.isNullOrEmpty()){
        val requestOptions = RequestOptions
            .circleCropTransform()
            .error(R.drawable.ic_account_circle_24dp)
            .placeholder(R.drawable.ic_account_circle_24dp)
        Glide.with(imageView.context).load(url).apply(requestOptions).into(imageView)
    }
}

@BindingAdapter("app:dishUrl")
fun setDishImage(imageView: AppCompatImageView, url: String?) {
    if (!url.isNullOrEmpty()){
        val requestOptions = RequestOptions
            .centerCropTransform()
            .error(R.drawable.dish_sample)
            .placeholder(R.drawable.dish_sample)
        Glide.with(imageView.context).load(url).apply(requestOptions).into(imageView)
    }
}

@BindingAdapter("bind:textError")
fun setTextError(textInput: TextInputLayout, error: String?) {
    if (!error.isNullOrEmpty()) {
        textInput.error = error
        textInput.isErrorEnabled = true
    }else{
        textInput.error = ""
        textInput.isErrorEnabled = false
    }
}