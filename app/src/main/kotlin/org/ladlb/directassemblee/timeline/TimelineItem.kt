package org.ladlb.directassemblee.timeline

import android.os.Parcelable
import androidx.annotation.IntDef

interface TimelineItem : Parcelable {

    companion object {

        const val WORK = 0
        const val BALLOT_GROUP = 1
        const val LAW = 2

        @Retention(AnnotationRetention.SOURCE)
        @IntDef(WORK, BALLOT_GROUP, LAW)
        annotation class ResultCode

    }

    @ResultCode
    fun getType(): Int

}