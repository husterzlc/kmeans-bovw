package t1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File; 
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.util.*;

public class entering{
	
	public static void main(String args[]) throws IOException
	{
	 //初始化参数
	 File dir = new File(args[0]); 
	 int indentifier_length= Integer.parseInt(args[1]);
	 int num_clusters=Integer.parseInt(args[2]);
	 File dir2=new File(args[3]);
	 File files[] = dir.listFiles(); 
	 int length=files.length;
	 int indentifier[]=new int[300];
	 String line=null;
	 ArrayList<int[]> final_features = new ArrayList<int[]>();
	 KMeans k;
	 k = new KMeans(num_clusters);
	 
	 //读入所有特征值
	 for(int i=0;i<length;i++)
	 {
		 FileReader filereader= new FileReader(args[0]+"\\"+files[i].getName());
         BufferedReader bufferreader = new BufferedReader(filereader);
         while((line=bufferreader.readLine())!=null)
         {String features[]=line.split(" ");
          
          for (int j = 0; j < indentifier_length; j++) 
          {   System.out.println(features[j]);
        	  indentifier[j]=Integer.valueOf(features[j]);
          }
          int [] newData;
          newData = Arrays.copyOfRange(indentifier, 0, indentifier_length-1);
          final_features.add(newData);
         }
         bufferreader.close();
         
	 }
	 
	 //开始聚类
	 k.begin(final_features);
	 
	 
	 
	 double laststress = k.clusteringStep(final_features);
	 double newStress = k.clusteringStep(final_features);

   
     double threshold = Math.max(20d, (double) k.getFeatureCount() / 1000d);
     int cstep = 3;
     while (Math.abs(newStress - laststress) > threshold && cstep < 12) {
        
         laststress = newStress;
         newStress = k.clusteringStep(final_features);
         cstep++;
     }
     
     
     //计算缩减后每个文件的特征值并产生新特征文件
	 
     for(int i=0;i<length;i++)
	 {   int [] cluster_count=new int[num_clusters];
	     Arrays.fill(cluster_count, 0);
	     FileReader filereader= new FileReader(args[0]+"\\"+files[i].getName());
         BufferedReader bufferreader = new BufferedReader(filereader);
         while((line=bufferreader.readLine())!=null)
         {String features[]=line.split(" ");
          for (int j = 0; j < indentifier_length; j++) 
          {
        	  indentifier[j]=Integer.valueOf(features[j]);
          }
          int [] newData;
          newData = Arrays.copyOfRange(indentifier, 0, indentifier_length-1);
          int mincluster=0;
          int minsum=0;
          for(int h=0;h<newData.length;h++)
          {
       	   minsum+=Math.abs(newData[h]-k.clusters[0].mean[h]);
          }
          for(int w=1;w<num_clusters;w++)
          {int temp_sum=0;
           for(int h=0;h<newData.length;h++)
           {
        	   temp_sum+=Math.abs(newData[h]-k.clusters[w].mean[h]);
           }
           if(temp_sum<minsum)
           {  mincluster=w;
              minsum=temp_sum;
           }
          }
          cluster_count[mincluster]++;
         }

         File f = new File(dir2,files[i].getName());
         f.createNewFile();
         for(int w=0;w<num_clusters;w++)
         {FileWriter fileWritter = new FileWriter(args[3]+"\\"+f.getName(),true);
          BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
          System.out.println(cluster_count[w]);
          bufferWritter.write(String.valueOf(cluster_count[w]));
          bufferWritter.flush();
          bufferWritter.write(" ");
          bufferWritter.flush();
         }
         bufferreader.close();
         
	 }
	 
	 
	}
	
	
}