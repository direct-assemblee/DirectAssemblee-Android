package org.ladlb.directassemblee.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class AbstractViewModel : ViewModel(), CoroutineScope by MainScope() {

    override fun onCleared() {
        super.onCleared()
        cancel()
    }

}