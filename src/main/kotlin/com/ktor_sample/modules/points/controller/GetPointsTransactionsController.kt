package com.ktor_sample.modules.points.controller

import com.ktor_sample.foundation.controller.BaseController
import com.ktor_sample.foundation.controller.requestcontext.KtorSampleEnvironment
import com.ktor_sample.foundation.controller.requestcontext.RequestContext
import com.ktor_sample.foundation.http.Empty
import com.ktor_sample.foundation.userId
import com.ktor_sample.modules.points.service.PointService
import com.ktor_sample.modules.points.service.model.PointsTransactionInfo

class GetPointsTransactionsController(
    ktorSampleEnvironment: KtorSampleEnvironment,
    private val pointService: PointService
) : BaseController<Empty>(ktorSampleEnvironment) {
    override suspend fun handle(context: RequestContext<Empty>): List<PointsTransactionInfo> {
        return pointService.getTransactionsInfo(context.call.userId())
    }
}
