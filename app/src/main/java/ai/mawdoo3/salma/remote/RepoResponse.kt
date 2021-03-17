package com.banking.core.remote


import retrofit2.Response

sealed class RepoResponse<T> {
    companion object {
        fun <T> create(response: Response<T>): RepoResponse<T> {
            if (response.isSuccessful) {
                val body = response.body() ?: return RepoEmptyResponse()
                return RepoSuccessResponse(body = body)
            } else if (response.code() == 401) {
                return RepoErrorResponse(UnauthorizedException())
            } else {
                return RepoErrorResponse(Exception(), response.code())
            }
        }

        fun <T> create(error: Throwable): RepoErrorResponse<T> {
            return RepoErrorResponse(error)
        }
    }
}

class RepoEmptyResponse<T> : RepoResponse<T>()
data class RepoErrorResponse<T>(val error: Throwable, val errorCode: Int = -1) : RepoResponse<T>()
data class RepoSuccessResponse<T>(val body: T) : RepoResponse<T>()