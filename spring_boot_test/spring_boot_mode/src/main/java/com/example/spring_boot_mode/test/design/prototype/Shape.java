package com.example.spring_boot_mode.test.design.prototype;

import java.util.Hashtable;

public abstract class Shape implements Cloneable {
    private String id;
    private String type;
    abstract void draw();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}

class  Ashape extends Shape{
    public Ashape(){
        setType("Ashape");
    }

    @Override
    void draw() {
        System.out.println("Ashape方法运行");
    }
}
class Bshape extends  Shape{
    public Bshape(){
        setType("Bshape");
    }

    @Override
    void draw() {
        System.out.println("Bshape放啊发运行");
    }
}

class ShapeCache{
    private static Hashtable<String,Shape> shapmap = new Hashtable<>();
    public static Shape getshap(String shapeId){
       return (Shape) shapmap.get(shapeId).clone();
    }
    public static void loadCache(){
        Shape ashape = new Ashape();
        ashape.setId("1");
        Shape bshape = new Bshape();
        bshape.setId("2");
        shapmap.put(ashape.getId(),ashape);
        shapmap.put(bshape.getId(), bshape);
    }
}
class ShapeCacheUser{
    public static void main(String[] args) {
        ShapeCache.loadCache();
        System.out.println(ShapeCache.getshap("1").getType());
        System.out.println(ShapeCache.getshap("2").getType());
    }
}