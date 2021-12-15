package com.example.myfinalproject_capstone.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Letter (
    val inputDate: Date? = null,
    val title: String? = null,
    val description: String? = null,
    val staffName: String? = null,
    val durationStart: Date? = null,
    val durationFinish: Date? = null,
    val status: Int? = null
) : Parcelable