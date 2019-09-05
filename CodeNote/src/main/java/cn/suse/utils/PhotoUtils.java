package cn.suse.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JLabel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.gif.GifMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.png.PngMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;

import lombok.extern.slf4j.Slf4j;

/**
 * 类PhotoUtils.java的实现描述：图片处理公共类
 */
@Slf4j
public class PhotoUtils {

	/**
	 * 获取照片的旋转度数 根据十六进制内容判断文件类型： 1.JPEG/JPG - 文件头标识 (2 bytes): $ff, $d8 (SOI)
	 * (JPEG 文件标识) - 文件结束标识 (2 bytes): $ff, $d9 (EOI) 2.PNG - 文件头标识 (8 bytes) 89
	 * 50 4E 47 0D 0A 1A 0A 3.GIF - 文件头标识 (6 bytes) 47 49 46 38 39(37) 61 G I F
	 * 8 9 (7) a 4.BMP - 文件头标识 (2 bytes) 42 4D B M
	 * 
	 * 常用文件的文件头如下：(以前六位为准) JPEG (jpg)，文件头：FFD8FF PNG (png)，文件头：89504E47 GIF
	 * (gif)，文件头：47494638
	 * 
	 * 
	 * 此方法貌似获取不到图片的旋转信息！！！！！！怀疑是jar版本问题，有空再试试
	 * @param inputStream
	 * @return
	 */
	@Deprecated
	public static Integer getPhotoOrientation(BufferedInputStream inputStream) {
		int angle = 0;
		try {
			inputStream.mark(0);
			byte[] b = new byte[3];
			inputStream.read(b, 0, b.length);
			inputStream.reset();// 游标复原,InputStream 和 FileInputStream不支持游标复原操作。
			String flag = bytesToHexString(b);
			flag = flag.toUpperCase();
			Metadata metadata = null;
			switch (flag) {
				case "FFD8FF":
					metadata = JpegMetadataReader.readMetadata(inputStream);
					break;
				case "89504E":
					metadata = PngMetadataReader.readMetadata(inputStream);
					break;
				case "474946":
					metadata = GifMetadataReader.readMetadata(inputStream);
					break;
				default:
					metadata = ImageMetadataReader.readMetadata(inputStream);
			}

			for (Directory directory : metadata.getDirectories()) {
				for (Tag tag : directory.getTags()) {
					if (tag.getTagType() == ExifDirectoryBase.TAG_ORIENTATION) {
						String description = tag.getDescription();
						System.out.println(description);
						if (description.contains("90")) {
							// 顺时针旋转90度
							angle = 90;
						} else if (description.contains("180")) {
							// 顺时针旋转180度
							angle = 180;
						} else if (description.contains("270")) {
							// 顺时针旋转270度
							angle = 270;
						}
					}
				}
			}
		} catch (Exception e) {
			log.warn("获取照片旋转度数异常：" + e.getMessage(), e);
		}
		return angle;
	}
	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
     * 在现有基础上顺时针旋转指定的度数
     *
     * @param src   被旋转图片
     * @param angle 旋转角度
     * @return 旋转后的图片
     */
    public static BufferedImage setPhotoOrientation(BufferedImage src, int angle) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // 计算旋转后图片的尺寸
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
                src_width, src_height)), angle);
        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // 进行转换
        g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angle), src_width / 2, src_height / 2);
 
        g2.drawImage(src, null, null);
        return res;
    }
 
    /**
     * 计算旋转后的图片
     *
     * @param src   被旋转的图片
     * @param angel 旋转角度
     * @return 旋转后的图片
     */
    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        // 如果旋转的角度大于90度做相应的转换
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
 
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);
 
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }
	/**
	 * pdf转图片
	 */
	public static BufferedImage pdf2Image(InputStream inputStream) {
		// 图像合并使用参数
		int dpi = 144;// 默认300dpi,dpi越大转换后越清晰，相应的图片文件越大，相对转换速度越慢
		int width = 0; // 总宽度
		int[] singleImgRGB; // 保存一张图片中的RGB数据
		int shiftHeight = 0;
		BufferedImage imageResult = null;// 保存每张图片的像素值
		PDDocument pdDocument = null;
		try {
			// 利用PdfBox生成图像
			pdDocument = PDDocument.load(inputStream);
			// PDFTextStripper text = new PDFTextStripper();
			// String text1 = text.getText(pdDocument);
			// log.info("PhotoUtils.pdf2Image 读取PDF文字:" + text1);
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			// 循环每个页码
			for (int i = 0, len = pdDocument.getNumberOfPages(); i < len; i++) {
				// dpi参数越大，越清晰
				BufferedImage image = renderer.renderImageWithDPI(i, dpi);
				int imageHeight = image.getHeight();
				int imageWidth = image.getWidth();
				if (i == 0) {// 计算高度和偏移量
					width = imageWidth;// 使用第一张图片宽度;
					// 保存每页图片的像素值
					imageResult = new BufferedImage(width, imageHeight * len, BufferedImage.TYPE_INT_RGB);
				} else {
					shiftHeight += imageHeight; // 计算偏移高度
				}
				singleImgRGB = image.getRGB(0, 0, width, imageHeight, null, 0, width);
				imageResult.setRGB(0, shiftHeight, width, imageHeight, singleImgRGB, 0, width); // 写入流中
			}
		} catch (Exception ex) {
			log.error("PhotoUtils.pdf2Image 从PDF转换JPG格式异常!", ex);
		} finally {
			if (null != pdDocument) {
				try {
					pdDocument.close();
				} catch (IOException ex) {
					log.error("PhotoUtils.pdf2Image 从PDF转换JPG格式释放资源异常!", ex);
				}
			}
		}
		return imageResult;
	}

	/**
	 * 图片添加水印
	 */
	public static void addWaterMark(BufferedImage image, String waterMark) {
		Font font = new Font("宋体", Font.BOLD, 24);// 水印字体，大小
		Color markContentColor = Color.lightGray;// 水印颜色
		Integer degree = 45;// 设置水印文字的旋转角度
		float alpha = 0.6f;// 设置水印透明度 默认为1.0 值越小颜色越浅
		Graphics2D g = null;
		try {
			g = image.createGraphics();// 得到画笔
			g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
			g.setColor(markContentColor); // 设置水印颜色
			g.setFont(font); // 设置字体
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));// 设置水印文字透明度
			g.rotate(Math.toRadians(degree));// 设置水印旋转
			JLabel label = new JLabel(waterMark);
			FontMetrics metrics = label.getFontMetrics(font);
			int width = metrics.stringWidth(label.getText());// 文字水印的宽
			int rowsNumber = image.getHeight() / width + image.getHeight() % width;// 图片的高/文字水印的宽=打印的行数(以文字水印的宽为间隔)
			int columnsNumber = image.getWidth() / width + image.getWidth() % width;// 图片的宽/文字水印的宽=每行打印的列数(以文字水印的宽为间隔)
			// 防止图片太小而文字水印太长，所以至少打印一次
			if (rowsNumber < 1) {
				rowsNumber = 1;
			}
			if (columnsNumber < 1) {
				columnsNumber = 1;
			}
			for (int j = 0; j < rowsNumber; j++) {
				for (int i = 0; i < columnsNumber; i++) {
					g.drawString(waterMark, i * width + j * width, -i * width + j * width);// 画出水印,并设置水印位置
				}
			}
		} catch (Exception ex) {
			log.error("PhotoUtils.addWaterMark 图片添加水印异常!", ex);
		} finally {
			if (g != null) {
				g.dispose();// 释放资源
			}
		}
	}

	public static void main(String[] args) throws Exception {
		/*File f = new File("D:/666.png");
		BufferedImage image = ImageIO.read(f);
		addWaterMark(image, "添加个水印");
		image = setPhotoOrientation(image, 90);
		
		ImageIO.write(image, "png", f);*/
		
		/*InputStream in = new FileInputStream(new File("D:/333.pdf"));
		BufferedImage pdf2Image = pdf2Image(in);
		ImageIO.write(pdf2Image, "jpg", new File("D:/333.jpg"));*/
		
		
		File f2 = new File("D:/666.png");
		Integer photoOrientation = getPhotoOrientation(new BufferedInputStream(new FileInputStream(f2)));
		System.out.println(photoOrientation);
	}
}