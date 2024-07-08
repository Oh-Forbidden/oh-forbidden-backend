package com.ohforbidden.bugreport.global.util

import java.time.ZoneOffset
import java.time.ZonedDateTime

fun createUtcDateTime() = ZonedDateTime.now(ZoneOffset.UTC)