package com.pgmate.lib.queue;


import java.util.LinkedList;

/**
 * @author Administrator
 *
 */
public class Queue extends LinkedList{

	public synchronized void push(Object o) {
		add(o);
		//log("**************(" + this.size() + ") : " + Thread.currentThread());
		notify();
	}

	public synchronized Object pop() {
		for(int i = 0; ;i++) {
			try {
				if(size() == 0) {
					wait();
			//		log("**************(" + this.size() + ") : " + Thread.currentThread());
				} else {
				//	log("**************(" + this.size() + ") : " + Thread.currentThread());
				}

				return removeFirst();
			} catch (Exception e) {
				if (i > 1) {
					
					try { Thread.sleep(100); } catch (Exception e1) {}
				}
				
				continue;
			}
		}
	}
	

}
