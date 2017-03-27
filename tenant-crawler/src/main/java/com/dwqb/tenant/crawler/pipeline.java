package com.dwqb.tenant.crawler;

import com.dwqb.tenant.core.dao.mysql.RoomDao;
import com.dwqb.tenant.core.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;


public class pipeline implements PageModelPipeline<Room> {

    @Autowired
    private RoomDao roomDao;

    @Override
    public void process(Room room, Task task) {

    }
}
