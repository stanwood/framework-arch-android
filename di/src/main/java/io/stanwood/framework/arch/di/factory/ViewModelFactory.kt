package io.stanwood.framework.arch.di.factory

import io.stanwood.framework.arch.core.ViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory<T : ViewModel> @Inject constructor(
    private val viewModel: Provider<T>
) : Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel.get() as T
}

interface Factory {
    fun <T : ViewModel> create(modelClass: Class<T>): T
}