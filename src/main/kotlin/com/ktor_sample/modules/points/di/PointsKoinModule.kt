package com.ktor_sample.modules.points.di

import com.ktor_sample.modules.points.api.exported.PointsPublicApi
import com.ktor_sample.modules.points.api.exported.impl.PointsPublicApiImpl
import com.ktor_sample.modules.points.api.internal.StoreApi
import com.ktor_sample.modules.points.api.internal.impl.StoreApiImpl
import com.ktor_sample.modules.points.controller.ChangePointsController
import com.ktor_sample.modules.points.controller.GetPointsBalanceController
import com.ktor_sample.modules.points.controller.GetPointsTransactionsController
import com.ktor_sample.modules.points.controller.GetPointsTransactionController
import com.ktor_sample.modules.points.repository.PointRepository
import com.ktor_sample.modules.points.repository.impl.PointRepositoryImpl
import com.ktor_sample.modules.points.service.PointService
import com.ktor_sample.modules.points.service.impl.PointServiceImpl
import org.koin.dsl.module

object PointsKoinModule {
    val instance = module {
        // Repositories
        single<PointRepository> { PointRepositoryImpl(get(), get()) }
        // APIs
        single<PointsPublicApi> { PointsPublicApiImpl(get()) }
        single<StoreApi> { StoreApiImpl(get()) }
        // Services
        single<PointService> { PointServiceImpl(get()) }
        // Controllers
        single { GetPointsBalanceController(get(), get()) }
        single { GetPointsTransactionsController(get(), get()) }
        single { ChangePointsController(get(), get()) }
        single { GetPointsTransactionController(get(), get()) }
    }
}