package com.uit.party.util

import com.uit.party.data.history_order.GetHistoryCartResponse
import com.uit.party.model.*
import com.uit.party.ui.profile.edit_profile.RequestUpdateProfile
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ServiceRetrofit {
    @HTTP(method = "POST", path = "user/signin", hasBody = true)
    suspend fun login(
        @Body body: LoginModel
    ): AccountResponse

    @POST("user/signup")
    suspend fun register(
        @Body body: RegisterModel
    ): AccountResponse

    @GET("user/signout")
    suspend fun logout(
        @Header("authorization") token: String
    ): BaseResponse

    @POST("user/resetpassword")
    @FormUrlEncoded
    suspend fun resetPassword(
        @Field("username") username: String
    ): BaseResponse

    @POST("user/resetconfirm")
    @FormUrlEncoded
    suspend fun verifyPassword(
        @Field("resetpassword") resetpassword: String,
        @Field("passwordnew") passwordnew: String
    ): BaseResponse

    @POST("user/changepassword")
    @FormUrlEncoded
    suspend fun changePassword(
        @Header("authorization") token: String,
        @Field("password") password: String,
        @Field("newpassword") passwordchange: String
    ): BaseResponse

    @GET("user/get_me")
    suspend fun getProfile(
        @Header("authorization") token: String
    ): AccountResponse

    @GET("user/get_history_cart")
    suspend fun getHistoryBooking(
        @Header("authorization") token: String,
        @Query("page") page: Int
    ): GetHistoryCartResponse?

    @POST("user/updateuser")
    suspend fun updateUser(
        @Header("authorization") token: String,
        @Body body: RequestUpdateProfile
    ): AccountResponse

    @Multipart
    @POST("user/uploadavatar")
    suspend fun updateAvatar(
        @Header("authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("image") requestBody: RequestBody
    ): BaseResponse

    @Multipart
    @POST("product/adddish")
    suspend fun addDish(
        @Header("authorization") token: String,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("type") type: RequestBody?,
        @Part("discount") discount: RequestBody?,
        @Part image: ArrayList<MultipartBody.Part>,
        @Part("image") requestBody: RequestBody
    ): AddDishResponse

    @POST("product/updatedish")
    suspend fun updateDish(
        @Header("authorization") token: String,
        @Body body: UpdateDishRequestModel
    ): UpdateDishResponse

    @GET("product/dishs")
    suspend fun getListDishes(
        @Header("authorization") token: String
    ): DishesResponse

    @HTTP(method = "POST", path = "product/getItemDish", hasBody = true)
    suspend fun getItemDish(
        @Body body: HashMap<String, String?>
    ): DishItemResponse

    //Rating
    @GET("product/rate")
    suspend fun getDishRates(
        @Query("id") id: String,
        @Query("page") page: Int
    ): RateResponseModel

    @POST("product/rate")
    suspend fun ratingDish(
        @Header("authorization") token: String,
        @Body body: RequestRatingModel
    ): SingleRateResponseModel

    @PUT("product/rate")
    suspend fun updateRatingDish(
        @Header("authorization") token: String,
        @Body body: RequestRatingModel
    ): SingleRateResponseModel

    @HTTP(method = "DELETE", path = "product/deletedish", hasBody = true)
    suspend fun deleteDish(
        @Header("authorization") token: String,
        @Body body: HashMap<String, String>
    ): BaseResponse

    @POST("product/book")
    suspend fun orderParty(
        @Header("authorization") token: String,
        @Body body: RequestOrderPartyModel
    ): BillResponseModel

    @GET("payment/get_payment")
    suspend fun getPayment(
        @Header("authorization") token: String,
        @Query("_id") id: String
    ): GetPaymentResponse
}

private val service: ServiceRetrofit by lazy {

    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addInterceptor(logging)
        .build()


    val builder = Retrofit.Builder()
        .baseUrl("https://partybooking.herokuapp.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
    val retrofit = builder.build()

    retrofit.create(ServiceRetrofit::class.java)
}

fun getNetworkService() = service

private var interceptor: Interceptor = Interceptor { chain ->
    val newRequest: Request = chain.request().newBuilder()

        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .method(chain.request().method, chain.request().body)
        .build()

    chain.proceed(newRequest)
}