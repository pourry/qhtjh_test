package com.example.spring_boot_mode.web;

import com.example.spring_boot_mode.service.ComicPicturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/novelPictures")
public class NovelPicturesController {
    @Autowired
    ComicPicturesService comicPicturesService;
}
