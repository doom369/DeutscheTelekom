package cc.blynk.dt;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The Blynk Project.
 * Created by Dmitriy Dumanskiy.
 * Created on 28.07.16.
 */
public class Launcher {

    public static void main(String[] args) throws Exception {
        String hostUrl = "https://dtagdemo.ram.m2m.telekom.com";
        String user = "pavel@blynk.cc";
        String pass = "Blynk2016";
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new MeasurementReader(hostUrl, user, pass), 0, 5, TimeUnit.SECONDS);
    }

}
