package com.cielo.ex3;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import static java.net.HttpURLConnection.*;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

/**
 * Created by 63289 on 2017/5/18.
 */
public class SocketHandler implements Runnable {
    private static final Pattern cgiPattern = Pattern.compile(".*\\.(exe|bat|cmd)$");
    private static final String indexFile = "index.html";
    private static final String lineSeparator = "\r\n";
    private Socket socket;
    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    private File root;
    private String requestMethod;
    private String requestPath;
    private File requestFile;
    private Map<String, String> requestHeaders;
    private StringBuilder response;

    public SocketHandler(Socket socket, File root) {
        this.socket = socket;
        this.root = root;
    }
    //在线程建立后初始化降低阻塞
    private void init() throws IOException {
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = socket.getOutputStream();
        requestHeaders = new HashMap<>();
        //获取HTTP方法和HTTP路径
        String request[] = bufferedReader.readLine().split(" ");
        requestMethod = request[0];
        String requestString[] = request[1].split("\\?", 2);
        requestPath = requestString[0];
        //添加HTTP请求头变量
        String param[] = bufferedReader.readLine().split(":", 2);
        while (param.length == 2) {
            requestHeaders.put(param[0].trim(), param[1].trim());
            param = bufferedReader.readLine().split(":", 2);
        }
        requestFile = new File(root, requestPath);
        if (requestFile.isDirectory()) requestFile = new File(requestFile, indexFile);
        response = new StringBuilder("HTTP/1.0 ");
    }
    private void closeSocket(){
        try {
            outputStream.flush();
            bufferedReader.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void transferFile(File file) throws IOException {
        long contentLength;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            String contentType = Files.probeContentType(Paths.get(file.toURI()));
            contentLength = file.length();
            response.append("200 OK");
            response.append(lineSeparator);
            response.append("Content-Type: ").append(contentType).append(lineSeparator);
            response.append("Content-Length: ").append(contentLength).append(lineSeparator);
            response.append(lineSeparator);
            outputStream.write(response.toString().getBytes());
            int length = fileInputStream.available();
            if (length == 0) length = 16384;
            byte[] bytes = new byte[length];
            fileInputStream.read(bytes);
            outputStream.write(bytes);
        } catch (IOException e) {
            handleException(HTTP_INTERNAL_ERROR);
        }finally {
            closeSocket();
        }
    }
    private void launchCGI(String CGIPath) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(CGIPath);
        try {
            Process cgiProcess = builder.start();
            //若body中出现空行则识别为结束
            if (requestMethod.equals("POST")) {
                String line;
                while (!(line=bufferedReader.readLine()).isEmpty()){
                    cgiProcess.getOutputStream().write(line.getBytes());
                }
                cgiProcess.getOutputStream().close();
            }
            response.append("200 OK");
            response.append(lineSeparator).append(lineSeparator);
            outputStream.write(response.toString().getBytes());
            int length = cgiProcess.getInputStream().available();
            if (length == 0) length = 16384;
            byte[] bytes = new byte[length];
            cgiProcess.getInputStream().read(bytes);
            outputStream.write(bytes);
        } catch (IOException e) {
            handleException(HTTP_BAD_REQUEST);
        }finally {
            closeSocket();
        }
    }
    private void handleException(int errCode) throws IOException {
        switch (errCode) {
            case HTTP_BAD_REQUEST:
                response.append("400 Bad Request");
                break;
            case HTTP_FORBIDDEN:
                response.append("403 Forbidden");
                break;
            case HTTP_NOT_FOUND:
                response.append("404 Not Found");
                break;
            case HTTP_INTERNAL_ERROR:
                response.append("500 Internal Server Error");
        }
        response.append(lineSeparator).append(lineSeparator);
        outputStream.write(response.toString().getBytes());
    }
    @Override
    public void run() {
        try {
            init();
            if (!requestFile.isFile()) handleException(HTTP_NOT_FOUND);
            else if (!requestFile.canRead()) handleException(HTTP_FORBIDDEN);
            else if (cgiPattern.matcher(requestPath).matches()) launchCGI(requestFile.getCanonicalPath());
            else transferFile(requestFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSocket();
        }
    }
}
