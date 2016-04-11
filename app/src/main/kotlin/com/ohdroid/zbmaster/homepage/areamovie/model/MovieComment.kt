package com.ohdroid.zbmaster.homepage.areamovie.model

import android.os.Parcel
import android.os.Parcelable
import cn.bmob.v3.BmobObject

/**
 * Created by ohdroid on 2016/4/11.
 */
class MovieComment(var movieUrl: String? = ""
                   , var commentAuthor: String? = ""
                   , var comment: String? = ""
                   , var favour: Int? = 0) : BmobObject(), Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(movieUrl)
        dest?.writeString(commentAuthor)
        dest?.writeString(comment)
        dest?.writeInt(favour ?: 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<MovieComment> {
            override fun newArray(size: Int): Array<out MovieComment>? {
                return Array(size, { MovieComment() })
            }

            override fun createFromParcel(source: Parcel?): MovieComment? {
                val movieUrl = source?.readString()
                val commentAuthor = source?.readString()
                val comment = source?.readString()
                val favour = source?.readInt()
                return MovieComment(movieUrl, commentAuthor, comment, favour)
            }

        }
    }


}