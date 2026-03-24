package com.example.animalcrossingapp.data.remote

import com.squareup.moshi.Json

data class NookAvailabilityDto(
    @Json(name = "month-northern") val monthNorthernHyphen: String? = null,
    @Json(name = "month-southern") val monthSouthernHyphen: String? = null,
    @Json(name = "month_northern") val monthNorthernUnderscore: String? = null,
    @Json(name = "month_southern") val monthSouthernUnderscore: String? = null,
    @Json(name = "time") val time: String? = null,
    @Json(name = "isAllDay") val isAllDayCamel: Boolean? = null,
    @Json(name = "is_all_day") val isAllDaySnake: Boolean? = null,
    @Json(name = "isAllYear") val isAllYearCamel: Boolean? = null,
    @Json(name = "is_all_year") val isAllYearSnake: Boolean? = null
) {
    val monthNorthern: String?
        get() = monthNorthernHyphen ?: monthNorthernUnderscore

    val monthSouthern: String?
        get() = monthSouthernHyphen ?: monthSouthernUnderscore

    val isAllDay: Boolean
        get() = isAllDayCamel == true || isAllDaySnake == true

    val isAllYear: Boolean
        get() = isAllYearCamel == true || isAllYearSnake == true
}

