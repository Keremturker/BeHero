package com.keremturker.behero.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ContextModule {
    @Binds
    abstract fun bindsContext(application: Application): Context
}