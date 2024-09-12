package com.example.spring_boot_mode.utils;

import org.apache.poi.util.LocaleUtil;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;
import java.util.List;

public class CustomTOCItem
{

    private CustomTOC toc;
    private XWPFDocument doc;
    public CustomTOCItem(XWPFDocument doc){
        this.doc = doc;
    }
    public void createCustomTOC() {

        CTSdtBlock block = doc.getDocument().getBody().addNewSdt();
//        doc.createTOC();
        this.toc = new CustomTOC(block);
//        wordSetting.setCustomHeadingStyle(doc, "Heading1", 1);
//        wordSetting.setCustomHeadingStyle(doc, "Heading2", 2);
    }
    public void addItem2TOC() {

        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        for (XWPFParagraph par : paragraphs) {
            String parStyle = par.getStyle();
            if (parStyle != null && parStyle.startsWith("Heading")) {
                List<CTBookmark> bookmarkList=par.getCTP().getBookmarkStartList();
                try {
                    int level = Integer.parseInt(parStyle.substring("Heading".length()));
                    if(level==1){
                        //添加栏目
                        toc.addRowOnlyTitle(level, par.getText());
                    }else{
                        //添加标题
                        toc.addRow(level, par.getText(), 1, bookmarkList.get(0).getName());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
