package com.siavashdev.shoppinglisttesting.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.siavashdev.shoppinglisttesting.R
import com.siavashdev.shoppinglisttesting.data.local.ShoppingDao
import com.siavashdev.shoppinglisttesting.data.local.ShoppingItemDatabase
import com.siavashdev.shoppinglisttesting.data.remote.PixabayApi
import com.siavashdev.shoppinglisttesting.other.Constants.BASE_URL
import com.siavashdev.shoppinglisttesting.other.Constants.DATABASE_NAME
import com.siavashdev.shoppinglisttesting.repositories.DefaultShoppingRepository
import com.siavashdev.shoppinglisttesting.repositories.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,ShoppingItemDatabase::class.java,DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: ShoppingDao,
        api: PixabayApi
    ) = DefaultShoppingRepository(dao,api) as ShoppingRepository

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(

        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )


    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi():PixabayApi{

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayApi::class.java)
    }




}