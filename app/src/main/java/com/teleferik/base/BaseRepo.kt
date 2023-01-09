package com.teleferik.base

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.teleferik.data.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException

abstract class BaseRepo {
    suspend fun <T> safeApiCalls(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Log.e("BaseRepo->","$throwable")
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(
                            false,
                            throwable.code(),
                            throwable.response()!!.errorBody() as ResponseBody
                        )
                    }

                    is JsonSyntaxException -> {
                        Resource.Failure(
                            false,
                            0,
                            throwable.localizedMessage
                            )
                    }

                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }
}