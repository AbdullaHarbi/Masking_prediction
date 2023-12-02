package com.example.demo.metadata;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;


@Controller
public class MetadataController {

    private metadataService metadataService;

    @Autowired
    public MetadataController(com.example.demo.metadata.metadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping
    public String index(Model model) {
        return "index";
    }

    @PostMapping
    public String uploadCustomersData(Model model,@RequestParam("file") MultipartFile file, @RequestParam("algorithm") String algorithm)  throws IOException {
        List<metdataResult> list = this.metadataService.validateMasking2(file.getInputStream(),algorithm);

        model.addAttribute("data", list);
        return "index";

    }
}
