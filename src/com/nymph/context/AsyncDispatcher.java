package com.nymph.context;

import com.nymph.queue.NyQueue;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright 2017 author: LiuYang, LiangTianDong
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * <p>
 * 请求调度器, 基于生产消费的模型来实现并发的处理请求和响应结果
 *
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年9月26日下午8:16:28
 */
public final class AsyncDispatcher extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // 执行队列的线程池
    private final ExecutorService contextPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);
    private final ExecutorService paramPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);
    private final ExecutorService viewPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);
    private final NyQueue<ContextParam> params = new NyQueue<>();
    private final NyQueue<ContextView> views = new NyQueue<>();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        ContextWrapper ctx = new ContextWrapper(request.startAsync());
        contextPool.execute(new ResolverUrlImpl(ctx, params));
    }


    /**
     * 执行参数解析器
     *
     * @throws InterruptedException
     */
    public void dispatchParam() {
        try {
            ContextParam param = params.take();
            paramPool.execute(new ResovlerParamImpl(param, views));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行视图解析器
     *
     * @throws InterruptedException
     */
    public void dispatchView() {
        try {
            ContextView view = views.take();
            viewPool.execute(new ResolverViewImpl(view));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 启动队列
     */
    @Override
    public void init() {
        new Thread(() -> {
            while (true) dispatchParam();
        }).start();
        new Thread(() -> {
            while (true) dispatchView();
        }).start();
    }

    @Override
    public void destroy() {
        try {
            contextPool.shutdown();
            contextPool.awaitTermination(30, TimeUnit.MINUTES);
            paramPool.shutdown();
            paramPool.awaitTermination(30, TimeUnit.MINUTES);
            viewPool.shutdown();
            viewPool.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
