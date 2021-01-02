package com.uit.party.ui.main.add_new_dish

import android.graphics.Bitmap
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uit.party.R
import com.uit.party.data.menu.MenuRepository
import com.uit.party.model.*
import com.uit.party.ui.main.MainActivity
import com.uit.party.util.*
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import com.uit.party.data.Result
import java.io.File
import javax.inject.Inject

class AddNewDishFragmentViewModel @Inject constructor(private val repository: MenuRepository) :
    ViewModel() {
    val mTitle = ObservableField("")
    val mDescription = ObservableField("")
    val mPrice = ObservableField("")
    val mButtonField = ObservableField(UiUtil.getString(R.string.add_dish))
    val mShowUploadImageDish = ObservableInt(View.VISIBLE)

    val mErrorTitle = ObservableField("")
    val mErrorDescription = ObservableField("")
    val mErrorPrice = ObservableField("")

    private val _messageCallback = MutableLiveData<Pair<Boolean, String?>>()
    val messageCallback: LiveData<Pair<Boolean, String?>>
        get() = _messageCallback

    var mTypeText = ""

    var mPosition = 0
    var mDishType = ""

    val mEnableSendButton = ObservableBoolean(false)

    private val listImagePath = ArrayList<String>()

    private var mTitleValid = false
    private var mDescriptionValid = false
    private var mPriceValid = false

    var mDishModel: DishModel? = null

    fun initData() {
        mTitle.set(mDishModel?.name)
        mDescription.set(mDishModel?.description)
        mPrice.set(mDishModel?.price)
        mTypeText = mDishModel?.categories.toString()

        mShowUploadImageDish.set(View.GONE)
        mEnableSendButton.set(true)
        mButtonField.set(UiUtil.getString(R.string.update_dish))
    }

    fun onAddImageDescription(view: View) {
        PickImageDialog.build(PickSetup()).setOnPickResult { result ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            result.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            listImagePath.add(result.path)
            Glide.with(view.context).load(File(result.path))
                .apply { RequestOptions.fitCenterTransform() }.into(view as AppCompatImageView)
        }.setOnPickCancel { /*binding.loadingAvatar.visibility = View.GONE */ }
            .show((view.context as MainActivity).supportFragmentManager)
    }

    fun onSendAddDishClicked() {
        if (mDishModel == null) {
            addNewDish()
        } else {
            updateDish()
        }
    }

    fun checkTitleValid(text: CharSequence?) {
        val message = text.requireFieldErrorMes()
        mErrorTitle.set(message)
        mTitleValid = message.isEmpty()
        checkEnableButtonSend()
    }

    fun checkDescriptionValid(text: CharSequence?) {
        val message = text.requireFieldErrorMes()
        mErrorDescription.set(message)
        mDescriptionValid = message.isEmpty()
        checkEnableButtonSend()
    }

    fun checkPriceValid(text: CharSequence?) {
        val message = text.requireFieldErrorMes()
        mErrorPrice.set(message)
        mPriceValid = message.isEmpty()
        checkEnableButtonSend()
    }

    private fun checkEnableButtonSend() {
        if (mTitleValid && mDescriptionValid && mPriceValid) {
            mEnableSendButton.set(true)
        }
    }

    private fun addNewDish() {
        viewModelScope.launch(Constants.coroutineIO) {
            try {
                val result = repository.addNewDish(
                    imagePaths = listImagePath,
                    title = mTitle.get(),
                    description = mDescription.get(),
                    price = mPrice.get(),
                    type = mDishType
                )
                if (result is Result.Success) {
                    _messageCallback.postValue(Pair(true, result.data.message))
                } else _messageCallback.postValue(
                    Pair(
                        false,
                        (result as Result.Error).exception.message
                    )
                )
            } catch (ex: Exception) {
                _messageCallback.postValue(Pair(false, ex.message))
            }
        }
    }

    private fun updateDish() {
        val updateModel = UpdateDishRequestModel(
            mDishModel?.id.toString(),
            mTitle.get() ?: "",
            mDescription.get() ?: "",
            mPrice.get() ?: "",
            mTypeText
        )

        viewModelScope.launch(Constants.coroutineIO) {
            try {
                val result = repository.updateDish(updateModel)
                if (result is Result.Success) {
                    _messageCallback.postValue(Pair(true, result.data.message))
                } else _messageCallback.postValue(
                    Pair(
                        false,
                        (result as Result.Error).exception.message
                    )
                )
            } catch (ex: Exception) {
                _messageCallback.postValue(Pair(false, ex.message))
            }
        }
    }
}