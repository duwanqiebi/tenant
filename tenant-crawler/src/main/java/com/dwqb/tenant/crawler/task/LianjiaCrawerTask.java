package com.dwqb.tenant.crawler.task;

import com.dwqb.tenant.core.model.Room;
import com.dwqb.tenant.crawler.pageprocessor.LianjiaPageProcessor;
import com.dwqb.tenant.schedule.IScheduleTaskDealSingle;
import com.dwqb.tenant.schedule.TaskItemDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Created by zhangqiang on 17/5/5.
 */
@Component
public class LianjiaCrawerTask implements IScheduleTaskDealSingle<Room> {

    @Autowired
    private LianjiaPageProcessor lianjiaPageProcessor;

    @Override
    public boolean execute(Room task, String ownSign) throws Exception {
        return false;
    }

    @Override
    public List<Room> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        String pageNum = taskItemList.get(0).getTaskItemId();
        lianjiaPageProcessor.doCrawer(pageNum);
        return null;
    }

    @Override
    public Comparator<Room> getComparator() {
        return null;
    }
}
