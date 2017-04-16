package com.dwqb.tenant.worker;

import com.dwqb.tenant.core.dao.jedis.JedisHashDao;
import com.dwqb.tenant.core.es.ESUtils;
import com.dwqb.tenant.core.model.Room;
import com.dwqb.tenant.core.model.RoomBayes;
import com.dwqb.tenant.core.utils.JsonUtils2;
import com.dwqb.tenant.core.utils.ObjUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Bayes {

    @Autowired
    private JedisHashDao jedisHashDao;

    public static final String TRUE_BAYES_WEIGHT_KEY = "tenant:bayes:weight:true";

    public static final String FALSE_BAYES_WEIGHT_KEY = "tenant:bayes:weight:false";

    public List<RoomBayes> getAll() throws Exception {
        List<RoomBayes> rooms = new ArrayList<>();
        String uri = "http://localhost:9200/room/room/_search?scroll=1m";
        String result = ESUtils.curl(uri,"POST", "{\"size\":1000}");
        Map map = JsonUtils2.json2Obj(result, Map.class);
        String scroll_id = (String) map.get("_scroll_id");

        List<RoomBayes> curRooms = getRoomsFromHits((Map) map.get("hits"));

        rooms.addAll(curRooms);

        while( ! CollectionUtils.isEmpty(curRooms)){
            curRooms = getScrollRooms(scroll_id);
            if( ! CollectionUtils.isEmpty(curRooms)){
                rooms.addAll(curRooms);
            }
        }

        return rooms;
    }


    public void getWeight(List<RoomBayes> roomBayes){
        Set<String> origins = new HashSet<>();
        Set<String> contractNames = new HashSet<>();
        Set<String> contractTels = new HashSet<>();
        Set<String> regions = new HashSet<>();
        Set<String> dirctions = new HashSet<>();
        Set<String> roomTypes = new HashSet<>();
        Set<String> floors = new HashSet<>();
        for(RoomBayes room : roomBayes){
            origins.add(room.getRoomOrigin());
            contractNames.add(room.getContractName());
            contractTels.add(room.getContractTel());
            regions.add(room.getRegion());
            dirctions.add(room.getDirction());
            roomTypes.add(room.getRoomType());
            floors.add(room.getFloor());
        }
        for(String s : origins){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                allSize ++;
                if(room.getRoomOrigin().equals(s)){
                    if("true".equals(room.getStatus())){
                        trueSize ++;
                    }else if("false".equals(room.getStatus())){
                        falseSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + trueSize/allSize ) / origins.size()));
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + falseSize/allSize ) / origins.size()));
        }
        for(String s : contractNames){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                allSize ++;
                if(room.getContractName().equals(s)){
                    if("true".equals(room.getStatus())){
                        trueSize ++;
                    }else if("false".equals(room.getStatus())){
                        falseSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + trueSize/allSize ) / origins.size()));
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + falseSize/allSize ) / origins.size()));
        }
        for(String s : contractTels){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                allSize ++;
                if(room.getContractTel().equals(s)){
                    if("true".equals(room.getStatus())){
                        trueSize ++;
                    }else if("false".equals(room.getStatus())){
                        falseSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + trueSize/allSize ) / origins.size()));
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + falseSize/allSize ) / origins.size()));
        }
        for(String s : regions){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                allSize ++;
                if(room.getRegion().equals(s)){
                    if("true".equals(room.getStatus())){
                        trueSize ++;
                    }else if("false".equals(room.getStatus())){
                        falseSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + trueSize/allSize ) / origins.size()));
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + falseSize/allSize ) / origins.size()));
        }
        for(String s : dirctions){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                allSize ++;
                if(room.getDirction().equals(s)){
                    if("true".equals(room.getStatus())){
                        trueSize ++;
                    }else if("false".equals(room.getStatus())){
                        falseSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + trueSize/allSize ) / origins.size()));
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + falseSize/allSize ) / origins.size()));
        }
        for(String s : roomTypes){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                allSize ++;
                if(room.getRoomType().equals(s)){
                    if("true".equals(room.getStatus())){
                        trueSize ++;
                    }else if("false".equals(room.getStatus())){
                        falseSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + trueSize/allSize ) / origins.size()));
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + falseSize/allSize ) / origins.size()));
        }
        for(String s : floors){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                allSize ++;
                if(room.getFloor().equals(s)){
                    if("true".equals(room.getStatus())){
                        trueSize ++;
                    }else if("false".equals(room.getStatus())){
                        falseSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + trueSize/allSize ) / origins.size()));
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,String.valueOf((origins.size() + falseSize/allSize ) / origins.size()));
        }
    }




    private List<RoomBayes> getScrollRooms(String scroll_id) throws Exception {
        String uri = "http://localhost:9200/_search/scroll";
        String result = ESUtils.curl(uri,"POST", "{\"scroll\": \"1m\",\"scroll_id\":\"" + scroll_id + "\"}");
        Map map = JsonUtils2.json2Obj(result, Map.class);
        List<RoomBayes> curRooms = getRoomsFromHits((Map) map.get("hits"));
        return curRooms;
    }

    private List<RoomBayes> getRoomsFromHits(Map hits) throws Exception {
        List<RoomBayes> curRooms = null;
        int total = (int) hits.get("total");
        if(total != 0){
            curRooms = new ArrayList<>(total);
            List hitList = (List)hits.get("hits");
            for(Object obj : hitList){
                Map curMap = (Map)obj;
                Room room = (Room) ObjUtils.mapToObject((Map)curMap.get("_source"),Room.class);
                RoomBayes roomBayes = new RoomBayes(room);
                curRooms.add(roomBayes);
            }
        }
        return curRooms;
    }

    public static void main(String[] args) throws Exception {
        Bayes bayes = new Bayes();
//        List<Room> list = bayes.getAll();
//        System.out.println(list.size());
    }
}
