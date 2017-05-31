package com.dwqb.tenant.worker;

import com.alibaba.dubbo.common.utils.StringUtils;
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
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.*;

@Component
public class Bayes {

    @Autowired
    private JedisHashDao jedisHashDao;

    public static final String TRUE_BAYES_WEIGHT_KEY = "tenant:bayes:weight:true";

    public static final String FALSE_BAYES_WEIGHT_KEY = "tenant:bayes:weight:false";

    public static final String TRUE_BAYES_XIANYAN_KEY = "tenant:bayes:xianyan:true";

    public static final String FALSE_BAYES_XIANYAN_KEY = "tenant:bayes:xianyan:false";

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
            if(allspace == 0){
                continue;
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

            if(i%3 == 0){
                room.setContractName(falseContractName.get(random.nextInt(falseContractName.size() -1)));
                room.setContractTel(falseContractTel.get(random.nextInt(falseContractTel.size() -1)));
            }else{
                room.setContractName(list.get(random.nextInt(size -1)).getContractName());
                room.setContractTel(list.get(random.nextInt(size -1)).getContractTel());
            }

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
            room.setPrice(price.multiply(new BigDecimal(room.getSpace())).doubleValue());
//            room.setPrice(price.multiply(new BigDecimal(room.getSpace())).multiply(new BigDecimal(3).divide(new BigDecimal(4))).doubleValue());
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
     * @param
     */
    public void getWeight(List<Room> rooms){

        List<RoomBayes> roomBayes = new ArrayList(rooms.size());
        int xianyanFalseSize = 0;
        int xianyanTrueSize = 0;
        for(Room room : rooms){
            roomBayes.add(new RoomBayes(room));
            if("false".equals(room.getStatus())){
                xianyanFalseSize ++;
            }else{
                xianyanTrueSize ++;
            }
        }
        Jedis jedis = RedisUtil.getJedis();
        jedis.set(TRUE_BAYES_XIANYAN_KEY,new BigDecimal(xianyanTrueSize).divide(new BigDecimal(rooms.size()),4, BigDecimal.ROUND_HALF_EVEN).toString());
        jedis.set(FALSE_BAYES_XIANYAN_KEY,new BigDecimal(xianyanFalseSize).divide(new BigDecimal(rooms.size()),4,BigDecimal.ROUND_HALF_EVEN).toString());
        RedisUtil.returnResource(jedis);

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

        jedis = RedisUtil.getJedis();
        for(String s : origins){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){

                if(room.getRoomOrigin().equals(s)){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            allSize = trueSize + falseSize;
            jedis.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(xianyanTrueSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            jedis.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(xianyanFalseSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            System.out.println(s);
        }
        RedisUtil.returnResource(jedis);
        jedis = RedisUtil.getJedis();
        for(String s : contractNames){
            if(s == null){
                continue;
            }
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                if(s.equals(room.getContractName())){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            allSize = trueSize + falseSize;
            jedis.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(xianyanTrueSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            jedis.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(xianyanFalseSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            System.out.println(s);
        }
        RedisUtil.returnResource(jedis);
        jedis = RedisUtil.getJedis();
        for(String s : contractTels){
            if(s == null){
                continue;
            }
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                if(s.equals(room.getContractTel())){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            allSize = trueSize + falseSize;

            jedis.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(xianyanTrueSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            jedis.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(xianyanFalseSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            System.out.println(s);

        }
        RedisUtil.returnResource(jedis);
        jedis = RedisUtil.getJedis();
        for(String s : regions){
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                if(room.getRegion().equals(s)){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            allSize = trueSize + falseSize;
            jedis.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(xianyanTrueSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            jedis.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(xianyanFalseSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            System.out.println(s);

        }
        RedisUtil.returnResource(jedis);
        jedis = RedisUtil.getJedis();
        for(String s : dirctions){
            if(s == null){
                continue;
            }
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                if(s.equals(room.getDirction())){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            allSize = trueSize + falseSize;
            jedis.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(xianyanTrueSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            jedis.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(xianyanFalseSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            System.out.println(s);

        }
        RedisUtil.returnResource(jedis);
        jedis = RedisUtil.getJedis();
        for(String s : roomTypes){
            if(s == null){
                continue;
            }
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                if(s.equals(room.getRoomType())){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            allSize = trueSize + falseSize;
            jedis.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(xianyanTrueSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            jedis.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(xianyanFalseSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            System.out.println(s);

        }
        RedisUtil.returnResource(jedis);
        jedis = RedisUtil.getJedis();
        for(String s : floors){
            if(s == null){
                continue;
            }
            int trueSize = 0;
            int falseSize = 0;
            int allSize = 0;
            //true
            for(RoomBayes room : roomBayes){
                if(s.equals(room.getFloor())){
                    if("false".equals(room.getStatus())){
                        falseSize ++;
                    }else{
                        trueSize ++;
                    }
                }

            }
            allSize = trueSize + falseSize;
            jedis.hset(TRUE_BAYES_WEIGHT_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(FALSE_BAYES_WEIGHT_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(allSize),4, BigDecimal.ROUND_HALF_EVEN).divide(new BigDecimal(origins.size()),4, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal("1")).toString());
            jedis.hset(TRUE_BAYES_CONDITION_KEY,s,new BigDecimal(trueSize).divide(new BigDecimal(xianyanTrueSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            jedis.hset(FALSE_BAYES_CONDITION_KEY,s,new BigDecimal(falseSize).divide(new BigDecimal(xianyanFalseSize),4,BigDecimal.ROUND_HALF_EVEN).toString());
            System.out.println(s);
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



    //测试代码
    public void test() throws Exception {
        List<Room> falseRooms = new ArrayList<>();
        List<Room> rooms = getAll();
        for(Room room : rooms){
            if("false".equals(room.getStatus())){
                falseRooms.add(room);
            }
        }

        List<Long> resultIds = new ArrayList<>();   //虚假房源id集合
        for(Room room : falseRooms){
            RoomBayes roomBayes  = new RoomBayes(room);
            //计算概率
            Boolean result = calBayes(roomBayes);
            if(result == false){
                resultIds.add(room.getId());
            }
        }

        //计算准确率
        double exactNum = 0;
        for(Room room : falseRooms){
            if(resultIds.contains(room.getId())){
                exactNum ++ ;
            }
        }
        System.out.println("准确率 = " + exactNum/(double)falseRooms.size());

    }

    private Boolean calBayes(RoomBayes roomBayes) {
        Jedis jedis = RedisUtil.getJedis();
        String trueXianyan = jedis.get(TRUE_BAYES_XIANYAN_KEY);
        String falseXianyan = jedis.get(FALSE_BAYES_XIANYAN_KEY);

        Map<String,String> trueWeigthMap = new HashMap<>();
        Map<String,String> trueConditionMap = new HashMap<>();
        Map<String,String> falseWeightMap = new HashMap<>();
        Map<String,String> falseConditionMap = new HashMap<>();

        String roomOriginTrueWeightStr = jedis.hget(TRUE_BAYES_WEIGHT_KEY,roomBayes.getRoomOrigin());
        trueWeigthMap.put("origin",roomOriginTrueWeightStr);
        String roomOriginFalseWeightStr = jedis.hget(FALSE_BAYES_WEIGHT_KEY,roomBayes.getRoomOrigin());
        falseWeightMap.put("origin",roomOriginFalseWeightStr);
        String riimOgiginTrueConditionStr = jedis.hget(TRUE_BAYES_CONDITION_KEY,roomBayes.getRoomOrigin());
        trueConditionMap.put("origin",riimOgiginTrueConditionStr);
        String riimOgiginfalseConditionStr = jedis.hget(FALSE_BAYES_CONDITION_KEY,roomBayes.getRoomOrigin());
        falseConditionMap.put("origin",riimOgiginfalseConditionStr);

        String contractTrueWeightStr = jedis.hget(TRUE_BAYES_WEIGHT_KEY,roomBayes.getContractName());
        trueWeigthMap.put("contractName",contractTrueWeightStr);
        String contractFalseWeightStr = jedis.hget(FALSE_BAYES_WEIGHT_KEY,roomBayes.getContractName());
        falseWeightMap.put("contractName",contractFalseWeightStr);
        String contractTrueConditionStr = jedis.hget(TRUE_BAYES_CONDITION_KEY,roomBayes.getContractName());
        trueConditionMap.put("contractName",contractTrueConditionStr);
        String contractfalseConditionStr = jedis.hget(FALSE_BAYES_CONDITION_KEY,roomBayes.getContractName());
        falseConditionMap.put("contractName",contractfalseConditionStr);

        if(roomBayes.getContractTel() == null){
            roomBayes.setContractTel("");
        }

        String contractTelTrueWeightStr = jedis.hget(TRUE_BAYES_WEIGHT_KEY,roomBayes.getContractTel());
        trueWeigthMap.put("contractTel",contractTelTrueWeightStr);
        String contractTelFalseWeightStr = jedis.hget(FALSE_BAYES_WEIGHT_KEY,roomBayes.getContractTel());
        falseWeightMap.put("contractTel",contractTelFalseWeightStr);
        String contractTelTrueConditionStr = jedis.hget(TRUE_BAYES_CONDITION_KEY,roomBayes.getContractTel());
        trueConditionMap.put("contractTel",contractTelTrueConditionStr);
        String contractTelfalseConditionStr = jedis.hget(FALSE_BAYES_CONDITION_KEY,roomBayes.getContractTel());
        falseConditionMap.put("contractTel",contractTelfalseConditionStr);

        String directionTrueWeightStr = jedis.hget(TRUE_BAYES_WEIGHT_KEY,roomBayes.getDirction());
        trueWeigthMap.put("direction",directionTrueWeightStr);
        String directionFalseWeightStr = jedis.hget(FALSE_BAYES_WEIGHT_KEY,roomBayes.getDirction());
        falseWeightMap.put("direction",directionFalseWeightStr);
        String directionTrueConditionStr = jedis.hget(TRUE_BAYES_CONDITION_KEY,roomBayes.getDirction());
        trueConditionMap.put("direction",directionTrueConditionStr);
        String directionfalseConditionStr = jedis.hget(FALSE_BAYES_CONDITION_KEY,roomBayes.getDirction());
        falseConditionMap.put("direction",directionfalseConditionStr);

        String floorTrueWeightStr = jedis.hget(TRUE_BAYES_WEIGHT_KEY,roomBayes.getFloor());
        trueWeigthMap.put("floor",floorTrueWeightStr);
        String floorFalseWeightStr = jedis.hget(FALSE_BAYES_WEIGHT_KEY,roomBayes.getFloor());
        falseWeightMap.put("floor",floorFalseWeightStr);
        String floorTrueConditionStr = jedis.hget(TRUE_BAYES_CONDITION_KEY,roomBayes.getFloor());
        trueConditionMap.put("floor",floorTrueConditionStr);
        String floorfalseConditionStr = jedis.hget(FALSE_BAYES_CONDITION_KEY,roomBayes.getFloor());
        falseConditionMap.put("floor",floorfalseConditionStr);

        String regionTrueWeightStr = jedis.hget(TRUE_BAYES_WEIGHT_KEY,roomBayes.getRegion());
        trueWeigthMap.put("region",regionTrueWeightStr);
        String regionFalseWeightStr = jedis.hget(FALSE_BAYES_WEIGHT_KEY,roomBayes.getRegion());
        falseWeightMap.put("region",regionFalseWeightStr);
        String regionTrueConditionStr = jedis.hget(TRUE_BAYES_CONDITION_KEY,roomBayes.getRegion());
        trueWeigthMap.put("region",regionTrueConditionStr);
        String regionfalseConditionStr = jedis.hget(FALSE_BAYES_CONDITION_KEY,roomBayes.getRegion());
        falseWeightMap.put("region",regionfalseConditionStr);

        String roomTypeTrueWeightStr = jedis.hget(TRUE_BAYES_WEIGHT_KEY,roomBayes.getRoomType());
        trueWeigthMap.put("roomType",roomTypeTrueWeightStr);
        String roomTypeFalseWeightStr = jedis.hget(FALSE_BAYES_WEIGHT_KEY,roomBayes.getRoomType());
        falseWeightMap.put("roomType",roomTypeFalseWeightStr);
        String roomTypeTrueConditionStr = jedis.hget(TRUE_BAYES_CONDITION_KEY,roomBayes.getRoomType());
        trueConditionMap.put("roomType",roomTypeTrueConditionStr);
        String roomTypefalseConditionStr = jedis.hget(FALSE_BAYES_CONDITION_KEY,roomBayes.getRoomType());
        falseConditionMap.put("roomType",roomTypefalseConditionStr);

        //P(true)
        BigDecimal powResult = BigDecimal.ZERO;
        for(Map.Entry<String,String> entry : trueConditionMap.entrySet()){
            String name = entry.getKey();
            String condition = entry.getValue();
            if(StringUtils.isBlank(condition)){
                continue;
            }
            String weight = trueWeigthMap.get(name);
            if(StringUtils.isBlank(weight)){
                weight = "1";
            }

            Double pwdResult = Math.pow(Double.parseDouble(condition),Double.parseDouble(weight));
            powResult = powResult.add(new BigDecimal(pwdResult));
        }
        BigDecimal trueBayes = powResult.multiply(new BigDecimal(trueXianyan));

        //P(false)
        powResult = BigDecimal.ZERO;
        for(Map.Entry<String,String> entry : falseConditionMap.entrySet()){
            String name = entry.getKey();
            String condition = entry.getValue();
            if(StringUtils.isBlank(condition)){
                continue;
            }
            String weight = falseWeightMap.get(name);
            if(StringUtils.isBlank(weight)){
                weight = "1";
            }

            Double pwdResult = Math.pow(Double.parseDouble(condition),Double.parseDouble(weight));
            powResult = powResult.add(new BigDecimal(pwdResult));
        }
        BigDecimal falseBayes = powResult.multiply(new BigDecimal(falseXianyan));

        System.out.println(trueBayes + "   ;   " + falseBayes);
        if(falseBayes.compareTo(trueBayes) > 0){
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        Bayes bayes = new Bayes();
        bayes.simulateFalseRoom();
//        List<Room> list = bayes.getAll();
//        System.out.println(list.size());
    }
}
