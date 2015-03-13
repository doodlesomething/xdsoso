# xdsoso
这个项目是一个校园搜索引擎的项目，旨在提供西电校园搜索，项目在Linux下基于Java开发,目前已经完成基本的爬虫部分、网页存储、中文分词部分，项目正在持续进行中。
<br/>关于这个项目的历程可以看看我的博客中[校园搜索引擎开发](http://blog.csdn.net/doubleselect/article/category/2929723 "校园搜索引擎开发")的介绍
<br/>

<br/>
src目录中主要为程序源码，doc为程序中用到的存储文件和字典文件，可以考虑将其分开<br/>
<br/>项目中用到的第三包包括：1.[HtmlParser](http://htmlparser.sourceforge.net/)主要用于提取文本内容的提取和标签如链接的提取；2.[cpDetector](http://cpdetector.sourceforge.net/)主要用于网页编码的预检测方便转换为统一的编码存储
<br/>**补充:**可能由于我项目中使用第三方包、平台的不同和文件路径的不同可能在您的机器上不一定能运行请见谅。