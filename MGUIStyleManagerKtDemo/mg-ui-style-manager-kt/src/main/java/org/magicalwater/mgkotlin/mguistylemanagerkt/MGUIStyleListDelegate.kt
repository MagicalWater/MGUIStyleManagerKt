package org.magicalwater.mgkotlin.mguistylemanagerkt

import android.support.v7.widget.RecyclerView
import org.magicalwater.mgkotlin.mgsectionadapterkt.holder.MGBaseHolder
import org.magicalwater.mgkotlin.mgsectionadapterkt.model.MGSection
import org.magicalwater.mgkotlin.mguistylemanagerkt.deserialization.APIStyleItemUI

/**
 * 讓 MGBaseAdapter 繼承此class, 複寫 listTagForUIStyle 載入 holderType 對應的 tag
 * 再 holder bind時呼叫 styleAttachOnList 方法進行style設置
 * */
interface MGUIStyleListDelegate {

    var styles: Map<Int, APIStyleItemUI?>?

    private fun listStyleMap(): Map<Int, APIStyleItemUI?> {
        if (styles == null) {
            styles = listTagForUIStyle().map {
                it.key to MGUIStyleManager.shared.getStyleForList(it.value)
            }.toMap()
        }
        return styles!!
    }

    //複寫此方法, 設定 holderType 對應的 tag
    fun listTagForUIStyle(): Map<Int, Int> = mapOf()

    fun styleAttachOnList(holder: MGBaseHolder, section: MGSection) {
        MGUIStyleManager.shared.settingStyleForListItem(section.row, listStyleMap()[section.holderType], holder.itemView)
    }
}