package com.ohforbidden.global.util

import java.time.ZoneOffset
import java.time.ZonedDateTime

fun createUtcDateTime() = ZonedDateTime.now(ZoneOffset.UTC)