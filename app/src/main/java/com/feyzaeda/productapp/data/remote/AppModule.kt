package com.feyzaeda.productapp.data.remote

import android.content.Context
import androidx.room.Room
import com.feyzaeda.productapp.data.local.RoomDb
import com.feyzaeda.productapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun getRetrofitInstanceProduct(): APIService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }


    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideMyDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app, RoomDb::class.java, "your_db_name"
    ).allowMainThreadQueries().build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideMyDaoFav(db: RoomDb) = db.productsFavDao()

    @Singleton
    @Provides
    fun provideMyDaoOrder(db: RoomDb) = db.productsOrderDao()
}