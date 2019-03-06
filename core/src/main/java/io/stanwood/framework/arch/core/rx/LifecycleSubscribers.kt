package io.stanwood.framework.arch.core.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

private val onErrorStub: (Throwable) -> Unit = { Timber.e(it) }
private val onCompleteStub: () -> Unit = {}

/**
 * Overloaded subscribe function that allows passing named parameters and lifecycle
 * The source will re/subscribed on 'onStart' and disposed on 'onStop'
 */
fun <T : Any> Observable<T>.subscribeBy(
    lifecycleOwner: LifecycleOwner,
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit = onCompleteStub,
    onNext: (T) -> Unit
) {
    LifecycleSubscriber(lifecycleOwner) { subscribeBy(onError, onComplete, onNext) }
}

/**
 * Overloaded subscribe function that allows passing named parameters and lifecycle
 * The source will re/subscribed on 'onStart' and disposed on 'onStop'
 */
fun <T : Any> Flowable<T>.subscribeBy(
    lifecycleOwner: LifecycleOwner,
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit = onCompleteStub,
    onNext: (T) -> Unit
) {
    LifecycleSubscriber(lifecycleOwner) { subscribeBy(onError, onComplete, onNext) }
}

/**
 * Overloaded subscribe function that allows passing named parameters and lifecycle
 * The source will re/subscribed on 'onStart' and disposed on 'onStop'
 */
fun <T : Any> Single<T>.subscribeBy(
    lifecycleOwner: LifecycleOwner,
    onError: (Throwable) -> Unit = onErrorStub,
    onSuccess: (T) -> Unit
) {
    LifecycleSubscriber(lifecycleOwner) { subscribeBy(onError, onSuccess) }
}

/**
 * Overloaded subscribe function that allows passing named parameters and lifecycle
 * The source will re/subscribed on 'onStart' and disposed on 'onStop'
 */
fun <T : Any> Maybe<T>.subscribeBy(
    lifecycleOwner: LifecycleOwner,
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit = onCompleteStub,
    onSuccess: (T) -> Unit
) {
    LifecycleSubscriber(lifecycleOwner) { subscribeBy(onError, onComplete, onSuccess) }
}

/**
 * Overloaded subscribe function that allows passing named parameters and lifecycle
 * The source will re/subscribed on 'onStart' and disposed on 'onStop'
 */
fun Completable.subscribeBy(
    lifecycleOwner: LifecycleOwner,
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit
) {
    LifecycleSubscriber(lifecycleOwner) { subscribeBy(onError, onComplete) }
}

private class LifecycleSubscriber(
    private val lifecycleOwner: LifecycleOwner,
    val subscribe: () -> Disposable
) : LifecycleObserver {
    private var disposable: Disposable? = null

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        disposable = subscribe.invoke()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        disposable?.dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        disposable?.dispose()
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}