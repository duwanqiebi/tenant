package com.dwqb.tenant.worker;

import com.dwqb.tenant.core.baiduAPI.BaiduMapAPI;
import com.dwqb.tenant.core.dao.jedis.JedisHashDao;
import com.dwqb.tenant.core.es.ESUtils;
import com.dwqb.tenant.core.model.Region;
import com.dwqb.tenant.core.model.Room;
import com.dwqb.tenant.core.model.RoomBayes;
import com.dwqb.tenant.core.model.RoomOrigin;
import com.dwqb.tenant.core.utils.IdGenerator;
import com.dwqb.tenant.core.utils.JsonUtils2;
import com.dwqb.tenant.core.utils.ObjUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class Bayes {

    @Autowired
    private JedisHashDao jedisHashDao;

    public static final String TRUE_BAYES_WEIGHT_KEY = "tenant:bayes:weight:true";

    public static final String FALSE_BAYES_WEIGHT_KEY = "tenant:bayes:weight:false";

    public static final String TRUE_BAYES_CONDITION_KEY = "tenant:bayes:condition:true";

    public static final String FALSE_BAYES_CONDITION_KEY = "tenant:bayes:condition:false";


    public void simulateFalseRoom() throws Exception {
        List<Room> list = getAll();
        Set<String> origins = new HashSet<>();
        Set<String> regions = new HashSet<>();
        Set<String> dirctions = new HashSet<>();
        Set<String> roomTypes = new HashSet<>();
        Set<String> floors = new HashSet<>();
        for(Room room : list){
            origins.add(room.getRoomOrigin());
            regions.add(room.getRegion());
            dirctions.add(room.getDirction());
            roomTypes.add(room.getRoomType());
            floors.add(room.getFloor());
        }
        Map<String,BigDecimal> originsMap = new HashMap<>();
        Map<String,BigDecimal> regionsMap = new HashMap<>();
        Map<String,BigDecimal> dirctionsMap = new HashMap<>();
        Map<String,BigDecimal> roomTypesMap = new HashMap<>();
        Map<String,BigDecimal> floorsMap = new HashMap<>();
        for(String s : origins){
            double allprice = 0;
            double allspace = 0;
            for(Room room : list){
                if(room.getRoomOrigin().equals(s)){
                    allprice += room.getPrice();
                    allspace += room.getSpace();
                }
            }
            originsMap.put(s,new BigDecimal(allprice).divide(new BigDecimal(allspace),4,BigDecimal.ROUND_HALF_EVEN));
        }
        for(String s : regions){
            double allprice = 0;
            double allspace = 0;
            for(Room room : list){
                if(room.getRegion().equals(s)){
                    allprice += room.getPrice();
                    allspace += room.getSpace();
                }
            }
            regionsMap.put(s,new BigDecimal(allprice).divide(new BigDecimal(allspace),4,BigDecimal.ROUND_HALF_EVEN));
        }
        for(String s : dirctions){
            double allprice = 0;
            double allspace = 0;
            for(Room room : list){
                if(room.getDirction() != null && room.getDirction().equals(s)){
                    allprice += room.getPrice();
                    allspace += room.getSpace();
                }
            }
            if(allspace != 0){
                dirctionsMap.put(s,new BigDecimal(allprice).divide(new BigDecimal(allspace),4,BigDecimal.ROUND_HALF_EVEN));
            }
        }
        for(String s : roomTypes){
            double allprice = 0;
            double allspace = 0;
            for(Room room : list){
                if(room.getDirction() != null && room.getRoomType().equals(s)){
                    allprice += room.getPrice();
                    allspace += room.getSpace();
                }
            }
            roomTypesMap.put(s,new BigDecimal(allprice).divide(new BigDecimal(allspace),4,BigDecimal.ROUND_HALF_EVEN));
        }
        for(String s : floors){
            double allprice = 0;
            double allspace = 0;
            for(Room room : list){
                if(room.getFloor()!= null && room.getFloor().equals(s)){
                    allprice += room.getPrice();
                    allspace += room.getSpace();
                }
            }
            if(allspace != 0){
                floorsMap.put(s,new BigDecimal(allprice).divide(new BigDecimal(allspace),4,BigDecimal.ROUND_HALF_EVEN));
            }
        }
        List<String> falseContractName = new ArrayList<>();
        falseContractName.add("王青");
        falseContractName.add("杨华东");
        falseContractName.add("邱龙");
        falseContractName.add("刘可为");
        falseContractName.add("何探敦");
        List<String> falseContractTel = new ArrayList<>();
        falseContractTel.add("15210377460");
        falseContractTel.add("17777855285");
        falseContractTel.add("18201165756");
        falseContractTel.add("13121144555");
        falseContractTel.add("15011177083");


        int size = list.size();
        Random random = new Random();
        for(int i = 0; i <= 500; i ++ ){
            Room room = new Room();
            room.setId(IdGenerator.getId());
            room.setName("虚假房源" + i);
            room.setStatus("false");
            room.setSubway(Collections.<String>emptyList());
            room.setContractName(falseContractName.get(random.nextInt(falseContractName.size() -1)));
            room.setContractTel(falseContractTel.get(random.nextInt(falseContractTel.size() -1)));
            room.setDescription("虚假房源模拟");
            room.setDirction(list.get(random.nextInt(size - 1)).getDirction());
            room.setFloor(list.get(random.nextInt(size - 1)).getFloor());

            Region region = null;
            while(region == null){
                room.setLongitude( 115.25 + (117.30 - 115.25) * random.nextDouble());
                room.setLatitude(39.26 + (41.03-39.26)*random.nextDouble());
                region = BaiduMapAPI.covertLocation(room.getLongitude(),room.getLatitude());
            }
            room.setRegion(region.toString());
            room.setUrl("www.baidu.com");
            room.setSpace((double) random.nextInt(150));
            if(i%2 == 0){
                room.setRoomOrigin(RoomOrigin.WUBA.toString());
            }else{
                room.setRoomOrigin(RoomOrigin.AN_JU_KE.toString());
            }
            Room randomroom = list.get(random.nextInt(size-1));
            room.setSpace(randomroom.getSpace());
            room.setRoomType(randomroom.getRoomType());
            room.setStruct(randomroom.getStruct());

            //计算价格
            BigDecimal price1 = (originsMap.get(room.getRoomOrigin()) == null)?BigDecimal.ZERO:originsMap.get(room.getRoomOrigin());
            BigDecimal price2 = (regionsMap.get(room.getRegion())== null)?BigDecimal.ZERO:regionsMap.get(room.getRegion());
            BigDecimal price3 = (dirctionsMap.get(room.getDirction())== null)?BigDecimal.ZERO:dirctionsMap.get(room.getDirction());
            BigDecimal price4 = (roomTypesMap.get(room.getRoomType())== null)?BigDecimal.ZERO:roomTypesMap.get(room.getRoomType());
            BigDecimal price5 = (floorsMap.get(room.getFloor())== null)?BigDecimal.ZERO:floorsMap.get(room.getFloor());
            BigDecimal[] prices = new BigDecimal[]{price1,price2,price3,price4,price5};
            BigDecimal price = new BigDecimal(100000000);
            for(BigDecimal curPrice : prices){
                if(curPrice.compareTo(price) < 0){
                    price = curPrice;
                }
            }
            room.setPrice(price.multiply(new BigDecimal(room.getSpace())).multiply(new BigDecimal(3).divide(new BigDecimal(4))).doubleValue());
            ESUtils.curl("http://112.74.79.166:9200/room/room/" + room.getId() ,"PUT", JsonUtils2.obj2Json(room));
        }


    }

    public List<Room> getAll() throws Exception {
        List<Room> rooms = new ArrayList<>();
        String uri = "http://112.74.79.166:9200/room/room/_search?scroll=1m";
        String result = ESUtils.curl(uri,"POST", "{\"size\":1000}");
        Map map = JsonUtils2.json2Obj(result, Map.class);
        String scroll_id = (String) map.get("_scroll_id");

        List<Room> curRooms = getRoomsFromHits((Map) map.get("hits"));

        rooms.addAll(curRooms);

        while( ! CollectionUtils.isEmpty(curRooms)){
            curRooms = getScrollRooms(scroll_id);
            if( ! CollectionUtils.isEmpty(curRooms)){
                rooms.addAll(curRooms);
            }
        }

        return rooms;
    }

    /**
     * 计算特征属性的权重
     * @param roomBayes
     */
    public void getWeight(List<Room> roomBayes){
        Set<String> origins = new HashSet<>();
        Set<String> contractNames = new HashSet<>();
        Set<String> contractTels = new HashSet<>();
        Set<String> regions = new HashSet<>();
        Set<String> dirctions = new HashSet<>();
        Set<String> roomTypes = new HashSet<>();
        Set<String> floors = new HashSet<>();
        for(Room room : roomBayes){
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
            for(Room room : roomBayes){
                allSize ++;
                if(room.getRoomOrigin().equals(s)){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
            jedisHashDao.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        for(String s : contractNames){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(Room room : roomBayes){
                allSize ++;
                if(room.getContractName().equals(s)){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
            jedisHashDao.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        for(String s : contractTels){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(Room room : roomBayes){
                allSize ++;
                if(room.getContractTel().equals(s)){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
            jedisHashDao.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        for(String s : regions){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(Room room : roomBayes){
                allSize ++;
                if(room.getRegion().equals(s)){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
            jedisHashDao.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        for(String s : dirctions){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(Room room : roomBayes){
                allSize ++;
                if(room.getDirction().equals(s)){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
            jedisHashDao.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        for(String s : roomTypes){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(Room room : roomBayes){
                allSize ++;
                if(room.getRoomType().equals(s)){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
            jedisHashDao.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        for(String s : floors){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(Room room : roomBayes){
                allSize ++;
                if(room.getFloor().equals(s)){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            jedisHashDao.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedisHashDao.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
            jedisHashDao.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).toString());
        }
    }




    private List<Room> getScrollRooms(String scroll_id) throws Exception {
        String uri = "http://112.74.79.166:9200/_search/scroll";
        String result = ESUtils.curl(uri,"POST", "{\"scroll\": \"1m\",\"scroll_id\":\"" + scroll_id + "\"}");
        Map map = JsonUtils2.json2Obj(result, Map.class);
        List<Room> curRooms = getRoomsFromHits((Map) map.get("hits"));
        return curRooms;
    }

    private List<Room> getRoomsFromHits(Map hits) throws Exception {
        List<Room> curRooms = null;
        int total = (int) hits.get("total");
        if(total != 0){
            curRooms = new ArrayList<>(total);
            List hitList = (List)hits.get("hits");
            for(Object obj : hitList){
                Map curMap = (Map)obj;
                Room room = (Room) ObjUtils.mapToObject((Map)curMap.get("_source"),Room.class);
                curRooms.add(room);
            }
        }
        return curRooms;
    }

    public static void main(String[] args) throws Exception {
        Bayes bayes = new Bayes();
        bayes.simulateFalseRoom();
//        List<Room> list = bayes.getAll();
//        System.out.println(list.size());
    }
}
