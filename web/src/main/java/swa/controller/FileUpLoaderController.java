package swa.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import swa.obj.ConfigFile;
import swa.service.DataStorer;
import swa.service.RedisUtil;

import java.util.Map;


/**
 * Created by jinyan on 5/26/17.
 */
@Controller
@RequestMapping("swa")
public class FileUpLoaderController {

    /**
     * todo 接收数据更新请求，并发送client端的数据更新请求
     *
     * @param fileName
     * @param value
     * @return
     */
    @RequestMapping("upload")
    public ModelAndView uploadFile(@RequestParam("fileName") String fileName, @RequestParam("value") String value) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("uploadedValue", value);
        modelAndView.addObject("fileName", fileName);
        Map<String, String> storedValue = ImmutableMap.of("testKey", value);
        ConfigFile configFile;
        String file = RedisUtil.get(fileName);
        if (Strings.isNullOrEmpty(file)) {
            configFile = new ConfigFile(1, storedValue, fileName);
        } else {
            configFile = JSONObject.parseObject(file, ConfigFile.class);
            configFile.setContent(storedValue);
            configFile.setVersion(configFile.getVersion() + 1);
        }
        DataStorer.setValue(fileName, configFile);
        System.out.println("store data:" + fileName + "##" + value);
        return modelAndView;
    }
}
