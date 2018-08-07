/**************************************************
 * MKOnlinePlayer v2.31
 * Ajax 后台数据交互请求模块
 * 编写：mengkun(http://mkblog.cn)
 * 时间：2017-9-13
 *************************************************/

// ajax加载搜索结果
function ajaxSearch() {
    if(rem.wd === ""){
        layer.msg('搜索内容不能为空', {anim:6});
        return false;
    }
    
    if(rem.loadPage == 1) { // 弹出搜索提示
        var tmpLoading = layer.msg('搜索中', {icon: 16,shade: 0.01});
    }
    var total=0;
    $.ajax({
        type: mkPlayer.method, 
        url: mkPlayer.api, 
        data: "count=" + mkPlayer.loadcount + "&source=" + rem.source + "&pages=" + rem.loadPage + "&words=" + rem.wd,
        dataType : "json",
        complete: function(XMLHttpRequest, textStatus) {
            if(tmpLoading) layer.close(tmpLoading);    // 关闭加载中动画
        },  // complete
        success: function(jsonData){
            total=jsonData.songList.data.total;
            
            // 调试信息输出
            if(mkPlayer.debug) {
                console.debug("搜索结果数：" + total);
                //console.debug("搜索结果 ："+jsonData)
            }
            
            if(rem.loadPage == 1)   // 加载第一页，清空列表
            {
                if(jsonData.songList.data.list.length === 0)   // 返回结果为零
                {
                    layer.msg('没有找到相关歌曲', {anim:6});
                    return false;
                }
                musicList[0].item = [];
                rem.mainList.html('');   // 清空列表中原有的元素
                addListhead();      // 加载列表头
            } else {
                $("#list-foot").remove();     //已经是加载后面的页码了，删除之前的“加载更多”提示
            }
            
            if(jsonData.songList.data.list.length === 0)
            {
                addListbar("nomore");  // 加载完了
                return false;
            }
            
            var tempItem = [], no = musicList[0].item.length;
            if (rem.source=="tencent") {
                for (var i = 0; i < jsonData.songList.length; i++) {
                    no ++;
                    tempItem =  {
                        id: jsonData.songList[i].id,  // 音乐ID
                        name: jsonData.songList[i].musicName,  // 音乐名字
                        artist: jsonData.songList[i].singer, // 艺术家名字
                        album: jsonData.songList[i].album,    // 专辑名字
                        mid: jsonData.songList[i].mid,//音乐mid
                        pic: null,    // 专辑图片
                        url: null   // mp3链接
                    };
                    console.log(tempItem);
                    musicList[0].item.push(tempItem);   // 保存到搜索结果临时列表中
                    addItem(no, tempItem.name, tempItem.artist, tempItem.album);  // 在前端显示
                }
            }else if(rem.source=="kugou"){
                for (var i = 0; i < jsonData.songList.data.list.length; i++) {
                    no ++;
                    tempItem =  {
                        id: jsonData.songList.data.list[i].id,  // 音乐ID
                        name: jsonData.songList.data.list[i].songName,  // 音乐名字
                        artist: jsonData.songList.data.list[i].singerName, // 艺术家名字
                        album: jsonData.songList.data.list[i].albumName,    // 专辑名字
                        hash: jsonData.songList.data.list[i].fileHash,//音乐mid
                        pic: null,    // 专辑图片
                        url: null   // mp3链接
                    };
                    musicList[0].item.push(tempItem);   // 保存到搜索结果临时列表中
                    addItem(no, tempItem.name, tempItem.artist, tempItem.album);  // 在前端显示
                }
            }
            // 调试信息输出
            if(mkPlayer.debug) {
                console.info("当前已加载条数："+no);
            }
            
            rem.dislist = 0;    // 当前显示的是搜索列表
            rem.loadPage ++;    // 已加载的列数+1
            
            dataBox("list");    // 在主界面显示出播放列表
            refreshList();  // 刷新列表，添加正在播放样式
            
            if(no >= total) {
                addListbar("nomore");  // 没加载满，说明已经加载完了
            } else {
                addListbar("more");     // 还可以点击加载更多
            }
            
            if(rem.loadPage == 2) listToTop();    // 播放列表滚动到顶部
        },   //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('搜索结果获取失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
        }   // error
    });//ajax
}

// 完善获取音乐信息
// 音乐所在列表ID、音乐对应ID、回调函数
function ajaxUrl(music, callback) {
    $.ajax({ 
        type: mkPlayer.method, 
        // url: "/mic/Qplay",  //QQ音乐接口
        url : "/mic/kgplay",
        data: "hash=" + music.hash,
        dataType : "json",
        success: function(jsonData){
            // 调试信息输出
            if(mkPlayer.debug) {
                // console.debug("歌曲链接：" + jsonData.mp3_l);  //QQ音乐
                console.debug("歌曲链接："+jsonData.data.play_url); //酷狗音乐
             }
            
            if(jsonData.play_url === "") {
                music.url = "err";
            } else {
                // music.url = jsonData.mp3_l;    // 记录结果
                music.url = jsonData.data.play_url;    // 记录结果
            }
            
            updateMinfo(music); // 更新音乐信息
            //
            callback(music);    // 回调函数
            return true;
        },   //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('歌曲链接获取失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
        }   // error 
    }); //ajax
    
}

