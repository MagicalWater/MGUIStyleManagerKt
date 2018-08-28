//package org.magicalwater.mgkotlin.mguistylemanagerkt
//
//import android.view.View
//import org.aspectj.lang.JoinPoint
//import org.aspectj.lang.annotation.After
//import org.aspectj.lang.annotation.Aspect
//import org.aspectj.lang.annotation.Before
//import org.jetbrains.anko.contentView
//import org.magicalwater.mgkotlin.mgsectionadapterkt.holder.MGBaseHolder
//import org.magicalwater.mgkotlin.mgsectionadapterkt.model.MGSection
//

/**
 * 此page處要搭配 aspectJX 解耦所有style設置的地方,
 * 但因為使用上目前遇到一些問題, 所以暫時不採用
 * */

//@Aspect
//class MGStyleAspectJX {
//
//    //在 Activity 開始時執行aop, 進行style注入
//    @After("execution(* com.rocketsz.platform.baseplatform.ui.aty.BaseAty.setupView(..))")
//    fun styleAttachOnAty(point: JoinPoint) {
////        println("執行 aop 注入")
//        val aty = point.target as BaseAty
//        settingStyle(aty.topPageTagForUIStyle(), aty.contentView)
//    }
//
//    //在 Fragment 開始時執行aop, 進行style注入
//    @After("execution(* com.rocketsz.platform.baseplatform.ui.fgt.BaseFgt.setupView(..))")
//    fun styleAttachOnFgt(point: JoinPoint) {
////        println("執行 aop 注入")
//        val fgt = point.target as BaseFgt
//        settingStyle(fgt.topPageTagForUIStyle(), fgt.view)
//    }
//
//    //在adapter需要bind時, 進行style注入
//    @After("execution(* com.rocketsz.platform.baseplatform.adapter.BaseAdapter.bindBodyHolder(..))")
//    fun styleAttachOnList(point: JoinPoint) {
//        val adapter = point.target as BaseAdapter
//        val holder = point.args[0] as MGBaseHolder
//        val section = point.args[1] as MGSection
//        UIStyleManager.shared.settingStyleForListItem(section.row, adapter.listStyleMap()[section.holderType], holder.itemView)
//    }
//
//    private fun settingStyle(tag: String?, view: View?) {
//        if (tag != null && view != null) {
//            UIStyleManager.shared.injectStyleForTopPage(tag, view)
//        }
//    }
//
//}