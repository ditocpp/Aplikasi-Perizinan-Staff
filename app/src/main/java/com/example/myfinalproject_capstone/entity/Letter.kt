package com.example.myfinalproject_capstone.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Letter (
    val inputDate: Date? = null, // sudah benarkah?
    val title: String? = null,
    val description: String? = null,
    val staffName: String? = null,
    val duration: Date? = null, // aku ga tau ini pake data type apa
    val status: Int? = null,
    val statusImage: Int? = null
) : Parcelable