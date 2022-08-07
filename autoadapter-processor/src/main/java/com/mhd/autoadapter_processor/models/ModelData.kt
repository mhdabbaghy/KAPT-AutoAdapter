package com.mhd.autoadapter_processor.models

data class ModelData(
    val packageName: String,
    val modelName: String,
    val layoutRes: Int,
    val viewHolderBindingData: List<ViewHolderBindingData>
)
