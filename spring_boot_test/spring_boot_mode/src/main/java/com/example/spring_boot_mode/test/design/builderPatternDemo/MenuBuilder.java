package com.example.spring_boot_mode.test.design.builderPatternDemo;

public class MenuBuilder {
    public  Menu putBurgerItem(){
        Menu menu = new Menu();
        menu.addItem(new VerBurger());
        menu.addItem(new ChickenBurger());
        return menu;
    }
    public Menu putDrinkItem(){
        Menu menu = new Menu();
        menu.addItem(new Coke());
        menu.addItem(new Pepsi());
        return menu;
    }
}
class BuilderPatternDemo {
    public static void main(String[] args) {
        MenuBuilder menuBuilder = new MenuBuilder();
        Menu menuburger = menuBuilder.putBurgerItem();
        System.out.println("burgert:"+menuburger.getCost());
        menuburger.showItems();



    }
}
