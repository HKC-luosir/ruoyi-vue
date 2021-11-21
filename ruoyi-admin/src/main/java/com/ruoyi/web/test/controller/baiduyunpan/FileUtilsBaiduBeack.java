package com.ruoyi.web.test.controller.baiduyunpan;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

/**
 * @Auther: Wang
 * @Date: 2021/07/25 02:02
 * 功能描述:
 */
public class FileUtilsBaiduBeack {

    private static final Log log = LogFactory.get();


//    public static void main(String[] args) {
//        //不能有空格
////        String filePath = "D:\\data\\ces\\";
////        String fileName = "day4_MySQL性能优化总结.zip";
//        String filePath = "D:\\Wang\\";
//        String fileName = "22.txt";
//        System.out.println(save(filePath, fileName));
//    }

    /**
     * @Description: TODO 保存文件
     * @param: filePath 文件路径
     * @param: fileName 文件名称
     * return 文件下载地址
     */
    @SneakyThrows
    private static String save(String filePath, String fileName) {
        //本地文件地址
        String absoluteFilePath = filePath + fileName;
        //云端文件地址
        String cloudPath = Constant.APP_PATH + fileName;

        //文件分片并获取md5值
        File file = new File(absoluteFilePath);
        File[] separate = separate(absoluteFilePath, Constant.UNIT);
        StringBuffer md5s = new StringBuffer();
        if (separate.length == 1) {
            md5s.append(getMD5(separate[0]));
//            md5s.append(DigestUtils.md5Hex(new FileInputStream(separate[0])));
        }
        if (separate.length > 1) {
            for (int i = 0; i < separate.length; i++) {
                md5s.append(getMD5(separate[i]) + "\",\"");
//                md5s.append(DigestUtils.md5Hex(new FileInputStream(separate[i])) + "\",\"");
                log.info("正在分片,{}{}", separate[i].toString(), i);
            }
            String s = md5s.toString();
            md5s = new StringBuffer(s.substring(0, md5s.length() - 3));
        }

        Long fileMax = file.length();

        //预上传
        String precreate = precreate(cloudPath,fileMax, 0, md5s.toString());
        log.info("预上传{} iploadid是 {}  md5{}", precreate,(String) new JSONObject(precreate).get("uploadid"),md5s.toString());

        //分片上传
        String upload = upload(cloudPath, (String) new JSONObject(precreate).get("uploadid"), separate);
        log.info("分片上传{}", upload);
        log.info("    >>>>>>>>>>>>>>>>>>>>>>>>>>");

        Thread.sleep(3000);
        //创建文件

        log.info("创建文件 fileName{} 文件大小{} md5:{}   ID:{}", fileName, FileSizeUtil.pathSize(absoluteFilePath),md5s.toString(),(String) new JSONObject(precreate).get("uploadid"));
        String create = create(fileName, fileMax, 0, md5s.toString(),(String) new JSONObject(precreate).get("uploadid"));
        log.info("创建文件{}", create);

        //获取下载地址
        String downUrl = getDownUrl(fileName);
        log.info("获取下载地址{}", downUrl);

        return downUrl;
    }


    /**
     * @Description: TODO 获取下载地址
     * @param: fileName 文件名
     */
    private static String getDownUrl(String fileName) {
        String fileSearch = HttpUtil.get(Constant.FILE_SEARCH + "&access_token=" + Constant.ATOKEN + "&key=" + fileName + "&recursion=1&dir=" + Constant.APP_PATH);
        System.out.println("fileSearch:"+fileSearch);

        JSONObject jsonObject = new JSONObject(fileSearch);
        JSONArray list = jsonObject.getJSONArray("list");
        JSONObject listJSONObject = list.getJSONObject(0);
        Long fs_id = listJSONObject.getLong("fs_id");
        String url = Constant.DOWN_LOUE_URL + "?method=filemetas&access_token=" + Constant.ATOKEN + "&fsids=[" + fs_id + "]&dlink=1";
        String s = HttpUtil.get(url);
        JSONObject sJsonObject = new JSONObject(s);
        JSONArray jsonArray = sJsonObject.getJSONArray("list");
        JSONObject jsonObjectClient = jsonArray.getJSONObject(0);
        String dlink = jsonObjectClient.getStr("dlink");
        return dlink;
    }

