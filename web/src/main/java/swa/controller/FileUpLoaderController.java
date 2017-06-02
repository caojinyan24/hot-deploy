package swa.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import swa.service.DataStorer;
import swa.service.DataUpdater;



/**
 * Created by jinyan on 5/26/17.
 */
@Controller
@RequestMapping("swa")
public class FileUpLoaderController {

    @RequestMapping("upload")
    public ModelAndView uploadFile(@RequestParam("fileName") String fileName, @RequestParam("value") String value) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("uploadedValue", value);
        modelAndView.addObject("fileName", fileName);
        DataStorer.setValue(fileName, ImmutableMap.of("testKey", value));
        DataUpdater.setData(fileName);
        System.out.println("store data:" + fileName + "##" + value);
        return modelAndView;
    }
}
