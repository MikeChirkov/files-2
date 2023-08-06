import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        String path = "/Users/mikechirkov/Games/savegames/";
        GameProgress gameProgress1 = new GameProgress(100, 15, 1, 115);
        GameProgress gameProgress2 = new GameProgress(1, 0, 100, 777);
        GameProgress gameProgress3 = new GameProgress(50, 150, 2, 200);

        saveGame(gameProgress1, path + "save1.dat");
        saveGame(gameProgress2, path + "save2.dat");
        saveGame(gameProgress3, path + "save3.dat");
        zipFiles(path, new ArrayList<String>() {
            {
                add(path + "save1.dat");
                add(path + "save2.dat");
                add(path + "save3.dat");
            }
        });
    }

    public static void saveGame(GameProgress gp, String path) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void zipFiles(String path, List<String> files) {
        try (FileOutputStream fos = new FileOutputStream(path + "save.zip");
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String filePath :
                    files) {
                String[] strArr = filePath.split("/");
                ZipEntry zipEntry = new ZipEntry(strArr[strArr.length - 1]);
                zos.putNextEntry(zipEntry);

                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zos.write(buffer);
                zos.closeEntry();
                File file = new File(filePath);
                file.delete();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}