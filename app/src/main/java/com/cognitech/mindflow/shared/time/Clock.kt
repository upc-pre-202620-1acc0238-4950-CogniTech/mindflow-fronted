package com.cognitech.mindflow.shared.time

import java.time.LocalDate

/** Injectable time boundary; domain policies never need Android or the system clock directly. */
fun interface Clock { fun today(): LocalDate }
object SystemClock : Clock { override fun today(): LocalDate = LocalDate.now() }
