package service;

import http.CustomHttpRequest;
import http.CustomHttpResponse;
import servlet.CustomBaseServlet;

import java.io.IOException;

public class Resource extends CustomBaseServlet {
    @Override
    public void doGet(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        res.setStatus(200);
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory();   // JVM이 확보한 전체 메모리
        long freeMemory = runtime.freeMemory();     // 사용 가능한 메모리
        long usedMemory = totalMemory - freeMemory; // 현재 사용 중인 메모리
        long maxMemory = runtime.maxMemory();       // JVM이 사용 가능한 최대 메모리


        res.write(String.format(
                """
                totalMemory : %d MB
                freeMemory : %d MB
                usedMemory : %d MB
                maxMemory : %d MB 
                        """
                ,totalMemory / 1024 /1024
                ,freeMemory  / 1024 /1024
                ,usedMemory  / 1024 /1024
                ,maxMemory  / 1024 /1024

        ));
    }
}
