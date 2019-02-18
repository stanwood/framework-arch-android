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

package io.stanwood.framework.arch.core.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.stanwood.framework.arch.core.Resource

object ResourceTransformer {
    fun <T> fromObservable(
        resumeInCaseOfError: ((Throwable) -> Observable<T>)? = null
    ): ObservableTransformer<T, Resource<T>> =
        ObservableResourceTransformer(resumeInCaseOfError)

    fun <T> fromSingle(
        resumeInCaseOfError: ((Throwable) -> Single<T>)? = null
    ): SingleTransformer<T, Resource<T>> =
        SingleResourceTransformer(resumeInCaseOfError)

    private class ObservableResourceTransformer<T>(
        private inline val resumeInCaseOfError: ((Throwable) -> Observable<T>)? = null
    ) : ObservableTransformer<T, Resource<T>> {
        override fun apply(upstream: Observable<T>): ObservableSource<Resource<T>> =
            (resumeInCaseOfError?.let { upstream.onErrorResumeNext(it) } ?: upstream)
                .map { createSuccess(it) }
                .onErrorReturn { createFailed(it) }
    }

    private class SingleResourceTransformer<T>(
        private inline val resumeInCaseOfError: ((Throwable) -> Single<T>)? = null
    ) : SingleTransformer<T, Resource<T>> {
        override fun apply(upstream: Single<T>): SingleSource<Resource<T>> =
            (resumeInCaseOfError?.let { upstream.onErrorResumeNext(it) } ?: upstream)
                .map { createSuccess(it) }
                .onErrorReturn { createFailed(it) }
    }

    private fun <T> createSuccess(source: T): Resource<T> =
        Resource.Success(source)

    private fun <T> createFailed(throwable: Throwable): Resource<T> = Resource.Failed(throwable)
}