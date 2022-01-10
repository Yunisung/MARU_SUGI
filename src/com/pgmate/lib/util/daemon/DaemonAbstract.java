package com.pgmate.lib.util.daemon;

/**
 * @author Administrator
 *
 */
public abstract class DaemonAbstract extends Thread {

	public abstract boolean execute()throws Exception;
}
