

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;  
import java.util.ArrayList;
import java.util.List;
import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.FileWriter;  
import java.io.IOException;  
import java.io.PrintStream;  
import java.io.PrintWriter;  
import java.io.RandomAccessFile;  


public class jobscheduler{
//add new names
	static RedBlackTree rbt = new RedBlackTree();
	static MinT pq = new MinT(2);
	static boolean job_running = false;
	static int running_id;
	static int finishtime = 0;
    final static String path = "./output_file.txt";

//output to a txt file
    public static void WriteStringToFile(String filePath, String s) {  
        try {  
            FileWriter fw = new FileWriter(filePath, true);  
            BufferedWriter bw = new BufferedWriter(fw);  
            bw.append(s);  
            bw.close();  
            fw.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
//insert operation: insert to min tree and redblack tree
	public static void Insert(int id, int total_time) {
		pq.insert(id, 0);
		rbt.insert(id, 0, total_time);
		
	}

//print operation: go to redblack tree to find this node with the id
	public static void Print(int id) {
		int res[] = rbt.get(id);
		
		WriteStringToFile(path, "(" + res[0] + "," + res[1] + "," + res[2] + ")" + "\n");
	}
	
//print range: go to redblack tree to find the nodes.	
	public static void Print(int id1, int id2) {
		
		ArrayList<int[]> a = rbt.printRange(id1, id2);
		int i = 0;
		while(i < a.size()){
			int res[] = a.get(i);
			if(i != a.size() - 1){
				WriteStringToFile(path, "(" + res[0] + "," + res[1] + "," + res[2] + ")" + ",");
			}
			if(i == a.size() - 1)
				WriteStringToFile(path, "(" + res[0] + "," + res[1] + "," + res[2] + ")");
			i++;
		}
		a.removeAll(a);
		WriteStringToFile(path, "\n" );
	}

//previous job: go to redblack tree to find the last one of the id	
	public static void PreviousJob(int id) {
		int res[] = rbt.floor(id);
		WriteStringToFile(path, "(" + res[0] + "," + res[1] + "," + res[2] + ")" + "\n");
	}

//next job: go to redblack tree to find the next one of the id	
	public static void NextJob(int id) {
		int res[] = rbt.ceil(id);
		WriteStringToFile(path, "(" + res[0] + "," + res[1] + "," + res[2] + ")" + "\n");
	}
	
//when the min tree becomes empty, the total program will stop	
	public static boolean isEmpty() {
		return pq.isEmpty();
	}
	
//determine if the job should delete the job or reinsert the job
	public void Run(int time) {
		if(job_running == false) return;//if we want to delete or reinsert, the job should not be running
		int tmp[] = rbt.get(running_id);
	//when the current time run to finish time	
		if(finishtime == time){
			job_running = false;
		//when we should delete
			if(tmp[2] == tmp[1] + 1){
				rbt.delete(running_id);	
			}
		//when we should reinsert
			else if(tmp[2] > tmp[1] + 1){
				pq.insert(running_id, tmp[1] + 1);
				rbt.insert(running_id, tmp[1] + 1, tmp[2]);
			}	
		}
	//when the current time has not yet run to finish time
		else if(finishtime > time){
			rbt.insert(running_id, tmp[1] + 1, tmp[2]);
		}
	}
	
//calculate the finish time of the running job	
	public void Dispatch(int time) {
		if(job_running == true || pq.isEmpty() == true) return;
		job_running = true;
		int tmp1[] = pq.delMin();
		running_id = tmp1[1];
		int tmp2[] = rbt.get(running_id);
		if(tmp2[2] - tmp2[1] > 5) finishtime = time + 5;
		else if(tmp2[2] - tmp2[1] <= 5) finishtime = time + tmp2[2] -tmp2[1];
	}
	
//get the input time from the input txt	
	public static int getinputtime(String command) {
		 String order = command.replaceAll("\\(" , ":");
		 order = order.replaceAll(",", ":");
		 order = order.replaceAll("\\)", ":");
		 
		 String order1[] = order.split(":");
		 int inputtime = Integer.parseInt(order1[0]);
		 return inputtime;
	 }
	 
//get the commands from the input txt	
	 public  void cmd(String command) {
    //replace all symbols to ":"
		 String order = command.replaceAll("\\(" , ":");
		 order = order.replaceAll(",", ":");
		 order = order.replaceAll("\\)", ":");

    //get the command(1), job ID(2) and total time(3).			 
		 String order1[] = order.split(":");
		 String inputcommand = order1[1];
		 int inputid = Integer.parseInt(order1[2]);
    //do the command
		
		 switch(inputcommand) {
        //insert
		 case " Insert":
			 int inputtotal_time = Integer.parseInt(order1[3]);
			 Insert(inputid, inputtotal_time);
			 break;
		     
	   //printjob		 
		 case " PrintJob":
			 if (order1.length > 3) {
				 int inputid2 = Integer.parseInt(order1[3]);
				 Print(inputid, inputid2);	
			 }
			 else {	 
				 Print(inputid);
			 }
			 break;
			 
       //nextjob		 
		 case " NextJob":
			 NextJob(inputid);
			 break;
			 
      //previousjob		 
		 case " PreviousJob":
			PreviousJob(inputid);
		     break;
		 }	 		 
    }
		 
		
		
//the main function	 
	
	public static void main(String[] args) throws IOException {
//find the input txt and read it
    	String filePath = "./" + args[0];
    	File file = new File(filePath);  
        BufferedReader command_reader = new BufferedReader(new FileReader(file));  
 	    String command = command_reader.readLine();
 	    int time;
 	   jobscheduler x = new jobscheduler();
 	   
//set the current time and do run function and dispatch to set the finish time
 	   for(time = 0;;time++){
 		//input commands has been finished
 		   if(command == null) {
 			//every job has been finished
 			   if(pq.isEmpty() == true) break; 
 			//commands finished, jobs remain to run
 			   else{
 				  x.Run(time);
 		 	      x.Dispatch(time);
 			   }
 		   }
 		//input commands has not been finished
 		   else{
 	          x.Run(time);
 	    //when current time runs to input time, do the command and set the file handle to the next line
 	          if(getinputtime(command) == time){
 	    	         x.cmd(command);
 	    		     command = command_reader.readLine();//read the next line of input txt
 	         	}
 	          x.Dispatch(time);//set the finishtime
 		   }
 	   }
	}



}
