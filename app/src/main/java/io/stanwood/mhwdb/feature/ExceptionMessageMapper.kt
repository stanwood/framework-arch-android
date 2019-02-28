/*
 * Copyright (c) 2019 stanwood GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.stanwood.mhwdb.feature

import android.content.Context
import io.stanwood.framework.network.util.NoConnectivityException
import io.stanwood.mhwdb.R
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ExceptionMessageMapper(val context: Context) : (Throwable) -> String {
    override fun invoke(throwable: Throwable): String =
        context.resources.let {
            when (throwable) {
                is HttpException -> it.getString(R.string.error_response, throwable.code())
                is NoConnectivityException, is SocketTimeoutException -> it.getString(R.string.error_no_connection)
                else -> it.getString(R.string.error_unknown)
            }
        }
}