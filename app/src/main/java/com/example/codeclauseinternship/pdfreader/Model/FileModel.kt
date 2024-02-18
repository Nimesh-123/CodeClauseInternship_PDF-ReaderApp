package com.example.codeclauseinternship.pdfreader.Model


class FileModel {
    var path: String? = null
    var date: String? = null
    var fileType: String? = null
    var filename: String? = null
    var isBookmarked = false
    var size: String? = null
    var sizeInMilli: Long = 0

    constructor(
        path: String?,
        date: String?,
        filename: String?,
        isBookmarked: Boolean,
        size: String?
    ) {
        this.path = path
        this.date = date
        this.filename = filename
        this.isBookmarked = isBookmarked
        this.size = size
    }
}