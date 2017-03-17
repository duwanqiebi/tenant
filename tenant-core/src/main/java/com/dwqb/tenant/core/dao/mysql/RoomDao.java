package com.dwqb.tenant.core.dao.mysql;


import com.dwqb.tenant.core.model.Room;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDao {

    int deleteByPrimaryKey(Integer id);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKeyWithBLOBs(Room record);

    int updateByPrimaryKey(Room record);
}