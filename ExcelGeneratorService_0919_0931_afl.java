// 代码生成时间: 2025-09-19 09:31:04
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelGeneratorService is a service class that uses Apache POI to create Excel files.
 * It provides functionality to generate an Excel file with a specified number of sheets and rows.
 */
@QuarkusMain
public class ExcelGeneratorService implements QuarkusApplication {

    // Injecting the application's root path
    private final Path rootPath;

    public ExcelGeneratorService(Path rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public int run(String... args) throws Exception {
        // Generate an Excel file with default settings
        generateExcelFile("MyExcelFile.xlsx", 5, 10);
        return 0;
    }

    /**
     * Generates an Excel file with the specified number of sheets and rows.
     *
     * @param fileName The name of the Excel file to be generated.
     * @param numberOfSheets The number of sheets in the Excel file.
     * @param numberOfRowsPerSheet The number of rows in each sheet.
     * @throws IOException If there is an I/O error while creating the file.
     */
    public void generateExcelFile(String fileName, int numberOfSheets, int numberOfRowsPerSheet) throws IOException {
        // Using XSSFWorkbook for Excel 2007+ (.xlsx) format
        Workbook workbook = new XSSFWorkbook();

        // Create a new sheet for each specified number of sheets
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.createSheet("Sheet" + (i + 1));

            // Create rows and cells for each sheet
            for (int j = 0; j < numberOfRowsPerSheet; j++) {
                Row row = sheet.createRow(j);
                for (int k = 0; k < 5; k++) { // Assuming 5 columns
                    Cell cell = row.createCell(k);
                    cell.setCellValue("Data" + (k + 1) + (j + 1)); // Sample data
                }
            }
        }

        // Write the workbook content to an output file
        try (FileOutputStream outputStream = new FileOutputStream(rootPath.resolve(fileName).toFile())) {
            workbook.write(outputStream);
            workbook.close();
            System.out.println("Excel file generated successfully: " + fileName);
        } catch (IOException e) {
            System.err.println("Error occurred while writing the Excel file.");
            throw e;
        }
    }

    /**
     * Main method for testing purposes.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Create an instance of the ExcelGeneratorService and run it
        new ExcelGeneratorService(Path.of(".")).run(args);
    }
}
