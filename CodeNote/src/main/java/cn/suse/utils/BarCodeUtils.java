package cn.suse.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BarCodeUtils {

    private static final String CHARACTER_SET    = "UTF-8";

    private static final String IMG_FORMAT = "png";
    
    private static final String IMG_PNG_FORMAT = "image/png";
	/**
	 * 生成一个二维码
	 * @param content 内容
	 * @param filePath 文件内容
	 * @throws WriterException
	 * @throws IOException
	 */
    public static void genQRCode(String content, String filePath) throws WriterException, IOException {
        int width = 200;
        int height = 200;
        // 生成编码
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, CHARACTER_SET);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵  
        // 输出图像  
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, IMG_FORMAT, path);
    }
    /**
     * 给二维码添加logo
     * @param matrixImage 源二维码图片
     * @param logoFile logo图片
     * @return 返回带有logo的二维码图片
     * @throws IOException
     */
    private static BufferedImage LogoMatrix(BufferedImage matrixImage, File logoFile) throws IOException{
        /**
         * 读取二维码图片，并构建绘图对象
         */
        Graphics2D g2 = matrixImage.createGraphics();

        int matrixWidth = matrixImage.getWidth();
        int matrixHeigh = matrixImage.getHeight();

        /**
         * 读取Logo图片
         */
        BufferedImage logo = ImageIO.read(logoFile);

        //开始绘制图片
        g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制
        BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke);// 设置笔画对象
        //指定弧度的圆角矩形
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);
        g2.setColor(Color.white);
        g2.draw(round);// 绘制圆弧矩形s

        //设置logo 有一道灰色边框
        BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke2);// 设置笔画对象
        RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+2, matrixHeigh/5*2+2, matrixWidth/5-4, matrixHeigh/5-4,20,20);
        g2.setColor(new Color(128,128,128));
        g2.draw(round2);// 绘制圆弧矩形

        g2.dispose();
        matrixImage.flush() ;
        return matrixImage ;
    }
    /**
     * 生成带logo的二维码图片
     * @param content
     * @param filePath
     * @throws WriterException
     * @throws IOException
     */
    public static void genQRCodeWithLogo(String content, String filePath, String logoFilePath) throws WriterException, IOException {
        int width = 200; // 图像宽度
        int height = 200; // 图像高度
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //内容编码格式
        hints.put(EncodeHintType.CHARACTER_SET, CHARACTER_SET);
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置二维码边的空度，非负数
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToPath(bitMatrix, IMG_FORMAT, new File(filePath).toPath());// 输出原图片
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
        /*
	            问题：生成二维码正常,生成带logo的二维码logo变成黑白
	            原因：MatrixToImageConfig默认黑白，需要设置BLACK、WHITE
	            解决：https://ququjioulai.iteye.com/blog/2254382
         */
        BufferedImage bufferedImage = LogoMatrix(MatrixToImageWriter.toBufferedImage(bitMatrix,matrixToImageConfig), new File(logoFilePath));
//        BufferedImage bufferedImage = LogoMatrix(toBufferedImage(bitMatrix), new File("D:\\创建带logo的二维码2.png"));
        ImageIO.write(bufferedImage, IMG_FORMAT, new File("D:\\创建带logo的二维码2.png"));//输出带logo图片
    }
    /**
     * 生成一个条形码
     * @param content
     * @param filePath
     * @throws WriterException
     * @throws IOException
     */
    public static void genBarCode(String content, String filePath) throws WriterException, IOException {
        genBarCode(content, filePath, BarcodeFormat.CODABAR);
    }
    private static void genBarCode(String content, String filePath, BarcodeFormat barCodeFormat)
            throws WriterException, IOException {
        int width = 300;
        int height = 120;
        // 生成编码
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, CHARACTER_SET);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, barCodeFormat, width, height, hints);// 生成矩阵  
        // 输出图像 
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, IMG_FORMAT, path);
    }
    /**
     * 生成一个带数字的条形码
     * @param content
     * @param filePath
     */
    public static void genBarCodeWithNumber(String content, String filePath) {
        // 精细度
        final int dpi = 150;
        // module宽度
        final double moduleWidth = UnitConv.in2mm(1.0f / dpi);
        // 配置对象
        Code39Bean bean = new Code39Bean();
        bean.setModuleWidth(moduleWidth);
        //bean.setWideFactor(3);
        //bean.doQuietZone(false);
        try {
            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(new FileOutputStream(filePath), 
            		IMG_PNG_FORMAT, dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            // 绘制图像  
            bean.generateBarcode(canvas, content);
            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 获取二维码或者条形码中的内容
     * @param filePath
     * @return
     */
    public static String getCodeInfo(String filePath) {
        Result result = null;
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, CHARACTER_SET);

            result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码  
        } catch (NotFoundException e) {
            log.warn("BarCodeUtils.getCodeInfo异常", e);
        } catch (IOException e) {
            log.warn("BarCodeUtils.getCodeInfo异常", e);
        }

        if (result != null){
        	return result.getText();
        }
        return null;
    }

    public static void main(String[] args) throws Exception, IOException {
    	
    	String filePath = "D://创建二维码.png";
        JSONObject json = new JSONObject();
        json.put("url", "https://cli.im/text?d683bc5d5796ad2e932494dd696b7422");
        json.put("from", "mobile");
        String content = json.toJSONString();
        genQRCode(content, filePath);
        
        filePath = "D://创建带logo的二维码.png";
        content = "看到logo了没";
        genQRCodeWithLogo(content, filePath, "D://ant.jpg");
        
        filePath = "D://创建条形码.png";
        content = "18381335898";
        genBarCode(content, filePath);
        
        filePath = "D://创建带数字的条形码.png";
        content = "18381335898";
        genBarCodeWithNumber(content, filePath);

    	filePath = "D://二维码.png";
    	content = getCodeInfo(filePath);
        log.info(content);
    }
}
