package com.hc.util;

/**
 * 线程相关工具
 * @author chensh
 */
public class ThreadUtil {
	/**
	 * 让线程睡眠指定时间
	 * @param ms 毫秒数
	 */
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException iex) {
		}
	}

	/**
	 * 线程暂停，等同于睡眠9223372036854775807L毫秒
	 */
	public static void sleep() {
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException iex) {
		}
	}

	/**
	 * 当前线程等待
	 * @param obj 对象
	 */
	public static void wait(Object obj) {
		synchronized (obj) {
			try {
				obj.wait();
			} catch (InterruptedException inex) {
			}
		}
	}

	public static void wait(Object obj, long timeout) {
		synchronized (obj) {
			try {
				obj.wait(timeout);
			} catch (InterruptedException inex) {
			}
		}
	}

	public static void notify(Object obj) {
		synchronized (obj) {
			obj.notify();
		}
	}

	public static void notifyAll(Object obj) {
		synchronized (obj) {
			obj.notifyAll();
		}
	}

	public static void join(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException inex) {
		}
	}

	public static void join(Thread thread, long millis) {
		try {
			thread.join(millis);
		} catch (InterruptedException inex) {
		}
	}

	public static void join(Thread thread, long millis, int nanos) {
		try {
			thread.join(millis, nanos);
		} catch (InterruptedException inex) {
		}
	}
}
