package org.magicalwater.mgkotlin.mguistylemanagerkt

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable
import org.magicalwater.mgkotlin.mguistylemanagerkt.deserialization.APIStyleConfig
import org.magicalwater.mgkotlin.mguistylemanagerkt.deserialization.APIStyleItemUI
import org.magicalwater.mgkotlin.mguistylemanagerkt.deserialization.APIStyleWidgetUI
import org.magicalwater.mgkotlin.mgutilskt.util.*
import java.io.InputStream

//個別Style統整管理
class MGUIStyleManager private constructor() {

    private lateinit var mConfig: APIStyleConfig
    private val mDrawableUtils = MGDrawableUtils()

    private var mListDefaultIndex = "default"
    private lateinit var mStylePath: String

    private lateinit var mContext: Context

    private val KEY_STYLE_VERSION = "KEY_STYLE_VERSION"

    //除了backgroun外, 其餘的tag
    var viewStyleSettingDelegate: MGUIStyleSettingDelegate? = null

    companion object {
        val shared: MGUIStyleManager = MGUIStyleManager()
    }

    //初始化styleConfig配置文件
    fun initStyleConfig(context: Context, styleJson: String) {
        mContext = context
//        val configJson = MGResourcesFileUtils.loadRawText(context, R.raw.style_config) ?: ""
        mConfig = MGJsonDataParseUtils.deserialize(styleJson, APIStyleConfig::class)!!
        mStylePath = context.filesDir.absolutePath
//        println("讓我看看反序列化吧: $mConfig")

        MGSettingUtils.shared.init(context)
    }

    //初始化資源檔(解壓縮 zip style 至手機目錄)
    //再解壓縮之前檢查版本, 若版本已存在或較新, 就沒必要更新
    fun initStyleExtractor(context: Context, styleIs: InputStream, endHandler: () -> Unit) {
        if (MGVersionUtils.compareIsNew(getStyleVersion(), mConfig.version)) {
            //有新版本, 需要解壓縮
            MGZipUtils.unZipFolder(styleIs, mStylePath) {
                setStyleVersion(mConfig.version)
                endHandler.invoke()
            }
        } else {
            styleIs.close()
            endHandler.invoke()
        }
    }

    private fun getStyleVersion(): String {
        return MGSettingUtils.shared.get(KEY_STYLE_VERSION, "0")
    }

    private fun setStyleVersion(version: String) {
        MGSettingUtils.shared.put(KEY_STYLE_VERSION, version)
    }

    /**
     * 給最上層的aty or fgt呼叫設置style
     * */
    //adapter呼叫對應的style
    fun injectStyleForTopPage(@StringRes pageTagId: Int, view: View) {
        injectStyleForTopPage(mContext.getString(pageTagId), view)
    }

    fun injectStyleForTopPage(pageTag: String, view: View) {
        val uiStyle = mConfig.ui.topPage[pageTag] ?: return
        loopForAllView(uiStyle, view)
    }

    //adapter呼叫對應的style
    fun getStyleForList(@StringRes listTagId: Int): APIStyleItemUI? {
        return getStyleForList(mContext.getString(listTagId))
    }

    //adapter呼叫對應的style
    fun getStyleForList(listTag: String): APIStyleItemUI? {
        return mConfig.ui.list[listTag]
    }

    //設置列表的某個屬性
    fun settingStyleForListItem(index: Int, style: APIStyleItemUI?, view: View) {
        style ?: return
        val indexText = index.toString()
        when {
            style.containsKey(indexText) -> loopForAllView(style[indexText]!!, view)
            style.containsKey(mListDefaultIndex) -> loopForAllView(style[mListDefaultIndex]!!, view)
        }
    }

    //設定某個單獨元件的style
    fun settingStyleForSingle(view: View) {
        searchAndSettingStyle(view, mConfig.ui.single)
    }

    //搜索所有的view以及子view, 並且設置style
    private fun loopForAllView(style: APIStyleWidgetUI, view: View) {
        searchAndSettingStyle(view, style)
        //RecyclerView 跟 ListView 不以此種方式
        //若tag包含在 disableLoopChild 的 view 也不往下搜尋child
        var isDisableLoopChild: Boolean =
        if (view.tag != null && view.tag is String) {
            mConfig.disableLoopChild.contains(view.tag as String)
        } else {
            false
        }
        if (view is ViewGroup && (view !is RecyclerView || view !is ListView || !isDisableLoopChild)) {
            (0 until view.childCount).forEach {
                loopForAllView(style, view.getChildAt(it))
            }
        }
    }

