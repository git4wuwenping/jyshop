package com.qyy.jyshop.util.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;



/**
 * 该类实现了将一组对象转换为Excel表格，并且可以从Excel表格中读取到一组List对象中
 * 该类利用了BeanUtils框架中的反射完成
 * 使用该类的前提，在相应的实体对象上通过ExcelReources来完成相应的注解
 */
public class ExcelUtil {
    private static ExcelUtil eu = new ExcelUtil();
    private ExcelUtil(){}

    public static ExcelUtil getInstance() {
        return eu;
    }
    /**
     * 处理单元格
     * @param template
     * @param objs
     * @param clz
     * @param isClasspath
     * @return
     */
    private ExcelTemplate handlerCellRangeAddress (String template, List objs, Class clz, boolean isClasspath,int titleColumn)  {
        ExcelTemplate et = ExcelTemplate.getInstance();
        try {
            if(isClasspath) {
                et.readTemplateByClasspath(template);
            } else {
                et.readTemplateByPath(template);
            }
            List<ExcelHeader> headers = getHeaderList(clz);
            Collections.sort(headers);
            
            String combineConProp = null; //哪列相同才合并
            List<Integer> combineIndexs = new ArrayList<>(); //合并列
            for(int i=0;i<headers.size();i++) {
            	ExcelHeader e = headers.get(i);
            	if(e.getIsCombineCondition()) {
            		combineConProp = e.getMethodName();
            	}
            	if(e.getIsCombine()) {
            		combineIndexs.add(i);
            	}
            }
            
            
            if(combineConProp == null) {
            	return et;
            }
            String combineConCl = combineConProp.substring(3);
        	if(!Character.isLowerCase(combineConCl.charAt(0)))
        		combineConCl = (new StringBuilder()).append(Character.toLowerCase(combineConCl.charAt(0))).append(combineConCl.substring(1)).toString();
            //输出标题单元格
            //et.createNewRow(); // 标题不合并
            //第1个column相同则合并 合并列为 1、3列
            //输出值
            int i=titleColumn,j=0,x=1; //i为行 j为列
            String temVal = "";
            for(Object obj:objs) {
            	//j=0;
                //et.createNewRow();
                for(ExcelHeader eh:headers) {
                	
                	String methodName = getMethodName(eh);
                	if(methodName.equals(combineConCl)) {
                		String propVal = BeanUtils.getProperty(obj,getMethodName(eh));
                		if(i==titleColumn) {
                			temVal = propVal;
                		}else {
                			if(temVal.equals(propVal)) {
                				x++;
                			}else {
                				if(x>1) {
                					for(int idx : combineIndexs) {
                						CellRangeAddress callRangeAddress = new CellRangeAddress(i-x,i-1,idx,idx);  ////起始行,结束行,起始列,结束列  
                    					et.addMergedRegion(callRangeAddress);
                					}
                				}
                				x=1;
                				temVal = propVal;
                			}
                			
                		}
                	}
                    //j++;
                }
                i++;
            }
            //最后一条判断合并
            if(x>1) {
				for(int idx : combineIndexs) {
					CellRangeAddress callRangeAddress = new CellRangeAddress(i-x,i-1,idx,idx);  ////起始行,结束行,起始列,结束列  
					et.addMergedRegion(callRangeAddress);
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return et;
    }
    
    /**
     * 处理对象转换为Excel
     * @param template
     * @param objs
     * @param clz
     * @param isClasspath
     * @return
     */
    private void handlerObj2Excel (ExcelTemplate et,String template, List objs, Class clz)  {
        try {
            List<ExcelHeader> headers = getHeaderList(clz);
            Collections.sort(headers);
            //输出标题
            et.createNewRow();
            for(ExcelHeader eh:headers) {
                et.createCell(eh.getTitle());
            }
            //输出值
            for(Object obj:objs) {
                et.createNewRow();
                for(ExcelHeader eh:headers) {
                    //              Method m = clz.getDeclaredMethod(mn);
                    //              Object rel = m.invoke(obj);
                	
                	String methodName = getMethodName(eh);
                    et.createCell(BeanUtils.getProperty(obj,getMethodName(eh)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据标题获取相应的方法名称
     * @param eh
     * @return
     */
    private String getMethodName(ExcelHeader eh) {
        String mn = eh.getMethodName().substring(3);
        mn = mn.substring(0,1).toLowerCase()+mn.substring(1);
        return mn;
    }
    /**
     * 将对象转换为Excel并且导出，该方法是基于模板的导出，导出到流
     * @param response 
     * @param datas 模板中的替换的常量数据
     * @param template 模板路径
     * @param os 输出流
     * @param objs 对象列表
     * @param clz 对象的类型
     * @param isClasspath 模板是否在classPath路径下
     * @param titleColumn 标题行数+1
     * @param xlsType 
     */
    //(map, "web-info-template.xls", new FileOutputStream("D:/temp/out.xls"), list, WebDto.class, true);
    public void exportObj2ExcelByTemplate(HttpServletResponse response, Map<String,String> datas, String template, OutputStream os, List objs, Class clz, boolean isClasspath,int titleColumn, String xlsType) {
        try {
        	ExcelTemplate et = handlerCellRangeAddress(template, objs, clz, isClasspath,titleColumn); //设置格式 合并单元格
            handlerObj2Excel(et,template, objs, clz); //数据填充
            et.replaceFinalData(datas); //填充自定义数据
            // 告诉浏览器用什么软件可以打开此文件
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(xlsType+"导出", "utf-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            et.wirteToStream( outputStream); //输出
            outputStream.flush();
            outputStream.close();
            //os.flush();
            //os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将对象转换为Excel并且导出，该方法是基于模板的导出，导出到一个具体的路径中
     * @param datas 模板中的替换的常量数据
     * @param template 模板路径
     * @param outPath 输出路径
     * @param objs 对象列表
     * @param clz 对象的类型
     * @param isClasspath 模板是否在classPath路径下
     * @param titleColumn 标题行数+1
     */
    public void exportObj2ExcelByTemplate(Map<String,String> datas,String template,String outPath,List objs,Class clz,boolean isClasspath,int titleColumn) {
    	ExcelTemplate et = handlerCellRangeAddress(template, objs, clz, isClasspath,titleColumn);
        handlerObj2Excel(et,template, objs, clz);
        et.replaceFinalData(datas);
        et.writeToFile(outPath);
    }

    /**
     * 将对象转换为Excel并且导出，该方法是基于模板的导出，导出到流,基于Properties作为常量数据
     * @param prop 基于Properties的常量数据模型
     * @param template 模板路径
     * @param os 输出流
     * @param objs 对象列表
     * @param clz 对象的类型
     * @param isClasspath 模板是否在classPath路径下
     * @param titleColumn 标题行数+1
     */
    public void exportObj2ExcelByTemplate(Properties prop, String template, OutputStream os, List objs, Class clz, boolean isClasspath,int titleColumn) {
    	ExcelTemplate et = handlerCellRangeAddress(template, objs, clz, isClasspath,titleColumn);
        handlerObj2Excel(et,template, objs, clz);
        et.replaceFinalData(prop);
        et.wirteToStream(os);
    }
    /**
     * 将对象转换为Excel并且导出，该方法是基于模板的导出，导出到一个具体的路径中,基于Properties作为常量数据
     * @param prop 基于Properties的常量数据模型
     * @param template 模板路径
     * @param outPath 输出路径
     * @param objs 对象列表
     * @param clz 对象的类型
     * @param isClasspath 模板是否在classPath路径下
     * @param titleColumn 标题行数+1
     */
    public void exportObj2ExcelByTemplate(Properties prop,String template,String outPath,List objs,Class clz,boolean isClasspath,int titleColumn) {
    	ExcelTemplate et = handlerCellRangeAddress(template, objs, clz, isClasspath,titleColumn);
        handlerObj2Excel(et,template, objs, clz);
        et.replaceFinalData(prop);
        et.writeToFile(outPath);
    }

    private Workbook handleObj2Excel(List objs, Class clz) {
        Workbook wb = new HSSFWorkbook();
        try {
            Sheet sheet = wb.createSheet();
            Row r = sheet.createRow(0);
            List<ExcelHeader> headers = getHeaderList(clz);
            Collections.sort(headers);
            //写标题
            for(int i=0;i<headers.size();i++) {
                r.createCell(i).setCellValue(headers.get(i).getTitle());
            }
            //写数据
            Object obj = null;
            for(int i=0;i<objs.size();i++) {
                r = sheet.createRow(i+1);
                obj = objs.get(i);
                for(int j=0;j<headers.size();j++) {
                    r.createCell(j).setCellValue(BeanUtils.getProperty(obj, getMethodName(headers.get(j))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }
    /**
     * 导出对象到Excel，不是基于模板的，直接新建一个Excel完成导出，基于路径的导出
     * @param outPath 导出路径
     * @param objs 对象列表
     * @param clz 对象类型
     */
    public void exportObj2Excel(String outPath,List objs,Class clz) {
        Workbook wb = handleObj2Excel(objs, clz);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outPath);
            wb.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos!=null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 导出对象到Excel，不是基于模板的，直接新建一个Excel完成导出，基于流
     * @param os 输出流
     * @param objs 对象列表
     * @param clz 对象类型
     */
    public void exportObj2Excel(OutputStream os,List objs,Class clz) {
        try {
            Workbook wb = handleObj2Excel(objs, clz);
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 从类路径读取相应的Excel文件到对象列表
     * @param path 类路径下的path
     * @param clz 对象类型
     * @param readLine 开始行，注意是标题所在行
     * @param tailLine 底部有多少行，在读入对象时，会减去这些行
     * @return
     */
    public List<Object> readExcel2ObjsByClasspath(String path,Class clz,int readLine,int tailLine) {
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(TemplateFileUtil.getTemplates(path));
            return handlerExcel2Objs(wb, clz, readLine,tailLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 从文件路径读取相应的Excel文件到对象列表
     * @param path 文件路径下的path
     * @param clz 对象类型
     * @param readLine 开始行，注意是标题所在行
     * @param tailLine 底部有多少行，在读入对象时，会减去这些行
     * @return
     */
    public List<Object> readExcel2ObjsByPath(String path,Class clz,int readLine,int tailLine) {
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(TemplateFileUtil.getTemplates(path));
            return handlerExcel2Objs(wb, clz, readLine,tailLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 从类路径读取相应的Excel文件到对象列表，标题行为0，没有尾行
     * @param path 路径
     * @param clz 类型
     * @return 对象列表
     */
    public List<Object> readExcel2ObjsByClasspath(String path,Class clz) {
        return this.readExcel2ObjsByClasspath(path, clz, 0,0);
    }
    /**
     * 从文件路径读取相应的Excel文件到对象列表，标题行为0，没有尾行
     * @param path 路径
     * @param clz 类型
     * @return 对象列表
     */
    public List<Object> readExcel2ObjsByPath(String path,Class clz) {
        return this.readExcel2ObjsByPath(path, clz,0,0);
    }

    private String getCellValue(Cell c) {
        String o = null;
        switch (c.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                o = ""; break;
            case Cell.CELL_TYPE_BOOLEAN:
                o = String.valueOf(c.getBooleanCellValue()); break;
            case Cell.CELL_TYPE_FORMULA:
                o = String.valueOf(c.getCellFormula()); break;
            case Cell.CELL_TYPE_NUMERIC:
                o = String.valueOf(c.getNumericCellValue()); break;
            case Cell.CELL_TYPE_STRING:
                o = c.getStringCellValue(); break;
            default:
                o = null;
                break;
        }
        return o;
    }

    private List<Object> handlerExcel2Objs(Workbook wb,Class clz,int readLine,int tailLine) {
        Sheet sheet = wb.getSheetAt(0);
        List<Object> objs = null;
        try {
            Row row = sheet.getRow(readLine);
            objs = new ArrayList<Object>();
            Map<Integer,String> maps = getHeaderMap(row, clz);
            if(maps==null||maps.size()<=0) throw new RuntimeException("要读取的Excel的格式不正确，检查是否设定了合适的行");
            for(int i=readLine+1;i<=sheet.getLastRowNum()-tailLine;i++) {
                row = sheet.getRow(i);
                Object obj = clz.newInstance();
                for(Cell c:row) {
                    int ci = c.getColumnIndex();
                    String mn = maps.get(ci).substring(3);
                    mn = mn.substring(0,1).toLowerCase()+mn.substring(1);
                    BeanUtils.copyProperty(obj,mn, this.getCellValue(c));
                }
                objs.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs;
    }

    private List<ExcelHeader> getHeaderList(Class clz) {
        List<ExcelHeader> headers = new ArrayList<ExcelHeader>();
        Method[] ms = clz.getDeclaredMethods();
        for(Method m:ms) {
            String mn = m.getName();
            if(mn.startsWith("get")) {
                if(m.isAnnotationPresent(ExcelResources.class)) {
                    ExcelResources er = m.getAnnotation(ExcelResources.class);
                    headers.add(new ExcelHeader(er.title(),er.order(),er.isCombine(),er.isCombineCondition(),mn));
                }
            }
        }
        return headers;
    }

    private Map<Integer,String> getHeaderMap(Row titleRow,Class clz) {
        List<ExcelHeader> headers = getHeaderList(clz);
        Map<Integer,String> maps = new HashMap<Integer, String>();
        for(Cell c:titleRow) {
            String title = c.getStringCellValue();
            for(ExcelHeader eh:headers) {
                if(eh.getTitle().equals(title.trim())) {
                    maps.put(c.getColumnIndex(), eh.getMethodName().replace("get","set"));
                    break;
                }
            }
        }
        return maps;
    }
}