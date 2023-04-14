package id.my.ariqnf.temuantelyu.util

import android.icu.text.SimpleDateFormat
import android.text.format.DateUtils
import com.google.firebase.Timestamp
import java.util.*

fun Timestamp.relativeTime(): CharSequence =
    DateUtils.getRelativeTimeSpanString(
        this.toDate().time,
        Calendar.getInstance().timeInMillis,
        DateUtils.MINUTE_IN_MILLIS
    )

fun Timestamp.formatDate(format: String = "dd-MM-yyyy", locale: Locale = Locale.getDefault()) =
    SimpleDateFormat(format, locale).format(this.toDate())