package com.shincolle.nclient;

import java.util.UUID;

import com.shincolle.primitive.Feature;

/**
 * NClient Main Handler
 * */
public final class NClient {
	
	/**
	 * Launch a nclient safely, false means launch faild, if launch succeed, it will block current thread and return true in the end
	 * */
	public static final boolean launch(UUID uuid) {
		try {
			Feature feature = new Feature(uuid);
			
			try {
				//Init: (WindowICON, GPU PROGRAM Insteal(GPU��һ���߳̿鸺��������Ⱦ�����е�һ���̸߳��𴰿���һ�����صĹ���׷�٣��������ݴ������߳̿�Ĺ����ڴ���)), createDisplay
				//Draw: while(needsupdate) update;
				
				
				
				//release
				System.gc();
			} catch(Throwable e) {
				e.printStackTrace();
			}
			
			//return succeed
			return true;
		} catch(Throwable e) {
			e.printStackTrace();
			return false;
		}
	}
}
