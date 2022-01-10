package com.pgmate.lib.vertx.util;

import java.io.File;
import java.util.function.Consumer;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * @author Administrator
 *
 */
public class Runner {

	public static void runJava(String prefix, Class<?> clazz, boolean clustered) {
		runJava(prefix, clazz, new VertxOptions().setClustered(clustered));
	}

	public static void runJava(String prefix, Class<?> clazz, VertxOptions options) {
		String Dir = prefix + clazz.getPackage().getName().replace(".", "/");
		run(Dir, clazz.getName(), options);
	}

	public static void runJava(String prefix, Class<?> clazz, DeploymentOptions deploymentOptions) {
		String Dir = prefix + clazz.getPackage().getName().replace(".", "/");
		run(Dir, clazz.getName(), new VertxOptions(), deploymentOptions);
	}

	public static void runScript(String prefix, String scriptName, boolean clustered) {
		File file = new File(scriptName);
		String dirPart = file.getParent();
		String scriptDir = prefix + dirPart;
		Runner.run(scriptDir, scriptDir + "/" + file.getName(), clustered);
	}

	public static void runScript(String prefix, String scriptName, VertxOptions options) {
		File file = new File(scriptName);
		String dirPart = file.getParent();
		String scriptDir = prefix + dirPart;
		Runner.run(scriptDir, scriptDir + "/" + file.getName(), options);
	}

	public static void run(String Dir, String verticleID, boolean clustered) {
		run(Dir, verticleID, new VertxOptions().setClustered(clustered));
	}

	public static void run(String Dir, String verticleID, VertxOptions options) {
		run(Dir, verticleID, options, null);
	}

	public static void run(String Dir, String verticleID, VertxOptions options, DeploymentOptions deploymentOptions) {
		System.setProperty("vertx.cwd", Dir);
		Consumer<Vertx> runner = vertx -> {
			try{
				if(deploymentOptions != null) {
					vertx.deployVerticle(verticleID, deploymentOptions);
				} else {
					vertx.deployVerticle(verticleID);
				}
			}catch (Throwable t) {
				t.printStackTrace();
			}
		};
			
		if(options.isClustered()) {
			Vertx.clusteredVertx(options, res -> {
				if(res.succeeded()) {
					Vertx vertx = res.result();
					runner.accept(vertx);
				}else {
					res.cause().printStackTrace();
				}
			});
		}else {
			Vertx vertx = Vertx.vertx(options);
			runner.accept(vertx);
		}
	}

}