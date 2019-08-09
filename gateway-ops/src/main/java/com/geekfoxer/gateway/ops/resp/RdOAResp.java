package com.geekfoxer.gateway.ops.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author pizhihui
 * @date 2019-08-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RdOAResp {

    private List<Object> resultRows;
    private String retmsg;
    private int retcode;
    private Map<String, Object> interfaceInfo;



}
