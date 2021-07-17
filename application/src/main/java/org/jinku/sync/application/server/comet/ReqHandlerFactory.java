package org.jinku.sync.application.server.comet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import org.jinku.sync.application.ao.ResultAo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReqHandlerFactory {

    @Autowired
    private List<ReqHandler> reqHandlerList;

    public ResultAo handleReq(String req, ChannelHandlerContext ctx) {
        JSONObject jsonObject = JSON.parseObject(req);
        ReqType reqType =  ReqType.get(jsonObject.getIntValue("reqType"));

        ReqHandler reqHandler = reqHandlerList.stream()
                .filter(handler -> handler.getReqType() == reqType)
                .findFirst().orElse(null);
        Preconditions.checkNotNull(reqHandler, "请求参数不合法");
        return reqHandler.handleReq(jsonObject.getString("reqData"), ctx);
    }

}
