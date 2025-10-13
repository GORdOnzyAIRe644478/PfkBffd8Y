// 代码生成时间: 2025-10-13 23:13:51
package com.example.compression;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * CompressionTool is a Quarkus application for data compression and decompression.
 */
@QuarkusMain
public class CompressionTool implements QuarkusApplication {

    @Override
    public int run(String... args) {
        if (args.length != 2) {
            System.out.println("Usage: <command> <data>" +
                    "where <command> is either 'compress' or 'decompress'" +
                    "and <data> is the string data to be processed.");
            return 1;
        }
        
        String command = args[0];
        String data = args[1];
        
        try {
            switch (command) {
                case "compress":
                    return compressData(data) ? 0 : 2;
                case "decompress":
                    return decompressData(data) ? 0 : 2;
                default:
                    System.out.println("Invalid command. Use 'compress' or 'decompress'.");
                    return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Compresses the given data using GZIP algorithm.
     *
     * @param data The data to be compressed.
     * @return true if compression was successful, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    private boolean compressData(String data) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzipOS = new GZIPOutputStream(baos)) {
            gzipOS.write(data.getBytes());
            String compressedData = baos.toString("UTF-8");
            System.out.println("Compressed data: " + compressedData);
            return true;
        }
    }

    /**
     * Decompresses the given data using GZIP algorithm.
     *
     * @param data The data to be decompressed.
     * @return true if decompression was successful, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    private boolean decompressData(String data) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes("UTF-8"));
             GZIPInputStream gzipIS = new GZIPInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipIS.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            String decompressedData = baos.toString("UTF-8");
            System.out.println("Decompressed data: " + decompressedData);
            return true;
        }
    }

    /**
     * Main method to start the Quarkus application.
     *
     * @param args The command line arguments.
     */
    public static void main(String... args) {
        Optional<QuarkusApplication> quarkusApp = QuarkusApplication.run(CompressionTool.class, args);
        quarkusApp.ifPresent(QuarkusApplication::run);
    }
}
