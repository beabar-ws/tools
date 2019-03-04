package com.g5.sky.wxpay.notice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.g5.sky.wxpay.utils.WxPayUtils.map2xml;

/**
 * todo 注释
 *
 * @author shuai . 2018/10/30 . 3:38 PM
 */
public class WxMiniPayRefundNoticeRe {

    public String toXml() {
        SortedMap<String, String> map = new TreeMap<>();
        map.put("return_code", this.return_code);
        map.put("return_msg", this.return_msg);
        return map2xml(map);
    }

    public void response(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getOutputStream().write(this.toXml().getBytes());
        response.getOutputStream().flush();
    }

    public WxMiniPayRefundNoticeRe(String return_code, String return_msg) {
        this.return_code = return_code;
        this.return_msg = return_msg;
    }

    public WxMiniPayRefundNoticeRe() {
    }

    private String return_code;

    private String return_msg;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }
}
