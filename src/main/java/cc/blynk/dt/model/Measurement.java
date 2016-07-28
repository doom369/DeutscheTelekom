package cc.blynk.dt.model;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;

/**
 * The Blynk Project.
 * Created by Dmitriy Dumanskiy.
 * Created on 28.07.16.
 */
public class Measurement implements Comparable<Measurement> {

    public String id;

    public Date time;

    public String self;

    public Source source;

    public String type;

    public Map<Integer, Unit> Temperatur;

    @Override
    public int compareTo(Measurement o) {
        return o != null && time != null ? o.time.compareTo(time) : -1;
    }
}
