使用方法：

1.双击Reader.jar文件
2.弹出页面选择某一个音频文件夹或者音频文件
3.选择结果输出文件夹路径（文件名固定为：traceLength.txt）
4. 点击计算按钮

结果将输出到固定的traceLength.txt中文件格式类似如下：
我最亲爱的.mp3 284
匆匆那年.mp3 241
相信自己.mp3 213

前边的为文件名，后边的为秒数

主要是借助开源的jaudiotagger库完成工作。 相关资料在 
http://www.jthink.net/jaudiotagger/index.jsp
