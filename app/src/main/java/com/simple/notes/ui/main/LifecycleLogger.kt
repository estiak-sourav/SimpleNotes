package com.simple.notes.ui.main

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
class LifecycleLogger(private val tag: String) : DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {}
}