    /**
     * @Description: TODO 创建文件
     * @param: fileName 文件名称
     * @param: size 文件大小 字节
     * @param: isDir 0文件 1目录（设置为目录是 size要设置为0）
     * @param: blockList （文件的md5值） 可以把文件分为多个，然后分批上传
     * @return: java.lang.String
     */
    private static String create(String fileName, Long size, Integer isDir, String blockList, String uploadid) {
        String strURL = Constant.FILE_MANAGER_URL + "?method=create&access_token=" + Constant.ATOKEN;
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("path", Constant.APP_PATH + fileName);
        paramMap.put("size", size);
        paramMap.put("autoinit", 1);
        paramMap.put("rtype", 2);
        String str ="[\"" +blockList +"\"]";
        paramMap.put("block_list", str);
        paramMap.put("isdir",isDir);
        paramMap.put("uploadid",uploadid);
//        paramMap.put("app_id",Constant.APP_ID);
        String result= HttpUtil.post(strURL, paramMap);
        System.out.println("block_list :" + paramMap.get("block_list"));
        System.out.println("uploadid :" + paramMap.get("uploadid"));
        System.out.println("path :" + paramMap.get("path"));
        return result;
    }

    /**
     * @Description: TODO 分片上传
     * @param: path 上传到百度网盘的地址
     * @param: uploadid 上传的id
     * @param: filePath 本地文件的地址
     * @return: java.lang.String
     */
    private static String upload(String path, String uploadid, File[] files) {
        try {

            for (int i = 0; i < files.length; i++) {
                String url = Constant.SLICING_UPLOAD_FILE_URL + "?method=upload" +
                        "&access_token=" + Constant.ATOKEN +
                        "&type=tmpfile&partseq=" + i +
                        "&path=" + getURLEncoderString(path) +
                        "&uploadid=" + uploadid;
                log.info("files : {}    path {}",files[i], path);
                //方式一
//                HashMap<String, Object> paramMap = new HashMap<>();
//                paramMap.put("file", FileUtil.file(files[i]));
//                String result= HttpUtil.post(url, paramMap);

                //方式二
                String result = sendFile(url, files[i]);
                log.info("正在上传分片文件{}{}", result, i);
            }
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Description: TODO 预上传
     * @param: cloudPath 云端路径
     * @param: size 文件大小 字节
     * @param: isDir 0文件 1目录（设置为目录是 size要设置为0）
     * @param: blockList （文件的md5值） 可以把文件分为多个，然后分批上传
     * @return: java.lang.String
     */
    private static String precreate(String cloudPath, Long size, Integer isDir, String blockList) {
        String strURL = Constant.GET_READY_FILE_URL + "?method=precreate&access_token=" + Constant.ATOKEN;
        String params = "path=" + getURLEncoderString(cloudPath) + "&size=" + size + "&autoinit=1&block_list=[\"" + blockList + "\"]&isdir=" + isDir;
        return open(strURL, params, "POST");
    }


    /**
     * @Description: TODO 获取md5值
     * String path 文件地址
     */
    private final static String[] strHex = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String getMD5(File path) {
        StringBuilder buffer = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(org.apache.commons.io.FileUtils.readFileToByteArray(path));
            for (int value : b) {
                int d = value;
                if (d < 0) {
                    d += 256;
                }
                int d1 = d / 16;
                int d2 = d % 16;
                buffer.append(strHex[d1]).append(strHex[d2]);
            }
            return buffer.toString();
        } catch (Exception e) {
            return null;
        }

//        BigInteger bi = null;
//               try {
//                      byte[] buffer = new byte[8192];
//                        int len = 0;
//                        MessageDigest md = MessageDigest.getInstance("MD5");
//
//                        FileInputStream fis = new FileInputStream(f);
//                       while ((len = fis.read(buffer)) != -1) {
//                               md.update(buffer, 0, len);
//                            }
//                         fis.close();
//                       byte[] b = md.digest();
//                         bi = new BigInteger(1, b);
//                     } catch (NoSuchAlgorithmException e) {
//                         e.printStackTrace();
//                   } catch (IOException e) {
//                       e.printStackTrace();
//                  }
//              return bi.toString(16);
    }


    /**
     * @Description: TODO
     * @param: strURL 网址，可以是 http://aaa?bbb=1&ccc=2 拼接的
     * @param: params 拼接的body参数也就是form表单的参数  ddd=1&eee=2
     * @param: method 请求方式 get/post/put/delte等
     * @return: java.lang.String
     */
    private static String open(String strURL, String params, String method) {
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept", "application/json");// 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json");// 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);// utf-8编码
            out.append(params);
            out.flush();
            out.close(); // 读取响应
            int length = connection.getContentLength();// 获取长度
            InputStream is = connection.getInputStream();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                return new String(data, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendFile(String url, String param, String file) {
        if (url == null || param == null) {
            return url;
        }

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置链接超时时间为2秒
            conn.setConnectTimeout(1000);
            //设置读取超时为2秒
            conn.setReadTimeout(1000);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            out.write(file);
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "地址：" + url);
            return null;
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        }
        return result;
    }


