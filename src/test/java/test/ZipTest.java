package test;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import utils.ZipUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class ZipTest {

    ZipUtils zu = new ZipUtils();
    String archiveName = "HW8";
    String pdfName = "sample";
    String xlsName = "file_example_XLS_10";
    String csvName = "addresses";
    String jsonName = "sample2";

    @Test
    void selenideDownloadTest() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedFile = $("#raw-url").download();
        try (InputStream is = new FileInputStream(downloadedFile)) {
            byte[] fileContent = is.readAllBytes();
            String contentText = new String(fileContent, StandardCharsets.UTF_8);
            assertThat(contentText).contains("Contributions to JUnit 5 are both welcomed and appreciated");
        }
    }

    @Test
    void pdfFileTest() throws Exception {

        PDF pdfFile = zu.usePdfFile(archiveName, pdfName);

        assertThat(pdfFile.text).contains("zzz");
    }

    @Test
    void xlsFileTest() throws Exception {

        XLS xlsFile = zu.useXlsFile(archiveName, xlsName);
        String xlsCellValue = xlsFile.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();

        assertThat(xlsCellValue).contains("Dulce");

    }

    @Test
    void csvFileTest() throws Exception {

        List<String[]> csvContent = zu.useCsvFile(archiveName, csvName);

        assertThat(csvContent).contains(
                new String[]{"John", "Doe", "120 jefferson st.", "Riverside", " NJ", " 08075"},
                new String[]{"Jack", "McGinnis", "220 hobo Av.", "Phila", " PA", "09119"}
        );
    }

    @Test
    void jsonFileTest() throws Exception {

        JsonObject jsonOFile = zu.useJsonFile(jsonName);

        assertThat(jsonOFile.get("firstName").getAsString()).isEqualTo("Joe");
        assertThat(jsonOFile.get("address").getAsJsonObject().get("city").getAsString())
                .isEqualTo("San Diego");
    }
}
