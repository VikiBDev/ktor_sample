package com.ktor_sample.modules.store.di

import com.ktor_sample.modules.store.api.exported.StorePublicApi
import com.ktor_sample.modules.store.api.exported.impl.StorePublicApiImpl
import org.koin.dsl.module

object StoreKoinModule {
    val instance = module {
        // APIs
        single<StorePublicApi> { StorePublicApiImpl() }
    }
}