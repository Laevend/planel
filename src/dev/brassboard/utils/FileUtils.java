package dev.brassboard.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;

public class FileUtils
{
	private static final LocalDateTime epoch = LocalDateTime.ofInstant(Instant.ofEpochMilli(0),ZoneId.systemDefault());
	
	/**
	 * Creates directories while checking that is has permissions to do so
	 * @param path Path of lowest directory
	 * @return true if directory exists or has been created
	 */
	public static boolean createDirectories(Path path)
	{
		if(Files.isDirectory(path))
		{
			return permissionCheck(path,"rwx");
		}
		else
		{
			if(!recursiveDirectoryPermissionCheck(path.getParent(),"rwx")) { return false; }
		}
		
		try
		{
			Files.createDirectories(path);
			return true;
		}
		catch(Exception e)
		{
			Logs.error("Error occured creating new directory!",e);
		}
		
		return false;
	}
	
	public static boolean createFile(Path path)
	{
		if(Files.exists(path)) { return true; }
		if(!FileUtils.createDirectories(path.getParent())) { return false; }
		
		try
		{
			Files.createFile(path);
			return true;
		}
		catch(Exception e)
		{
			Logs.error("Error occured creating new file!",e);
		}
		
		return false;
	}
	
	public static boolean createFile(long lengthOfFile,Path path)
	{
		if(Files.exists(path)) { return true; }
		if(!FileUtils.createDirectories(path.getParent())) { return false; }
		
		RandomAccessFile f = null;
		
		try
		{
			f = new RandomAccessFile(path.toString(),"rw");
			f.setLength(lengthOfFile);
			f.close();
			return true;
		}
		catch(Exception e)
		{
			Logs.error("Error occured. File of path '" + path.toString() + "' could not be created!",e);
		}
		
		return false;
	}
	
	public static boolean createFile(long lengthOfFile,Path path,LocalDateTime lastModifiedTime,LocalDateTime lastAccessTime,LocalDateTime createTime)
	{
		if(!createFile(lengthOfFile,path)) { return false; }
		
		BasicFileAttributeView attr = Files.getFileAttributeView(path,BasicFileAttributeView.class);
		FileTime lastModTime = FileTime.fromMillis(lastModifiedTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		FileTime lastAccTime = FileTime.fromMillis(lastAccessTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		FileTime creationTime = FileTime.fromMillis(createTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		
		try
		{
			attr.setTimes(lastModTime,lastAccTime,creationTime);
			return true;
		}
		catch(Exception e)
		{
			Logs.error("Error occured. Could not change file times",e);
		}
		
		return false;
	}
	
	/**
	 * If a directory doesn't exist keep going up in the directory structure until a directory
	 * that exists is found so permissions can be checked before creating new directories
	 * @param path Path of a directory
	 * @param permissions Permissions to check @see {@link FileUtils#permissionCheck(Path, String)}
	 * @return True if a directory was found and the JVM has the permissions requested. False if a directory was not found and/or inadequate permissions
	 */
	public static boolean recursiveDirectoryPermissionCheck(Path path,String permissions)
	{
		if(path == null) { return false; }
		
		if(Files.isDirectory(path))
		{
			return permissionCheck(path,permissions);
		}
		else
		{
			return recursiveDirectoryPermissionCheck(path.getParent(),permissions);
		}
	}
	
	/**
	 * Attempts to retrieve the creation date of a file.
	 * 
	 * <p>If this fails, the epoch date (1/1/1970) is returned instead
	 * @param p File path
	 * @return
	 */
	public static LocalDateTime getFileCreationDate(Path p)
	{
		if(!Files.exists(p)) { return epoch; }
		if(!permissionCheck(p,"r")) { return epoch; }
		
		try
		{
			BasicFileAttributes attr = Files.readAttributes(p,BasicFileAttributes.class);
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(attr.creationTime().toMillis()),ZoneId.systemDefault());
		}
		catch(Exception e)
		{
			Logs.error("Error occured retrieving file " + p.getFileName() + " creation date!",e);
			return epoch;
		}	
	}
	
	/**
	 * Attempts to retrieve the last modified date of a file.
	 * 
	 * <p>If this fails, the epoch date (1/1/1970) is returned instead
	 * @param p File path
	 * @return
	 */
	public static LocalDateTime getFileLastModifiedDate(Path p)
	{
		if(!Files.exists(p)) { return epoch; }
		if(!permissionCheck(p,"r")) { return epoch; }
		
		try
		{
			BasicFileAttributes attr = Files.readAttributes(p,BasicFileAttributes.class);
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(attr.lastModifiedTime().toMillis()),ZoneId.systemDefault());
		}
		catch(Exception e)
		{
			Logs.error("Error occured retrieving file " + p.getFileName() + " modified date!",e);
			return epoch;
		}	
	}
	
	/**
	 * Attempts to retrieve the last access date of a file.
	 * 
	 * <p>If this fails, the epoch date (1/1/1970) is returned instead
	 * @param p File path
	 * @return
	 */
	public static LocalDateTime getFileLastAccessDate(Path p)
	{
		if(!Files.exists(p)) { return epoch; }
		if(!permissionCheck(p,"r")) { return epoch; }
		
		try
		{
			BasicFileAttributes attr = Files.readAttributes(p,BasicFileAttributes.class);
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(attr.lastAccessTime().toMillis()),ZoneId.systemDefault());
		}
		catch(Exception e)
		{
			Logs.error("Error occured retrieving file " + p.getFileName() + " last access date!",e);
			return epoch;
		}	
	}
	
