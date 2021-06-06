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

@file:JvmName("MasaViewExtensions")

package ai.mawdoo3.salma.utils


import android.widget.TextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


fun TextView.getTextLineCount(text: String, lineCount: (Int) -> (Unit)) {
    val params: PrecomputedTextCompat.Params = TextViewCompat.getTextMetricsParams(this)
    val ref: WeakReference<TextView> = WeakReference(this)

    GlobalScope.launch(Dispatchers.Default) {
        val text = PrecomputedTextCompat.create(text, params)
        GlobalScope.launch(Dispatchers.Main) {
            ref.get()?.let { textView ->
                TextViewCompat.setPrecomputedText(textView, text)
                lineCount.invoke(textView.lineCount)
            }
        }
    }
}