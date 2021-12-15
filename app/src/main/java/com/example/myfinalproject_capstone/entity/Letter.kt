package com.example.myfinalproject_capstone.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Letter (
    val letterID: String? = null,
    val inputDate: String? = null,
    val title: String? = null,
    val description: String? = null,
    val staffID: String? = null,
    val companyID: String? = null,
    val durationStart: String? = null,
    val durationFinish: String? = null,
    val status: Int? = null
) : Parcelable