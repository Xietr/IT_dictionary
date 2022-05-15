package gordeev.it_dictionary.base

sealed class Result<T> {
    open fun get(): T? = null
}

data class Success<T>(val data: T) : Result<T>() {
    override fun get(): T = data
}

data class ErrorResult<T>(val throwable: Throwable) : Result<T>()