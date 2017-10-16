package update.door;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *  整理桌面文件
 * 
 * */
public class Test2 {
	
	
	static String contains = ".rar";
	static String oldPath = "C:\\Users\\sl\\Desktop";
	static String oldPath2 = "C:\\Users\\public\\Desktop";
	static String newPath = "E:\\rar\\";
	public static void main(String[] args) {
		String[] fname = {".rar",".xlsx",".doc",".zip",".png",".json",".py",".lnk",".txt",".exe"};
		String[] fpath = {"E:\\rar\\","E:\\xlsx2017\\","E:\\docx2017\\","E:\\zip2017\\","E:\\png2017\\","E:\\json2017\\","E:\\py2017\\","E:\\lnk2017\\","E:\\txt123\\","E:\\exe2017\\"};
		File baseFile = new File(oldPath);
		File baseFile2 = new File(oldPath2);
		if (baseFile.exists()) {
			for (int i = 0; i < fname.length; i++) {
				moveFile(baseFile,fname[i],fpath[i]);
			}
		} else {
			System.out.println("error");
		}
		if(baseFile2.exists()) {
			moveFile(baseFile2, ".lnk", "E:\\lnk2017\\");
		}else {
			System.out.println("error");

		}
		System.out.println("成功");
		
		
	}
	
	
	//保存目录下的文件名	
	public static List<String> fileNames() {
		String dirname = "C:\\Users\\sl\\Desktop";
		List<String> list = new ArrayList<String>();
		File f = new File(dirname);
		System.out.println("目录: "+ dirname);
		String s[] = f.list();
		//枚举目录
		for (int i = 0; i < s.length; i++) {
			File f2 = new File(dirname +"/" + s);
			if (f2.isDirectory()) {
				System.out.println("目录: "+s[i]);
			} else {
//				System.out.println("文件: "+s[i]);
				list.add(s[i]);
			}
		}
		return list;
	}
	
	//移动文件
	public static void moveFile(File dir,String fname,String fpath) {
		
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			// 获取文件全路径
			String str = fs[i].getAbsolutePath();
			if (str.contains(fname)) {
				File oldFile = new File(str);
				//创建新文件夹
				File fnewFile = new File(fpath);
				if(!fnewFile.exists()) {
					fnewFile.mkdir();
				}
				//新文件的全路径
				File fnew = new File(fpath+oldFile.getName());
				oldFile.renameTo(fnew);
			}
			if(fs[i].isDirectory()) {
				try {
					moveFile(fs[i],fname,fpath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
		
		
	}
	

}
