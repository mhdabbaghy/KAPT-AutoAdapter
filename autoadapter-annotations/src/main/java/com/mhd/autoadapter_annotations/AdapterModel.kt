package com.mhd.autoadapter_annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AdapterModel(val layoutRes: Int)