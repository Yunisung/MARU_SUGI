package com.pgmate.lib.vertx.main;

import com.pgmate.lib.vertx.conf.VertXConfigBean;

import io.vertx.ext.web.Router;

/**
 * @author Administrator
 *
 */
public abstract class RouteWorker {

	public abstract void execute(VertXConfigBean vertxConfig,Router router);
}

