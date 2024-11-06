package com.example.spring_boot_mode.web;

import com.example.spring_boot_mode.service.mode.ComicPicturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anicomicPictures")
public class ComicPicturesController {
    @Autowired
    ComicPicturesService comicPicturesService;
}
