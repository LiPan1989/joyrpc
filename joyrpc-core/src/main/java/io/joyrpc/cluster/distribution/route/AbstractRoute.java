package io.joyrpc.cluster.distribution.route;

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

import io.joyrpc.cluster.Node;
import io.joyrpc.cluster.distribution.LoadBalance;
import io.joyrpc.cluster.distribution.Route;
import io.joyrpc.extension.URL;
import io.joyrpc.util.TriFunction;

import java.util.concurrent.CompletableFuture;

/**
 * 抽象的Route实现
 *
 * @param <T>
 * @param <R>
 */
public abstract class AbstractRoute<T, R> implements Route<T, R> {

    /**
     * 负载均衡
     */
    protected LoadBalance<T> loadBalance;
    /**
     * 路由操作
     */
    protected TriFunction<Node, Node, T, CompletableFuture<R>> function;
    /**
     * URL
     */
    protected URL url;

    @Override
    public void setLoadBalance(LoadBalance<T> loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public void setFunction(TriFunction<Node, Node, T, CompletableFuture<R>> function) {
        this.function = function;
    }

    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

}
