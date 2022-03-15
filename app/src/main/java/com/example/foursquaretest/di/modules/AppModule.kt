package com.example.foursquaretest.di.modules


import android.content.Context
import com.example.foursquaretest.BuildConfig
import com.example.foursquaretest.data.api.NearPlacesAPI
import com.example.foursquaretest.utils.APIInterceptor
import com.example.foursquaretest.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideInterceptor() : APIInterceptor = APIInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(apiInterceptor : APIInterceptor) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiInterceptor)
            .callTimeout(2,TimeUnit.MINUTES)
            .connectTimeout(2,TimeUnit.MINUTES)
            .writeTimeout(2,TimeUnit.MINUTES)
            .readTimeout(2,TimeUnit.MINUTES)
            .build()
    } else OkHttpClient
        .Builder()
        .build()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String,moshi : Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()



    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideNearPlacesAPI(retrofit: Retrofit): NearPlacesAPI = retrofit.create(NearPlacesAPI::class.java)


}