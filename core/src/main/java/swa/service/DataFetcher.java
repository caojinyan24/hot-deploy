package swa.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jinyan.cao on 2017/6/8.
 */
public class DataFetcher extends HttpServlet {
    private static final long serialVersionUID = 7579688337027366050L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String fileName = req.getParameter("fileName");// TODO: 2017/6/9
        try {
            resp.getWriter().write(JSONObject.toJSONString(DataStorer.getValue(fileName)));
            resp.setStatus(200);
            resp.setHeader("fileName", fileName);
            resp.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
