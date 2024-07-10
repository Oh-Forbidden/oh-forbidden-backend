package com.ohforbidden.bugreport.global.util

import java.time.ZoneOffset
import java.time.ZonedDateTime

fun createUtcZoneDateTime() = ZonedDateTime.now(ZoneOffset.UTC)