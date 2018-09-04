package org.magicalwater.mgkotlin.mguistylemanagerkt

import android.view.View

interface MGUIStyleSettingDelegate {
    fun styleSetting(view: View, style: MGUIStyleManager.Style)
}