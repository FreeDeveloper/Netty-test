/*
 * Copyright 2020 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.smile.websocket;

import com.smile.websocket.codec.PushProtocolDecoder;
import com.smile.websocket.codec.PushProtocolEncoder;
import com.smile.websocket.handler.AuthHandler;
import com.smile.websocket.handler.PushWebSocketHandler;
import com.smile.websocket.handler.ServerIdleCheckHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.ObjectUtil;

public class WebSocketPushServerInitializer extends ChannelInitializer<SocketChannel> {

    private final String pushPath;

    public WebSocketPushServerInitializer(String chatPath) {
        this.pushPath = ObjectUtil.checkNotNull(chatPath, "chatPath");
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 增加IP黑名单过滤,过滤规则是拒掉127.1.*.*的所有ip
        IpSubnetFilterRule ipSubnetFilterRule = new IpSubnetFilterRule("127.1.0.1", 16, IpFilterRuleType.REJECT);
        RuleBasedIpFilter ruleBasedIpFilter = new RuleBasedIpFilter(ipSubnetFilterRule);
        // 增加鉴权
        AuthHandler authHandler = new AuthHandler();

        channel.pipeline()
                .addLast("ipFilter", ruleBasedIpFilter)
                .addLast("idleCheck", new ServerIdleCheckHandler())
                .addLast("lowLoggingHandler", new LoggingHandler(LogLevel.DEBUG))
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(65536))
                .addLast(new WebSocketPushClientPageHandler())
                .addLast(new WebSocketServerProtocolHandler(pushPath, null, true))
                // 将TextWebSocketFrame转换成自定义协议
                .addLast("protocolDecode", new PushProtocolDecoder())
                // 将自定义协议转换为转换成TextWebSocketFrame
                .addLast("protocolEncode", new PushProtocolEncoder())
                .addLast("authHandler", authHandler)
                .addLast("highLoggingHandler", new LoggingHandler(LogLevel.INFO))
//                .addLast("flushEnhance",new FlushConsolidationHandler(5,true))
                .addLast("pushHandler",new PushWebSocketHandler());

    }
}
