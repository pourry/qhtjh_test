package com.example.spring_boot_mode.web;

import com.example.spring_boot_mode.config.encrypt.sm2.SM2Util;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.flowable.Flowable;
import com.example.spring_boot_mode.entity.mode.SysUser;
import com.example.spring_boot_mode.service.Loginservice;
import com.example.spring_boot_mode.utils.CustomTOC;
import com.example.spring_boot_mode.utils.CustomTOCItem;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.redis.RedisSafe;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    static {
        System.out.println("静态语句块执行了");
    }
    @Autowired
    RedisSafe redisSafe;
    @Autowired
    private Loginservice loginservice;

    //获取sm2 秘钥
    @GetMapping("togetSm2")
    public ResponseObjectEntity togetSm2(){
        //每次重启都会自动生成新的sm2
        String publicKey = SM2Util.getPublicKey();
        return ResponseUtil.success(publicKey);
    }
    @GetMapping("getalltest")
    public ResponseObjectEntity getalltest(){
        List<SysUser> relist= loginservice.getSysUser();
        return ResponseUtil.success(relist);
    }
    @GetMapping("getflowabletest")
    public ResponseObjectEntity getflowable(){
        List<Flowable> relist= loginservice.getflowable();
        return ResponseUtil.success(relist);
    }
    @GetMapping("downloadWord")
    public void downloadWord(HttpServletResponse response) throws IOException {



        // 创建一个新的空白Word文档
        XWPFDocument doc = new XWPFDocument();
        CTBookmark bookmarkStart = null;
                CustomTOCItem customTOCItem = new CustomTOCItem(doc);
        customTOCItem.createCustomTOC();


        // 创建一个导航窗格的标题
        XWPFParagraph navTitlePara = doc.createParagraph();
        navTitlePara.setStyle("Heading2");
        XWPFRun navTitleRun = null;
        navTitleRun = navTitlePara.createRun();
        navTitleRun.setText("测试测试测试1111");
        navTitleRun.setBold(true);
        navTitleRun.setFontSize(16);
        navTitlePara.getCTP().addNewBookmarkStart().setName(String.valueOf("测试测试测试1111".hashCode()));
        navTitlePara.getCTP().addNewBookmarkStart().setId(new BigInteger("1"));
        navTitlePara.getCTP().addNewBookmarkEnd().setId(new BigInteger("1"));
//        navTitlePara.getCTP().addNewBookmarkEnd().setId(BigInteger.valueOf(123));
        navTitleRun.addBreak(BreakType.PAGE);
        navTitlePara = doc.createParagraph();
        navTitlePara.setStyle("Heading2");
        navTitleRun = navTitlePara.createRun();
        navTitleRun.setText("测试测试测试222");
        navTitlePara.getCTP().addNewBookmarkStart().setName(String.valueOf("测试测试测试222".hashCode()));
        navTitlePara.getCTP().addNewBookmarkStart().setId(new BigInteger("2"));
        navTitlePara.getCTP().addNewBookmarkEnd().setId(new BigInteger("2"));
        navTitleRun.setBold(true);
        navTitleRun.setFontSize(16);
        navTitleRun.addBreak(BreakType.PAGE);

        navTitlePara = doc.createParagraph();
        navTitlePara.setStyle("Heading2");
        navTitleRun = navTitlePara.createRun();
        navTitleRun.setText("测试测试测试222");
        navTitlePara.getCTP().addNewBookmarkStart().setName(String.valueOf("测试测试测试222".hashCode()));
        navTitlePara.getCTP().addNewBookmarkStart().setId(new BigInteger("2"));
        navTitlePara.getCTP().addNewBookmarkEnd().setId(new BigInteger("2"));
        navTitleRun.setBold(true);
        navTitleRun.setFontSize(16);

        customTOCItem.addItem2TOC();


//        navTitlePara = doc.createParagraph();
//        navTitlePara.setStyle("Heading3");
//        navTitleRun = navTitlePara.createRun();
//        navTitleRun.setText("测试测试测试333");
//        navTitleRun.setBold(true);
//        navTitleRun.setFontSize(16);


        response.setHeader("content-disposition", "attachment;filename="+new String("test.docx".getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        doc.write(response.getOutputStream());
    }


}
