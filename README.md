# MGUIStyleManagerKt
[ ![Download](https://api.bintray.com/packages/water/mgbase/mg-ui-style-manager-kt/images/download.svg) ](https://bintray.com/water/mgbase/mg-ui-style-manager-kt/_latestVersion) 
![](https://img.shields.io/badge/language-kotlin-orange.svg)  

依照給view設置tag的方式關聯ui設置, 達到快速變換ui的效果  

具體配置 config 請看  
[style配置範例](https://github.com/MagicalWater/MGUIStyleManagerKt/blob/master/MGUIStyleManagerKtDemo/mg-ui-style-manager-kt/src/main/res/raw/style_config_demo.txt)  
[style配置說明](https://github.com/MagicalWater/MGUIStyleManagerKt/blob/master/MGUIStyleManagerKtDemo/mg-ui-style-manager-kt/src/main/res/raw/style_config_description.txt)  

## 版本  
0.0.3 - 同步loop設置view會出現設置失敗的不確定性, 因此改為異步搜索, 再跳回主線程設置  
0.0.2 - 加入新結點, ui/single, 方便程式內單獨設置某個元件的style  
0.0.1 - 專案初次提交  

## 添加依賴  

### Gradle  
compile 'org.mgwater.mgbase:mg-ui-style-manager-kt:{version}'  
( 其中 {version} 請自行替入此版號 [ [ ![Download](https://api.bintray.com/packages/water/mgbase/mg-ui-style-manager-kt/images/download.svg) ](https://bintray.com/water/mgbase/mg-ui-style-manager-kt/_latestVersion) )
