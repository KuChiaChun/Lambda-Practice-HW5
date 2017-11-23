import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
public class InvoiceManagementSystem {
	static String choice;
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		boolean Enter = true;
		boolean loop = true;
		//initialize array of invoices
		Invoice[] invoice = {
				new Invoice(83,"Electric sander",7,57.98),
				new Invoice(24,"Power saw",18,99.99),
				new Invoice(7,"Sledge hammer",11,21.50),
				new Invoice(77,"Hammer",76,11.99),
				new Invoice(39,"Lawn mower",3,79.50),
				new Invoice(68,"Screwdriver",106,6.99),
				new Invoice(56,"Jig saw",21,11.00),
				new Invoice(3,"Wrench",34,7.50),
				new Invoice(45,"Wrench",13,7.50),
				new Invoice(22,"Hammer",47,11.99)
		};
		do {
			if(Enter) {
				System.out.println("Welcome to invoices management system.");
				System.out.println("Functions: Sort/Report/Select");
				System.out.print("Choice(-1 to exit):");
				choice = s.nextLine();
			}
			else {
				//使用者不是輸入 正確功能時要求使用者重新輸入
				System.out.print("Wrong input. Enter again:");
				choice = s.nextLine();
			}
			//get list view of the invoices
			List<Invoice> list = Arrays.asList(invoice);
			switch(choice.toLowerCase()) {
				case "sort":
					System.out.print("Order by ID/Quantity/Price?");
					String Order = s.nextLine(); 
					switch (Order.toLowerCase()) {
						case "price" :		
							System.out.println("Invoices sorted by price:\n-------------------------------");
							list.stream()
							.sorted(Comparator.comparing(Invoice::getPrice))//sorted by price
							.forEach(System.out::println);
							Enter = true;
							System.out.print("-------------------------------\n\n");
							break;
		
						case "quantity" :
							System.out.println("Invoices sorted by quantity:\n-------------------------------");
							list.stream()
							.sorted(Comparator.comparing(Invoice::getQuantity))//sorted by quantity
							.forEach(System.out::println);
							Enter = true;
							System.out.print("-------------------------------\n\n");
							break;
						case "id" :
							System.out.println("Invoices sorted by ID:\n-------------------------------");
							list.stream()
							.sorted(Comparator.comparing(Invoice::getPartNumber))//sorted by id
							.forEach(System.out::println);
							Enter = true;
							System.out.print("-------------------------------\n\n");
							break;
						default:
							System.out.println("Invoices sorted by "+Order+"\n:-------------------------------");
							list.stream()
							.sorted(Comparator.comparing(Invoice::getPartNumber))//defult : sorted by id
							.forEach(System.out::println);
							Enter = true;
							System.out.print("-------------------------------\n\n");
					}
					break;
				case "report":
					System.out.println("Invoices group by description:\n-------------------------------");
					list.stream().forEach(e->e.setTotalprice(e.getPrice()*e.getQuantity()));//計算各商品金額
					//將各個商品的總金額計算出來   
					Map<String, Double> invoiceGroupByDescription =
							list.stream()
							.collect(Collectors.groupingBy(Invoice::getPartDescription,Collectors.summingDouble(Invoice::getTotalprice)));
					invoiceGroupByDescription.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e->System.out.printf("Description: %-20sInvoice amount:%.2f\n",e.getKey(),e.getValue()));
//					sortset.stream().forEach(e->System.out.printf("Description: %-20sInvoice amount:%.2f\n",e.getKey(),e.getValue()));
//					List<Map.Entry<String,Double>> sortlist = new ArrayList<Map.Entry<String,Double>>(invoiceGroupByDescription.entrySet());
//					Collections.sort(sortlist,(m, m2) ->m.getValue().compareTo(m2.getValue()));
//					sortlist.forEach(m -> System.out.printf("Description: %-20sInvoice amount:%.2f\n",m.getKey(),m.getValue()));
					Enter = true;
					System.out.print("-------------------------------\n\n");
					break;
				case "select":
					System.out.print("Input the range to show (min,max): ");
					String temp = s.nextLine();
					String[] range = temp.split(",");
					//判斷使用者是否正確輸入範圍
					do {
						if(range.length!=2) {
							System.out.print("請重新輸入範圍:");
							temp = s.nextLine();
							range = temp.split(",");
							loop = true;
						}
						else {
							loop = false;
							//判斷使用者是不是輸入數字
							try{
								Double.parseDouble(range[0]);
								Double.parseDouble(range[1]);
							}
							catch (NumberFormatException e) {
								System.out.print("請重新輸入範圍:");
								temp = s.nextLine();
								range = temp.split(",");
							}
							Double min = Double.parseDouble(range[0]);
							Double max = Double.parseDouble(range[1]);
							System.out.printf("Invoices mapped to description and invoice amount for invoices in the range %.2f-%.2f:\n-------------------------------\n",min,max);
							list.stream().forEach(e->e.setTotalprice(e.getPrice()*e.getQuantity()));//計算各商品金額
							Predicate<Invoice> minmax = e->(e.getTotalprice()>=min&&e.getTotalprice()<=max);//把不符合金額區間的去除
							list.stream()
								.filter(minmax)
								.sorted(Comparator.comparing(Invoice::getTotalprice))//用金額排序
								.forEach(e->System.out.printf("Description: %-20sInvoice amount:%.2f\n",e.getPartDescription(),e.getTotalprice()));
						}
					}
					while(loop);
					Enter = true;
					System.out.print("-------------------------------\n\n");
					break;										
				default:
					//如果使用者不是輸入 "sort" or "report" or "select" 就要將 Enter設為false 讓do while 迴圈再跑一次 讓使用者重新輸入 
					Enter = false;
				
			}
		}
		while(!choice.equals("-1"));
		if(choice.equals("-1")) {System.exit(0);}				
	}	
}
