/*
 * Copyright 2017 Arthur Ivanets, arthur.ivanets.l@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("ViewExtensions")

package ai.mawdoo3.salma.utils

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


fun View.enable() {
    isEnabled = true
}


fun View.disable() {
    isEnabled = false
}


fun View.makeVisible() {
    this.visibility = View.VISIBLE
}


fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}


fun View.makeGone() {
    this.visibility = View.GONE
}


/**
 * @isVisible sets the View visibility to [View.VISIBLE] if true, [View.GONE] if false.
 */
fun View.setVisible(isVisible: Boolean) {
    this.visibility = (if (isVisible) View.VISIBLE else View.GONE)
}

fun Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Any>(key)

fun Fragment.setNavigationResult(key: String = "result", result: Any) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun View.disableWithDelay() {
    isEnabled = false
    Handler(Looper.getMainLooper()).postDelayed({ isEnabled = true }, 500)
}

