/*
 * Copyright 2015 Alimusic All right reserved. This software is the
 * confidential and proprietary information of Alimusic ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alimusic .
 */
package com.taobao.cun.module.framework.bundle.monitor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类MonitorServlet.java的实现描述：TODO 类实现描述 
 * @author steven 2015年11月25日 下午8:31:58
 */
public class MonitorServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -6530876658683561756L;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("success");
    }
}
