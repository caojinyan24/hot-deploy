package swa.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swa.service.DataStorer;

import java.util.Map;

/**
 * Created by jinyan on 5/26/17.
 */
@Controller
@RequestMapping("swa")
public class FileUpLoaderController {
    @RequestMapping("upload")
    public String uploadFile(@RequestParam("fileName") String fileName, @RequestParam("value") String value) {
        DataStorer.setValue(fileName, ImmutableMap.of("1", value));
        System.out.println("store data:" + fileName + "##" + value);
        return "index";
    }
}
