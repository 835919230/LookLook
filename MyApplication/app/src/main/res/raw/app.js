/**
 * Created by 诚 on 2016/7/7.
 */
var app = {
    host:"http://"+window.location.host,
    pathname:window.location.pathname,

    initUrl:"http://"+window.location.host+"/ajax",
    uploadUrl:"http://"+window.location.host+"/uploadfile",
    deleteUrl:"http://"+window.location.host+"/deletefile",
    renameUrl:"http://"+window.location.host+"/renamefile",
    makeUrl:"http://"+window.location.host+"/makedir",
    
    renameInput:'<input type="text" class="renameInput" />',
    renameSubmitBtn:'<button class="renameSubmitBtn btn btn-warning btn-sm">更改</button>',
    renameHidden:'<input type="hidden" value="" class="renameHidden"/>',
    renameHiddenForFilename:'<input type="hidden" value="" class="renameHiddenForFilename"/>',

    makeDirInput:'<input type="text" id="makeDirInput" placeholder="请输入文件名"/>',
    makeDirBtn:'<button type="button" id="makeDirBtn" onclick="app.makeDir()" class="btn btn-warning btn-sm">确定</button>',
    
    init:function (uri) {
    $('#pathname').val(app.pathname);
        $.ajax({
            type:'post',
            url:this.initUrl,
            data:{filePath:uri},
            dataType:'json',
            beforeSend:function () {
             console.log('开始初始化');
            },
            success:function (json) {
                var str = "";
                console.log(json);
                for (var idx in json){
                    console.log(json[idx]);
                    var obj = json[idx];
                    var url = app.host+obj.path;
                    str += '<tr>'+
                            '<td class="renameTd"><a class="filename" href="'+url+'">'+obj.name+'</a> </td>'+
                            '<td>'+obj.lastModified+'</td>'+
                            '<td>'+obj.size+'</td>'+
                            '<td>'+
                            '<button class="btn btn-primary renameBtn">重命名</button>'+
                            '<button class="btn btn-danger deleteBtn">删除</button>'+
                            '</td>'
                            '</tr>';
                }
                $('#tbody').html(str);

                $('#uploadBtn').click(function (e) {
                                app.uploadFile();
                            });

                            $('.deleteBtn').click(function (e) {
                                if (!confirm("确定要删除该文件吗？")){
                                    e.preventDefault();
                                    return false;
                                }
                                var filename = $(this).parent().parent().find('.filename').text();

                                console.log("要删除的文件是 :"+filename);
                                console.log(app.pathname+'/'+filename)
                                app.deleteFile(filename);
                            });

                            $('.renameBtn').click(function (e) {
                                var renameTd = $(this).parent().parent().find('.renameTd');
                                console.log(renameTd.html());
                                if ($(this).text()==='重命名') {
                                    var pre = renameTd.html();
                                    var fName = renameTd.find('a').text();
                                    renameTd.html(app.renameInput + app.renameSubmitBtn + app.renameHidden + app.renameHiddenForFilename);
                                    renameTd.find('.renameHidden').val(pre);
                                    renameTd.find('.renameHiddenForFilename').val(fName);
                                    $(this).text('取消重命名');
                                    app.dispatchEvent();
                                } else {
                                    renameTd.html(renameTd.find('.renameHidden').val());
                                    $(this).text('重命名');
                                }
                            });

            }
        });
    },
    uploadFile:function () {
        $('#uploadForm').ajaxSubmit({
            url:this.uploadUrl,
            type:'post',
            success:function (e) {
                alert('上传成功');
                app.init(app.pathname);
            },
            error:function (e) {
                alert('上传失败');
            },
            timeout:300*1000
        });
    },
    deleteFile:function (filename) {
        if(!filename){
            alert("文件名不能为空");
            return false;
        }
        $.ajax({
            type:'post',
            data:{filepath:app.pathname+'/'+filename},
            url:this.deleteUrl,
            dataType:'json',
            success:function (json) {
                alert(json.message);
                app.init(app.pathname);
            },
            error:function () {
                alert('发生错误了阿偶');
            }
        });
    },
    renameFile:function (newFilename,oldFilename) {
        if (!newFilename){
            alert("文件名不能为空")
            return false;
        }
        
        $.ajax({
            type:'post',
            data:{newFilepath:this.pathname+'/'+newFilename,oldFilepath:this.pathname+'/'+oldFilename},
            url:this.renameUrl,
            dataType:'json',
            success:function (json) {
                if (json.succeed){
                    alert(json.message);
                    app.init(app.pathname);
                }
                else
                    alert(json.message);

            },
            error:function () {
                alert("发生错误了阿偶");
            }
        });
        
    },
    
    makeDir:function () {
        var filename = $('#makeDirInput').val();
        if (filename.length <= 0)
        {
            alert("文件名不能为空！")
            return false;
        }
        $.ajax({
            type:'post',
            data:{filepath:app.pathname,filename:filename},
            url:this.makeUrl,
            dataType:'json',
            success:function (json) {
                alert(json.message);
                console.log(json);
                if (json.succeed){
                    app.init(app.pathname);
                    $('#makeDirContainer').html('<button id="makeDirBtn" class="btn btn-success" onclick="app.makeDirBtnFunc()">创建新目录</button>');
                }
            },
            error:function () {
                alert('阿偶发生错误了');
            }
        });
    },
    log:function () {
        console.log("host:"+this.host);
        console.log("pathname:"+this.pathname);
        console.log("initUrl:"+this.initUrl);
        console.log("uploadUrl:"+this.uploadUrl);
        console.log("deleteUrl:"+this.deleteUrl);
        console.log("renameUrl:"+this.renameUrl);
        console.log("makeUrl:"+this.makeUrl);
    },
    dispatchEvent:function () {
        $('.renameSubmitBtn').click(function (e) {
            var This = $(this);
            console.log(This.prev().val());
            if (confirm("确定要更改吗？"))
                app.renameFile(This.prev().val(),This.next().next().val());
        });
    },
    makeDirBtnFunc:function () {
        var name = $('#makeDirBtn').text();
        if (name === '创建新目录'){
            $('#makeDirBtn').text('取消创建新目录');
            $('#makeDirContainer').append(app.makeDirInput+app.makeDirBtn);
        } else {
            $('#makeDirContainer').html('<button id="makeDirBtn" class="btn btn-success" onclick="app.makeDirBtnFunc()">创建新目录</button>')
        }
    }
}