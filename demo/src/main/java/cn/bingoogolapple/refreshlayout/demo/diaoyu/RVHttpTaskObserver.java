//
//  HAHttpTaskObserver.java
//  HoneyAnt
//
//  Created by dongjianbo on 15-1-4.
//  Copyright 2015 www.mafengwo.cn
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not
//  use this file except in compliance with the License.  You may obtain a copy
//  of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
//  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
//  License for the specific language governing permissions and limitations under
//  the License.
//
package cn.bingoogolapple.refreshlayout.demo.diaoyu;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import cn.bingoogolapple.refreshlayout.demo.ui.fragment.RvModel;

public class RVHttpTaskObserver extends Object {
//	private static final String PREFIX_INDEX = "idx";
	private static final String PREFIX_FLAG = "flg";
	
	/**
	 * HAHttpTaskListener 以listener方式监听任务状态改变
	 */
	public interface RVHttpTaskListener {
		public void onHttpTaskAdded(RvModel task);
		
		public void onHttpTaskStarted(RvModel task);
		
		public void onHttpTaskSucceeded(RvModel task);
		
		public void onHttpTaskCanceled(RvModel task);
		
		public void onHttpTaskFailed(RvModel task);
		
		public void onHttpTaskFinished(RvModel task); // Succeeded|Canceled|Failed
	}
	
	/**
	 * RVHttpTaskBlock， 以block方式监听任务状态改变 针对单一状态进行监听
	 */
	public static interface RVHttpTaskBlock {
		public void block(RvModel task);
	}
	
	/**
	 * selector监听，public onXXX:(RvModel)task;
	 */
	
	/**
	 * single instance
	 */
	private static RVHttpTaskObserver instance = null;
	
	protected RVHttpTaskObserver() {
		
	}
	
	public static synchronized RVHttpTaskObserver getInstance() {
		if (instance == null) {
			instance = new RVHttpTaskObserver();
		}
		
		return instance;
	}
	
	/**
	 * 通知顺序与添加顺序相同
	 */
	private ConcurrentHashMap<String, ArrayList<RVTaskObserver>> taskObserverMap = new ConcurrentHashMap<String, ArrayList<RVTaskObserver>>();
	

	public synchronized void addTaskObserver(Object observer, String taskFlag) {
		ArrayList<RVTaskObserver> observersArray = this.getObserversArray(RVTaskDelegateObserver.class.getName(), observer, 0, null, PREFIX_FLAG + taskFlag);
		if (observersArray != null) {
			RVTaskDelegateObserver taskObserver = new RVTaskDelegateObserver();
			taskObserver.observer = observer;
			observersArray.add(taskObserver);
		}
	}
	
	public synchronized void addTaskObserver(Object observer, String taskFlag, int taskStatus, String observerSelector) {
		ArrayList<RVTaskObserver> observersArray = this.getObserversArray(RVTaskSelectorObserver.class.getName(), observer, taskStatus, observerSelector, PREFIX_FLAG + taskFlag);
		if (observersArray != null) {
			RVTaskSelectorObserver taskObserver = new RVTaskSelectorObserver();
			taskObserver.status = taskStatus;
			taskObserver.observer = observer;
			taskObserver.selector = observerSelector;
			observersArray.add(taskObserver);
		}
	}

	public synchronized void addTaskObserver(Object observer, String taskFlag, int taskStatus, RVHttpTaskBlock observerBlock) {
		ArrayList<RVTaskObserver> observersArray = this.getObserversArray(RVTaskBlockObserver.class.getName(), observer, taskStatus, null, PREFIX_FLAG + taskFlag);
		if (observersArray != null) {
			RVTaskBlockObserver taskObserver = new RVTaskBlockObserver();
			taskObserver.status = taskStatus;
			taskObserver.observer = observer;
			taskObserver.block = observerBlock;
			observersArray.add(taskObserver);
		}
	}

	public synchronized boolean removeTaskObserver(Object observer, String taskFlag) {
		boolean isRemoved = false;
		
		ArrayList<RVTaskObserver> observersArray = this.taskObserverMap.get(PREFIX_FLAG + taskFlag);
		if (observersArray != null) {
			CopyOnWriteArrayList<RVTaskObserver> tempArray = new CopyOnWriteArrayList<RVTaskObserver>(observersArray);
			for (RVTaskObserver observerItem : tempArray) {
				if (observerItem.observer.equals(observer)) {
					observersArray.remove(observerItem);
					isRemoved = true;
				}
			}
			
			if (observersArray.size() <= 0) {
				this.taskObserverMap.remove(PREFIX_FLAG + taskFlag);
			}
		}
		
		return isRemoved;
	}
	