    /**
     * @param: filePath
     * @param: unit 单个文件大小
     * @return: 返回文件的目录
     */
    private static File[] separate(Object obj, Integer unit) {

        try {

            InputStream bis = null;//输入流用于读取文件数据
            OutputStream bos = null;//输出流用于输出分片文件至磁盘
            File file = null;
            if (obj instanceof String) {
                file = new File((String) obj);
            }
            if (obj instanceof File) {
                file = (File) obj;
            }

            String filePath = file.getAbsolutePath();
            File newFile = new File(filePath.substring(0, filePath.lastIndexOf("\\") + 1));
            String directoryPath = newFile.getAbsolutePath();
            long splitSize = unit * 1024 * 1024;//单片文件大小,MB
            if (file.length() < splitSize) {
                log.info("文件小于单个分片大小，无需分片{}", file.length());
                return new File[]{file};
            }


            //分片二
            RandomAccessFile in=null;
            RandomAccessFile out =null;
            long length=file.length();//文件大小
            long count=length%splitSize==0?(length/splitSize):(length/splitSize+1);//文件分片数
            byte[] bt=new byte[1024];
            in=new RandomAccessFile(file, "r");
            for (int i = 1; i <= count; i++) {
                out = new RandomAccessFile(new File(filePath+"."+i), "rw");//定义一个可读可写且后缀名为.part的二进制分片文件
                long begin = (i-1)*splitSize;
                long end = i* splitSize;
                int len=0;
                in.seek(begin);
                while (in.getFilePointer()<end&&-1!=(len=in.read(bt))) {
                    out.write(bt, 0, len);
                }
                out.close();
            }


            //分片一
//            bis = new BufferedInputStream(new FileInputStream(file));
//            long writeByte = 0;//已读取的字节数
//            int len = 0;
//            byte[] bt = new byte[1024];
//            while (-1 != (len = bis.read(bt))) {
//                if (writeByte % splitSize == 0) {
//                    if (bos != null) {
//                        bos.flush();
//                        bos.close();
//                    }
//                    bos = new BufferedOutputStream(new FileOutputStream(filePath + "." + (writeByte / splitSize + 1) + ".part"));
//                }
//                writeByte += len;
//                bos.write(bt, 0, len);
//            }


            log.info("文件分片成功！");

            //排除被分片的文件
            if (newFile.isDirectory()) {
                File[] files = newFile.listFiles();
                File[] resultFiles = new File[files.length - 1];
                int j = 0;
                for (int i = 0; i < files.length; i++) {
                    if (!files[i].equals(file)) {
                        resultFiles[j] = files[i];
                        j++;
                    }
                }
                return resultFiles;
            }

            bos.flush();
            bos.close();
            bis.close();
            return new File[0];
        } catch (Exception e) {
            log.info("文件分片失败！");
            e.printStackTrace();
        }
        return null;
    }

    //splitNum:要分几片，currentDir：分片后存放的位置，subSize：按多大分片
    public static File[] nioSpilt(Object object, int splitNum, String currentDir, double subSize) {
        try {
            File file = null;
            if (object instanceof String) {
                file = new File((String) object);
            }
            if (object instanceof String) {
                file = (File) object;
            }
            FileInputStream fis = new FileInputStream(file);
            FileChannel inputChannel = fis.getChannel();
            FileOutputStream fos;
            FileChannel outputChannel;
            long splitSize = (long) subSize;
            long startPoint = 0;
            long endPoint = splitSize;
            for (int i = 1; i <= splitNum; i++) {
                fos = new FileOutputStream(currentDir + i);
                outputChannel = fos.getChannel();
                inputChannel.transferTo(startPoint, splitSize, outputChannel);
                startPoint += splitSize;
                endPoint += splitSize;
                outputChannel.close();
                fos.close();
            }
            inputChannel.close();
            fis.close();
            File newFile = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\") + 1));
            if (newFile.isDirectory()) {
                return newFile.listFiles();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File[0];
    }

    /**
     * @Description: TODO 发送文件
     * @param: url 发送地址
     * @param: file 发送文件
     * @return: java.lang.String
     */
    public static String sendFile(String url, File file) {
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setContentType(ContentType.MULTIPART_FORM_DATA);
            builder.addBinaryBody("file", file);
            String body = "";
            //创建httpclient对象
            CloseableHttpClient client = HttpClients.createDefault();
            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);
            //设置请求参数
            HttpEntity httpEntity = builder.build();
            httpPost.setEntity(httpEntity);
            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);
            //获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            //释放链接
            response.close();
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String URLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }




}