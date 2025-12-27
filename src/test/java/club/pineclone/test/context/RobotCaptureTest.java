package club.pineclone.test.context;

import junit.framework.TestCase;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RobotCaptureTest extends TestCase {

    public void testCapture() throws Exception {
        Robot robot = new Robot();
        // 动态设置你要截图的区域，或通过窗口获取Rectangle
        Rectangle rect = new Rectangle(20, 100, 250, 200);
        BufferedImage screenshot = robot.createScreenCapture(rect);

        System.out.println("截图宽度: " + screenshot.getWidth());
        System.out.println("截图高度: " + screenshot.getHeight());

        // --- 2. 初始化 Tesseract OCR ---
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata"); // tessdata 路径
        tesseract.setLanguage("chi_sim");                     // 中文简体
        tesseract.setPageSegMode(6);                          // PSM 7: 单行文本
        tesseract.setOcrEngineMode(1);                        // LSTM 引擎

        long start = System.currentTimeMillis();
        String result = tesseract.doOCR(screenshot);
        long elapsed = System.currentTimeMillis() - start;

        System.out.println("识别结束,耗时：" + elapsed + "ms");
        System.out.println("识别结果：\n" + result);


    }

}
