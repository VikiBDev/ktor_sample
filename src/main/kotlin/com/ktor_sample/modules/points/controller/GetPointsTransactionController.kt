package com.ktor_sample.modules.points.controller

import com.ktor_sample.foundation.controller.BaseController
import com.ktor_sample.foundation.controller.exception.MissingParameterException
import com.ktor_sample.foundation.controller.requestcontext.KtorSampleEnvironment
import com.ktor_sample.foundation.controller.requestcontext.RequestContext
import com.ktor_sample.foundation.http.Empty
import com.ktor_sample.modules.points.controller.mapper.PointsTransactionMapper
import com.ktor_sample.modules.points.controller.model.PointsTransactionWrapperView
import com.ktor_sample.modules.points.service.PointService
import java.util.*

class GetPointsTransactionController(
    ktorSampleEnvironment: KtorSampleEnvironment,
    private val pointService: PointService
) : BaseController<Empty>(ktorSampleEnvironment) {
    override suspend fun handle(context: RequestContext<Empty>): PointsTransactionWrapperView {
        val transactionId =
            context.call.parameters["transactionId"] ?: throw MissingParameterException("transactionId")

        val transaction = pointService.getTransaction(UUID.fromString(transactionId))
        return transaction.let { PointsTransactionMapper.convert(it, context.language) }
    }
}