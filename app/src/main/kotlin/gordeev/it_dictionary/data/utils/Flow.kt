package gordeev.it_dictionary.data.utils

import gordeev.it_dictionary.base.ErrorResult
import gordeev.it_dictionary.base.Result
import gordeev.it_dictionary.base.Success
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

fun <T> flowWithResult(
    timeoutMs: Long = defaultTimeoutMs,
    doWork: suspend () -> T,
): Flow<Result<T>> = flow {
    try {
        withTimeout(timeoutMs) {
            emit(Success(doWork()))
        }
    } catch (t: TimeoutCancellationException) {
        emit(ErrorResult(t))
    }
}.catch { t -> emit(ErrorResult(t)) }

private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(3)
