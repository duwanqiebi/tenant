package com.dwqb.tenant.schedule.taskmanager;

interface IScheduleProcessor {
	 /**
	  * �Ƿ��Ѿ��������ڴ������е����ݣ��ڽ��ж����л���ʱ��
	  * ���뱣֤�����ڴ�����ݴ������
	  * @return
	  */
	 public boolean isDealFinishAllData();
	 /**
	  * �жϽ����Ƿ�������״̬
	  * @return
	  */
	 public boolean isSleeping();
	 /**
	  * ֹͣ��������
	  * @throws Exception
	  */
	 public void stopSchedule() throws Exception;
	 
	 /**
	  * ��������Ѿ�ȡ���ڴ��е����ݣ��������߳�ʧ�ܵ�ʱ����ã����������ظ�
	  */
	 public void clearAllHasFetchData();
}
