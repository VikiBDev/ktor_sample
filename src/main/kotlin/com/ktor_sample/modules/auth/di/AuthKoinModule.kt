package com.ktor_sample.modules.auth.di

import com.ktor_sample.modules.auth.repository.AuthRepository
import com.ktor_sample.modules.auth.repository.impl.AuthRepositoryImpl
import com.ktor_sample.modules.auth.service.AuthService
import com.ktor_sample.modules.auth.service.AuthServiceImpl
import org.koin.dsl.module

object AuthKoinModule {
    val instance = module {
        // Repository
        single<AuthRepository> { AuthRepositoryImpl(get()) }

        // Service
        single<AuthService> { AuthServiceImpl(get()) }
    }
}