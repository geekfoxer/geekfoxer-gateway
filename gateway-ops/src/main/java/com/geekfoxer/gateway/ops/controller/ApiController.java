package com.geekfoxer.gateway.ops.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.geekfoxer.gateway.common.TranserUtil;
import com.geekfoxer.gateway.ops.req.TestApiQuery;
import com.geekfoxer.gateway.ops.resp.PageResult;
import com.geekfoxer.gateway.ops.resp.RespBean;
import com.geekfoxer.gateway.ops.service.ApiService;
import com.geekfoxer.gateway.ops.utils.FileType;
import com.geekfoxer.gateway.ops.utils.Query;
import com.geekfoxer.gateway.ops.vo.ApiVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import net.dongliu.requests.RequestBuilder;
import net.dongliu.requests.Requests;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pizhihui
 * @date 2019-08-07
 */
@RestController
@RequestMapping("/gateway/api")
@Slf4j
public class ApiController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/list")
    public RespBean list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        PageResult pageResult = apiService.queryList(query);
        return RespBean.pageSuccess(pageResult);
    }

    @PostMapping("/save")
    public RespBean save(@RequestBody ApiVo apiVO) {
        log.info("添加 API 数据: {}", apiVO);
        try {
            // grpc路由
//            if (zipFile != null) {
//                InputStream directoryZipStream = zipFile.getInputStream();
//                RespBean response = judgeFileType(directoryZipStream, "zip");
//                if (response != null) {
//                    return response;
//                } else {
//                    // todo for grpc
////                    byte[] protoContext = protobufService.compileDirectoryProto(zipFile);
////                    apiVO.setProtoContext(protoContext);
//                }
//            }
            apiService.save(apiVO);
        } catch (Exception e) {
            throw new IllegalArgumentException("保存路由失败", e);
        }
        return RespBean.ok();
    }


    @PostMapping("test")
    public String testApi(TestApiQuery testApiQuery) {

        String url = "http://127.0.0.1:8899" + testApiQuery.getUri();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        RequestBuilder requestBuilder = Requests.post(url);

        if (StringUtils.isNotBlank(testApiQuery.getJsonBody())) {
            requestBuilder.body(testApiQuery.getJsonBody())
                    .headers(headers);
        }
        // 获取参数
        String s = requestBuilder
                .timeout(300000, 5000000)
                .send()
                .readToText();
        log.info("【测试API接口获取到的数据】: {}", s);
        return s;
    }

    @PostMapping("/remove")
    @ResponseBody()
    public RespBean save(Long id) {
        if (apiService.remove(id) > 0) {
            log.info("打个日志测试下 jereble 的热部署....");
            return RespBean.ok();
        } else {
            return RespBean.error("删除失败");
        }
    }

    private RespBean judgeFileType(InputStream inpustream, String type) throws IOException {
        String fileType = FileType.calculateFileHexString(inpustream);
        if (!type.equals(fileType)) {
            return RespBean.error("只能上传" + type + "类型文件");
        } else {
            return null;
        }
    }
}
