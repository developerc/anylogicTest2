package main;

import java.util.ArrayList;
import java.util.Map;

public class Main {

    public static void main(String[] args){
        Map<String, ArrayList<Ticket>> map;
        ArrayList<Ticket> ticketArrayList;
        HandleTicketJson htj = new HandleTicketJson(args[0]);
        if (htj.ticketsJson != null){
            map = htj.getObjFromJson();
            ticketArrayList = map.get("tickets");
            System.out.println("Среднее время полета: " + htj.getAverageTime(ticketArrayList) + " минут");
            System.out.println("90-й процентиль времени полета: " + htj.percentil90 + " минут");
        } else {
            System.out.println("Не удалось открыть файл");
        }
    }
}
//запускать из командной строки:
//java -jar anylogicTest2.jar /home/saperov/tickets.json