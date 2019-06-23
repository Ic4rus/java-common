package com.icarus.jc.file.pdf;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class JasperReportsUtils {

	public static void exportPDF() {
		
		try {
			InputStream template = new FileInputStream("C:\\Users\\thanght1\\Desktop\\certificate.jasper");
			Map<String, Object> parameter = new HashMap<>();
			parameter.put("STUDENT_NAME", "Nguyen Hai Duy 2");
			parameter.put("STUDENT_ID", "FUNZI ID: UMGHS2X9");
			parameter.put("COURSE_NAME", "Hoc lay vo giau");
			parameter.put("FINISHED_DATE", "on May 8, 2019");
//			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList<Test>());
			JRDataSource dataSource = new JREmptyDataSource();
			JasperPrint print = JasperFillManager.fillReport(template, parameter, dataSource);
			JasperExportManager.exportReportToPdfFile(print, "D:\\1.pdf");
			System.out.println("DONE!");
			
			OutputStream ouputStream= new FileOutputStream("D:\\212.png");     
	        DefaultJasperReportsContext.getInstance();   
	        JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());      
	 
	        BufferedImage rendered_image = null;      
	        rendered_image = (BufferedImage)printManager.printPageToImage(print, 0,1.6f); 
	        ImageIO.write(rendered_image, "png", ouputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		
		exportPDF();
	}
	
	class Test {
		
	}
}
