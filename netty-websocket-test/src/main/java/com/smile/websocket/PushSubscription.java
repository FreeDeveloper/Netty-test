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

import io.netty.channel.Channel;
import io.netty.util.internal.ObjectUtil;

public final class PushSubscription {

    private final String userId;
    private final String deviceId;
    private final String clientInfo;
    private final Channel channel;

    public PushSubscription(String id, String destination, Channel channel) {
        this.userId = ObjectUtil.checkNotNull(id, "id");
        this.deviceId = ObjectUtil.checkNotNull(destination, "deviceId");
        this.clientInfo = ObjectUtil.checkNotNull(destination, "clientInfo");
        this.channel = ObjectUtil.checkNotNull(channel, "channel");
    }

    public String getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public Channel channel() {
        return channel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PushSubscription that = (PushSubscription) obj;

        if (!userId.equals(that.userId)) {
            return false;
        }

        if (!deviceId.equals(that.deviceId)) {
            return false;
        }

        return channel.equals(that.channel);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + deviceId.hashCode();
        result = 31 * result + clientInfo.hashCode();
        result = 31 * result + channel.hashCode();
        return result;
    }
}
