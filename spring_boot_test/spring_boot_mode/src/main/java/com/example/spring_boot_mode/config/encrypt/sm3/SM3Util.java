package com.example.spring_boot_mode.config.encrypt.sm3;


import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.SM3;
import com.example.spring_boot_mode.entity.Test;
import org.bouncycastle.crypto.digests.SM3Digest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* SM3算法：SM3杂凑算法是我国自主设计的密码杂凑算法，适用于商用密码应用中的数字签名和验证消息认证码的生成与验证以及随机数的生成，
* 可满足多种密码应用的安全需求。
* 为了保证杂凑算法的安全性，其产生的杂凑值的长度不应太短，例如MD5输出128比特杂凑值，输出长度太短，影响其安全性。
* SHA-1算法的输出长度为160比特，SM3算法的输出长度为256比特，因此SM3算法的安全性要高于MD5算法和SHA-1算法。
* */

//摘要签名算法   以及防止重复攻击
public class SM3Util {
    public static void main(String[] args) {
        String str = "hello2023";
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("err", "dder");
        list.add("errr");
        list.add(map);
        Test test = new Test();
        test.setId("idddd");
        test.setName("err人人通");
        System.out.println("-----------------------");
        System.out.println(getSignValue(test));
//        toSM3(str);
    }



    //将获取到的数据进行 摘要加密
    public static String toSM3(Object object){
        String sObject  = "";
        //将对像拼接为String类型对象
        if (object instanceof Map) {
            sObject = object.toString();
        }
        sObject=  SmUtil.sm3("34444er二发");
        return sObject;
    }


    public static String getSignValue(Object classA) {
        Field[] fs = classA.getClass().getDeclaredFields();//获取所有属性
        String[][] temp = new String[fs.length][2]; //用二维数组保存  参数名和参数值
        for (int i=0; i<fs.length;  i++) {
            fs[i].setAccessible(true);
            temp[i][0] = fs[i].getName().toLowerCase(); //获取属性名
            try {
                temp[i][1] = String.valueOf(fs[i].get(classA)) ;//把属性值放进数组
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
        temp = doChooseSort(temp); //对参数实体类按照字母顺序排续
        String result = "";
        for (int i = 0; i < temp.length; i++) {//按照签名规则生成待加密字符串
            result = result + temp[i][0]+"="+temp[i][1]+"&";
        }
        result = result.substring(0, result.length()-1);//消除掉最后的“&”
        return result;
    }

    private static String[][] doChooseSort(String[][] data) {//排序方式为选择排序
        String[][] temp = new String[data.length][2];
        temp = data;
        int n = temp.length;
        for (int i = 0; i < n-1; i++) {
            int k = i;// 初始化最小值的小标
            for (int j = i+1; j<n; j++) {
                if (temp[k][0].compareTo(temp[j][0]) > 0) {    //下标k字段名大于当前字段名
                    k = j;// 修改最大值的小标
                }
            }
            // 将最小值放到排序序列末尾
            if (k > i) {  //用相加相减法交换data[i] 和 data[k]
                String tempValue ;
                tempValue = temp[k][0];
                temp[k][0] = temp[i][0];
                temp[i][0] = tempValue;
                tempValue = temp[k][1];
                temp[k][1] = temp[i][1];
                temp[i][1] = tempValue;
            }
        }
        return temp;
    }
}
