package com.mhd.autoadapter

import com.mhd.autoadapter_annotations.AdapterModel
import com.mhd.autoadapter_annotations.ViewHolderBinding

@AdapterModel(R.layout.item_person)
data class Person(
    @ViewHolderBinding(R.id.name) val name: String,
    @ViewHolderBinding(R.id.address) val address: String
)