	public synchronized boolean removeTaskObserver(Object observer) {
		if (observer == null) {
			return false;
		}
		
		boolean isRemoved = false;
		
		Set<String> allKeys = this.taskObserverMap.keySet();
		if (allKeys != null) {
			CopyOnWriteArraySet<String> tempKeys = new CopyOnWriteArraySet<String>(allKeys);
			for (String key : tempKeys) {
				ArrayList<RVTaskObserver> observers = this.taskObserverMap.get(key);
				if (observers != null) {
					CopyOnWriteArrayList<RVTaskObserver> tempArray = new CopyOnWriteArrayList<RVTaskObserver>(observers);
					for (RVTaskObserver observerItem : tempArray) {
						if (observerItem.observer.equals(observer)) {
							observers.remove(observerItem);
							isRemoved = true;
						}
					}
					
					if (observers.size() <= 0) {
						this.taskObserverMap.remove(key);
					}
				}
			}
		}
		
		return isRemoved;
	}

	public synchronized void removeTaskObserverByIdentify(String taskFlag) {
		this.taskObserverMap.remove(PREFIX_FLAG + taskFlag);
	}
	
	public synchronized void removeAllTaskObservers() {
		this.taskObserverMap.clear();
	}
	
	public synchronized void printAllTaskObservers() {
		Log.d("printAllTaskObservers-begin", "begin");
		
		Set<String> allKeys = this.taskObserverMap.keySet();
		if (allKeys != null) {
			CopyOnWriteArraySet<String> tempKeys = new CopyOnWriteArraySet<String>(allKeys);
			for (String key : tempKeys) {
				ArrayList<RVTaskObserver> observers = this.taskObserverMap.get(key);
				if (observers != null) {
					CopyOnWriteArrayList<RVTaskObserver> tempArray = new CopyOnWriteArrayList<RVTaskObserver>(observers);
					for (RVTaskObserver observerItem : tempArray) {
						Log.d("printAllTaskObservers: ", observerItem.getClass().getName());
					}
				}
			}
		}
		
		Log.d("printAllTaskObservers-end", "end");
	}
	
	/**
	 * notify to handler
	 * 
	 * @param task
	 */
	public synchronized void notifyRequestAdded(RvModel task) {

		task.status = RvModel.HttpTaskStatusAdded;
		
		this.notifyTaskStatusChanged(task);

	}
	
	public synchronized void notifyRequestStarted(RvModel task) {
		Message msg = Message.obtain();
		msg.what = RvModel.HttpTaskStatusStarted;
		msg.obj = task;
		this.handler.sendMessage(msg);
	}
	
	public synchronized void notifyRequestSucceeded(RvModel task) {
		Message msg = Message.obtain();
		msg.what = RvModel.HttpTaskStatusSucceeded;
		msg.obj = task;
		this.handler.sendMessage(msg);
	}
	
	public synchronized void notifyRequestCanceled(RvModel task) {
		Message msg = Message.obtain();
		msg.what = RvModel.HttpTaskStatusCanceled;
		msg.obj = task;
		this.handler.sendMessage(msg);
	}
	
	public synchronized void notifyRequestFailed(RvModel task) {
		Message msg = Message.obtain();
		msg.what = RvModel.HttpTaskStatusFailed;
		msg.obj = task;
		this.handler.sendMessage(msg);
	}

	public synchronized void notifyRequestFinish(RvModel task) {
		Message msg = Message.obtain();
		msg.what = RvModel.HttpTaskStatusFinish;
		msg.obj = task;
		this.handler.sendMessage(msg);
	}
	
