package com.ktor_sample.modules.points.exception

import com.ktor_sample.foundation.controller.exception.core.SilentException

object NotEnoughPointsException : SilentException("point.error.not_enough_points", "not_enough_points")