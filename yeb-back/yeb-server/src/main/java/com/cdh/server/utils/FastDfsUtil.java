package com.cdh.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
public class FastDfsUtil {

    /**
     * 初始化客户端
     * ClientGlobal.init(path); 读取dfs配置文件并初始化对应属性
     */
    static {
        try {
            String path = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(path);
        } catch (Exception e) {
            log.error("初始化DFS失败->{}",e.getMessage());
        }
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public static String[] upload(MultipartFile file){
        String filename = file.getOriginalFilename();
        log.info("文件名:{}", filename);
        StorageClient storageClient = null;
        String[] upLoadRes = null;
        try {
            storageClient = getStorageClient();
            upLoadRes = storageClient.upload_file(file.getBytes(),filename.substring(filename.lastIndexOf(".")+1),null);
        } catch (Exception e) {
            log.error("上传文件失败->{}",e.getMessage());
        }
        if (null == upLoadRes){
            log.error("上传文件失败->{}",storageClient.getErrorCode());
        }
        return upLoadRes;
    }

    /**
     * 获取文件路径
     * @return
     */
    public static String getTrackerUrl(){
        TrackerClient trackerClient = new TrackerClient();
        StorageServer storageServer = null;
        try {
            TrackerServer trackerServer = getTrackerServer();
            storageServer = trackerClient.getStoreStorage(trackerServer);
        } catch (IOException e) {
            log.error("文件路径获取失败->{}",e.getMessage());
        }
        return "http://"+ Objects.requireNonNull(storageServer).getInetSocketAddress().getHostString()+":8888/";
    }

    /**
     * 获取文件信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo getFileInfo(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            log.error("文件信息获取失败->{}",e.getMessage());
        }
        return  null;
    }

    /**
     * 文件下载
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream download(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            return new ByteArrayInputStream(storageClient.download_file(groupName, remoteFileName));
        } catch (Exception e) {
            log.error("文件下载失败->{}",e.getMessage());
        }
        return  null;
    }

    /**
     * 文件删除
     * @param groupName
     * @param remoteFileName
     */
    public static void removeFile(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
             storageClient.delete_file(groupName, remoteFileName);
        } catch (Exception e) {
            log.error("文件删除失败->{}",e.getMessage());
        }
    }

    /**
     * 获取storage客户端
     * @return
     * @throws IOException
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        return new StorageClient(trackerServer, null);
    }

    /**
     * 生成tracker服务器
     * @return
     * @throws IOException
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        return trackerClient.getConnection();
    }

}
