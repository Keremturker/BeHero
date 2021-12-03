package com.keremturker.behero.di

import android.content.Context
import androidx.annotation.NonNull
import com.keremturker.behero.utils.Constants.BASE_SHARED_PREF_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    internal fun provideSharedPreference(@NonNull context: Context) =
        context.getSharedPreferences(BASE_SHARED_PREF_KEY, Context.MODE_PRIVATE)

}