package com.ktor_sample.modules.points.controller

import com.ktor_sample.foundation.controller.BaseController
import com.ktor_sample.foundation.controller.requestcontext.KtorSampleEnvironment
import com.ktor_sample.foundation.controller.requestcontext.RequestContext
import com.ktor_sample.foundation.userId
import com.ktor_sample.modules.points.PointAmount
import com.ktor_sample.modules.points.service.PointService

internal class ChangePointsController(
    ktorSampleEnvironment: KtorSampleEnvironment,
    private val pointService: PointService
) : BaseController<PointAmount>(ktorSampleEnvironment) {
    override suspend fun handle(context: RequestContext<PointAmount>) {
        pointService.addPoints(context.call.userId(), context.body.amount)
    }
}