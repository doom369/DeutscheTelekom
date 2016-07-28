package cc.blynk.dt.model;

/**
 * The Blynk Project.
 * Created by Dmitriy Dumanskiy.
 * Created on 28.07.16.
 */
public class Unit {

    public String unit;

    public String value;

    @Override
    public String toString() {
        return "Unit{" +
                "unit='" + unit + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
