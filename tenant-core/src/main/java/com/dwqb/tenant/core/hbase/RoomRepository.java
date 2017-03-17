package com.dwqb.tenant.core.hbase;

import com.dwqb.tenant.core.model.Room;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.ResultsExtractor;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangqiang on 17/3/15.
 */
@Repository
public class RoomRepository {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    public static byte[] CF_INFO = Bytes.toBytes("cfInfo");

    private String tableName = "rooms";

    private byte[] name = Bytes.toBytes("name");
    private byte[] price = Bytes.toBytes("price");
    private byte[] longitude = Bytes.toBytes("longitude");
    private byte[] latitude = Bytes.toBytes("latitude");
    private byte[] region = Bytes.toBytes("region");
    private byte[] priceType = Bytes.toBytes("priceType");
    private byte[] space = Bytes.toBytes("space");
    private byte[] dirction = Bytes.toBytes("dirction");
    private byte[] struct = Bytes.toBytes("struct");
    private byte[] roomType = Bytes.toBytes("roomType");
    private byte[] floor = Bytes.toBytes("floor");

//    public List<Room> findAll() {
//
////        hbaseTemplate.
//
//
//        return hbaseTemplate.find(tableName, "cfInfo", new RowMapper<Room>() {
//
//            @Override
//            public Room mapRow(Result result, int rowNum) throws Exception {
//                return new Room(
//                        Bytes.toString(result.getValue(CF_INFO,name)),
//                        Bytes.toDouble(result.getValue(CF_INFO,price)),
//                        Bytes.toDouble(result.getValue(CF_INFO,longitude)),
//                        Bytes.toDouble(result.getValue(CF_INFO,latitude)),
//                        Bytes.toString(result.getValue(CF_INFO,region)),
//                        Bytes.toString(result.getValue(CF_INFO,priceType)),
//                        null,
//                        Bytes.toDouble(result.getValue(CF_INFO,space)),
//                        Bytes.toString(result.getValue(CF_INFO,dirction)),
//                        Bytes.toString(result.getValue(CF_INFO,struct)),
//                        Bytes.toString(result.getValue(CF_INFO,roomType)),
//                        Bytes.toString(result.getValue(CF_INFO,floor)),
//                        null
//                        );
//            }
//        });
//
//    }

}
