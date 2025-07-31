package io.github.mjyoun.core.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.constraints.NotNull;

import org.mozilla.universalchardet.UniversalDetector;

/**
 * 파일 관련 유틸
 * 
 * @author MJ Youn
 * @since 2025. 07. 30.
 */
public class FileUtils {

    /**
     * 파일 인코딩 감지
     * 
     * @param filePath
     *            파일 경로
     * @return 감지 결과
     * @throws IOException
     *             파일 없음. 읽기 실패
     * 
     * @author MJ Youn
     * @since 2025. 07. 30.
     */
    public static Charset detectFileEncoding(@NotNull String filePath) throws IOException {
        return FileUtils.detectFileEncoding(Paths.get(filePath));
    }

    /**
     * 파일 인코딩 감지
     * 
     * @param path
     *            파일 경로
     * @return 감지 결과
     * @throws IOException
     *             파일 없음. 읽기 실패
     * 
     * @author MJ Youn
     * @since 2025. 07. 30.
     */
    public static Charset detectFileEncoding(@NotNull Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new FileNotFoundException("파일이 존재하지 않습니다.");
        }

        byte[] buf = new byte[4096];
        UniversalDetector detector = new UniversalDetector(null); // null 대신 CharsetListener를 넣어 감지된 인코딩을 콜백으로 받을 수도 있습니다.

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            int nread;
            while ((nread = bis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
        }
        detector.dataEnd();

        Charset encoding = Charset.forName(detector.getDetectedCharset());
        detector.reset(); // 다음 감지를 위해 리셋

        return encoding;
    }

}
