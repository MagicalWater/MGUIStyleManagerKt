package org.magicalwater.mgkotlin.mguistylemanagerkt.deserialization

data class APIStyleConfig(var version: String,
                          var rootFolder: String,
                          var globalRes: Map<String, APIStyleRes>,
                          var disableLoopChild: List<String>,
                          var ui: StyleUI) {

    data class StyleUI(var topPage: APIStylePageUI,
                       var list: APIStyleListUI)

    data class WidgetAttr(
            var background: StyleBackground?,
            var content: StyleContent?,
            var text: StyleText?
    )

    data class StyleBackground(
            var src: String?,
            var color: String?
    )

    data class StyleContent(
            var src: String?,
            var color: String?
    )

    data class StyleText(
            var color: String?
    )

}

typealias APIStyleRes = Map<String,String>

typealias APIStylePageUI = Map<String, APIStyleWidgetUI>
typealias APIStyleListUI = Map<String, APIStyleItemUI>

typealias APIStyleItemUI = Map<String, APIStyleWidgetUI>
typealias APIStyleWidgetUI = Map<String, APIStyleConfig.WidgetAttr>