	/**
	 * Runs a permission check on a file to see if the JVM has permissions to read, write or execute.
	 * @param p Path to the file/directory
	 * @param permissions The permissions to be checked if granted. Entered as: r or rw or rwx
	 * @return
	 */
	public static boolean permissionCheck(Path p,String permissions)
	{
		boolean hasAllPermissions = true;
		
		for(char perm : permissions.toCharArray())
		{
			switch(perm)
			{
				case 'r':
				{
					if(!Files.isReadable(p)) { Logs.error("Required READ permission has not been granted for file " + p.getFileName()); hasAllPermissions = false; }
					break;
				}
				case 'w':
				{
					if(!Files.isWritable(p)) { Logs.error("Required WRITE permission has not been granted for file " + p.getFileName()); hasAllPermissions = false; }
					break;
				}
				case 'x':
				{
					if(!Files.isExecutable(p)) { Logs.error("Required EXECUTE permission has not been granted for file " + p.getFileName()); hasAllPermissions = false; }
					break;
				}
			}
		}
		
		return hasAllPermissions;
	}
	
	public static void delete(Path path)
	{
		if(!permissionCheck(path,"rw")) { return; }
		
		int files = walkAndCount(path);
		
		if(files == 0) { Logs.error("No Files were found to be deleted."); return; }
		
		deleteFiles(path,files);
	}
	
	public static int walkAndCount(Path path)
	{
		AtomicInteger numOfFiles = new AtomicInteger(0);
		
		try
		{
			Files.walkFileTree(path,new SimpleFileVisitor<Path>()
			{
				@Override
				public FileVisitResult postVisitDirectory(Path dir,IOException exc) throws IOException
				{
					if(!permissionCheck(dir,"r")) { return FileVisitResult.CONTINUE; }
					numOfFiles.incrementAndGet();
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFile(Path file,BasicFileAttributes attrs) throws IOException
				{
					if(!permissionCheck(file,"r")) { return FileVisitResult.CONTINUE; }
					numOfFiles.incrementAndGet();
					return FileVisitResult.CONTINUE;
				}
			});
			
			return numOfFiles.get();
		}
		catch(Exception e)
		{
			Logs.error("Error occured trying to map directory: " + path.toString(),e);
		}
		
		return 0;
	}
	
	private static void deleteFiles(Path path,int numOfFiles)
	{
		try
		{
			Files.walkFileTree(path,new SimpleFileVisitor<Path>()
			{
				@Override
				public FileVisitResult postVisitDirectory(Path dir,IOException exc) throws IOException
				{
					if(!permissionCheck(dir,"rw")) { return FileVisitResult.CONTINUE; }
					
					Files.delete(dir);
					
					if(Files.exists(dir))
					{
						Logs.error("Error occured attempting to delete directory: " + dir.toString());
					}
					
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFile(Path file,BasicFileAttributes attrs) throws IOException
				{
					if(!permissionCheck(file,"rw")) { return FileVisitResult.CONTINUE; }
					
					Files.delete(file);
					
					if(Files.exists(file))
					{
						Logs.error("Error occured attempting to delete file: " + file.toString());
					}
					
					return FileVisitResult.CONTINUE;
				}
			});
		}
		catch(Exception e)
		{
			Logs.error("Error occured attempting to delete: " + path.toString(),e);
		}
	}
	
	public void copyFile(Path src,Path dest)
	{
		try(InputStream is = new FileInputStream(src.toString()); OutputStream os = new FileOutputStream(dest.toString()))
		{
			byte[] buffer = new byte[8192];
			int length;
			
			while((length = is.read(buffer)) > 0)
			{
				os.write(buffer,0,length);
			}
		}
		catch(Exception e)
		{
			Logs.error("Error occured copying file " + src.getFileName() + " from " + src.toString() + " to " + dest.toString(),e);
		}
	}
}
