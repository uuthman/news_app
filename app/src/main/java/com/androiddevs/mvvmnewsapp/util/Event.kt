package com.androiddevs.mvvmnewsapp.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import java.util.concurrent.atomic.AtomicBoolean

class Event<T>(private val content: T) {

    private val isConsumed = AtomicBoolean(false)

    fun consume(block: (T) -> Unit): T? = getValue()?.also(block)

    internal fun getValue(): T? =
        if (isConsumed.compareAndSet(false, true)) content
        else null

    fun peek() = content
}

inline fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline handleEvent: (T) -> Unit
) {
    observe(owner){
        event -> event.consume { handleEvent.invoke(it) }
    }
}

fun <T> T.asEvent() = Event(this)