package swa.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理获取文件最新版本的请求
 * Created by jinyan.cao on 2017/6/8.
 */
public class HttpServerService extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerService.class);
    private static final long serialVersionUID = 7579688337027366050L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String fileName = req.getParameter("fileName");// TODO: 2017/6/9, 文件名暂时统一为fileName
        try {
            resp.getWriter().write(JSON.toJSONString(DataStorerService.getValue(fileName)));
            resp.setStatus(200);
            resp.setHeader("fileName", fileName);
            resp.flushBuffer();
        } catch (Exception e) {
            logger.info("doGet error:", e);
        }
    }
}
