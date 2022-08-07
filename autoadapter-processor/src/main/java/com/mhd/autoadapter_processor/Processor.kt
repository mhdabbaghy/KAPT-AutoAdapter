package com.mhd.autoadapter_processor

import com.mhd.autoadapter_annotations.AdapterModel
import com.mhd.autoadapter_annotations.ViewHolderBinding
import com.mhd.autoadapter_processor.code_gen.AdapterCodeBuilder
import com.mhd.autoadapter_processor.models.ModelData
import com.mhd.autoadapter_processor.models.ViewHolderBindingData
import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
class Processor : AbstractProcessor() {

    private companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        mutableSetOf(AdapterModel::class.java.canonicalName)

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val kaptKotlinGeneratedDir =
            processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
                ?: return false
        roundEnv.getElementsAnnotatedWith(AdapterModel::class.java)
            .forEach {
                val modelData = getModelData(it)
                val fileName = "${modelData.modelName}Adapter"
                FileSpec.builder(modelData.packageName, fileName)
                    .addType(AdapterCodeBuilder(fileName, modelData).build())
                    .build()
                    .writeTo(File(kaptKotlinGeneratedDir)) // 4
            }

        return true
    }

    private fun getModelData(element: Element): ModelData {
        val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
        val modelName = element.simpleName.toString()
        val annotation = element.getAnnotation(AdapterModel::class.java)
        val layoutRes = annotation.layoutRes
        val viewHolderBindingData = element.enclosedElements.mapNotNull {
            val viewHolderBinding = it.getAnnotation(ViewHolderBinding::class.java)
            if (viewHolderBinding == null) {
                null
            } else {
                val elementName = it.simpleName.toString()
                val fieldName = elementName.substring(0, elementName.indexOf('$'))
                    .removePrefix("get")
                    .replaceFirstChar(Char::lowercase)
                ViewHolderBindingData(fieldName, viewHolderBinding.viewId)
            }
        }
        return ModelData(packageName, modelName, layoutRes, viewHolderBindingData)

    }
}