package cn.suse.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SFTPUtils {

    private Session session;

    private ChannelSftp channelSftp;

    public SFTPUtils(String userName, String password,String ftpHost,int ftpPort) {
        create(userName, password,ftpHost,ftpPort);
    }

    private void create(String userName, String password,String ftpHost,int ftpPort) {
        try {
            JSch jSch = new JSch();
            this.session = jSch.getSession(userName, ftpHost, ftpPort);
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
            session.setConfig(properties);
            session.setPassword(password);
            session.connect(); // 通过Session建立链接
            Channel channel = session.openChannel("sftp"); // 打开SFTP通道
            channel.connect(); // 建立SFTP通道的连接
            this.channelSftp = (ChannelSftp) channel;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try{
            channelSftp.exit();
        }catch (Exception e){
            log.error("close error", e);
        }
        try {

            session.disconnect();
        } catch (Exception e) {
            log.error("disconnect error", e);
        }
    }

    public void uploadFile(String root, String path, String fileName, InputStream in) {
        try {
            String pos = channelSftp.pwd();
            channelSftp.cd(root);
            mkDir(path);
            channelSftp.setFilenameEncoding("UTF-8");
            channelSftp.put(in, fileName, ChannelSftp.APPEND);
            channelSftp.cd(pos);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }finally{
            if(in!=null)
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("close error", e);
                }
        }
    }

    private void mkDir(String dirName) {
        String[] dirs = dirName.split("/");
        try {
            for (int i = 0; i < dirs.length; i++) {
                if (!cd(dirs[i])) {
                    channelSftp.mkdir(dirs[i]);
                    cd(dirs[i]);
                }
            }
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean cd(String dirName) {
        try {
            channelSftp.cd(dirName);
            return true;
        } catch (SftpException e) {
            log.error("cd error", e);
        }
        return false;
    }

    public InputStream downloadFile(String filePath) {
        try {
            return channelSftp.get(filePath);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }
}
