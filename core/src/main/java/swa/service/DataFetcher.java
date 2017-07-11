package swa.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接收获取文件信息的请求
 * Created by jinyan.cao on 2017/6/8.
 */
public class DataFetcher extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DataFetcher.class);
    private static final long serialVersionUID = 7579688337027366050L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String fileName = req.getParameter("fileName");// TODO: 2017/6/9
        try {
            resp.getWriter().write(JSON.toJSONString(DataStorer.getValue(fileName)));
            resp.setStatus(200);
            resp.setHeader("fileName", fileName);
            resp.flushBuffer();
        } catch (Exception e) {
            logger.info("doGet error:", e);
        }
    }
}
