package com.ruoyi.web.test.controller;

import com.github.wujun234.uid.UidGenerator;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.note.domain.NoteContentMgDb;
import com.ruoyi.note.service.INoteRepositoryService;
//import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Wang
 * @Date: 2020/10/08 19:11
 * 功能描述:
 */
public class MongdbApplicationTests  extends BaseSpringBootTest{
    @Autowired
    private INoteRepositoryService noteRepositoryService;

    @Autowired
    private RedisCache redisCache;


    @Resource
    private UidGenerator cachedUidGenerator;

    @Resource
    private UidGenerator defaultUidGenerator;

    /**
     * 查询所有信息
     */
//    @Test
//    public void findAll() {
//        List<NoteContentMgDb> all = noteRepositoryService.findAll();
//        System.out.println(all.size());
//    }

    /**
     * 新增信息
     */
//    @Test
//    public void save() {
//        NoteContentMgDb noteContentMgDb = new NoteContentMgDb();
//        noteContentMgDb.setId(15108230363503104L);
//        noteContentMgDb.setNoteContent("宋人头");
//        noteRepositoryService.save(noteContentMgDb);
//    }


    /**
     * 修改信息
     */
//    @Test
//    public void update() {
//        NoteContentMgDb noteContentMgDb = new NoteContentMgDb();
////        noteContentMgDb.setId(2L);
//        noteContentMgDb.setNoteContent("吴很帅");
//        noteRepositoryService.update(noteContentMgDb);
//    }

    /**
     * 删除信息
     */
//    @Test
//    public void delete() {
//        noteRepositoryService.delete(3);
//    }
//








    @Test
    public  void addredis(){
    for (int i=0;i<10;i++){
        redisCache.setCacheObject(Constants.NM_NOTE_CONTENT+defaultUidGenerator.getUID(),i);
    }

    }
    @Test
    public  void getRedis(){
       String redis= redisCache.getCacheObject("nm_note:15563127529716224");
        System.out.println("redis:"+redis);

    }


    /**
     * 缓存文章 转义到 mgDB
     *
     * @param
     * @return
     */
//    @Test
//    public void redisToMgDB(){
//        //模糊查询 获取所有的key
//        Collection<String> listNote= redisCache.keys(Constants.NM_NOTE_CONTENT+"*");
//        for(String str:listNote){
//            //文章UUID
//            String mgDbContentUUID=str.replace(Constants.NM_NOTE_CONTENT,"");
//            System.out.println("str:"+str);
//            //文章
//            String redisContent= redisCache.getCacheObject(str).toString();
//            //查询mongoDb  存在就修改 不存在就新增
//            List<NoteContentMgDb> listMgDbContent =  noteRepositoryService.findById(mgDbContentUUID);
//            if (listMgDbContent!=null&&!listMgDbContent.isEmpty()){
//                //修改
//                NoteContentMgDb noteContentMgDb = new NoteContentMgDb();
//                noteContentMgDb.setId(Long.valueOf(mgDbContentUUID));
//                noteContentMgDb.setNoteContent(redisContent);
//                noteRepositoryService.update(noteContentMgDb);
//            }else {
//                //新增
//                NoteContentMgDb noteContentMgDb = new NoteContentMgDb();
//                noteContentMgDb.setId(Long.valueOf(mgDbContentUUID));
//                noteContentMgDb.setNoteContent(redisContent);
//                noteRepositoryService.save(noteContentMgDb);
//            }
//            //删除对应缓存
//            //redisCache.deleteObject(str);
//        }
//
//
//    }











}