    //設置style, 搜索出相對應的style之後呼叫回調進行設置的行為
    private fun searchAndSettingStyle(view: View, style: APIStyleWidgetUI) {
        val tag = view.tag ?: return
        if (tag !is String) return
        val styleWidget = style[tag] ?: return
        settingStyle(view, styleWidget)
    }

    //最終設置style, 除了backgroun直接設置之外, 其餘外拋
    private fun settingStyle(view: View, attr: APIStyleConfig.WidgetAttr) {
        generatorBackground(view, attr)
        viewStyleSettingDelegate?.styleSetting(
                view,
                generatorStyle(view, attr)
        )
    }

    //生成 Style class 給外部進行設置
    private fun generatorStyle(view: View, style: APIStyleConfig.WidgetAttr): Style {
        return Style(
                generatorContent(view, style),
                generatorText(view, style)
        )
    }

    //background較為特殊, 直接在此處設定
    private fun generatorBackground(view: View, style: APIStyleConfig.WidgetAttr) {
        val background = style.background ?: return
        val src = getRealString(background.src)
        val color = getRealString(background.color)
        if (src != null) {
            //加載圖片
            MGImgLoadUtils.loadDrawable(mContext, getImagePath(src)) {
                view.backgroundDrawable = it
            }
        } else if (color != null) {
            val colors = parseColor(color)
            if (colors.size > 1) {
                //代表兩個以上的顏色, 只能用Drawable的方式
                view.backgroundDrawable =
                        mDrawableUtils.setGradient(
                                MGDrawableUtils.Gradient(
                                        MGDrawableUtils.GRADIENT_LINEAR,
                                        GradientDrawable.Orientation.TOP_BOTTOM, colors.toMutableList()
                                )
                        ).build()
            } else {
                view.backgroundColor = colors[0]
            }
        }
    }

    //生成content
    private fun generatorContent(view: View, style: APIStyleConfig.WidgetAttr): Content {
        val styleContent = style.content ?: return Content()
        val src = getRealString(styleContent.src)
        val color = getRealString(styleContent.color)

        val content = Content()
        if (src != null) { content.srcPath = getImagePath(src) }
        if (color != null) { content.colors = parseColor(color) }
        return content
    }

    //生成text
    private fun generatorText(view: View, style: APIStyleConfig.WidgetAttr): Text {
        val text = style.text ?: return Text()
        val color = getRealString(text.color) ?: return Text()
        return Text(parseColor(color)[0])
    }

    //得到圖片的路徑
    private fun getImagePath(name: String): String {
        return mStylePath + "/" + mConfig.rootFolder + "/"  + name
    }

    /**
     * 得到確切的字串, 因為有可能是指向Res
     * 例如 - 可以用 @string 指定字串
     * 在@ 後面加的字串 即代表 global 底下第一層, 斜線分隔第二層
     */
    private fun getRealString(string: String?): String? {
        string ?: return null
        if (string.isEmpty()) return null
        //先檢查是否為指向, 檢查方式是第一個字母是否為@
        if (string[0] == '@') {
            //去掉第一個字母之後將將斜線 / 分割變成兩個字串, 在下去做搜尋
            val textSplit = string.substring(1).split("/")
            //如果分割長度不是二, 或者無效就直接返回空字串
            if (textSplit.size < 2) {
                return null
            } else {
                return mConfig.globalRes[textSplit[0]]?.get(textSplit[1]) ?: return null
            }
        } else {
            return string
        }
    }

    //解析顏色字串, 若有多個顏色, 以 ~ 分隔
    private fun parseColor(color: String): List<Int> {
        val colorArray = color.split("~")
        return colorArray.map { Color.parseColor(it) }
    }

    data class Style(var content: Content, var text: Text)
    data class Content(var srcPath: String? = null, var colors: List<Int>? = null)
    data class Text(var color: Int? = null)
}