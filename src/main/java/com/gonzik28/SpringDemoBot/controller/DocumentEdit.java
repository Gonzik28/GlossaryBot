package com.gonzik28.SpringDemoBot.controller;

import com.gonzik28.SpringDemoBot.dto.RequestGlossaryDto;
import com.gonzik28.SpringDemoBot.service.GlossaryService;
import com.groupdocs.conversion.Converter;
import com.groupdocs.conversion.filetypes.SpreadsheetFileType;
import com.groupdocs.conversion.options.convert.SpreadsheetConvertOptions;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class DocumentEdit {
    private final static String FILE_EXTENSION_XLS = "xls";
    private final static String FILE_EXTENSION_XLSX = "xlsx";
    private final static String SAVE_DIRECTORY = "src/main/resources/";

    static File loadFile(String botToken, String fileId) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fileId);
        InputStream inputStream = url.openConnection().getInputStream();
        String response = new String(inputStream.readAllBytes());
        inputStream.close();

        JSONObject jsonObject = new JSONObject(response);
        boolean ok = jsonObject.getBoolean("ok");
        if (ok) {
            JSONObject result = jsonObject.getJSONObject("result");
            String filePath = result.getString("file_path");

            String fileName = new File(filePath).getName();
            String fileExtension = FilenameUtils.getExtension(fileName);
            if (fileExtension.equalsIgnoreCase(FILE_EXTENSION_XLS) ||
                    fileExtension.equalsIgnoreCase(FILE_EXTENSION_XLSX)) {
                URL fileUrl = new URL("https://api.telegram.org/file/bot" + botToken + "/" + filePath);
                InputStream fileInputStream = fileUrl.openConnection().getInputStream();

                String saveFilePath = SAVE_DIRECTORY + fileName;
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                fileInputStream.close();
                System.out.println("File saved to " + saveFilePath);
                return new File(saveFilePath);
            } else {
                System.out.println("File type not supported.");
                return null;
            }
        } else {
            System.out.println("Failed to get file information.");
            return null;
        }
    }

    static boolean documentPull(Update update, String token, GlossaryService glossaryService) {
        Document document = update.getMessage().getDocument();
        String name = document.getFileName();
        String extension = FilenameUtils.getExtension(name);
        if (extension.equals("xls") || extension.equals("xlsx")) {
            try {
                File load = DocumentEdit.loadFile(token, document.getFileId());
                Converter converter = new Converter(load.getPath());
                SpreadsheetConvertOptions options = new SpreadsheetConvertOptions();
                options.setFormat(SpreadsheetFileType.Csv);
                converter.convert("src/main/resources/" + name + ".csv", options);
                List<RequestGlossaryDto> requestGlossaryDtoList =
                        ParserDocument.parserCSV("src/main/resources/" + name + ".csv");
                requestGlossaryDtoList.forEach(requestGlossaryDto -> {
                    glossaryService.create(requestGlossaryDto);
                });
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

}