	/**
	 * handler to listener
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case RvModel.HttpTaskStatusAdded: {
					
				}
					break;
				case RvModel.HttpTaskStatusStarted: {
					RvModel task = (RvModel) msg.obj;
					if (task != null) {
						task.status = RvModel.HttpTaskStatusStarted;
						
						notifyTaskStatusChanged(task);

					}
				}
					break;
				case RvModel.HttpTaskStatusSucceeded: {
					RvModel task = (RvModel) msg.obj;
					if (task != null) {
						task.status = RvModel.HttpTaskStatusSucceeded;
						
						notifyTaskStatusChanged(task);

					}
				}
					break;
				case RvModel.HttpTaskStatusCanceled: {
					RvModel task = (RvModel) msg.obj;
					if (task != null) {
						task.status = RvModel.HttpTaskStatusCanceled;
						
						notifyTaskStatusChanged(task);
						
						// remove index observer
						removeTaskObserverByIdentify(task.rvFlag);

					}
				}
					break;
				case RvModel.HttpTaskStatusFailed: {
					RvModel task = (RvModel) msg.obj;
					if (task != null) {
						task.status = RvModel.HttpTaskStatusFailed;
						
						notifyTaskStatusChanged(task);
						
						// remove index observer
						removeTaskObserverByIdentify(task.rvFlag);

					}
				}
					break;

				case RvModel.HttpTaskStatusFinish: {
					RvModel task = (RvModel) msg.obj;
					if (task != null) {
						task.status = RvModel.HttpTaskStatusFinish;

						notifyTaskStatusChanged(task);

						// remove index observer
						removeTaskObserverByIdentify(task.rvFlag);

					}
				}
				break;
				default:
					break;
			}
		}
	};
	
	private ArrayList<RVTaskObserver> getObserversArray(String className, Object observer, int taskStatus, String selector, String mapkey) {
		ArrayList<RVTaskObserver> observersArray = this.taskObserverMap.get(mapkey);
		if (observersArray == null) {
			observersArray = new ArrayList<RVTaskObserver>();
			this.taskObserverMap.put(mapkey, observersArray);
		}
		
		boolean shouldAdd = true;
		for (RVTaskObserver taskObserver : observersArray) {
			if (taskObserver.equals(className, observer, taskStatus, selector)) {
				shouldAdd = false;
				break;
			}
		}
		
		if (shouldAdd) {
			return observersArray;
		}
		
		return null;
	}
	
	private void notifyTaskStatusChanged(RvModel task) {

//		if (task.status == RvModel.HttpTaskStatusAdded) {
//			// 前置处理插件
//			for (PrePlugin plugin : task.getPrePlugins()) {
//				plugin.onHttpTaskPrePluginExecute(task, task.request);
//			}
//		} else if (task.status == RvModel.HttpTaskStatusSucceeded
//				|| task.status == RvModel.HttpTaskStatusCanceled
//				|| task.status == RvModel.HttpTaskStatusFailed) {
//			// 后置插件处理
//			for (RVHttpTaskPostPlugin plugin : task.getPostPlugins()) {
//				plugin.onHttpTaskPostPluginExecute(task, task.response);
//			}
//		}
		
//		ArrayList<RVTaskObserver> indexObserversArray = this.taskObserverMap.get(PREFIX_INDEX + task.index);
//		if (indexObserversArray != null) {
//			CopyOnWriteArrayList<RVTaskObserver> observersArray = new CopyOnWriteArrayList<RVTaskObserver>(indexObserversArray);
//			if (observersArray != null) {
//				for (RVTaskObserver observerItem : observersArray) {
//					observerItem.perform(task);
//				}
//			}
//		}
		
		if (task.rvFlag != null) {
			ArrayList<RVTaskObserver> flagObserversArray = this.taskObserverMap.get(PREFIX_FLAG + task.rvFlag);
			if (flagObserversArray != null) {
				CopyOnWriteArrayList<RVTaskObserver> observersArray = new CopyOnWriteArrayList<RVTaskObserver>(flagObserversArray);
				if (observersArray != null) {
					for (RVTaskObserver observerItem : observersArray) {
						observerItem.perform(task);
					}
				}
			}
		}
		
	}
	
	/**
	 * private
	 */
	
	/**
	 * RVTaskObserver Observer基类
	 */
	private abstract class RVTaskObserver {
		public Object observer;
		
		public abstract boolean equals(String className, Object observer, int status, String selector);
		
		public abstract void perform(RvModel task);
	}
	
	/**
	 * RVTaskDelegateObserver 以delegate方式监听任务状态改变
	 */
	private class RVTaskDelegateObserver extends RVTaskObserver {
		
		@Override
		public boolean equals(String className, Object observer, int status, String selector) {
			if (!this.getClass().getName().equals(className)) {
				return false;
			}
			
			if (!this.observer.equals(observer)) {
				return false;
			}
			
			return true;
		}
		
		@Override
		public void perform(RvModel task) {
			if (this.observer != null) {
				RVHttpTaskListener listener = (RVHttpTaskListener) this.observer;
				switch (task.status) {
					case RvModel.HttpTaskStatusAdded: {
						listener.onHttpTaskAdded(task);
					}
						break;
					case RvModel.HttpTaskStatusStarted: {
						listener.onHttpTaskStarted(task);
					}
						break;
					case RvModel.HttpTaskStatusSucceeded: {
						listener.onHttpTaskSucceeded(task);
						listener.onHttpTaskFinished(task);
					}
						break;
					case RvModel.HttpTaskStatusFailed: {
						listener.onHttpTaskFailed(task);
						listener.onHttpTaskFinished(task);
					}
						break;
					case RvModel.HttpTaskStatusCanceled: {
						listener.onHttpTaskCanceled(task);
						listener.onHttpTaskFinished(task);
					}
						break;
					
					default:
						break;
				}
			}
		}
	}
	
	/**
	 * RVTaskSelectorObserver 以selector方式监听任务状态改变 针对指定状态进行监听
	 */
	private class RVTaskSelectorObserver extends RVTaskObserver {
		public int status;
		public String selector;
		
		@Override
		public boolean equals(String className, Object observer, int status, String selector) {
			if (!this.getClass().getName().equals(className)) {
				return false;
			}
			
			if (!this.observer.equals(observer)) {
				return false;
			}
			
			if (this.status != status) {
				return false;
			}
			
			if (!this.selector.equals(selector)) {
				return false;
			}
			
			return true;
		}
		
		@Override
		public void perform(RvModel task) {
			if (this.observer != null && this.selector != null && (this.status & task.status) != 0) {
				try {
					this.observer.getClass().getMethod(this.selector, task.getClass()).invoke(this.observer, task);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * RVTaskBlockObserver 以block方式监听任务状态改变 针对指定状态进行监听
	 */
	private class RVTaskBlockObserver extends RVTaskObserver {
		public int status;
		public RVHttpTaskBlock block;
		
		@Override
		public boolean equals(String className, Object observer, int status, String selector) {
			return false;
		}
		
		@Override
		public void perform(RvModel task) {
			if (this.block != null && (this.status & task.status) != 0) {
				this.block.block(task);
			}
		}
	}
}