// 完善获取音乐封面图
// 包含音乐信息的数组、回调函数
function ajaxPic(music, callback)
{
    //已经有数据，直接回调
    if(music.pic !== null && music.pic !== "err" && music.pic !== "") {
        callback(music);
        return true;
    }
    // pic_id 为空，赋值链接错误。直接回调
    if(music.pic_id === null) {
        music.pic = "err";
        updateMinfo(music); // 更新音乐信息
        callback(music);
        return true;
    }
    
    $.ajax({ 
        type: mkPlayer.method, 
        // url: "/mic/ablum",
        url :"/mic/kgplay",
        // data: "mid="+music.mid,
        data: "hash=" + music.hash,
        dataType : "json",
        success: function(jsonData){
            // 调试信息输出
            if(mkPlayer.debug) {
                // console.log("歌曲封面：" + jsonData.pic);
                console.log("歌曲封面：" + jsonData.data.img);
            }
            
            if(jsonData.url !== "") {
                // music.pic = jsonData.pic;    // 记录结果
                music.pic = jsonData.data.img;    // 记录结果
            } else {
                music.pic = "err";
            }
            
            updateMinfo(music); // 更新音乐信息
            
            callback(music);    // 回调函数
            return true;
        },   //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('歌曲封面获取失败 - ' + XMLHttpRequest.status);
            //console.error(XMLHttpRequest + textStatus + errorThrown);
        }   // error 
    }); //ajax
    
}

// ajax加载用户歌单
// 参数：歌单网易云 id, 歌单存储 id，回调函数
var flag=false;
var temp=0;
function ajaxPlayList(lid, id, callback){
    console.log("加载的ID"+id);
    if(!lid) return false;
    
    // 已经在加载了，跳过
    if(musicList[id].isloading === true) {
        return true;
    }
    
    musicList[id].isloading = true; // 更新状态：列表加载中
    // if (!flag){
    //     $.ajax({
    //         type: mkPlayer.method,
    //         url :"/mic/top",
    //         dataTpe : "json",
    //         success : function (data) {
    //             for (var i=0;i<data.top.length;i++){
    //                 musicList[3].item.push({name:data.top[i].song.musicName,album:data.top[i].song.album,mid:data.top[i].song.mid,artist:data.top[i].song.listSinger[0].name})
    //             }
    //             //console.log(musicList[3].item);
    //             //musicList[3].isloading=false; //更新加载状态
    //         }
    //     });
    //     flag=true;
    // }

    $.ajax({
        type: mkPlayer.method,
        url: "/list/userList",
        data: "id=" + 1,
        dataType : "json",
        complete: function(XMLHttpRequest, textStatus) {
            musicList[id].isloading = false;    // 列表已经加载完了
        },  // complete
        success: function(jsonData){
            console.log(jsonData);
            // 存储歌单信息
            var tempList =[{
                id:'',
                name:'',
                cover:'',
                creatorName:'',
                creatorAvatar:'',
                item:[]
            }];
            for (var i=0;i<jsonData.userList.length;i++){
                tempList.push({id:jsonData.userList[i].id,name:jsonData.userList[i].name,cover:'',creatorName:'',creatorAvatar:'',item:[]});
            }
            console.log(tempList);

            if(tempList[temp].cover !== '') {
                tempList[temp].cover = musicList[id].cover;
            } else {
                tempList[temp].cover = musicList[id].cover;
            }

            // if(typeof jsonData.playlist.tracks !== undefined || jsonData.playlist.tracks.length !== 0) {
            //     // 存储歌单中的音乐信息
            //     for (var i = 0; i < jsonData.playlist.tracks.length; i++) {
            //         tempList.item[i] =  {
            //             id: jsonData.playlist.tracks[i].id,  // 音乐ID
            //             name: jsonData.playlist.tracks[i].name,  // 音乐名字
            //             artist: jsonData.playlist.tracks[i].ar[0].name, // 艺术家名字
            //             album: jsonData.playlist.tracks[i].al.name,    // 专辑名字
            //             source: "netease",     // 音乐来源
            //             url_id: jsonData.playlist.tracks[i].id,  // 链接ID
            //             pic_id: null,  // 封面ID
            //             lyric_id: jsonData.playlist.tracks[i].id,  // 歌词ID
            //             pic: jsonData.playlist.tracks[i].al.picUrl,    // 专辑图片
            //             url: null   // mp3链接
            //         };
            //     }
            // }

            // 歌单用户 id 不能丢
            // if(musicList[id].creatorID) {
            //     tempList.creatorID = musicList[id].creatorID;
            //     if(musicList[id].creatorID === rem.uid) {   // 是当前登录用户的歌单，要保存到缓存中
            //         var tmpUlist = playerReaddata('ulist');    // 读取本地记录的用户歌单
            //         if(tmpUlist) {  // 读取到了
            //             for(i=0; i<tmpUlist.length; i++) {  // 匹配歌单
            //                 if(tmpUlist[i].id == lid) {
            //                     tmpUlist[i] = tempList; // 保存歌单中的歌曲
            //                     playerSavedata('ulist', tmpUlist);  // 保存
            //                     break;
            //                 }
            //             }
            //         }
            //     }
            // }

            // 存储列表信息
            musicList[id] = tempList[temp];

            // 首页显示默认列表
            if(id == mkPlayer.defaultlist) loadList(id);
            if(callback) callback(id);    // 调用回调函数

            // 改变前端列表
            $(".sheet-item[data-no='" + id + "'] .sheet-cover").attr('src', tempList[temp].cover);    // 专辑封面
            $(".sheet-item[data-no='" + id + "'] .sheet-name").html(tempList[temp].name);     // 专辑名字

            // 调试信息输出
            // if(mkPlayer.debug) {
            //     console.debug("歌单 [" +tempList.name+ "] 中的音乐获取成功");
            // }
        },   //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('歌单读取失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
            $(".sheet-item[data-no='" + id + "'] .sheet-name").html('<span style="color: #EA8383">读取失败</span>');     // 专辑名字
        }   // error
    });//ajax
    temp++;
}

