package com.example.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presentation.util.TypedUIState.Error
import com.example.presentation.util.TypedUIState.Loading
import com.example.presentation.util.TypedUIState.Normal
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.cancellation.CancellationException
import kotlin.properties.Delegates

sealed interface TypedUIState<out D, out E> {

    fun normalDataOrNull(): D? = (this as? Normal<D>)?.data

    fun errorDataOrNull(): E? = (this as? Error<E>)?.data

    data class Normal<out T>(val data: T) : TypedUIState<T, Nothing>

    data object NormalN : TypedUIState<Nothing, Nothing>

    data object Loading : TypedUIState<Nothing, Nothing>

    data class Error<out T>(val data: T) : TypedUIState<Nothing, T>
}

inline fun <D, E, R> TypedUIState<D, E>.mapError(mapper: (E) -> R): TypedUIState<D, R> = when (this) {
    is Error -> Error(mapper(data))
    is Loading -> this
    is Normal -> this
    is TypedUIState.NormalN -> this
}

inline fun <D, E, R> TypedUIState<D, E>.mapNormal(mapper: (D) -> R): TypedUIState<R, E> = when (this) {
    is Error -> this
    is Loading -> this
    is Normal -> Normal(mapper(data))
    is TypedUIState.NormalN -> this
}

typealias SimpleUIState = TypedUIState<Unit, Unit>

// Factories
fun <E> Normal(): TypedUIState<Unit, E> = Normal(Unit)

fun <D> Error(): TypedUIState<D, Unit> = Error(Unit)

// Utility Functions

fun <D, E> MutableStateFlow<TypedUIState<D, E>>.setNormal(data: D) {
    value = Normal(data)
}

fun <E> MutableStateFlow<TypedUIState<Unit, E>>.setNormal() = setNormal(Unit)

fun <D, E> MutableStateFlow<TypedUIState<D, E>>.setLoading() {
    value = TypedUIState.Loading
}

fun <D, E> MutableStateFlow<TypedUIState<D, E>>.setError(data: E) {
    value = Error(data)
}

fun <D> MutableStateFlow<TypedUIState<D, Unit>>.setError() = setError(Unit)

inline fun <D, E, R> Flow<TypedUIState<D, E>>.mapNormal(
    crossinline mapper: suspend (D) -> R,
): Flow<TypedUIState<R, E>> = mapLatest { uiState ->
    uiState.mapNormal { mapper(it) }
}

inline fun <D, E, R> Flow<TypedUIState<D, E>>.mapError(
    crossinline mapper: suspend (E) -> R,
): Flow<TypedUIState<D, R>> = mapLatest { uiState ->
    uiState.mapError { mapper(it) }
}

inline fun <D, E> MutableStateFlow<TypedUIState<D, E>>.updateIfNormal(
    enforce: Boolean = false,
    updater: (D) -> D,
) = update { state ->
    (state as? Normal<D>)
        ?.mapNormal(updater)
        ?: if (enforce) {
            throw IllegalStateException("Value passed to the update function was not Normal!")
        } else {
            state
        }
}

inline fun <D, E> MutableStateFlow<TypedUIState<D, E>>.updateIfError(
    enforce: Boolean = false,
    updater: (E) -> E,
) = update { state ->
    (state as? Error<E>)
        ?.mapError(updater)
        ?: if (enforce) {
            throw IllegalStateException("Value passed to the update function was not Error!")
        } else {
            state
        }
}

class SerialJob(
    private val onCancel: ((CancellationException) -> Unit)? = null,
) {

    private var job: Job? by Delegates.observable(null) { _, old, _ ->
        old?.cancel()
    }

    fun cancel(cause: CancellationException? = null) = job?.cancel(cause)

    suspend operator fun <R> invoke(runnable: suspend () -> R): R? {
        return coroutineScope {
            val newJob = async { runnable() }
            job = newJob
            try {
                newJob.await()
            } catch (error: CancellationException) {
                onCancel?.invoke(error)
                null
            }
        }
    }
}

context(ViewModel)
fun SerialJob.launchCatching(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchCatching(onError) {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchCatchingOnIO(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchCatchingOnIO(onError) {
        invoke { block() }
    }
}

fun CoroutineScope.launchCatchingOnIO(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return (this + Dispatchers.IO).launchCatching(onError, block)
}

fun CoroutineScope.launchCatching(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return launch {
        runCatching {
            block()
        }.onFailure {
            if (it is CancellationException) throw it else onError?.invoke(it)
        }
    }
}

class MutableEventFlow<T : Any> private constructor(
    private val backingFlow: MutableStateFlow<Event<T>?>,
) : MutableStateFlow<Event<T>?> by backingFlow {

    constructor() : this(MutableStateFlow(null))

    fun setEvent(data: T) {
        value = Event(data)
    }

    fun asEventFlow() = EventFlow(this)
}

class EventFlow<out T : Any> constructor(
    private val mutable: MutableEventFlow<T>,
) : StateFlow<Event<T>?> by mutable {

    suspend fun retrieveEach(collector: suspend (T?) -> Unit) {
        mutable.collectLatest { collector(it?.retrieve()) }
    }
}

data class Event<out T : Any>(
    private val data: T,
) {

    private val _isRetrieved = atomic(false)
    val isRetrieved: Boolean
        get() = _isRetrieved.value

    fun peek(): T {
        return data
    }

    fun retrieve(): T? {
        return if (!_isRetrieved.getAndSet(true)) data else null
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + _isRetrieved.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event<*>

        if (data != other.data) return false
        if (isRetrieved != other.isRetrieved) return false

        return true
    }
}
