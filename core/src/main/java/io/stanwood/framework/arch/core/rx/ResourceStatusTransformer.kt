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

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.stanwood.framework.arch.core.Resource
import io.stanwood.framework.arch.core.ResourceStatus
import org.reactivestreams.Publisher

object ResourceStatusTransformer {

    fun <T> fromObservable(): ObservableTransformer<Resource<T>, ResourceStatus> =
        ObservableResourceTransformer()

    fun <T> fromSingle(): SingleTransformer<Resource<T>, ResourceStatus> =
        SingleResourceTransformer()

    fun <T> fromFlowable(): FlowableTransformer<Resource<T>, ResourceStatus> =
        FlowableResourceTransformer()

    private class FlowableResourceTransformer<T> : FlowableTransformer<Resource<T>, ResourceStatus> {
        override fun apply(upstream: Flowable<Resource<T>>): Publisher<ResourceStatus> =
            upstream.map { it.toResourceStatus() }
    }

    private class ObservableResourceTransformer<T> : ObservableTransformer<Resource<T>, ResourceStatus> {
        override fun apply(upstream: Observable<Resource<T>>): ObservableSource<ResourceStatus> =
            upstream.map { it.toResourceStatus() }
    }

    private class SingleResourceTransformer<T> : SingleTransformer<Resource<T>, ResourceStatus> {
        override fun apply(upstream: Single<Resource<T>>): SingleSource<ResourceStatus> =
            upstream.map { it.toResourceStatus() }
    }
}

fun <T> Resource<T>.toResourceStatus() =
    data
        ?.let { ResourceStatus.Success }
        ?: when (this) {
            is Resource.Failed -> ResourceStatus.Error(msg, cause)
            is Resource.Loading -> ResourceStatus.Loading
            else -> ResourceStatus.Success
        }