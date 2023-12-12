package com.example.spring_boot_mode.config.encrypt.sm2;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;

import java.security.KeyPair;

/*
* SM2算法：
*    SM2椭圆曲线公钥密码算法是我国自主设计的公钥密码算法，
*    包括SM2-1椭圆曲线数字签名算法，SM2-2椭圆曲线密钥交换协议，
*    SM2-3椭圆曲线公钥加密算法，分别用于实现数字签名密钥协商和数据加密等功能。
*    SM2算法与RSA算法不同的是，SM2算法是基于椭圆曲线上点群离散对数难题，
*    相对于RSA算法，256位的SM2密码强度已经比2048位的RSA密码强度要高。

* */
/*
* SM2为非对称加密，基于ECC。该算法已公开。
* 由于该算法基于ECC，故其签名速度与秘钥生成速度都快于RSA。
* ECC 256位（SM2采用的就是ECC 256位的一种）
* 安全强度比RSA 2048位高，
* 但运算速度快于RSA。
* */
public class SM2Util {
    public static void main(String[] args) {
        testzdy();
    }
    //自定义秘钥对
  public static   void testzdy(){
      String text = "test测试";
      // 使用 SecureUtil 创建一个生成器
      KeyPair keyPair = SecureUtil.generateKeyPair("SM2");
      //私钥
      // 使用pair.getPrivate()可生成秘钥
      // 这里把秘钥转化为字节
      byte[] priKey = keyPair.getPrivate().getEncoded();
      //公钥
      // 同样的，使用pair.getPublic()可以得到公钥
      byte[] pubKey = keyPair.getPublic().getEncoded();
      SM2 sm2obj = SmUtil.sm2(priKey,pubKey);
      //公钥加密
      String encStr = sm2obj.encryptBcd(text, KeyType.PublicKey);
      byte[] decStr = sm2obj.decryptFromBcd(encStr,KeyType.PrivateKey);
      String strdecStr = StrUtil.utf8Str(decStr);
      System.out.println(encStr);

      System.out.println(strdecStr);
  }

}
