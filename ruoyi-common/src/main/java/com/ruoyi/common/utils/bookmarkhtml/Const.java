package com.ruoyi.common.utils.bookmarkhtml;

import org.springframework.stereotype.Component;

/**
 * @Auther: Wang
 * @Date: 2020/08/22 23:03
 * 功能描述:
 */
@Component
public class Const {

    public static String BASE_PATH;

    public static String LOGIN_SESSION_KEY = "Favorites_user";

    public static String PASSWORD_KEY = "@#$%^&*()OPG#$%^&*(HG";
    public static String URL_CODE = "(?<=charset=)(.+)(?=\")";//取网址的编码

    public static String DES3_KEY = "9964DYByKL967c3308imytCB";

    public static String default_logo="img/logo.jpg";

    public static String userAgent="Mozilla";

    public static String default_Profile=BASE_PATH+"/img/logo.jpg";

    public static String LAST_REFERER = "LAST_REFERER";

    public static int COOKIE_TIMEOUT= 30*24*60*60;

    public static String MenuIocURL= "https://up.raindrop.io/collection/templates/social-media-logos-6/47social.png";
    //已修改父级id的书签
    public static String BOOKMARK_STATE_FLAG= "666";




//	  @Autowired(required = true)
//	  public void setBasePath(@Value("${favorites.base.path}")String basePath) {
//		  Const.BASE_PATH = basePath;
//	  }


}
