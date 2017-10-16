package work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;


public class ReplaceStr {
	
	public static final String searchText = "10070";//文件中默认的版本号
	public static String svnFpath = "";
	public static String severFpath = "";
	public static String replaceText = "";
	

	public static void main(String[] args) throws Exception {
		
		
		if(json()) {
		File sFile = new File(svnFpath);
		File dFile = new File(severFpath);
		System.out.println("开始执行");
		replaceText = readAppointedLineNumber(sFile, 3);
//		System.out.println("是这里出错了？");
		System.out.println(replaceText);
		do_replace(dFile, searchText, replaceText);
		System.out.println("执行完毕");
		System.out.println("-----------------------------------------------");
		}
		else {
			System.out.println("D盘中的配置文件未找到");
		}
		Updated.update();

	}

	public static boolean json() throws IOException {
		boolean result = false;
		
//		ReplaceStr.class.getClassLoader().getResourceAsStream(
//					"path.json");
		String JsonContext = ReadFile("d:\\path.json");
		JSONArray jsonArray = new JSONArray(JsonContext);
		int size = jsonArray.length();
		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			svnFpath = (String) jsonObject.get("svnFpath");
			severFpath = (String) jsonObject.get("severFpath");
			result = true;
		}
		
		return result;
		
	}
	
	// 读取指定行的字符
	public static String readAppointedLineNumber(File file,int lineNumber) {  
		
        String source = "";
//        System.out.println("是这里出错了？");
        FileReader in = null;  
        LineNumberReader reader = null;  
        try {
			in = new FileReader(file);
			
			reader = new LineNumberReader(in);
//			System.out.println("是这里出错了？");
			source = reader.readLine();
			//        System.out.println("当前行号为:"  
			//                + reader.getLineNumber()); 
			int lines = 0;
			while (source != null) {
				lines++;
				source = reader.readLine();
				if ((lines - lineNumber) == 0) {
					System.out.println("The svn's version is: [" + source
							+ "].");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				in.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}  
	         
		}
        return source;
    }  
	
	// 字符替换
	public static void do_replace(File file,String searchTxt,String replaceText) {
        if (searchTxt.isEmpty())
            return;
        try {
        	BufferedReader fis = new BufferedReader(new FileReader(file));// 创建文件输入流
            char[] data = new char[1024];// 创建缓冲字符数组
            int rn = 0;
            StringBuilder sb = new StringBuilder();// 创建字符串构建器
            while ((rn = fis.read(data)) > 0) {// 读取文件内容到字符串构建器
                String str = String.valueOf(data, 0, rn);
                sb.append(str);
            }
            fis.close();// 关闭输入流
            // 从构建器中生成字符串，并替换搜索文本
            String str = sb.toString().replace(searchTxt, replaceText);
            BufferedWriter fout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));// 创建文件输出流
            fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
            fout.close();// 关闭输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
          
}

	//读配置文件
	public static boolean disposeIni() throws IOException {
			boolean result =false;
			System.out.println("has file");
//			File f = new File(ReplaceStr.class.getResource("path.ini").getFile());
		    File f = new File("path.ini");
		    //Thread.currentThread().getContextClassLoader().getResourceAsStream("D:\\path.ini");
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			String source = "1";
			FileReader in = new FileReader(f);
			LineNumberReader reader = new LineNumberReader(in);
			int lines = 0;
			while (source != null) {
				lines++;
				source = reader.readLine();
				if (lines == 1) {
					if(source.trim() != null) {
						svnFpath = source.trim();
					System.out.println(svnFpath);
					System.out.println("-------------------");
					}
					else {
						try {
							throw new Exception("请填写D盘中配置文件");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if (lines == 2) {
					if(source.trim() != null) {
					severFpath = source.trim();
					System.out.println(severFpath);
					System.out.println("-------------------");
					result = true;
					break;
					}
					else {
						try {
							throw new Exception("请填写D盘中配置文件");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			reader.close();  
		    in.close();  
			return result;
		}
		
	// 读取配置文件
	public static boolean dispose() {
		boolean result = false;
		InputStream input = null;
		Properties p = new Properties();
		// OutputStream output = null;
		try {
			input = ReplaceStr.class.getClassLoader().getResourceAsStream(
					"config.properties");
			if (input == null) {
				System.out.println("Sorry, unable to find config.properties");
				return false;
			}
			p.load(input);
			// output = new FileOutputStream("config.properties");
			svnFpath = p.getProperty("sourceFilePath");
			severFpath = p.getProperty("destFilePath");
			System.out.println(p.getProperty("destFilePath"));
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// sourceFpath = p.getProperty("sourceFpath");
		return result;

	}
	
	public static String ReadFile(String path) throws IOException {

	        File file = new File(path);

	        if(!file.exists()||file.isDirectory()) {
	            throw new FileNotFoundException();
	        }

	        StringBuffer sb = new StringBuffer();
	        try {
	            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
	            @SuppressWarnings("resource")
				BufferedReader bufferReader = new BufferedReader(read);
	            String lineTxt = null;
	            while ((lineTxt = bufferReader.readLine()) != null) {
	                sb.append(lineTxt);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        System.out.println(sb.toString());
	        return sb.toString();
	        
	    }
	
}
