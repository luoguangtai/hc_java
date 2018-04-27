package com.hc.sys;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import com.hc.util.BeanUtil;
import com.hc.util.DateUtil;
import com.hc.util.NumericUtil;



/**
 * 继承自HttpServletRequestWrapper，用以转码，取代HttpServletRequest
 * @author 陈顺华
 */
public class RequestProxy extends HttpServletRequestWrapper {
    //文本
    private Map<String, String[]> parameters = new HashMap<String, String[]>();
    //文件
    private Map<String, FileItem[]> files = new HashMap<String, FileItem[]>();
    //输入编码
    //private static final String in_encoding = "ISO-8859-1";
    //编码
    private static final String out_encoding = "UTF-8";

    /**
     * 构造HttpRequest
     * @param request
     * @param fileUpload_maxSize 上传文件的最大尺寸，单位M
     * @throws FileUploadException
     * @throws UnsupportedEncodingException
     */
    public RequestProxy(HttpServletRequest request, int fileUpload_maxSize) throws FileUploadException,
            UnsupportedEncodingException {
        super(request);
        //检查输入请求是否为multipart表单数据
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        // 如果是上传文件multipart/form-data
        if (isMultipart) {
            // Create a new file upload handler
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding(out_encoding);
            //设置单个文件的最大值
            upload.setFileSizeMax(fileUpload_maxSize * 1024 * 1024);
            List<?> list = upload.parseRequest(request);

            // 用List暂存再转换到Array
            Map<String, List<String>> formField = new HashMap<String, List<String>>();
            Map<String, List<FileItem>> formFile = new HashMap<String, List<FileItem>>();
            // 循环所有field
            for (int i = 0; i < list.size(); i++) {
                FileItem item = (FileItem) list.get(i);
                String name = item.getFieldName();
                // 文本字段
                if (item.isFormField()) {
                    //先判断Map里是否有List，如果没有则添加一个List
                    List<String> existingValues = formField.get(name);
                    if (existingValues == null) {
                        existingValues = new ArrayList<String>();
                        formField.put(name, existingValues);
                    }
                    //根据配置情况是否对输入内容去除前后空格
                    if(Config.isTrimRequestParameter()){
                        existingValues.add(item.getString(out_encoding).trim());
                    }
                    else{
                        existingValues.add(item.getString(out_encoding));
                    }
                }
                // 文件
                else {
                    List<FileItem> existingValues = formFile.get(name);
                    if (existingValues == null) {
                        existingValues = new ArrayList<FileItem>();
                        formFile.put(name, existingValues);
                    }
                    existingValues.add(item);
                }
            }
            // 转换到数组
            Iterator<Map.Entry<String, List<String>>> iterator_field = formField.entrySet().iterator();
            while (iterator_field.hasNext()) {
                Map.Entry<String, List<String>> entry = iterator_field.next();
                String[] newValue = new String[entry.getValue().size()];
                entry.getValue().toArray(newValue);
                parameters.put(entry.getKey(), newValue);
            }

            Iterator<Map.Entry<String, List<FileItem>>> iterator_file = formFile.entrySet().iterator();
            while (iterator_file.hasNext()) {
                Map.Entry<String, List<FileItem>> entry = iterator_file.next();
                FileItem[] newValue = new FileItem[entry.getValue().size()];
                entry.getValue().toArray(newValue);
                files.put(entry.getKey(), newValue);
            }
        } // 否则读出所有参数后放入HashMap
        else {
            Map map = request.getParameterMap();
            Iterator iterator = map.entrySet().iterator();
            // 取出所有的值转换到指定编码
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String name = (String) entry.getKey();
                // 转码前,Ext2.2之后Ajax提交的数据不需要转码
                String[] oldValue = (String[]) entry.getValue();
                // 转码后
                //String[] newValue = new String[oldValue.length];
                //for (int i = 0; i < oldValue.length; i++) {
                //    newValue[i] = convertCharset(oldValue[i]);
                //}
                //是否去除前后空格
                if(Config.isTrimRequestParameter()){
                    String[] newValue = new String[oldValue.length];
                    for(int i=0; i<oldValue.length; i++){
                        newValue[i] = oldValue[i].trim();
                    }
                    this.parameters.put(name, newValue);
                }
                else{
                    this.parameters.put(name, oldValue);
                }
            }
        }
    }

    /**
     * 把参数转换为Form
     * @param c 需要返回的类型
     * @return 转换后的对象
     */
    public <T> T getForm(Class<T> c) {
        return BeanUtil.map2bean(this.parameters, c);
    }

    /**
     * 把参数转换到一对一的Map返回
     * @return
     */
    public Map<String, String> getParamMap() {
        Map<String, String> m = new HashMap<String, String>();
        Iterator<String> keys = this.parameters.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            m.put(key, getParameter(key));
        }
        return m;
    }

    /**
     * 所有参数
     * 
     * @return
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return this.parameters;
    }

    /**
     * 重写Request的方法,根据字段名取值
     */
    @Override
    public String[] getParameterValues(String name) {
        return parameters.get(name);
    }

    /**
     * 重写Request的方法，根据字段名取值
     */
    @Override
    public String getParameter(String name) {
        String[] value = parameters.get(name);
        // 没有值返回null
        if (value == null || value.length == 0) {
            return null;
        } // 返回第一个
        else {
            return value[0];
        }
    }

    /**
     * 所有文件
     * @return
     */
    public Map<String, FileItem[]> getFilesMap() {
        return this.files;
    }

    /**
     * 根据字段名取文件
     * 
     * @param name
     * @return
     */
    public FileItem[] getFiles(String name) {
        return files.get(name);
    }

    /**
     * 根据字段名取文件
     * 
     * @param name
     * @return
     */
    public FileItem getFile(String name) {
        FileItem[] value = files.get(name);
        // 没有值返回null
        if (value == null || value.length == 0) {
            return null;
        } // 返回第一个
        else {
            return value[0];
        }
    }
    
    /**
     * 以String形式返回指定名称的值，如果对象为空则返回默认值
     * @param name
     * @param defval
     * @return
     */
    public String param(String name, String defval){
        String param = this.getParameter(name);
        if(param==null){
            param = defval;
        }
        return param;
    }

    /**
     * 以int形式返回指定名称的值,如果该name对应的对象为空，则返回默认值
     * @param name 指定名称
     * @param defval 默认值
     * @return
     */
    public int param(String name, int defval) {
        String param = this.getParameter(name);
        return NumericUtil.parseIntger(param, defval);
    }

    /**
     * 以float形式返回指定名称的值,如果该name对应的对象为空，则返回默认值
     * @param name 指定名称
     * @param defval 默认值
     * @return
     */
    public float param(String name, float defval) {
        String param = this.getParameter(name);
        return NumericUtil.parseFloat(param, defval);
    }

    /**
     * 以double形式返回指定名称的值,如果该name对应的对象为空，则返回默认值
     * @param name 指定名称
     * @param defval 默认值
     * @return
     */
    public double param(String name, double defval) {
        String param = this.getParameter(name);
        return NumericUtil.parseDouble(param, defval);
    }

    /**
     * 以boolean形式返回指定名称的值,如果该name对应的对象为空，则返回默认值
     * @param name 指定名称
     * @param defval 默认值
     * @return
     */
    public boolean param(String name, boolean defval) {
        String param = this.getParameter(name);
        boolean value = defval;
        if (StringUtils.isNotBlank(param)) {
            value = Boolean.parseBoolean(param);
        }
        return value;
    }

    /**
     * 以Date形式返回指定名称的值,如果该name对应的对象为空，则返回默认值
     * @param name 指定名称
     * @param defval 默认值
     * @return
     */
    public Date param(String name, Date defval) {
        String param = this.getParameter(name);
        Date value = defval;
        if (StringUtils.isNotBlank(param)) {
            value = DateUtil.parse(param);
        }
        return value;
    }

    /**
     * 转换
     * 
     * @param str
     * @return
     */
//    private String convertCharset(String str) {
//        if (str == null) {
//            return null;
//        }
//        try {
//            return new String(str.getBytes(in_encoding), out_ecoding);
//        } catch (UnsupportedEncodingException e) {
//            return null;
//        }
//    }
}
