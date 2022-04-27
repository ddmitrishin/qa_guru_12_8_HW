package utils;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    ClassLoader classLoader = ZipUtils.class.getClassLoader();

    public PDF usePdfFile(String archiveName, String pdfName) throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/zip/" + archiveName + ".zip"));
        ZipInputStream zs = new ZipInputStream(classLoader.getResourceAsStream("zip/" + archiveName + ".zip"));
        ZipEntry entry;
        while ((entry = zs.getNextEntry()) != null) {
            if (entry.getName().equals(pdfName + ".pdf")) {
                try (InputStream is = zipFile.getInputStream(entry)) {
                    {
                        PDF pdf = new PDF(is);
                        return pdf;
                    }
                }
            }
        }
        throw new Exception("PDF File not found");
    }

    public XLS useXlsFile(String archiveName, String xlsName) throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/zip/" + archiveName + ".zip"));
        ZipInputStream zs = new ZipInputStream(classLoader.getResourceAsStream("zip/" + archiveName + ".zip"));
        ZipEntry entry;
        while ((entry = zs.getNextEntry()) != null) {
            if (entry.getName().equals(xlsName + ".xls")) {
                try (InputStream is = zipFile.getInputStream(entry)) {
                    {
                        XLS xls = new XLS(is);
                        return xls;
                    }
                }
            }
        }
        throw new Exception("XLS File not found");
    }

    public List<String[]> useCsvFile(String archiveName, String csvName) throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/zip/" + archiveName + ".zip"));
        ZipInputStream zs = new ZipInputStream(classLoader.getResourceAsStream("zip/" + archiveName + ".zip"));
        ZipEntry entry;
        while ((entry = zs.getNextEntry()) != null) {
            if (entry.getName().equals(csvName + ".csv")) {
                try (InputStream is = zipFile.getInputStream(entry);
                     CSVReader csvReader = new CSVReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    {
                        List<String[]> content = csvReader.readAll();
                        return content;
                    }
                }
            }
        }
        throw new Exception("CSV File not found");
    }

    public JsonObject useJsonFile(String jsonName) throws Exception {
        Gson gson = new Gson();
        try (InputStream stream = classLoader.getResourceAsStream("json/" + jsonName + ".json")) {
            byte[] fileContent = stream.readAllBytes();
            String strContent = new String(fileContent, StandardCharsets.UTF_8);
            JsonObject jsonObject = gson.fromJson(strContent, JsonObject.class);
            return jsonObject;
        }
    }
}
