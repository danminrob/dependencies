package dependencies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
	
	static Map<String,List<String>> allDependencies;
	static List<String> allPrograms;

	public static void main(String[] args) {
		allPrograms = new ArrayList<String>();
		allDependencies = new TreeMap<String,List<String>>();
		Scanner scanner = new Scanner(System.in).useDelimiter("\\n");;
		
		while(true){
			String line = scanner.next().trim();
			try {
				manageDependency(line);
			}catch(Exception e) {
				System.out.println("Error.");
				e.printStackTrace();
			}
		}
				
	}
	
	public static void manageDependency(String line) throws Exception {
		System.out.println(line);
		
		List<String> args = new ArrayList<>(Arrays.asList(line.split("\\s+")));
		String command = args.get(0);
		args.remove(0);
		
		switch(command) {
			case "DEPEND":
				depend(args);
				break;
			case "INSTALL":
				install(args.get(0));
				break;
			case "REMOVE":
				remove(args.get(0));
				break;
			case "LIST":
				list();
				break;
			case "END":
				end();
				break;
		}
	}
	
	public static void depend(List<String> dependencies) {
		String dependant = dependencies.get(0);
		dependencies.remove(0);
		
		allDependencies.put(dependant, dependencies);
	}
	
	public static void install(String component) {
		List<String> dependencies = allDependencies.get(component);
		if(allPrograms.contains(component)) {
			System.out.println("	"+component+" is already installed.");
		}else {
			if(dependencies!=null && !dependencies.isEmpty()) {
				for(String dependency:dependencies) {
					if(!allPrograms.contains(dependency)) {
						System.out.println("	Installing "+dependency);
						allPrograms.add(dependency);
					}
				}
			}
			System.out.println("	Installing "+component);
			allPrograms.add(component);
		}
	}
	
	public static void list() {
		for(String program : allPrograms) 
			System.out.println("	"+program);			
	}
	
	public static void remove(String component) {
		if(!allPrograms.contains(component)) {
			System.out.println("	"+component+" is not installed.");
		}else {
			findAndRemove(component, true);
			List<String> dependencies = allDependencies.get(component);
			if(dependencies!=null && !dependencies.isEmpty()) {//has dependencies
				for(String dependency: dependencies) {
					findAndRemove(dependency, false);
				}
			}
		}
	}
	
	public static void end() {
		System.exit(0);
	}
	
	public static void findAndRemove(String component, boolean explicit) {
		for (Map.Entry<String,List<String>> dependency : allDependencies.entrySet()){
			if(allPrograms.contains(dependency.getKey())
					&& dependency.getValue().contains(component)){
				if(explicit)
					System.out.println("	"+component+" is still needed.");
				return;
			}
		}
		System.out.println("	Removing "+component);
		allPrograms.remove(component);
	}

}
