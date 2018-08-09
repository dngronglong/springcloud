package com.my.springcloud.utils;


import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.gson.Gson;
import com.my.springcloud.beans.*;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class GetUrl {

    public static List<Music.Song> getMusicList(String word, String page, String count) {
        String url = "http://s.music.qq.com/fcgi-bin/music_search_new_platform?t="+page+"&n="+count+"&aggr=1&cr=1&loginUin=0&format=json&inCharset=GB2312&outCharset=utf-8&notice=0&platform=jqminiframe.json&needNewCode=0&p=1&catZhida=0&remoteplace=sizer.newclient.next_song&w=" + word;
        HttpConfig httpConfig = HttpConfig.custom().url(url);
        try {
            String response = HttpClientUtil.send(httpConfig);
            Gson gson = new Gson();
            Music music = gson.fromJson(response, Music.class);//new TypeToken<Music>(){}.getType()
            List<Music.Song> musicList = music.getData().getSong().getList();
            musicList.forEach((song) -> {
                String f = song.getF();
                String[] j = f.split("\\|");
                if (j.length == 1) {
                    song.setAlbum("");
                } else {
                    String lyricSrc = "http://music.qq.com/miniportal/static/lyric/"+String.valueOf(Integer.valueOf(j[0]) % 100) + "/" +String.valueOf(j[0])+".xml";
                    song.setLyricSrc(lyricSrc);
                    song.setMid(j[20]);
                    song.setAlbum(j[5]);
                }
            });
            return musicList;
        } catch (HttpProcessException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 获取封面
     * @param mid
     * @return
     */
    public static MusicHQ getAblum(String mid){
        String url="http://www.douqq.com/qqmusic/qqapi.php?mid="+mid;
        HttpConfig httpConfig = HttpConfig.custom().url(url).encoding("UTf-8");
        try {
            String response = HttpClientUtil.send(httpConfig);
            response=response.substring(1).replace("\\\\\\","");
            response=response.substring(0,response.length()-1);
            response=response.replace("\\","");
            Gson gson = new Gson();
            MusicHQ musicHQ=gson.fromJson(response, MusicHQ.class);
            //System.out.println(musicHQ);
            return musicHQ;
        } catch (HttpProcessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getLyric(String lyricSrc) {
        lyricSrc = "http://music.qq.com/miniportal/static/lyric/68/101803868.xml";
        HttpConfig httpConfig = HttpConfig.custom().url(lyricSrc).encoding("GB2312");

        try {
            String response = HttpClientUtil.send(httpConfig);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new URL(lyricSrc));
            Element rootElm = document.getRootElement();
            return rootElm.getText();
        } catch (HttpProcessException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return null;
    }


//    public static List<MusicBean> qqMusicUtil(String word) {
//        JSONObject jsonObject = GetUrl.getMid(word);
//        JSONObject jsonObject1 = JSONObject.fromObject(jsonObject.getString("data"));
//        JSONObject jsonObject2 = JSONObject.fromObject(jsonObject1.getString("song"));
//        JSONArray jsonObject3 = JSONArray.fromObject(jsonObject2.getString("list"));
//        List<MusicBean> list = new ArrayList<>();
//        for (int i = 0; i < jsonObject3.size(); i++) {
//            MusicBean musicBean = new MusicBean();
//            String s = jsonObject3.getJSONObject(i).getString("f");
//            String[] j = s.split("\\|");
//            if (j.length == 1) {
//                j = j[0].split("@@");
//                musicBean.setMusicName(jsonObject3.getJSONObject(i).getString("fsong"));
//                musicBean.setAlbum("");
//                musicBean.setSinger(jsonObject3.getJSONObject(i).getString("fsinger"));
//            } else {
//                musicBean.setMusicName(jsonObject3.getJSONObject(i).getString("fsong"));
//                musicBean.setAlbum(jsonObject3.getJSONObject(i).getString("fsinger"));
//                musicBean.setSinger(j[3]);
//                musicBean.setMid(j[20]);
//            }
//
//            list.add(musicBean);
//        }
//        //System.out.println(list);
//        return list;
//    }

    public static MusicBean qqUrl(String mid, String musicName) {
        try {
            mid = URLEncoder.encode(mid, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://www.douqq.com/qqmusic/qqapi.php?mid=" + mid;
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "utf-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //String s=json.toString().replace("\\","");
        JSONObject jsonObject = JSONObject.fromObject(json.substring(1, json.length() - 1).replace("\\\"", "\""));
        MusicBean musicBean = new MusicBean();
        musicBean.setAudio_name(musicName);
        jsonObject = JSONObject.fromObject(json.substring(1, json.length() - 1).replace("\\", ""));
        musicBean.setM4a(jsonObject.getString("m4a"));
        musicBean.setMp3_l(jsonObject.getString("mp3_l"));
        musicBean.setMp3_h(jsonObject.getString("mp3_h"));
        musicBean.setApe(jsonObject.getString("ape"));
        musicBean.setFlac(jsonObject.getString("flac"));
        musicBean.setCover(jsonObject.getString("pic"));
        return musicBean;
    }

    /**
     * 新歌热搜
     * @return
     */
    public static List<TopBean.SongList> top(){
        String url="https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8¬ice=0&platform=h5&needNewCode=1&tpl=3&page=detail&type=top&topid=27&_=1519963122923";
        HttpConfig httpConfig = HttpConfig.custom().url(url);
        try {
            String response = HttpClientUtil.send(httpConfig);
            Gson gson = new Gson();
            TopBean topBean = gson.fromJson(response, TopBean.class);//new TypeToken<Music>(){}.getType()
            List<TopBean.SongList> musicList = topBean.getSongList();
            //System.out.println(musicList);
            return musicList;
        } catch (HttpProcessException e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * 随机推荐音乐
     */
    public static List<TopBean.SongList> random(){
        String url="https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8¬ice=0&platform=h5&needNewCode=1&tpl=3&page=detail&type=top&topid=36&_=1520777874472";
        HttpConfig httpConfig = HttpConfig.custom().url(url);
        try {
            String response = HttpClientUtil.send(httpConfig);
            Gson gson = new Gson();
            TopBean topBean = gson.fromJson(response, TopBean.class);//new TypeToken<Music>(){}.getType()
            List<TopBean.SongList> musicList = topBean.getSongList();
            //System.out.println(musicList);
            return musicList;
        } catch (HttpProcessException e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * 酷狗接口获取hash
     */
    public static KuGouMusicBean getJson(String words, String page, String limit) {
        try {
            words = URLEncoder.encode(words, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://songsearch.kugou.com/song_search_v2?callback=jQuery191034642999175022426_1489023388639&keyword=" + words + "&page="+page+"&pagesize="+limit+"&userid=-1&clientver=&platform=WebFilter&tag=em&filter=2&iscorrection=1&privilege_filter=0&_=1489023388641";
        HttpConfig httpConfig = HttpConfig.custom().url(url);
        try {
            String response = HttpClientUtil.send(httpConfig);
            String js = response.replace("jQuery191034642999175022426_1489023388639", "").replace("(", "").replace(")", "");
            js=js.replace("<em>","").replace("<\\/em>","");
            Gson gson=new Gson();
            System.out.println(js);
            KuGouMusicBean kuGouMusicBean=gson.fromJson(js,KuGouMusicBean.class);
            return kuGouMusicBean;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * 酷狗接口获取url
     */
    public static String getUrl(String hash) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String url = "http://www.kugou.com/yy/index.php?r=play/getdata&hash=" + hash;
//        String mD = GetMD5(hash + "kgcloud");
//        System.out.println(mD);
//        String url="http://trackercdn.kugou.com/i/?cmd=4&hash="+ hash+ "&key="+mD+ "&pid=1&forceDown=0&vip=1";
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "utf-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
