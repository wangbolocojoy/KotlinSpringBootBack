package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import java.io.Serializable

data class PostImagesBody(
        var imageUrl: String?
): abstractObjectToString(), Serializable
