package com.uit.party.data.menu

import androidx.lifecycle.LiveData
import com.uit.party.data.Result
import com.uit.party.data.PartyBookingDatabase
import com.uit.party.model.*
import com.uit.party.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class MenuRepository @Inject constructor(
    private val networkService: ServiceRetrofit,
    private val sharedPrefs: SharedPrefs,
    database: PartyBookingDatabase
) {

    private val menuDao = database.menuDao
    val listMenu: LiveData<List<DishModel>> = menuDao.allDish

    private val descriptionType = "image-type".toRequestBody("text/plain".toMediaTypeOrNull())

    suspend fun addNewDish(
        imagePaths: ArrayList<String>,
        title: String?,
        description: String?,
        price: String?,
        type: String
    ): Result<AddDishResponse> {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        val multipartPath = ArrayList<MultipartBody.Part>()
        val parseType = "multipart/form-data"
        for (row in imagePaths) {
            val file = File(row)

            multipartPath.add(
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    file.asRequestBody(parseType.toMediaTypeOrNull())
                )
            )
        }

        val result = handleRequest {
            networkService.addDish(
                sharedPrefs.token,
                title?.toRequestBody(MultipartBody.FORM),
                description?.toRequestBody(MultipartBody.FORM),
                price?.toRequestBody(MultipartBody.FORM),
                type.toRequestBody(MultipartBody.FORM),
                ("0").toRequestBody((MultipartBody.FORM)),
                multipartPath,
                descriptionType
            )
        }

        if (result is Result.Success) {
            result.data.dish?.let { menuDao.insertDish(it) }
        }

        return result
    }

    suspend fun updateDish(updateModel: UpdateDishRequestModel): Result<UpdateDishResponse> {
        val result = handleRequest {
            networkService.updateDish(sharedPrefs.token, updateModel)
        }
        if (result is Result.Success) {
            val dishModel = result.data.dish
            if (dishModel != null)
                menuDao.updateDish(result.data.dish)
        }
        return result
    }

    suspend fun getListDishes() {
        try {
            val result = networkService.getListDishes(sharedPrefs.token)
            val dishes = result.listDishes
            if (dishes != null) {
                menuDao.deleteMenu()
                menuDao.insertAllDish(dishes)
            }
        } catch (cause: Throwable) {
            Result.Error(Exception(cause))
        }
    }

    suspend fun insertDish(dishModel: DishModel) {
        try {
            menuDao.insertDish(dishModel)
        } catch (cause: Throwable) {
            Result.Error(Exception(cause))
        }
    }

    suspend fun deleteDish(id: String): Result<BaseResponse> {
        return try {
            val map = HashMap<String, String>()
            map["_id"] = id
            val result: BaseResponse = networkService.deleteDish(sharedPrefs.token, map)
            menuDao.deleteDish(id)
            Result.Success(result)
        } catch (cause: Throwable) {
            Result.Error(Exception(cause))
        }
    }
}