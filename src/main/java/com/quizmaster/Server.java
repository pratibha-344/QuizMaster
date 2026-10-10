package com.quizmaster;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Server {
    private Server() {}

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        File war = Path.of("target", "quizmaster.war").toFile().getAbsoluteFile();
        if (!war.isFile()) {
            throw new IllegalStateException("Build the app first with: mvn clean package");
        }

        Path baseDir = Path.of("target", "tomcat").toAbsolutePath();
        Path appBase = baseDir.resolve("webapps");
        Files.createDirectories(appBase);
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(baseDir.toString());
        tomcat.getHost().setAppBase(appBase.toString());
        tomcat.getConnector();
        Context context = tomcat.addWebapp("", war.getPath());
        context.setParentClassLoader(Thread.currentThread().getContextClassLoader());
        tomcat.start();
        System.out.println("QuizMaster is ready at http://localhost:" + port);
        tomcat.getServer().await();
    }
}
