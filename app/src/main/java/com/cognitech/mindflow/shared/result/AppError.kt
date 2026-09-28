package com.cognitech.mindflow.shared.result

sealed interface AppError { val message: String
    data class Validation(override val message: String) : AppError
    data class Storage(override val message: String) : AppError
    data class Unknown(override val message: String) : AppError
}
