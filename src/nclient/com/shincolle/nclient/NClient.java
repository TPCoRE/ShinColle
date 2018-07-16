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
				//Init: (WindowICON, GPU PROGRAM Insteal(GPU里一个线程块负责世界渲染，其中的一个线程负责窗口上一个像素的光线追踪，世界数据储存在线程块的共享内存中)), createDisplay
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
