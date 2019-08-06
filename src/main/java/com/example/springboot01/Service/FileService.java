package com.example.springboot01.Service;

import com.example.springboot01.config.CustomConfig;
import com.example.springboot01.constant.DataBaseEnum.DocType;
import com.example.springboot01.entity.Doc;
import com.example.springboot01.exception.TException;
import com.example.springboot01.response.ResponseEnum;
import com.example.springboot01.utility.TUtil;
import com.example.springboot01.utility.multipart.FileSender;
import com.example.springboot01.utility.multipart.FileUtil;
import com.example.springboot01.utility.multipart.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * @title:  文件传输
 * @author: Yuanbo
 * @date: 2019/8/2  18:01
 */
@Slf4j
@Service
public class FileService {

    @Autowired
    private CustomConfig config;

    /**
     * @description:   上传文件
     * @Author Bob
     * @date 2019/8/2 18:15
     */
    public Doc upload(MultipartFile file, DocType docType) {
        log.debug("Upload a {} file", docType);

        // get upload file name
        String uploadName = file.getOriginalFilename();
        if (uploadName == null) {
            uploadName = Long.toString(System.currentTimeMillis());
        }

        String mimeType = null;
        String md5 = null;
        String ext = null;
        // check doc type, and get mimeType, ext
        if (docType != DocType.OTHER) {
            try {
                mimeType = FileUtil.TIKA.detect(file.getInputStream());
                ext = MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();
                if (ext.isEmpty()
                        || docType == DocType.IMAGE && !FileUtil.IMG_EXT.contains(ext.substring(1))) {
//            || docType == DocType.VIDEO && !FileUtil.VIDEO_EXT.contains(ext.substring(1))) {
                    throw TException.of(ResponseEnum.FILE_FORMAT_ERROR).with("format:" + ext);
                }
            }
            catch (ClientAbortException e) {
                System.out.println("clientAortExecpition.....");
            } catch (IOException | MimeTypeException e) {
                e.printStackTrace();
                throw new RuntimeException("File tika check fail", e);
            }
        } else {
            // just ignore other files
            mimeType = file.getContentType();
            if (mimeType == null) {
                mimeType = MimeTypes.OCTET_STREAM;
            }
            int extIdx = uploadName.lastIndexOf('.');
            ext = extIdx == -1 ? "" : uploadName.substring(extIdx);
        }

        // calculate hash
        try {
            md5 = FileUtil.md5(file);
        } catch (IOException e) {
            throw TException.of(ResponseEnum.UPLOAD_FAIL).with(e);
        }

        // save file
        File localFile = prepareFile(docType, md5 + ext);
        String dbPath = localFile.getPath().substring(typeToPath(docType).length());

        // check if file exist
        if (localFile.exists()) {
            // find db if exist
            // TODO 查询数据库是否存在
//      Doc exist = docMapper.exists(dbPath);
            Doc exist = null;
            if (exist != null) {
                return exist;
            }
        }

        try {
            if (docType == DocType.IMAGE && ext.endsWith(ImageUtil.COMPRESS_FORMAT)
                    && file.getSize() > 102400) {
                // 只有jpg格式能被压缩
                ImageUtil.compress(file.getInputStream(), localFile);
            } else {
                file.transferTo(localFile);
            }
        } catch (IOException e) {
            throw TException.of(ResponseEnum.UPLOAD_FAIL).with(e);
        }

        // save to db and return
        Doc doc = new Doc(TUtil.uuid(), docType, uploadName,
                dbPath, file.getSize(), md5, mimeType, LocalDateTime.now());
        //TODO 存入数据库
//        docMapper.insert(doc);

        log.info("uploaded file: {}", doc);
        return doc;
    }



    /**
     * @description:  将文件创建好，以及创建父文件夹
     * @Author Bob
     * @date 2019/8/2 18:16
     */
    private File prepareFile(DocType type, String filename) {
        String path = typeToPath(type);
        String timePath = YearMonth.now().toString();
        File file = new File(String.format("%s/%s/%s", path, timePath, filename));
        File folder = file.getParentFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return file;
    }

    /**
     * @description:  获取文档类型路径
     * @Author Bob
     * @date 2019/8/2 18:20
     */
    public String typeToPath(DocType type) {
        switch (type) {
            case IMAGE:
                return config.getPathImage();
            case VIDEO:
                return config.getPathVideo();
            default:
                return config.getPathApplication();
        }
    }

    /**
     * @description:  在文件表中找到文件信息
     * @Author Bob
     * @date 2019/8/5 11:11
     */
//    public Doc findfile(String id) {
//        return docMapper.selectById(id);
//    }

    /**
     * @description:  下载文件
     * @Author Bob
     * @date 2019/8/5 11:15
     */
    public void download(String id, Doc file, HttpServletRequest request, HttpServletResponse response) {
        String filePath = typeToPath(file.getKind()) + file.getPath();

        if (file.getKind() == DocType.VIDEO) {
            try {
                FileSender.from(new File(filePath))
                        .with(request).with(response).serveResource();
            } catch (ClientAbortException e) {
                // ignore ClientAbortException
            } catch (FileNotFoundException e) {
                log.debug("FileNotFoundException:{}", id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String disposition = file.getKind() == DocType.IMAGE ? "inline" : "attachment";
            response.setHeader("Content-disposition", String.format(disposition + "; filename=\"%s\"",
                    file.getUploadName()));
            response.setContentType(file.getMimeType());

            int read;
            byte[] bytes = new byte[1024];
            try (InputStream in = new FileInputStream(filePath);
                 OutputStream out = response.getOutputStream()) {
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            } catch (FileNotFoundException e) {
                log.debug("FileNotFoundException:{}", id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
