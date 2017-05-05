package com.dwqb.tenant.crawler.task;

import com.dwqb.tenant.core.model.Room;
import com.dwqb.tenant.crawler.pageprocessor.ZiruPageProcessor;
import com.dwqb.tenant.schedule.IScheduleTaskDealSingle;
import com.dwqb.tenant.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Created by zhangqiang on 17/5/5.
 */
@Component
public class ZiruCrawerTask implements IScheduleTaskDealSingle<Room> {

    private Logger logger = LoggerFactory.getLogger(ZiruCrawerTask.class);

    @Autowired
    private ZiruPageProcessor ziruPageProcessor;

    @Override
    public boolean execute(Room task, String ownSign) throws Exception {
        return true;
    }

    @Override
    public List<Room> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        String pageNum = taskItemList.get(0).getTaskItemId();
        ziruPageProcessor.doCrawer(pageNum);
        return null;
    }

    @Override
    public Comparator<Room> getComparator() {
        return null;
    }
}
