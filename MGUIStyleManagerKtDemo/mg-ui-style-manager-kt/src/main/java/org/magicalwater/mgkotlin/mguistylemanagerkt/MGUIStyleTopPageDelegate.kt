package org.magicalwater.mgkotlin.mguistylemanagerkt

import android.view.View

interface MGUIStyleTopPageDelegate {
    fun topPageTagForUIStyle(): String?

    fun styleAttachOnView(view: View?) {
        settingStyle(topPageTagForUIStyle(), view)
    }

    private fun settingStyle(tag: String?, view: View?) {
        if (tag != null && view != null) {
            MGUIStyleManager.shared.injectStyleForTopPage(tag, view)
        }
    }
}