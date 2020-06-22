import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetFileTool {
	
	
	

	public static void main(String[] args) throws Exception {
		
		//��srcPathĿ¼�µ������ļ���������Ŀ¼�µ��ļ����ƶ���tarPathĿ¼��
		moveMusic("G:\\voaloid_temp\\NEW", "G:\\voaloid_temp\\NEW");
		
		//ɾ���ظ�����
		//removeSameMusicFile("G:\\voaloid_temp\\NEW");
		
		//moveZip("G:\\voaloid_temp\\NEW", "G:\\voaloid_temp\\NEW");
	}


	/**
	 * ��ԴĿ¼�µ�����zip�ļ��ƶ���Ŀ��Ŀ¼
	 * @param srcPath
	 * @param tarPath
	 * @throws Exception 
	 */
	private static void moveZip(String srcPath, String tarPath) throws Exception {
		List<File> collector = new ArrayList<File>();
		collectAllZipFile(srcPath, collector);
		//�ƶ��ļ�
		moveToDir(collector, tarPath);
		System.out.println("�ƶ����");		
	}


	private static void moveMusic(String srcPath, String tarPath) throws Exception {
		List<File> collector = new ArrayList<File>();
		collectAllFile(srcPath, collector);
		//�ƶ��ļ�
		moveToDir(collector, tarPath);
		System.out.println("�ƶ����");
	}


	/**
	 * ɾ���ظ�����
	 * @param path
	 * @return
	 */
	private static void removeSameMusicFile(String path) {
		List<File> collector = new ArrayList<File>();
		collectAllFile(path, collector);
		removeSameMusic(collector);
	}


	private static void removeSameMusic(List<File> collector) {
		Map<String, List<File>> sameCodeFileMap = new HashMap<String, List<File>>();
		Pattern pattern = Pattern.compile(".*(sm\\d+)");
		for(File file : collector){
			String fileName = file.getName();
			int dotIndex = fileName.indexOf(".");
			if(dotIndex > 0){
				fileName = fileName.substring(dotIndex + 1);
			}
			
			dotIndex = fileName.lastIndexOf(".");
			if(dotIndex > 0){
				fileName = fileName.substring(0, dotIndex);
			}
			
			fileName = fileName.replaceAll(" ", "").replaceAll("-", "");
			
			Matcher matcher = pattern.matcher(fileName);
			//System.out.println(fileName);
			if(matcher.find()){
				String smCode = matcher.group(1);
				
				List<File> sameCodeFileList = sameCodeFileMap.get(smCode);
				if(null == sameCodeFileList){
					sameCodeFileList = new ArrayList<>();
					sameCodeFileMap.put(smCode, sameCodeFileList);
				}
				sameCodeFileList.add(file);
			}
		}
		
		for(List<File> sameCodeFileList : sameCodeFileMap.values()){
			int listSize = sameCodeFileList.size();
			if(listSize < 2){
				continue;
			}
			
			for(int i = 1; i < listSize; ++i){
				File deleteFile = sameCodeFileList.get(i);
				System.out.println("ɾ���ظ�����:" +  deleteFile.getName());
				if(!deleteFile.delete()){
					System.out.println("ɾ���ظ�����ʧ��:" +  deleteFile.getName());
				}
			}
		}
		
		System.out.println("ִ�гɹ�");
	}


	private static void moveToDir(List<File> collector, String moveToDir) throws Exception {
		
		
		for(File file : collector){
			File destDir = new File(moveToDir + "\\"  + file.getName());
			
			if(file.getAbsolutePath().equals(destDir.getAbsolutePath())){
				System.out.println("����Ҫ�ƶ�" + destDir.getAbsolutePath());
				continue;
			}
			
			if(destDir.exists()){
				System.out.println("�ظ�" + destDir.getAbsolutePath());
				continue;
			}
			
			if(!file.renameTo(destDir)){
				throw new Exception("�ƶ�ʧ��" + destDir.getAbsolutePath());
			}
		}
	}




	private static void printAllFileName(List<File> collector) {
		System.out.println(collector.size());
		for(File file : collector){
			System.out.println(file.getName());
		}
	}
	
	
	private static void collectAllZipFile(String path, List<File> collector) {
		File[] fileList = new File(path).listFiles();
		for(File file : fileList){
			if (file.isDirectory()) {
				collectAllFile(file.getAbsolutePath(), collector);
				continue;
			}
			
			String fileName = file.getName().toLowerCase();
			if(!fileName.endsWith(".7z") && !fileName.endsWith(".zip")) {
				continue;
			}
			
			
			collector.add(file);
		}
	}
	
	
	
	
	private static void collectAllFile(String path, List<File> collector) {
		File[] fileList = new File(path).listFiles();
		for(File file : fileList){
			if (file.isDirectory()) {
				collectAllFile(file.getAbsolutePath(), collector);
				continue;
			}
			
			collector.add(file);
		}
	}

}