// ajax加载歌词
// 参数：音乐ID，回调函数
function ajaxLyric(music, callback) {
    lyricTip('歌词加载中...');
    
    // if(!music.lyric_id) callback('');  // 没有歌词ID，直接返回
    
    $.ajax({
        type: mkPlayer.method,
        // url: "/mic/ablum",
        url :"/mic/kgplay",
        // data: "mid="+music.mid,
        data: "hash=" + music.hash,
        dataType : "json",
        success: function(jsonData){
            // 调试信息输出
            if (mkPlayer.debug) {
                console.debug("歌词获取成功");
            }
            
            if (jsonData.data.lyrics) {
                callback(jsonData.data.lyrics, music.lyric_id);    // 回调函数
            } else {
                callback('', music.lyric_id);    // 回调函数
            }
        },   //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('歌词读取失败 - ' + XMLHttpRequest.status);
            //console.error(XMLHttpRequest + textStatus + errorThrown);
            //callback('', music.lyric_id);    // 回调函数
        }   // error   
    });//ajax
}


// ajax加载用户的播放列表
// 参数 用户的网易云 id
function ajaxUserList(uid)
{
    var tmpLoading = layer.msg('加载中...', {icon: 16,shade: 0.01});
    $.ajax({
        type: mkPlayer.method,
        url: mkPlayer.api,
        data: "types=userlist&uid=" + uid,
        dataType : "jsonp",
        complete: function(XMLHttpRequest, textStatus) {
            if(tmpLoading) layer.close(tmpLoading);    // 关闭加载中动画
        },  // complete
        success: function(jsonData){
            if(jsonData.code == "-1" || jsonData.code == 400){
                layer.msg('用户 uid 输入有误');
                return false;
            }
            
            if(jsonData.playlist.length === 0 || typeof(jsonData.playlist.length) === "undefined")
            {
                layer.msg('没找到用户 ' + uid + ' 的歌单');
                return false;
            }else{
                var tempList,userList = [];
                $("#sheet-bar").remove();   // 移除登陆条
                rem.uid = uid;  // 记录已同步用户 uid
                rem.uname = jsonData.playlist[0].creator.nickname;  // 第一个列表(喜欢列表)的创建者即用户昵称
                layer.msg('欢迎您 '+rem.uname);
                // 记录登录用户
                playerSavedata('uid', rem.uid);
                playerSavedata('uname', rem.uname);
                
                for (var i = 0; i < jsonData.playlist.length; i++)
                {
                    // 获取歌单信息
                    tempList = {
                        id: jsonData.playlist[i].id,    // 列表的网易云 id
                        name: jsonData.playlist[i].name,   // 列表名字
                        cover: jsonData.playlist[i].coverImgUrl,   // 列表封面
                        creatorID: uid,   // 列表创建者id
                        creatorName: jsonData.playlist[i].creator.nickname,   // 列表创建者名字
                        creatorAvatar: jsonData.playlist[i].creator.avatarUrl,   // 列表创建者头像
                        item: []
                    };
                    // 存储并显示播放列表
                    addSheet(musicList.push(tempList) - 1, tempList.name, tempList.cover);
                    userList.push(tempList);
                }
                playerSavedata('ulist', userList);
                // 显示退出登录的提示条
                sheetBar();
            }
            // 调试信息输出
            if(mkPlayer.debug) {
                console.debug("用户歌单获取成功 [用户网易云ID：" + uid + "]");
            }
        },   //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('歌单同步失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
        }   // error
    });//ajax
    return true;
}