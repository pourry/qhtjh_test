package com.example.spring_boot_mode.config.encrypt.sm2;


import cn.hutool.core.codec.BCD;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.ECKeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http11.filters.VoidInputFilter;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

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
/*
编码方式：
* Base64：  （8进制编码）  隐蔽性好（不易被人直接识别的形式）
                        速度慢些，编码后相对较小
                        Base64 编码主要用在传输、存储、表示二进制领域，
                        不能算得上加密，只是无法直接看到明文。
                        也可以通过打乱 Base64 编码来进行加密。
* BCD：      可读性好
* Hex：     （16进制编码） 隐蔽性好（不易被人直接识别的形式）
                        速度明显快，但体积要大
* */
//数据加密 数字签名  使用
@Slf4j
public class SM2Util {

    public static String PRIVATE_KEY;
    public static String PUBLIC_KEY;


    //每次重启自动耍小心sm2的 公钥和私钥
    static {
        SM2 sm2 = SmUtil.sm2();
        //私钥
        byte[] privateKeyb = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
        //公钥
        PublicKey publicKeyStr = sm2.getPublicKey();
        byte[] publicKeyb = ((BCECPublicKey) publicKeyStr).getQ().getEncoded(false);

        PRIVATE_KEY = HexUtil.encodeHexStr(privateKeyb);
        PUBLIC_KEY = HexUtil.encodeHexStr(publicKeyb);
        System.out.println("私钥："+PRIVATE_KEY);
        System.out.println("公钥："+PUBLIC_KEY);
    }
    public static String getPublicKey(){
        return PUBLIC_KEY;
    }


    //SM2 加密
    public static String encrypt(String data) {
        SM2 sm2 = SmUtil.sm2(HexUtil.decodeHex(PRIVATE_KEY),HexUtil.decodeHex(PUBLIC_KEY));
        //ECKeyUtil.toSm2PrivateParams  可以自动识别  base64 和 hex
//        SM2 sm2 = SmUtil.sm2(ECKeyUtil.toSm2PrivateParams(PRIVATE_KEY), ECKeyUtil.toSm2PublicParams(PUBLIC_KEY));
        String encryptBcd = sm2.encryptBcd(data, KeyType.PublicKey);
        if (StrUtil.isNotBlank(encryptBcd)) {
            // 生成的加密密文会带04，因为前端sm-crypto默认的是1-C1C3C2模式，这里需去除04才能正常解密
            if (encryptBcd.startsWith("04")) {
                encryptBcd = encryptBcd.substring(2);
            }
            // 前端解密时只能解纯小写形式的16进制数据，这里需要将所有大写字母转化为小写
            encryptBcd = encryptBcd.toLowerCase();
        }
        return encryptBcd;
    }

    //SM2 解密
    public static String decrypt(String encryptData) {
        if (StrUtil.isBlank(encryptData)) {
            throw new RuntimeException("解密串为空，解密失败");
        }
        SM2 sm2 = SmUtil.sm2(HexUtil.decodeHex(PRIVATE_KEY),HexUtil.decodeHex(PUBLIC_KEY));
        // BC库解密时密文开头必须带04，如果没带04则需补齐
        if (!encryptData.startsWith("04")) {
            encryptData = "04".concat(encryptData);
        }
        byte[] decryptFromBcd = sm2.decryptFromBcd(encryptData, KeyType.PrivateKey);
        if (decryptFromBcd != null && decryptFromBcd.length > 0) {
            return StrUtil.utf8Str(decryptFromBcd);
        } else {
            log.info("密文解密失败------------------failed--------------------");
            return null;
        }

    }


    //随机秘钥对
    public static  void testsj(){
        String text = "test测试";
        SM2 sm2 = SmUtil.sm2();
        String sencod = sm2.encryptBase64(text, KeyType.PublicKey);
        String sdecod = StrUtil.utf8Str(sm2.decryptStr(sencod,KeyType.PrivateKey));
        System.out.println(sencod);
        System.out.println(sdecod);
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

      System.out.println(Base64.encode(priKey));
      System.out.println(Base64.encode(pubKey));
      System.out.println(encStr);
      System.out.println(strdecStr);
  }

  //生成 公钥、私钥
    public static void testsc(){
        SM2 sm2 = SmUtil.sm2();
        //私钥
        byte[] privateKeyb = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
        //公钥
        PublicKey publicKeyStr = sm2.getPublicKey();
        byte[] publicKeyb = ((BCECPublicKey) publicKeyStr).getQ().getEncoded(false);

        String privateKey = Base64.encode(privateKeyb);
        String publicKey = Base64.encode(publicKeyb);
        System.out.println("Base64编码的SM2私钥："+privateKey);
        System.out.println("Base64编码的SM2公钥："+publicKey);
        System.out.println("\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\");
        privateKey = HexUtil.encodeHexStr(privateKeyb);
        publicKey = HexUtil.encodeHexStr(publicKey);
        System.out.println("Hex编码的SM2私钥："+privateKey);
        System.out.println("Hex编码的SM2公钥："+publicKey);
    }



}
