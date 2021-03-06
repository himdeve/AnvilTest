package com.squareup.scopes

import javax.inject.Scope
import kotlin.reflect.KClass

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoGen(val clazz: KClass<*>)
