package io.joyrpc.protocol.joy.codec;

/*-
 * #%L
 * joyrpc
 * %%
 * Copyright (C) 2019 joyrpc.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.joyrpc.codec.serialization.Serialization;
import io.joyrpc.protocol.AbstractCodec;
import io.joyrpc.protocol.Protocol;
import io.joyrpc.protocol.message.Invocation;
import io.joyrpc.transport.message.Message;
import io.joyrpc.transport.session.DefaultSession;

import java.util.Objects;

import static io.joyrpc.protocol.AbstractCodec.Empty.NULL;

/**
 * joy编解码器
 *
 * @date: 2019/1/14
 */
public class JoyCodec extends AbstractCodec {

    public JoyCodec(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected void adjustEncode(final Message message, final Serialization serialization) {
        //Invocation可以不传输类名和别名，BizReqHandler已经根据会话补充了类名和别名
        Object payLoad = message.getPayLoad();
        if (payLoad instanceof Invocation) {
            Invocation invocation = (Invocation) payLoad;
            invocation.setClassName(null);
            //session中的别名与invocation的别名不同，认为是动态分组，invocation别名不置空，否则置空
            DefaultSession session = (DefaultSession) message.getSession();
            if (session != null && Objects.equals(invocation.getAlias(), session.getAlias())) {
                invocation.setAlias(null);
            }
            if (!serialization.allowArrayNullElement()) {
                //把null参数转换成枚举
                Object[] args = invocation.getArgs();
                for (int i = 0; i < args.length; i++) {
                    if (args[i] == null) {
                        args[i] = NULL;
                    }
                }
            }
        }
    }

    @Override
    protected void adjustDecode(final Message message, final Serialization serialization) {
        if (!serialization.allowArrayNullElement()) {
            Object payLoad = message.getPayLoad();
            if (payLoad instanceof Invocation) {
                Invocation invocation = (Invocation) payLoad;
                //把null参数还原回来
                Object[] args = invocation.getArgs();
                for (int i = 0; i < args.length; i++) {
                    if (args[i] == NULL) {
                        args[i] = null;
                    }
                }
            }
        }
    }
}
