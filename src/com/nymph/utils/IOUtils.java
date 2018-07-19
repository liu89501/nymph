package com.nymph.utils;

import java.io.InputStream;
import java.util.concurrent.*;

/**
 * IO工具类
 */
public abstract class IOUtils {

    /**
     * 异步读取一行数据, 如果读取的数据没有换行符('\n' int 类型 = 10)将会阻塞超时抛出异常
     * @param in        流
     * @param timeout   超时时间(毫秒)
     * @return
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws ExecutionException
     */
    public static byte[] readLineAsync(InputStream in, long timeout) {
        FutureTask<byte[]> task = new FutureTask<>(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                byte[] bytes = new byte[256];
                int count = 0;
                int read = 0;
                for ( ; read != 10; count++) {
                    read = in.read();
                    bytes[count] = (byte) read;
                    if (count == bytes.length - 1) {
                        byte[] tmp = new byte[bytes.length * 2];
                        System.arraycopy(bytes, 0, tmp, 0, bytes.length);
                        bytes = tmp;
                        tmp = null;
                    }
                }
                byte[] buf = new byte[count + 1];
                System.arraycopy(bytes, 0, buf, 0, buf.length);
                return bytes;
            }
        });
        Thread thread = new Thread(task);
        thread.start();
        byte[] buf = null;
        try {
            buf = task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            thread.stop();
            throw new RuntimeException(e);
        }
        return buf;
    }
}
